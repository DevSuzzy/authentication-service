package io.mosip.authentication.service.service.Impl;

import io.mosip.authentication.service.dto.request.AuditLogRequest;
import io.mosip.authentication.service.dto.response.AuditLogResponse;
import io.mosip.authentication.service.entity.AuditEvent;
import io.mosip.authentication.service.repository.AuditEventRepository;
import io.mosip.authentication.service.service.AuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {


    private final AuditEventRepository auditEventRepository;

    @Override
    public AuditLogResponse logEvent(AuditLogRequest request) {
        String eventId = UUID.randomUUID().toString();
        Instant timestamp = Instant.now();
        AuditEvent event = new AuditEvent(eventId, request.getEventType(), request.getDescription(),
                request.getUserId(), timestamp);
        auditEventRepository.save(event);
        return new AuditLogResponse(eventId, timestamp);
    }
}