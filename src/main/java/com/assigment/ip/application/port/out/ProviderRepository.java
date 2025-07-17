package com.assigment.ip.application.port.out;

import com.assigment.ip.domain.model.Provider;

import java.util.Optional;

public interface ProviderRepository {

    Optional<Provider> findBy(String name);

}
