package io.mosip.authentication.service.dto.request;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogRequestDTO {
    @NotNull(message = "eventType is mandatory")
    private String eventType;

    private String description;

    @NotNull(message = "userId is mandatory")
    private String userId;
}