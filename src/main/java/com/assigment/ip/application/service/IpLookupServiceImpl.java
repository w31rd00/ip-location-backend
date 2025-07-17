package com.assigment.ip.application.service;

import com.assigment.ip.application.port.in.IpLookupUseCase;
import com.assigment.ip.application.port.in.IpValidateUseCase;
import com.assigment.ip.application.port.out.*;
import com.assigment.ip.domain.model.IpLocation;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
public class IpLookupServiceImpl implements IpLookupUseCase {

    private final IpLocationProvider provider;
    private final IpLocationRepository repository;
    private final IpRequestQueue ipRequestQueue;
    private final Notifier notifier;
    private final IpValidateUseCase ipValidateUseCase;
    private final Logger logger;

    @Override
    public UUID registerIp(String ip) {
        if (!ipValidateUseCase.isCorrectIpFormat(ip)) {
            throw new IncorrectIpFormatException("IP address has incorrect format, should be x.x.x.x");
        }
        UUID requestId;
        Optional<UUID> maybeRequestId = repository.findRequestIdBy(ip);
        requestId = maybeRequestId.orElseGet(() -> repository.save(ip));
        ipRequestQueue.enqueue(requestId);
        return requestId;
    }

    @Override
    public void processIp(UUID requestId) {
        Optional<IpLocation> maybeIpLocation = repository.findBy(requestId);
        if (maybeIpLocation.isEmpty()) return;
        logger.info(String.format("Start processing request with id: %s", requestId));
        IpLocation ipLocation;
        if (!ipValidateUseCase.isValid(requestId)) {
            ipLocation = processInvalid(requestId, maybeIpLocation.get());
        } else {
            ipLocation = maybeIpLocation.get();
        }
        logger.info(String.format("Request id: %s, processed successfully", requestId));
        notifier.notify(requestId, ipLocation);
    }

    @Override
    public void processInvalidIp(UUID requestId) {
        Optional<IpLocation> maybeIpLocation = repository.findBy(requestId);
        if (maybeIpLocation.isEmpty()) return;
        IpLocation ipLocation = processInvalid(requestId, maybeIpLocation.get());
        notifier.notify(requestId, ipLocation);
    }

    private IpLocation processInvalid(UUID requestId, IpLocation ipLocation) {
        IpLocation result = provider.fetchIpLocationBy(ipLocation.ipAddress());
        if (!ipValidateUseCase.isNotBlank(result)) {
            throw new EmptyResultException("Result is not currently available, please try again later.");
        }
        repository.complete(requestId, result);
        return result;
    }
}
