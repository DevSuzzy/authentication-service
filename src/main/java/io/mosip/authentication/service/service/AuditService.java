package io.mosip.authentication.service.service;

import io.mosip.authentication.service.dto.request.AuditLogRequestDTO;
import io.mosip.authentication.service.dto.response.AuditLogResponseDTO;

public interface AuditService {
    AuditLogResponseDTO logEvent(AuditLogRequestDTO request);
}