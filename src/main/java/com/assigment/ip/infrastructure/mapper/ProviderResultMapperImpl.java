package com.assigment.ip.infrastructure.mapper;

import com.assigment.ip.application.port.out.ProviderResultMapper;
import com.assigment.ip.domain.model.IpLocation;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ProviderResultMapperImpl implements ProviderResultMapper {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        MAPPER.addMixIn(IpLocation.class, IpLocationMixIn.class);
    }

    @Override
    public IpLocation map(String json) {
        try {
            return MAPPER.readValue(json, IpLocation.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
