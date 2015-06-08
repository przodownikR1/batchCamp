package pl.java.scalatech.listeners;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ChunkBatchListener implements ChunkListener {
    @Override
    public void beforeChunk(ChunkContext context) {
        log.info("+++ beforeChunk ....{}", context.getStepContext().getId());
    }

    @Override
    public void afterChunk(ChunkContext context) {
        log.info("+++ afterChunk ....{}", context.getStepContext().getId());
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        log.info("+++ afterErrorChunk ....{}", context.getStepContext().getId());
    }
}