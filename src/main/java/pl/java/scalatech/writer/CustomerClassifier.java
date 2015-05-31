package pl.java.scalatech.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.classify.Classifier;

import pl.java.scalatech.domain.Customer;

public class CustomerClassifier implements Classifier<Customer, ItemWriter<Customer>> {

    private ItemWriter<Customer> fileItemWriter;
    private ItemWriter<Customer> jdbcItemWriter;

    @Override
    public ItemWriter<Customer> classify(Customer customer) {
        if(customer.getAge() > 50) {
            return fileItemWriter;
        }
        return jdbcItemWriter;
    }

    public void setFileItemWriter(ItemWriter<Customer> fileItemWriter) {
        this.fileItemWriter = fileItemWriter;
    }

    public void setJdbcItemWriter(ItemWriter<Customer> jdbcItemWriter) {
        this.jdbcItemWriter = jdbcItemWriter;
    }
}