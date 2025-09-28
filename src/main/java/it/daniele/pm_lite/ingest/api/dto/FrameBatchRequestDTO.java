package it.daniele.pm_lite.ingest.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

public record FrameBatchRequestDTO(
        @NotBlank String eventId,
        @NotBlank String channel,
        @NotNull List<Frame> frames
) {
    public @AllArgsConstructor @NoArgsConstructor
    static @Data class Frame {
        @NotNull Instant ts;
        @NotNull byte[] payload;
}}
