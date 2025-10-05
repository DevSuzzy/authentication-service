package io.mosip.authentication.service.controller;

import io.mosip.authentication.core.constant.IdAuthCommonConstants;

import io.mosip.authentication.core.logger.IdaLogger;

import io.mosip.authentication.service.dto.response.HealthDetailsResponseDTO;
import io.mosip.authentication.service.service.HealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.mosip.kernel.core.logger.spi.Logger;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/health")
@Tag(name = "health-controller", description = "Health Controller")
public class HealthController {

    private static final Logger mosipLogger = IdaLogger.getLogger(HealthController.class);

    private final HealthService healthService;

    @GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get service health details", description = "Returns service health status and metadata", tags = {"health-controller"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service health details retrieved"),
        @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    public HealthDetailsResponseDTO getHealthDetails() {

            if (healthService.isServiceDown()) {
                mosipLogger.error(IdAuthCommonConstants.SERVICE_UNAVAILABLE, this.getClass().getSimpleName(),
                        "getHealthDetails", "Service is down (simulated)");
                throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Service is down (simulated)");
            }
            return healthService.getHealthDetails();

    }
}