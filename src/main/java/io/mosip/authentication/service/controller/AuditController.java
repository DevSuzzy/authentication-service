package io.mosip.authentication.service.controller;

import io.mosip.authentication.common.service.helper.AuditHelper;

import io.mosip.authentication.core.constant.AuditEvents;
import io.mosip.authentication.core.constant.AuditModules;
import io.mosip.authentication.core.constant.IdAuthCommonConstants;

import io.mosip.authentication.core.util.DataValidationUtil;
import io.mosip.authentication.service.dto.request.AuditLogRequestDTO;
import io.mosip.authentication.service.dto.response.AuditLogResponseDTO;
import io.mosip.authentication.core.dto.ObjectWithMetadata;
import io.mosip.authentication.core.exception.IDDataValidationException;
import io.mosip.authentication.core.exception.IdAuthenticationAppException;

import io.mosip.authentication.core.logger.IdaLogger;
import io.mosip.authentication.service.service.AuditService;
import io.mosip.authentication.service.validator.AuditLogRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import io.mosip.kernel.core.logger.spi.Logger;

@RestController
@RequestMapping(value = "/api/v1/audit")
@Tag(name = "audit-controller", description = "Audit Controller")
@SecurityRequirement(name = "Authorization")
public class AuditController {

    private static final Logger mosipLogger = IdaLogger.getLogger(AuditController.class);

    @Autowired
    private AuditService auditService;

    @Autowired
    private AuditHelper auditHelper;

    @Autowired
    private AuditLogRequestValidator auditLogRequestValidator;

    @InitBinder("auditLogRequest")
    private void initAuditLogRequestBinder(WebDataBinder binder) {
        binder.setValidator(auditLogRequestValidator);
    }

    @PostMapping(value = "/log", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Log an audit event", description = "Logs an audit event with mandatory eventType and userId", tags = {"audit-controller"})
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Event logged successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request payload"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public AuditLogResponseDTO logEvent(@Validated @RequestBody AuditLogRequestDTO auditLogRequestDTO, Errors errors, HttpServletRequest request)
            throws IdAuthenticationAppException, IDDataValidationException {
        if (request instanceof ObjectWithMetadata) {
            ObjectWithMetadata requestWithMetadata = (ObjectWithMetadata) request;
            try {
                DataValidationUtil.validate(errors);
                AuditLogResponseDTO response = auditService.logEvent(auditLogRequestDTO);
                String idType = "UIN"; // Default to UIN; adjust if needed
                String description = auditLogRequestDTO.getDescription() != null ? auditLogRequestDTO.getDescription() : "Audit event logged";
                auditHelper.audit(AuditModules.AUDIT_LOG, AuditEvents.AUDIT_REQUEST_RESPONSE,
                        auditLogRequestDTO.getUserId(), idType, description);
                return response;
            } catch (IDDataValidationException e) {
                mosipLogger.error(IdAuthCommonConstants.SESSION_ID, this.getClass().getSimpleName(),
                        "logEvent", e.getErrorCode() + " : " + e.getErrorText());
                auditHelper.audit(AuditModules.AUDIT_LOG, AuditEvents.AUDIT_REQUEST_RESPONSE,
                        auditLogRequestDTO.getUserId(), "UIN", e);
                throw e;
            } catch (Exception e) {
                mosipLogger.error(IdAuthCommonConstants.SESSION_ID, this.getClass().getSimpleName(),
                        "logEvent", e.getMessage());
                auditHelper.audit(AuditModules.AUDIT_LOG, AuditEvents.AUDIT_REQUEST_RESPONSE,
                        auditLogRequestDTO.getUserId(), "UIN",
                        new IdAuthenticationAppException("IDA-MLC-009", "Unable to process audit log request", e));
                throw new IdAuthenticationAppException("IDA-MLC-009", "Unable to process audit log request", e);
            }
        } else {
            mosipLogger.error(IdAuthCommonConstants.SESSION_ID, this.getClass().getSimpleName(),
                    "logEvent", "HttpServletRequest is not instanceof ObjectWithMetadata");
            throw new IdAuthenticationAppException("IDA-MLC-009", "HttpServletRequest is not instanceof ObjectWithMetadata");
        }
    }
}