package io.mosip.authentication.service.controller;

import io.mosip.authentication.service.dto.response.HealthDetailsResponseDTO;
import io.mosip.authentication.service.entity.Metadata;
import io.mosip.authentication.service.service.HealthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HealthController.class)
public class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthService healthService;

    @Test
    void getHealthDetails_up() throws Exception {
        Metadata metadata = new Metadata("id-authentication-service", "1.2.1.0", "dev");

        HealthDetailsResponseDTO details = new HealthDetailsResponseDTO(
                "UP", Instant.now(), metadata, "true");

        when(healthService.getHealthDetails()).thenReturn(details);
        when(healthService.isServiceDown()).thenReturn(false);

        mockMvc.perform(get("/api/v1/health/details"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.metadata.serviceName").value("id-authentication-service"));
    }

    @Test
    void getHealthDetails_down() throws Exception {
        when(healthService.isServiceDown()).thenReturn(true);

        mockMvc.perform(get("/api/v1/health/details"))
                .andExpect(status().isServiceUnavailable());
    }
}
