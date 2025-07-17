package com.assigment.ip.adapters.web;

import com.assigment.ip.application.port.in.IpLookupUseCase;
import com.assigment.ip.infrastructure.notifier.SseClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final IpLookupUseCase lookupUseCase;
    private final SseClient notifier;

    @GetMapping("/ip/subscribe/{requestId}")
    public SseEmitter subscribe(@PathVariable UUID requestId) {
        return notifier.subscribe(requestId);
    }

    @PostMapping("/ip/lookup")
    public ResponseEntity<Map<String, UUID>> lookup(@RequestParam String ip) {
        UUID requestId = lookupUseCase.registerIp(ip);
        return ResponseEntity.accepted()
                .body(Map.of("requestId", requestId));
    }

}
