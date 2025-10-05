package io.mosip.authentication.service.service;
import io.mosip.authentication.service.dto.response.HealthDetailsResponseDTO;

public interface HealthService {
    HealthDetailsResponseDTO getHealthDetails();

    boolean isServiceDown();
}