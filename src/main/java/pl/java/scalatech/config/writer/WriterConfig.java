package pl.java.scalatech.config.writer;

import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.java.scalatech.domain.Domain;

@Configuration
public class WriterConfig<T extends Domain> {

    
    @Bean
    public ItemWriter<T> genericWriter() {
        return ;
    
    }
}
