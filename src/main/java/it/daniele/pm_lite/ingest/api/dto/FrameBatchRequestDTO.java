package it.daniele.pm_lite.ingest.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;

public record FrameBatchRequestDTO(
        @NotBlank String eventId,
        @NotBlank String channel,
        @NotNull List<Frame> frames
) {
    public static @Data class Frame {
        @NotNull Instant ts;
        @NotNull byte[] payload;
}}
