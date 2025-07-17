package com.assigment.ip.application.service;

import com.assigment.ip.application.port.in.IpLookupUseCase;
import com.assigment.ip.application.port.in.IpValidateUseCase;
import com.assigment.ip.application.port.out.*;
import com.assigment.ip.domain.model.IpLocation;
import com.assigment.ip.domain.model.Validity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class IpLookupUseCaseTest {

    private IpLookupUseCase lookupUseCase;
    private IpLocationProvider provider;
    private IpLocationRepository repository;
    private IpRequestQueue queue;
    private Notifier notifier;
    private Logger logger;
    private IpValidateUseCase ipValidateUseCase;


    @BeforeEach
    void setup() {
        provider = Mockito.mock(IpLocationProvider.class);
        repository = Mockito.mock(IpLocationRepository.class);
        queue = Mockito.mock(IpRequestQueue.class);
        notifier = Mockito.mock(Notifier.class);
        logger = Mockito.mock(Logger.class);
        ipValidateUseCase = new IpValidateServiceImpl(repository, logger);
        lookupUseCase = new IpLookupServiceImpl(provider, repository, queue, notifier, ipValidateUseCase, logger);
    }


    @Test
    void getLocationByIpAddress_New() {
        UUID requestId = UUID.randomUUID();
        String ipAddress = "192.168.11.12";
        Mockito.when(repository.save(ipAddress))
                .thenReturn(requestId);
        Mockito.when(queue.dequeue())
                .thenReturn(requestId);
        requestId = lookupUseCase.registerIp(ipAddress);

        IpLocation dbSavedLocation = new IpLocation(
                "192.168.11.12",
                null,
                null,
                null,
                null,
                0.0,
                0.0
        );

        IpLocation expectedIpLocation = new IpLocation(
                "192.168.11.12",
                "Europe",
                "Georgia",
                "Tbilisi",
                "Tbilisi",
                51.075153,
                -114.12841
        );
        Mockito.when(repository.findBy(requestId))
                        .thenReturn(Optional.of(dbSavedLocation));

        Mockito.when(provider.fetchIpLocationBy(ipAddress))
                        .thenReturn(expectedIpLocation);

        ArgumentCaptor<UUID> requestIdCaptor = ArgumentCaptor.forClass(UUID.class);
        ArgumentCaptor<IpLocation> ipLocationArgumentCaptor = ArgumentCaptor.forClass(IpLocation.class);

        lookupUseCase.processIp(requestId);

        Mockito.verify(notifier).notify(requestIdCaptor.capture(), ipLocationArgumentCaptor.capture());

        UUID passedRequestId = requestIdCaptor.getValue();
        IpLocation passedIpLocation = ipLocationArgumentCaptor.getValue();

        assertThat(passedRequestId).isEqualTo(requestId);
        assertThat(passedIpLocation).isEqualTo(expectedIpLocation);

    }


    @Test
    void getLocationByIpAddress_Existing() {
        UUID requestId = UUID.randomUUID();
        String ipAddress = "192.168.11.12";
        Mockito.when(repository.findRequestIdBy(ipAddress))
                .thenReturn(Optional.of(requestId));
        Mockito.when(queue.dequeue())
                .thenReturn(requestId);
        requestId = lookupUseCase.registerIp(ipAddress);

        IpLocation expectedIpLocation = new IpLocation(
                "192.168.11.12",
                "Europe",
                "Georgia",
                "Tbilisi",
                "Tbilisi",
                51.075153,
                -114.12841
        );
        Mockito.when(repository.findBy(requestId))
                        .thenReturn(Optional.of(expectedIpLocation));

        Mockito.when(repository.isCompleted(requestId))
                        .thenReturn(true);

        Mockito.when(repository.getValidity(requestId))
                        .thenReturn(Optional.of(new Validity(LocalDateTime.now().minusDays(1), 30, expectedIpLocation)));

        lookupUseCase.processIp(requestId);

        Mockito.verify(provider, Mockito.never()).fetchIpLocationBy(Mockito.anyString());

    }


}