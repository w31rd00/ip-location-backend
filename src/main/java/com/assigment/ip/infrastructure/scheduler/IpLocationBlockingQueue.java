package com.assigment.ip.infrastructure.scheduler;

import com.assigment.ip.application.port.out.IpRequestQueue;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class IpLocationBlockingQueue implements IpRequestQueue {

    private final BlockingQueue<UUID> requestQueue;

    public IpLocationBlockingQueue() {
        this.requestQueue = new LinkedBlockingQueue<>();
    }

    @Override
    public void enqueue(UUID requestId) {
        try {
            this.requestQueue.put(requestId);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UUID dequeue() {
        try {
            return this.requestQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


}
