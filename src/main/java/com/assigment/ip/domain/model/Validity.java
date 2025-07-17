package com.assigment.ip.domain.model;

import java.time.LocalDateTime;

public record Validity(
        LocalDateTime completedAt,
        int validityDays,
        IpLocation ipLocation
) {
}
