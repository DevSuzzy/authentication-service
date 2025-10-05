package io.mosip.authentication.service.controller;

import io.mosip.authentication.common.service.helper.AuditHelper;
import io.mosip.authentication.core.constant.AuditEvents;
import io.mosip.authentication.core.constant.AuditModules;
import io.mosip.authentication.core.constant.IdAuthenticationErrorConstants;
import io.mosip.authentication.service.dto.response.AuditLogResponse;
import io.mosip.authentication.core.dto.ObjectWithMetadata;
import io.mosip.authentication.core.exception.IDDataValidationException;
import io.mosip.authentication.core.exception.IdAuthenticationBaseException;
import io.mosip.authentication.service.dto.request.AuditLogRequest;
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

    @Mock
    private AuditService auditService;

    @Mock
    private AuditHelper auditHelper;

    @Mock
    private AuditLogRequestValidator auditLogRequestValidator;

    @Mock
    private AuditEventRepository auditEventRepository;

    @InjectMocks
    private AuditController auditController;

    private AuditLogRequest auditLogRequest;
    private Errors errors;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Ensure mocks are initialized
        auditLogRequest = new AuditLogRequest();
        auditLogRequest.setEventType("LOGIN");
        auditLogRequest.setUserId("12345");
        auditLogRequest.setDescription("User attempted login");
        errors = new BindException(auditLogRequest, "auditLogRequest");
    }

    private static class MockObjectWithMetadata implements ObjectWithMetadata {
        private Map<String, Object> metadata = new HashMap<>();

        @Override
        public Map<String, Object> getMetadata() {
            return metadata;
        }

        @Override
        public void setMetadata(Map<String, Object> metadata) {
            this.metadata = metadata;
        }
    }

    @Test
    void logEvent_success() throws Exception {
        AuditLogResponse response = new AuditLogResponse("uuid-123", Instant.now());
        when(auditService.logEvent(any(AuditLogRequest.class))).thenReturn(response);
        doNothing().when(auditHelper).audit(any(AuditModules.class), any(AuditEvents.class),
                any(String.class), any(String.class), any(String.class));

        MockObjectWithMetadata mockMetadata = new MockObjectWithMetadata();
        mockMetadata.setMetadata(new HashMap<>());
        mockMetadata.getMetadata().put("requestId", "test-request-id");

        mockMvc.perform(post("/api/v1/audit/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"eventType\":\"LOGIN\",\"userId\":\"12345\",\"description\":\"User attempted login\"}")
                        .requestAttr(ObjectWithMetadata.class.getName(), mockMetadata))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.eventId").value("uuid-123"));
    }

    @Test
    void logEvent_invalidPayload() throws Exception {
        MockObjectWithMetadata mockMetadata = new MockObjectWithMetadata();
        mockMetadata.setMetadata(new HashMap<>());
        mockMetadata.getMetadata().put("requestId", "test-request-id");

        when(auditService.logEvent(any(AuditLogRequest.class)))
                .thenThrow(new IDDataValidationException(IdAuthenticationErrorConstants.SERVER_ERROR));
        doNothing().when(auditHelper).audit(any(AuditModules.class), any(AuditEvents.class),
                any(String.class), any(String.class), any(IdAuthenticationBaseException.class));

        mockMvc.perform(post("/api/v1/audit/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"eventType\":\"LOGIN\"}")
                        .requestAttr(ObjectWithMetadata.class.getName(), mockMetadata))
                .andExpect(status().isBadRequest());
    }

    @Test
    void logEvent_noMetadata() throws Exception {
        mockMvc.perform(post("/api/v1/audit/log")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"eventType\":\"LOGIN\",\"userId\":\"12345\"}"))
                .andExpect(status().isInternalServerError());
    }
}