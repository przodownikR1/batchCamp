package pl.java.scalatech.config.batch.firstTasklet;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.repeat.RepeatStatus;

@Configuration
@Slf4j
public class ControlFlowTaskletConfig {
   

    @Autowired
    StepBuilderFactory stepBuilderFactory;

    @Bean
    Job job(JobBuilderFactory jobBuilderFactory) {
        return jobBuilderFactory.get("job").start(step1()).on("FAILED").to(step2()).from(step1()).on("COMPLETED").to(step3()).end().build();
    }

    Step step1() {
        return stepBuilderFactory.get("step1").tasklet((stepContribution, chunkContext) ->
        {
            log.info("Executed 1 step");
            stepContribution.setExitStatus(ExitStatus.FAILED);
            return RepeatStatus.FINISHED;

        }).build();
    }

    Step step2() {
        return stepBuilderFactory.get("step2").tasklet((stepContribution, chunkContext) ->
        {
            log.info("Executed 2 step. Step 1 FAILED.");
            return null;
        }).build();
    }

    Step step3() {
        return stepBuilderFactory.get("step3").tasklet((stepContribution, chunkContext) ->
        {
            log.info("Executed 3 step. Step 1 COMPLETED");
            return RepeatStatus.FINISHED;
        }).build();
    }

}
