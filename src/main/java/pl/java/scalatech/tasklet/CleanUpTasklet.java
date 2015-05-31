package pl.java.scalatech.tasklet;

import java.io.File;
import java.nio.file.Files;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("step")
@Slf4j
public class CleanUpTasklet implements Tasklet {
    @Value("${dictionary.target.file}")
    private String targetFile;

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        Long jobExecutionId = chunkContext.getStepContext().getStepExecution().getJobExecutionId();
        Long stepId = chunkContext.getStepContext().getStepExecution().getId();
        Long jobInstanceId = chunkContext.getStepContext().getStepExecution().getJobExecution().getJobInstance().getId();

        log.info("jobExecutionId: {} , stepId: {},  jobInstanceId: {}", jobExecutionId, stepId, jobInstanceId);
        log.info("deleting targetFile:" + targetFile);
        //guava ?>
        Files.deleteIfExists(new File(targetFile).toPath());
        return RepeatStatus.FINISHED;
    }
}