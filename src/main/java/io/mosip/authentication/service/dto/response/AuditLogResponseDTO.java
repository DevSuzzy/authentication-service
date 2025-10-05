package io.mosip.authentication.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponseDTO {
    private String eventId;
    private Instant timestamp;


}