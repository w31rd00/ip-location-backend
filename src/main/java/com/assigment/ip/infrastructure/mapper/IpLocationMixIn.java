package com.assigment.ip.infrastructure.mapper;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class IpLocationMixIn {

    @JsonProperty("countryName")
    abstract String country();
    @JsonProperty("cityName")
    abstract String city();
    @JsonProperty("regionName")
    abstract String region();

}
