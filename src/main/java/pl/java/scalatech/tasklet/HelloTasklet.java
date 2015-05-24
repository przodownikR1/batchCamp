package pl.java.scalatech.tasklet;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import static org.springframework.batch.repeat.RepeatStatus.FINISHED;
@Slf4j
public class HelloTasklet implements Tasklet {
    public RepeatStatus execute(final StepContribution sc, final ChunkContext cc) throws Exception {
        
        log.info("First simple task ..... execute !!! ");
        log.info("+++ StepContribution :  {} ",sc);
        log.info("+++  ChunkContext  :  {}  -> jobName  : {} ",cc,cc.getStepContext().getJobName());
        log.info("+++  StepContext :  jobParameters :  {} , stepExecution : {} , stepName :  {} ",cc.getStepContext().getJobParameters(),cc.getStepContext().getStepExecution(),cc.getStepContext().getStepName());
        return FINISHED;
    }
}