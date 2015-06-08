package pl.java.scalatech.config.job;

import java.text.SimpleDateFormat;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import pl.java.scalatech.config.batch.BatchConfig;
import pl.java.scalatech.config.reader.ReaderConfig;
import pl.java.scalatech.domain.Customer;
import pl.java.scalatech.repository.CustomerRepository;

@Configuration
@Import({ BatchConfig.class, ReaderConfig.class })
@Slf4j
public class AdapterWriterJob {
    static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
    // TODO
    // @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private ItemReader<Customer> genericReader;

    @Bean
    public CustomDateEditor customDateEditor() {
        return new CustomDateEditor(sdf, false);
    }

    @Bean
    public ItemProcessor<Customer, Customer> processor() {
        return item -> {
            log.info("+++  process : {}", item);
            return item;
        };
    }

    @Bean
    public Job importUserJob(Step step1, Step step2) {
        return jobs.get("simpleCustomers").incrementer(new RunIdIncrementer()).start(step1).build();
    }

    @Bean
    public ItemWriter<Customer> iwa() {
        ItemWriterAdapter<Customer> iwa = new ItemWriterAdapter<>();
        iwa.setTargetObject(customerRepository);
        iwa.setTargetMethod("save");
        return iwa;

    }

    @Bean
    public Step step1() {
        return steps.get("step1").<Customer, Customer> chunk(10).reader(genericReader).processor(processor()).writer(iwa()).build();
    }
}
