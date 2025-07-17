package com.assigment.ip.application.port.in;

import com.assigment.ip.domain.model.IpLocation;

import java.util.UUID;

public interface IpValidateUseCase {

    boolean isValid(UUID requestId);

    boolean isNotBlank(IpLocation ipLocation);

    boolean isCorrectIpFormat(String ipAddress);
}
