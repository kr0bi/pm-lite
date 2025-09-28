package it.daniele.pm_lite.ingest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import it.daniele.pm_lite.ingest.api.dto.EventCreateRequestDTO;
import it.daniele.pm_lite.ingest.error.GlobalExceptionHandler;
import it.daniele.pm_lite.ingest.service.IngestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class IngestControllerStandaloneTest {

    private MockMvc mvc;

    @Mock
    IngestService service;

    private final ObjectMapper om = JsonMapper.builder()
            .findAndAddModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .build();

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders
                .standaloneSetup(new IngestController(service))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createEvent_returns202() throws Exception {
        var req = new EventCreateRequestDTO(
                "evt-1", "QUENCH",
                Instant.parse("2025-09-28T12:00:00Z"),
                "HIGH", List.of("CH1","CH2")
        );

        mvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(req)))
                .andExpect(status().isAccepted());

        verify(service).createEvent(req);
    }
}

