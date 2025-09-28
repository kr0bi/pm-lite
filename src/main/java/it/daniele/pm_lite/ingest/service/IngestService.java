package it.daniele.pm_lite.ingest.service;

import it.daniele.pm_lite.ingest.api.dto.EventCreateRequestDTO;
import it.daniele.pm_lite.ingest.api.dto.FrameBatchRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class IngestService {
    private static final Logger log = LoggerFactory.getLogger(IngestService.class);

    public void createEvent(EventCreateRequestDTO req) {
        // TODO: later publish to Kafka, write metadata to DB, etc.
        log.info("EVENT create: id={}, type={}, at={}, severity={}, channels={}",
                req.eventId(), req.type(), req.triggeredAt(), req.severity(), req.channels());
    }

    public int ingestFrames(FrameBatchRequestDTO req) {
        var count = req.frames() != null ? req.frames().size() : 0;
        // TODO: later write payloads to S3, publish to Kafka
        log.info("FRAMES ingest: eventId={}, channel={}, batchSize={}", req.eventId(), req.channel(), count);
        return count;
    }
}
