package com.assigment.ip.domain.model;

public record IpLocation(
        String ipAddress,
        String continent,
        String country,
        String region,
        String city,
        double latitude,
        double longitude
) {
}
