package pl.java.scalatech.config.job;



import javax.sql.DataSource;

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
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;

import pl.java.scalatech.domain.Customer;
import pl.java.scalatech.processor.CustomerProcessor;
//fistName,surname,age,salary,city,email,birthdate,login
@Configuration
public class FlatFileJob{
    @Bean
        public ItemReader<Customer> reader() {
    		FlatFileItemReader<Customer> reader = new FlatFileItemReader<>();
    		reader.setResource(new ClassPathResource("customer.csv"));
    		reader.setLineMapper(new DefaultLineMapper<Customer>() {{
    			setLineTokenizer(new DelimitedLineTokenizer() {{
    				setNames(new String[] {"firstName", "surname","age","salary","city","email","birthdate","login"});
    			}});
    			setFieldSetMapper(new BeanWrapperFieldSetMapper<Customer>() {{
    				setTargetType(Customer.class);
    			}});
    			
    		}});
    		return reader;
    	}
    	
    	@Bean
        public ItemProcessor<Customer, Customer> processor() {
            return new CustomerProcessor();
        }
    	
    	@Bean
        public ItemWriter<Customer> writer(DataSource dataSource) {
    		JdbcBatchItemWriter<Customer> writer = new JdbcBatchItemWriter<>();
    		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
    		writer.setSql("INSERT INTO people (fistName,surname,age,salary,city,email,birthdate,login) VALUES (:fistName, :surname,:age,:salary,:city,:email,:birthdate,:login)");
    		writer.setDataSource(dataSource);
    		return writer;
    	}
    
    	
    	@Bean
        public Job importUserJob(JobBuilderFactory jobs, Step step) {
    		return jobs.get("importCustomers")
    				.incrementer(new RunIdIncrementer())
    				.flow(step)
    				.end()
    				.build();
    	}
    	
    	@Bean
        public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<Customer> reader,
                ItemWriter<Customer> writer, ItemProcessor<Customer, Customer> processor) {
    		return stepBuilderFactory.get("step")
    				.<Customer, Customer> chunk(10)
    				.reader(reader)
    				.processor(processor)
    				.writer(writer)
    				.build();
    	}
    	
    	@Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    
        @Bean
        public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) {
            JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor = new JobRegistryBeanPostProcessor();
            jobRegistryBeanPostProcessor.setJobRegistry(jobRegistry);
            return jobRegistryBeanPostProcessor;
        }
}

