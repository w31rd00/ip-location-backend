package com.assigment.ip.application.service;

import com.assigment.ip.application.port.in.IpValidateUseCase;
import com.assigment.ip.application.port.out.IpLocationRepository;
import com.assigment.ip.application.port.out.Logger;
import com.assigment.ip.domain.model.IpLocation;
import com.assigment.ip.domain.model.Validity;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@RequiredArgsConstructor
public class IpValidateServiceImpl implements IpValidateUseCase {

    private static final Pattern PUBLIC_IP_PATTERN = Pattern.compile(
            "\\b(?:(?:[1-9]\\d?|1\\d{2}|2[0-4]\\d|25[0-5])\\.){3}" +
                    "(?:[1-9]\\d?|1\\d{2}|2[0-4]\\d|25[0-5])\\b"
    );


    private final IpLocationRepository repository;
    private final Logger logger;

    @Override
    public boolean isValid(UUID requestId) {
        Optional<Validity> maybeValidity = repository.getValidity(requestId);

        if (maybeValidity.isEmpty()) {
            logger.info(String.format("Ip with request id: %s, is not valid", requestId));
            return false;
        }

        Validity validity = maybeValidity.get();

        return validity.completedAt()
                .plusDays(validity.validityDays())
                .isAfter(LocalDateTime.now()) && isNotBlank(validity.ipLocation());
    }

    @Override
    public boolean isNotBlank(IpLocation ipLocation) {
        return Objects.nonNull(ipLocation.city())
                || Objects.nonNull(ipLocation.country())
                || Objects.nonNull(ipLocation.continent());
    }

    @Override
    public boolean isCorrectIpFormat(String ipAddress) {
        if (Objects.isNull(ipAddress)) return false;
        return PUBLIC_IP_PATTERN.matcher(ipAddress).matches();
    }
}
