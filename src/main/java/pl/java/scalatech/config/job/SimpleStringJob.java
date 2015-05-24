package pl.java.scalatech.config.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
    public Job simpleJob(JobBuilderFactory jobs, Step step1) {
        return jobs.get("simpleStringProcessing")
                .incrementer(new RunIdIncrementer())
                .flow(step1)
                .end()
                .build();
    }
    
    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<String> reader,
            ItemWriter<String> writer, ItemProcessor<String, String> processor) {
        return stepBuilderFactory.get("step")
                .<String,String> chunk(3)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

   
}
