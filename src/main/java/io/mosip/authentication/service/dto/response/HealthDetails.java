package io.mosip.authentication.service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthDetails {
    private String status;
    private Instant timestamp;
    private Metadata metadata;
    private String someConfig;


   @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Metadata {
       private String serviceName;
       private String version;
       private String environment;

   }
}