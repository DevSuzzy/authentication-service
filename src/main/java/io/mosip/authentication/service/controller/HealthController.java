package io.mosip.authentication.service.controller;

import io.mosip.authentication.core.constant.IdAuthCommonConstants;
import io.mosip.authentication.core.constant.IdAuthenticationErrorConstants;

import io.mosip.authentication.core.exception.IdAuthenticationAppException;
import io.mosip.authentication.core.logger.IdaLogger;

import io.mosip.authentication.service.dto.response.HealthDetails;
import io.mosip.authentication.service.service.HealthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.mosip.kernel.core.logger.spi.Logger;

@RestController
@RequestMapping(value = "/api/v1/health")
@Tag(name = "health-controller", description = "Health Controller")
public class HealthController {

    private static final Logger mosipLogger = IdaLogger.getLogger(HealthController.class);

    @Autowired
    private HealthService healthService;

    @GetMapping(value = "/details", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Get service health details", description = "Returns service health status and metadata", tags = {"health-controller"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Service health details retrieved"),
        @ApiResponse(responseCode = "503", description = "Service unavailable")
    })
    public HealthDetails getHealthDetails() throws IdAuthenticationAppException {
        try {
            if (healthService.isServiceDown()) {
                mosipLogger.error(IdAuthCommonConstants.SESSION_ID, this.getClass().getSimpleName(),
                        "getHealthDetails", "Service is down (simulated)");
                throw new IdAuthenticationAppException(IdAuthenticationErrorConstants.SERVER_ERROR);
            }
            return healthService.getHealthDetails();
        } catch (Exception e) {
            mosipLogger.error(IdAuthCommonConstants.SESSION_ID, this.getClass().getSimpleName(),
                    "getHealthDetails", e.getMessage());
            throw new IdAuthenticationAppException("IDA-MLC-007", e);
        }
    }
}