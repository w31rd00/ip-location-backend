package com.assigment.ip.infrastructure.notifier;

import com.assigment.ip.application.port.out.Notifier;
import com.assigment.ip.domain.model.IpLocation;
import com.assigment.ip.infrastructure.db.Event;
import com.assigment.ip.infrastructure.db.EventJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class EventNotifier implements Notifier {

    private final ObjectMapper mapper;
    private final EventJpaRepository eventJpaRepository;

    @Override
    public void notify(UUID requestId, IpLocation ipLocation) {
        Event eventEntity = Event.builder()
                .requestId(requestId)
                .data(mapper.valueToTree(ipLocation))
                .build();
        eventJpaRepository.save(eventEntity);
    }


}
