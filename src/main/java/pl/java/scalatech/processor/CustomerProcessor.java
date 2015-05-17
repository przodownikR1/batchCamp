package pl.java.scalatech.processor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.ItemProcessor;

import pl.java.scalatech.domain.Customer;
@Slf4j
public class CustomerProcessor implements ItemProcessor<Customer, Customer>{

    @Override
    public Customer process(Customer item) throws Exception {
        log.info("+++ customer processor : {}" ,item);
        return item;
    }

}
