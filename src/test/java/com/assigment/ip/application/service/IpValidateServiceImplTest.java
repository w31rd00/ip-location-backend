package com.assigment.ip.application.service;

import com.assigment.ip.application.port.in.IpValidateUseCase;
import com.assigment.ip.application.port.out.IpLocationRepository;
import com.assigment.ip.application.port.out.Logger;
import com.assigment.ip.domain.model.IpLocation;
import com.assigment.ip.domain.model.Validity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class IpValidateServiceImplTest {


    private IpValidateUseCase ipValidateUseCase;
    private IpLocationRepository repository;
    private Logger logger;


    @BeforeEach
    void setup() {
        repository = Mockito.mock(IpLocationRepository.class);
        logger = Mockito.mock(Logger.class);
        ipValidateUseCase = new IpValidateServiceImpl(repository, logger);
    }

    @Test
    void existingIpLocation_isValid() {
        UUID requestId = UUID.randomUUID();
        IpLocation expectedIpLocation = new IpLocation(
                "192.168.11.12",
                "Europe",
                "Georgia",
                "Tbilisi",
                "Tbilisi",
                51.075153,
                -114.12841
        );

        Mockito.when(repository.getValidity(requestId))
                .thenReturn(Optional.of(new Validity(LocalDateTime.now().minusDays(1), 30, expectedIpLocation)));

        assertThat(ipValidateUseCase.isValid(requestId)).isTrue();
    }

    @Test
    void existingIpLocation_isInValid() {
        UUID requestId = UUID.randomUUID();
        IpLocation expectedIpLocation = new IpLocation(
                "192.168.11.12",
                "Europe",
                "Georgia",
                "Tbilisi",
                "Tbilisi",
                51.075153,
                -114.12841
        );

        Mockito.when(repository.getValidity(requestId))
                .thenReturn(Optional.of(new Validity(LocalDateTime.now().minusMonths(2), 30, expectedIpLocation)));

        assertThat(ipValidateUseCase.isValid(requestId)).isFalse();
    }

    @Test
    void incorrectIpFormat() {
        assertThat(ipValidateUseCase.isCorrectIpFormat("999.999.999.999")).isFalse();
        assertThat(ipValidateUseCase.isCorrectIpFormat("0.0.0.0")).isFalse();
        assertThat(ipValidateUseCase.isCorrectIpFormat("0.1.1.1")).isFalse();
        assertThat(ipValidateUseCase.isCorrectIpFormat("192.168.1")).isFalse();
        assertThat(ipValidateUseCase.isCorrectIpFormat("192.168.1.-1")).isFalse();
        assertThat(ipValidateUseCase.isCorrectIpFormat("192.\t168.01.1")).isFalse();
        assertThat(ipValidateUseCase.isCorrectIpFormat(null)).isFalse();
        assertThat(ipValidateUseCase.isCorrectIpFormat("")).isFalse();
    }

    @Test
    void correctIpFormat() {
        assertThat(ipValidateUseCase.isCorrectIpFormat("1.1.1.1")).isTrue();
        assertThat(ipValidateUseCase.isCorrectIpFormat("8.8.8.8")).isTrue();
        assertThat(ipValidateUseCase.isCorrectIpFormat("255.255.255.255")).isTrue();
        assertThat(ipValidateUseCase.isCorrectIpFormat("192.168.1.1")).isTrue();
        assertThat(ipValidateUseCase.isCorrectIpFormat("192.1.1.1")).isTrue();
    }
}