package com.assigment.ip.infrastructure.db;

import com.assigment.ip.application.port.out.IpLocationRepository;
import com.assigment.ip.domain.model.IpLocation;
import com.assigment.ip.domain.model.Validity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;


@Repository
@RequiredArgsConstructor
public class IpLocationRepositoryImpl implements IpLocationRepository {

    private final IpLookupJpaRepository repository;
    private final ProviderJpaRepository providerJpaRepository;

    @Override
    @Transactional
    public UUID save(String ip) {
        IpLookupResult ipLookupResult = IpLookupResult.builder()
                .ipAddress(ip)
                .provider(getProvider())
                .build();
        return repository.save(ipLookupResult).getRequestId();
    }

    @Override
    @Transactional
    public void complete(UUID requestId, IpLocation ipLocation) {
        IpLookupResult ipLookupResult = IpLookupResult.builder()
                .requestId(requestId)
                .ipAddress(ipLocation.ipAddress())
                .city(ipLocation.city())
                .country(ipLocation.country())
                .continent(ipLocation.continent())
                .latitude(ipLocation.latitude())
                .longitude(ipLocation.longitude())
                .region(ipLocation.region())
                .completed(true)
                .fetchedAt(LocalDateTime.now())
                .provider(getProvider())
                .build();
        repository.save(ipLookupResult);
    }

    @Override
    public Optional<IpLocation> findBy(UUID requestId) {
        return repository.findById(requestId).map(this::mapFrom);
    }

    @Override
    public Optional<UUID> findRequestIdBy(String ipAddress) {
        return repository.findByIpAddress(ipAddress).map(IpLookupResult::getRequestId);
    }

    @Override
    public boolean isCompleted(UUID requestId) {
        return repository.existsByRequestIdAndCompletedTrue(requestId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Validity> getValidity(UUID requestId) {
        Optional<IpLookupResult> maybeIpResult = repository.findById(requestId);
        if (maybeIpResult.isEmpty() || !maybeIpResult.get().isCompleted()) return Optional.empty();

        IpLookupResult ipLookupResult = maybeIpResult.get();
        Provider provider = ipLookupResult.getProvider();
        return Optional.of(new Validity(ipLookupResult.getFetchedAt(), provider.getValidityDays(), mapFrom(ipLookupResult)));
    }

    private Provider getProvider() {
        /*
            currently using hardcoded provider name, can be enhanced later and give the client list of providers
            client will then choose the provider and provider will be passed here, will help us to eliminate hardcoded values
         */
        return providerJpaRepository.findByName("FreeIPAPI").orElseThrow();
    }

    private IpLocation mapFrom(IpLookupResult ipLookupResult) {
        return new IpLocation(
                ipLookupResult.getIpAddress(),
                ipLookupResult.getContinent(),
                ipLookupResult.getCountry(),
                ipLookupResult.getRegion(),
                ipLookupResult.getCity(),
                ipLookupResult.getLatitude(),
                ipLookupResult.getLongitude()
        );
    }
}
