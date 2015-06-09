package pl.java.scalatech.config.job;

import java.util.concurrent.Executors;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
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
import org.springframework.context.annotation.Scope;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import pl.java.scalatech.config.jpa.JpaConfig;
import pl.java.scalatech.domain.Person;
import pl.java.scalatech.listeners.JobLoggerListener;
import pl.java.scalatech.reader.FastGenerateDataReader;
import pl.java.scalatech.repository.PersononRepository;

@Configuration
@Slf4j
@Import(JpaConfig.class)
@ComponentScan(basePackages = "pl.java.scalatech.listeners")
public class FastProducerJob {
    private static final int THREAD_POOL_SIZE = 5;

    @Autowired
    private JobLoggerListener jobLoggerListener;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private PersononRepository personRepository;

    @Bean
    public TaskExecutor taskExecutor() {
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(THREAD_POOL_SIZE));
    }

    @Bean
    public Job simpleJob(Step step1) {
        return jobs.get("fastProcessing").incrementer(new RunIdIncrementer()).flow(step1).end().listener(jobLoggerListener).build();
    }

    @Bean
    public Step step1(ItemReader<Person> reader, ItemWriter<Person> writer, ItemProcessor<Person, Person> processor) {
        return steps.get("step1").<Person, Person> chunk(100).faultTolerant().reader(reader).processor(processor).writer(writer).build(); // .taskExecutor(taskExecutor()
    }

    @Bean
    public ItemReader<Person> reader() {
        return new FastGenerateDataReader();
    }

    @Bean
    @Scope(value = "prototype")
    public ItemWriter<Person> writer() {
        return items -> personRepository.save(items);
    }

    @Bean
    public ItemProcessor<Person, Person> processor() {
        return item -> {
            log.info("{}", item);
            return item;
        };
    }

}
