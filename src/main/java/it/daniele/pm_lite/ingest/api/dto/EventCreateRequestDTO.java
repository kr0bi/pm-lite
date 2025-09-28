package it.daniele.pm_lite.ingest.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

public record EventCreateRequestDTO(
        @NotBlank String eventId,
        @NotBlank String type,
        @NotNull Instant triggeredAt,
        @NotBlank String severity,
        List<String> channels
) {}
