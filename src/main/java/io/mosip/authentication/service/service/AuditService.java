package io.mosip.authentication.service.service;

import io.mosip.authentication.service.dto.request.AuditLogRequest;
import io.mosip.authentication.service.dto.response.AuditLogResponse;

public interface AuditService {
    AuditLogResponse logEvent(AuditLogRequest request);
}