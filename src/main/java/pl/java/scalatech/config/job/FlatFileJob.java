package pl.java.scalatech.config.job;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.BindException;

import pl.java.scalatech.config.batch.BatchConfig;
import pl.java.scalatech.domain.BatchUser;
import pl.java.scalatech.domain.Customer;
import pl.java.scalatech.processor.BatchUserProcessor;
import pl.java.scalatech.processor.CustomerProcessor;

import com.google.common.collect.Maps;

@Configuration
@Import(BatchConfig.class)
@Slf4j
public class FlatFileJob {
    static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yy");
    @Autowired
    private JobBuilderFactory jobs;

    @Autowired
    private StepBuilderFactory steps;

    @Bean
    public CustomDateEditor customDateEditor() {
        return new CustomDateEditor(sdf, false);
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
    ItemReader<BatchUser> batchUserReader() {
        // login,loginDate,salary,phone,logged
        FlatFileItemReader<BatchUser> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource("batchUser.csv"));
        reader.setLineMapper(new DefaultLineMapper<BatchUser>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "login", "loginDate", "salary", "phone", "logged" });
                    }
                });
                setFieldSetMapper(new BeanWrapperFieldSetMapper<BatchUser>() {
                    {
                        setTargetType(BatchUser.class);
                        Map<String, CustomDateEditor> maps = Maps.newHashMap();
                        maps.put("java.util.Date", customDateEditor());
                        setCustomEditors(maps);
                    }
                });
            }
        });
        return reader;
    }

    @Bean
    public ItemReader<Customer> reader() {
        FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
        reader.setLinesToSkip(1);
        reader.setResource(new ClassPathResource("customer.csv"));
        reader.setLineMapper(new DefaultLineMapper<Customer>() {
            {
                setLineTokenizer(new DelimitedLineTokenizer() {
                    {
                        setNames(new String[] { "firstName", "surname", "age", "salary", "city", "email", "birthdate", "description" });
                    }
                });

                setFieldSetMapper(customerMapper());

            }
        });
        return reader;
    }

    @Bean
    public ItemProcessor<Customer, Customer> processor() {
        return new CustomerProcessor();
    }

    @Bean
    ItemProcessor<BatchUser, BatchUser> processorBatchUser() {
        return new BatchUserProcessor();
    }

    @Bean
    public ItemWriter<BatchUser> writeBatchUser(DataSource dataSource) {
        // login,loginDate,salary,phone,logged
        JdbcBatchItemWriter<BatchUser> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<BatchUser>());
        writer.setSql("INSERT INTO batchUser(login,loginDate,salary,phone,logged) VALUES (:login, :loginDate,:salary,:phone,:logged)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public ItemWriter<Customer> writer(DataSource dataSource) {
        JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
        writer.setSql("INSERT INTO customer(firstName,surname,age,salary,city,email,birthdate,description) VALUES (:firstName, :surname,:age,:salary,:city,:email,:birthdate,:description)");
        writer.setDataSource(dataSource);
        return writer;
    }

    @Bean
    public Job importUserJob(Step step1, Step step2) {
        return jobs.get("importCustomers").incrementer(new RunIdIncrementer()).start(step1).next(step2).build();
    }

    @Bean
    public Step step1(ItemReader<Customer> reader, ItemWriter<Customer> writer, ItemProcessor<Customer, Customer> processor) {
        return steps.get("step1").<Customer, Customer> chunk(10).reader(reader).processor(processor).writer(writer).build();
    }

    @Bean
    public Step step2(ItemReader<BatchUser> reader, ItemWriter<BatchUser> writer, ItemProcessor<BatchUser, BatchUser> processorBatchUser) {
        return steps.get("step2").<BatchUser, BatchUser> chunk(10).reader(reader).processor(processorBatchUser).writer(writer).build();
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

   
}
