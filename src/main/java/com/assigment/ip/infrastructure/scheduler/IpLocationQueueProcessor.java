package com.assigment.ip.infrastructure.scheduler;

import com.assigment.ip.application.port.in.IpLookupUseCase;
import com.assigment.ip.application.port.out.IpRequestQueue;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class IpLocationQueueProcessor {

    private final IpLookupUseCase lookupUseCase;
    private final IpRequestQueue queue;
    private final ScheduledExecutorService executorService;

    @PostConstruct
    public void scheduleTask() {
        executorService.scheduleAtFixedRate(this::processNextRequest, 0, 1, TimeUnit.SECONDS);
    }

    private void processNextRequest() {
        UUID requestId = queue.dequeue();
        if (Objects.isNull(requestId)) return;
        this.lookupUseCase.processIp(requestId);
    }

}
