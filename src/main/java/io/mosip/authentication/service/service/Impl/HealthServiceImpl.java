package io.mosip.authentication.service.service.Impl;

import io.mosip.authentication.service.dto.response.HealthDetails;
import io.mosip.authentication.service.service.HealthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class HealthServiceImpl implements HealthService {

    @Value("${mosip.id.auth.serviceName}")
    private String serviceName;

    @Value("${mosip.id.auth.version}")
    private String version;

    @Value("${mosip.id.auth.environment}")
    private String environment;

    @Value("${mosip.id.auth.someConfig}")
    private String someConfig;

    @Value("${mosip.health.simulateDown:false}")
    private boolean simulateDown;

    @Override
    public HealthDetails getHealthDetails() {
        String status = simulateDown ? "DOWN" : "UP";
        HealthDetails.Metadata metadata = new HealthDetails.Metadata(serviceName, version, environment);
        return new HealthDetails(status, Instant.now(), metadata, someConfig);
    }

    @Override
    public boolean isServiceDown() {
        return simulateDown;
    }


}