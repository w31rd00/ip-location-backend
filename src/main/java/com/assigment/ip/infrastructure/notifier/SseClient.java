package com.assigment.ip.infrastructure.notifier;

import com.assigment.ip.application.port.in.IpLookupUseCase;
import com.assigment.ip.application.port.in.IpValidateUseCase;
import com.assigment.ip.infrastructure.db.Event;
import com.assigment.ip.infrastructure.db.EventJpaRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class SseClient {

    private final EventJpaRepository eventJpaRepository;
    private final ScheduledExecutorService executorService;
    private final IpLookupUseCase ipLookupUseCase;
    private final IpValidateUseCase ipValidateUseCase;
    private final SubscribeRequestQueue subscribeRequestQueue;
    private final Map<UUID, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter subscribe(UUID requestId) {
        SseEmitter sseEmitter = new SseEmitter(0L);
        emitters.compute(requestId, (key, emitter) -> {
            if (Objects.nonNull(emitter)) emitter.complete();
            return sseEmitter;
        });
        sseEmitter.onCompletion(() -> emitters.remove(requestId));
        sseEmitter.onTimeout(() -> {
            emitters.remove(requestId);
            sseEmitter.complete();
        });

        subscribeRequestQueue.enqueue(requestId);

        return sseEmitter;
    }


    @PostConstruct
    public void scheduleSseEmit() {
        executorService.scheduleAtFixedRate(this::sendEventIfExists, 0, 1, TimeUnit.SECONDS);
    }

    private void sendEventIfExists() {
        UUID requestId = this.subscribeRequestQueue.dequeue();
        boolean isValid = ipValidateUseCase.isValid(requestId);
        if (!isValid) {
            ipLookupUseCase.processInvalidIp(requestId);
        }
        try {
            Optional<Event> maybeEvent = eventJpaRepository.findTopByRequestIdOrderByIdDesc(requestId);
            if (maybeEvent.isPresent()) {
                Event event = maybeEvent.get();
                SseEmitter requestIdEmitter = emitters.get(requestId);
                sendSSEvent(requestIdEmitter, event);
                completeEvent(event);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void sendSSEvent(SseEmitter requestIdEmitter, Event event) {
        SseEmitter.SseEventBuilder result = SseEmitter.event()
                .name("ip-result")
                .data(event.getData());
        try {
            requestIdEmitter.send(result);
            requestIdEmitter.complete();
        } catch (IOException e) {
            requestIdEmitter.completeWithError(e);
        }
    }

    private void completeEvent(Event event) {
        if (!event.isCompleted()) {
            event.setCompleted(true);
            event.setCompletedAt(LocalDateTime.now());
            eventJpaRepository.save(event);
        }
    }
}
