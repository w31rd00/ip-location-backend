package com.assigment.ip.infrastructure.db;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "provider")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Provider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 100, nullable = false, unique = true)
    private String name;

    @Column(name = "base_url")
    private String baseUrl;

    @Column(name = "validity_days", nullable = false)
    private int validityDays = 30;

    @Column(nullable = false)
    private boolean enabled = true;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<IpLookupResult> ipLookupResultEntities = new ArrayList<>();

}
