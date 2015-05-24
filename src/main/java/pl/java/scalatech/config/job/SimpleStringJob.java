package pl.java.scalatech.config.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.java.scalatech.processor.SimpleStringProcessor;
import pl.java.scalatech.reader.SimpleStringReader;
import pl.java.scalatech.writer.SimpleStringWriter;

@Configuration
public class SimpleStringJob {

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
    public Job simpleJob(JobBuilderFactory jobs, Step step) {
        return jobs.get("simpleStringProcessing")
                .incrementer(new RunIdIncrementer())
                .flow(step)
                .end()
                .build();
    }
    
    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<String> reader,
            ItemWriter<String> writer, ItemProcessor<String, String> processor) {
        return stepBuilderFactory.get("step1")
                .<String,String> chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    //https://github.com/Endron/dataPumpDemo.git
    @Bean
    public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
        JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
        jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
        return jobRegistryBeanPostProcessor;
    }
}
