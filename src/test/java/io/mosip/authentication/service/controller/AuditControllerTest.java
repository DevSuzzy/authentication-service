package io.mosip.authentication.service.controller;

import io.mosip.authentication.common.service.helper.AuditHelper;
import io.mosip.authentication.core.constant.AuditEvents;
import io.mosip.authentication.core.constant.AuditModules;
import io.mosip.authentication.core.constant.IdAuthenticationErrorConstants;
import io.mosip.authentication.service.dto.response.AuditLogResponseDTO;
import io.mosip.authentication.core.dto.ObjectWithMetadata;
import io.mosip.authentication.core.exception.IDDataValidationException;
import io.mosip.authentication.core.exception.IdAuthenticationBaseException;
import io.mosip.authentication.service.dto.request.AuditLogRequestDTO;
import io.mosip.authentication.service.repository.AuditEventRepository;
import io.mosip.authentication.service.service.AuditService;
import io.mosip.authentication.service.validator.AuditLogRequestValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuditController.class)
@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
@ContextConfiguration(classes = {AuditController.class, AuditLogRequestValidator.class})
public class AuditControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuditService auditService;

    @MockBean
    private AuditHelper auditHelper;

    @MockBean
    private AuditLogRequestValidator auditLogRequestValidator;

    @Test
    void logEvent_success() throws Exception {
        AuditLogResponseDTO response = new AuditLogResponseDTO("uuid-123", Instant.now());
        when(auditService.logEvent(any(AuditLogRequestDTO.class))).thenReturn(response);


        mockMvc.perform(post("/api/v1/audit/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"eventType\":\"LOGIN\",\"userId\":\"12345\",\"description\":\"User attempted login\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventId").value("uuid-123"));
    }

    @Test
    void logEvent_invalidPayload() throws Exception {
        when(auditService.logEvent(any(AuditLogRequestDTO.class)))
                .thenThrow(new IDDataValidationException(IdAuthenticationErrorConstants.SERVER_ERROR));

        mockMvc.perform(post("/api/v1/audit/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"eventType\":\"LOGIN\"}"))
                .andExpect(status().isBadRequest());
    }
}
