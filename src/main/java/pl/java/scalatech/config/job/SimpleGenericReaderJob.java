package pl.java.scalatech.config.job;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.validation.BindException;

import pl.java.scalatech.config.batch.BatchConfig;
import pl.java.scalatech.config.reader.ReaderConfig;
import pl.java.scalatech.domain.Customer;

import com.google.common.collect.Lists;

@Configuration
@Import({ BatchConfig.class, ReaderConfig.class })
@Slf4j
public class SimpleGenericReaderJob {
    static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
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
    public Job importUserJob(Step step1, Step step2) {
        return jobs.get("simpleCustomers").incrementer(new RunIdIncrementer()).start(step1).build();
    }

    @Bean
    public Step step1() {
        return steps.get("step1").<Customer, Customer> chunk(10).reader(genericReader).processor(processor()).writer(writer()).build();
    }
    
    @Bean
    public FieldSetMapper<Customer> customerMapper() {
        FieldSetMapper<Customer> mapper = new FieldSetMapper<Customer>() {

            @Override
            public Customer mapFieldSet(FieldSet fieldSet) throws BindException {
                // fistName,surname,age,salary,city,email,birthdate,desc
                Customer customer = new Customer();
                customer.setFirstName(fieldSet.readString(0));
                customer.setSurname(fieldSet.readString(1));
                customer.setAge(fieldSet.readInt(2));
                customer.setSalary(fieldSet.readBigDecimal(3, new BigDecimal(0)));
                customer.setCity(fieldSet.readString(4));
                customer.setEmail(fieldSet.readString(5));
                try {
                    String birthDate = fieldSet.readString(6);
                    customer.setBirthdate(sdf.parse(birthDate));
                } catch (ParseException e) {
                    log.error("", e);
                }
                customer.setDescription(fieldSet.readString(7));
                return customer;
            }
        };
        return mapper;
    }
    
    @Bean
    public List<String> columns(){
      return  Lists.newArrayList();
    }

    @Bean
    public ItemWriter<Customer> writer() {
        return items ->
        {
            for (Customer c : items) {
                log.info("+++ write : {} ", c);
            }

        };
    }

    @Bean
    public ItemProcessor<Customer, Customer> processor() {
        return item ->
        {
            log.info("+++  process : {}", item);
            return item;
        };
    }

}
