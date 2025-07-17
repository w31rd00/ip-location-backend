package com.assigment.ip.application.port.out;

import com.assigment.ip.domain.model.IpLocation;

import java.util.UUID;

public interface Notifier {

    void notify(UUID requestId, IpLocation ipLocation);

}
