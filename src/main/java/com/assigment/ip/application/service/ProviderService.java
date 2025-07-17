package com.assigment.ip.application.service;

import com.assigment.ip.application.port.out.IpLocationProvider;
import com.assigment.ip.application.port.out.Logger;
import com.assigment.ip.application.port.out.ProviderRepository;
import com.assigment.ip.application.port.out.ProviderResultMapper;
import com.assigment.ip.domain.model.IpLocation;
import com.assigment.ip.domain.model.Provider;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class ProviderService implements IpLocationProvider {


    private final ProviderRepository repository;
    private final ProviderResultMapper mapper;
    private final String providerName;
    private final Logger logger;
    private LocalDateTime lastFetchedTime;

    public ProviderService(
            ProviderRepository repository,
            ProviderResultMapper mapper,
            String providerName,
            Logger logger
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.providerName = providerName;
        this.logger = logger;
    }


    @Override
    public IpLocation fetchIpLocationBy(String ipAddress) {
        Optional<Provider> maybeProvider = repository.findBy(providerName);
        if (maybeProvider.isEmpty()) {
            throw new IllegalArgumentException(String.format("No such provider with name %s", providerName));
        }
        Provider provider = maybeProvider.get();

        return getIpLocationFromApi(provider, ipAddress);
    }

    private IpLocation getIpLocationFromApi(Provider provider, String ipAddress) {
        waitIfExceedRateLimit();
        HttpClient httpClient = HttpClient
                .newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS)
                .build();
        HttpRequest getRequest = HttpRequest.newBuilder(buildUriFrom(ipAddress, provider))
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(getRequest, HttpResponse.BodyHandlers.ofString());
            lastFetchedTime = LocalDateTime.now();
            logger.info(String.format("Ip Location data has successfully fetched for ip address: %s", ipAddress));
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        return mapper.map(response.body());
    }

    private void waitIfExceedRateLimit() {
        if (Objects.isNull(lastFetchedTime)) return;

        if (LocalDateTime.now().isBefore(lastFetchedTime.plusSeconds(1))) {
            try {
                TimeUnit.SECONDS.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private URI buildUriFrom(String ipAddress, Provider provider) {
        URI uri;
        try {
            uri = new URI(String.format("%s%s", provider.baseUrl(), ipAddress));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return uri;
    }
}
