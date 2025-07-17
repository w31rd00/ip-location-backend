package com.assigment.ip.application.port.out;

import java.util.UUID;

public interface IpRequestQueue {

    void enqueue(UUID requestId);

    UUID dequeue();

}
