package com.assigment.ip.infrastructure.notifier;

import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class SubscribeRequestQueue {

    private final BlockingQueue<UUID> requestQueue = new LinkedBlockingQueue<>();

    public void enqueue(UUID requestId) {
        try {
            requestQueue.put(requestId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public UUID dequeue() {
        try {
            return requestQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
