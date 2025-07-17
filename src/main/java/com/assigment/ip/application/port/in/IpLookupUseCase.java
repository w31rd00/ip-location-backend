package com.assigment.ip.application.port.in;


import java.util.UUID;

public interface IpLookupUseCase {

    UUID registerIp(String ip);

    void processIp(UUID requestId);

    void processInvalidIp(UUID requestId);
}
