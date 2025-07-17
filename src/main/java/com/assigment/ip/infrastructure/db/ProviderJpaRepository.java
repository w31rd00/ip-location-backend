package com.assigment.ip.infrastructure.db;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderJpaRepository extends JpaRepository<Provider, Integer> {

    @Query("SELECT p FROM Provider p WHERE p.name = :name")
    Optional<Provider> findByName(String name);

}
