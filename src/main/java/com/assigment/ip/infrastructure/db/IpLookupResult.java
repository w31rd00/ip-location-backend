package com.assigment.ip.infrastructure.db;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "ip_lookup_result", indexes = {
    @Index(name = "idx_ip_lookup_ip", columnList = "ip_address"),
    @Index(name = "idx_ip_lookup_provider", columnList = "provider_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IpLookupResult {

    @Id
    @GeneratedValue(generator = "uuid2")
    @Column(name = "request_id", columnDefinition = "UUID")
    private UUID requestId;

    @Column(name = "ip_address", length = 45, nullable = false, unique = true)
    private String ipAddress;

    @Column(length = 100)
    private String continent;

    @Column(length = 100)
    private String country;

    @Column(length = 100)
    private String region;

    @Column(length = 100)
    private String city;

    @Column
    private double latitude;

    @Column
    private double longitude;

    @Column(name = "fetched_at")
    private LocalDateTime fetchedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private boolean completed;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "provider_id", foreignKey = @ForeignKey(name = "fk_provider"))
    private Provider provider;

}
