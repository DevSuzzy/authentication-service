package io.mosip.authentication.service.service.Impl;

import io.mosip.authentication.service.dto.response.HealthDetailsResponseDTO;
import io.mosip.authentication.service.entity.Metadata;
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
    public HealthDetailsResponseDTO getHealthDetails() {
        String status = simulateDown ? "DOWN" : "UP";
        Metadata metadata = new Metadata(serviceName, version, environment);
        return new HealthDetailsResponseDTO(status, Instant.now(), metadata, someConfig);
    }

    @Override
    public boolean isServiceDown() {
        return simulateDown;
    }
}
