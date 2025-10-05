package io.mosip.authentication.service.service;
import io.mosip.authentication.service.dto.response.HealthDetails;

public interface HealthService {
    HealthDetails getHealthDetails();
    boolean isServiceDown();
}