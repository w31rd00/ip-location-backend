package com.assigment.ip.application.port.out;

import com.assigment.ip.domain.model.IpLocation;
import com.assigment.ip.domain.model.Validity;

import java.util.Optional;
import java.util.UUID;

public interface IpLocationRepository {

    UUID save(String ip);

    void complete(UUID requestId, IpLocation ipLocation);

    Optional<IpLocation> findBy(UUID requestId);

    Optional<UUID> findRequestIdBy(String ipAddress);

    boolean isCompleted(UUID requestId);

    Optional<Validity> getValidity(UUID requestId);
}
