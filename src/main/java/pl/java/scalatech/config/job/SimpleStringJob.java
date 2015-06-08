package pl.java.scalatech.config.job;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import pl.java.scalatech.listeners.JobLoggerListener;
import pl.java.scalatech.processor.SimpleStringProcessor;
import pl.java.scalatech.reader.SimpleStringReader;
import pl.java.scalatech.writer.SimpleStringWriter;

@Configuration
@Slf4j
@Import(SimpleConfigListener.class)
@ComponentScan(basePackages = "pl.java.scalatech.listeners")
public class SimpleStringJob {

    @Autowired
    private JobExecutionListener jobListener;

    @Autowired
    private ItemProcessListener<String, String> itemProcessListener;

    @Autowired
    private ChunkListener chunkBatchListener;

    @Autowired
    private ItemReadListener<String> itemReadListener;

    @Bean
    public JobExecutionListener jobListener() {
        return new JobLoggerListener();
    }

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
        return jobs.get("simpleStringProcessing").incrementer(new RunIdIncrementer()).flow(step1).end().listener(jobListener).build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<String> reader, ItemWriter<String> writer, ItemProcessor<String, String> processor) {
        return stepBuilderFactory.get("step").<String, String> chunk(3).reader(reader).processor(processor).writer(writer).listener(itemProcessListener)
                .listener(itemReadListener).listener(chunkBatchListener).build();
    }

}
