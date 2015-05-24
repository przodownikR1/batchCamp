package pl.java.scalatech.config.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.java.scalatech.processor.SimpleStringProcessor;
import pl.java.scalatech.reader.SimpleStringReader;
import pl.java.scalatech.tasklet.HelloTasklet;
import pl.java.scalatech.writer.SimpleStringWriter;
@Configuration
public class SimpleStringStepsJob {
    
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    
    @Bean
    public ItemReader<String> reader() {
        return new SimpleStringReader();
    }
    @Bean
    public ItemWriter<String> writer() {
        return new SimpleStringWriter();
    }
    @Bean
    public ItemProcessor<String, String> processor() {
        return new SimpleStringProcessor();
    }  

    @Bean
    public Tasklet helloTasklet() {
        return new HelloTasklet();
    }
   
    @Bean
    public Job simpleJob(Step step1,Step step2) {
        return jobs.get("simpleStepsProcessing").incrementer(new RunIdIncrementer()).start(step1).next(step2).build();
    }
    
    @Bean
    public Step step1( ItemReader<String> reader, ItemWriter<String> writer, ItemProcessor<String, String> processor) {
        return steps.get("step1").<String,String> chunk(2).reader(reader).processor(processor).writer(writer).build();
    }
    
    @Bean
    public Step step2() {
        return steps.get("step2").tasklet(helloTasklet()).build();
    }
   
   
}