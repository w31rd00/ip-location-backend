package com.assigment.ip.domain.model;

import java.net.URI;

public record Provider(
        String name,
        URI baseUrl,
        int validityDays,
        boolean enabled
) {
}
