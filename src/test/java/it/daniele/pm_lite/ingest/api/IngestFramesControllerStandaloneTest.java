package it.daniele.pm_lite.ingest.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import it.daniele.pm_lite.ingest.api.dto.FrameBatchRequestDTO;
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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class IngestFramesControllerStandaloneTest {

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
    void ingestFrames_returns202_andCount() throws Exception {
        var req = new FrameBatchRequestDTO(
                "evt-1",
                "CH1",
                List.of(
                        new FrameBatchRequestDTO.Frame(Instant.parse("2025-09-28T12:00:00.100Z"), new byte[]{0x00,0x01,0x02}),
                        new FrameBatchRequestDTO.Frame(Instant.parse("2025-09-28T12:00:00.200Z"), new byte[]{0x03,0x04})
                )
        );

        when(service.ingestFrames(req)).thenReturn(req.frames().size());

        mvc.perform(post("/api/events/{eventId}/frames", "evt-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(req)))
                .andExpect(status().isAccepted())
                .andExpect(content().string("2"));

        verify(service).ingestFrames(req);
    }

    @Test
    void ingestFrames_mismatchedEventId_returns400() throws Exception {
        var req = new FrameBatchRequestDTO(
                "evt-OTHER",
                "CH1",
                List.of(new FrameBatchRequestDTO.Frame(Instant.parse("2025-09-28T12:00:00Z"), new byte[]{0x01}))
        );

        mvc.perform(post("/api/events/{eventId}/frames", "evt-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ingestFrames_invalidPayload_returns400() throws Exception {
        var req = new FrameBatchRequestDTO(
                "evt-1",
                "CH1",
                null // oppure List.of() se hai @NotEmpty
        );

        mvc.perform(post("/api/events/{eventId}/frames", "evt-1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(req)))
                .andExpect(status().isBadRequest());
    }
}

