package io.mosip.authentication.service.service.Impl;

import io.mosip.authentication.service.dto.request.AuditLogRequestDTO;
import io.mosip.authentication.service.dto.response.AuditLogResponseDTO;
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
    public AuditLogResponseDTO logEvent(AuditLogRequestDTO request) {
        // Generate unique ID for the audit event
        String eventId = UUID.randomUUID().toString();
        Instant timestamp = Instant.now();


        AuditEvent event = AuditEvent.builder()
                .eventId(eventId)
                .eventType(request.getEventType())
                .description(request.getDescription())
                .userId(request.getUserId())
                .timestamp(timestamp)
                .build();

        auditEventRepository.save(event);


        return new AuditLogResponseDTO(eventId, timestamp);
    }
}
