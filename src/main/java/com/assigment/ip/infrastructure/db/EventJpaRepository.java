package com.assigment.ip.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EventJpaRepository extends JpaRepository<Event, Integer> {
    Optional<Event> findTopByRequestIdOrderByIdDesc(UUID requestId);
}
