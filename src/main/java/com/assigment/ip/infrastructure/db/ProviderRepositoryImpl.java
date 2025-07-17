package com.assigment.ip.infrastructure.db;

import com.assigment.ip.application.port.out.ProviderRepository;
import com.assigment.ip.domain.model.Provider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProviderRepositoryImpl implements ProviderRepository {

    private final ProviderJpaRepository repository;


    @Override
    public Optional<Provider> findBy(String name) {
        return repository.findByName(name)
                .map(provider -> new Provider(
                        provider.getName(),
                        getUri(provider.getBaseUrl()),
                        provider.getValidityDays(),
                        provider.isEnabled())
                );
    }

    private URI getUri(String baseUrl) {
        try {
            return new URI(baseUrl);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
