package it.daniele.pm_lite.ingest.error;

import java.time.Instant;

public record ApiError(String code, String message, Instant ts) {
    public static ApiError of(String code, String message) {
        return new ApiError(code, message, Instant.now());
    }
}
