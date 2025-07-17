package com.assigment.ip.application.port.out;

import com.assigment.ip.domain.model.IpLocation;

public interface ProviderResultMapper {

    IpLocation map(String json);

}
