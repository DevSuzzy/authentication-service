package io.mosip.authentication.service.dto.response;

import io.mosip.authentication.service.entity.Metadata;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthDetailsResponseDTO {
    private String status;
    private Instant timestamp;
    private Metadata metadata;
    private String someConfig;

}