package it.daniele.pm_lite.ingest.api;

import it.daniele.pm_lite.ingest.api.dto.EventCreateRequestDTO;
import it.daniele.pm_lite.ingest.api.dto.FrameBatchRequestDTO;
import it.daniele.pm_lite.ingest.service.IngestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class IngestController {

    private final IngestService service;

    public IngestController(IngestService service) {
        this.service = service;
    }

    @PostMapping("/events")
    public ResponseEntity<Void> createEvent(@Valid @RequestBody EventCreateRequestDTO request) {
        service.createEvent(request);
        return ResponseEntity.accepted().build(); // 202 Accepted (async semantics)
    }

    @PostMapping("/events/{eventId}/frames")
    public ResponseEntity<Integer> ingestFrames(@PathVariable String eventId,
                                                @Valid @RequestBody FrameBatchRequestDTO request) {
        // Basic guard to avoid mismatched payloads
        if (!eventId.equals(request.eventId())) {
            return ResponseEntity.badRequest().build();
        }
        int accepted = service.ingestFrames(request);
        return ResponseEntity.accepted().body(accepted);
    }
}