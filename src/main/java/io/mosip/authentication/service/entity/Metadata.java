package io.mosip.authentication.service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class Metadata {
       private String serviceName;
       private String version;
       private String environment;

   }
