package pl.java.scalatech.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.common.base.Stopwatch;

@Slf4j
@Service
public class SimpleTaskService {

    private final JobLauncher jobLauncher;

    private final Job job;
    @PostConstruct
    public void init() {
        log.info("Simple task service init....");
    }
    
    @Autowired
    public SimpleTaskService(Job job, JobLauncher jobLauncher) {
        this.job = job;
        this.jobLauncher = jobLauncher;
        log.info("+++  {}",SimpleTaskService.class.getSimpleName());
    }
   @Scheduled(fixedRate=2000)
    public void work() {
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> run a job begin.");

            Stopwatch stopWatch = Stopwatch.createStarted();
           
            stopWatch.start();

            Map<String, JobParameter> map = new HashMap<>();
            map.put("date", new JobParameter(new Date()));

            jobLauncher.run(job, new JobParameters(map));

            stopWatch.stop();
            log.info("execute time: " + stopWatch.elapsed(TimeUnit.MILLISECONDS) + " msec");

            log.info("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< run a job end.");

        } catch (Exception e) {
            log.error("fatal: " + e.getMessage());
        }
    }
}
