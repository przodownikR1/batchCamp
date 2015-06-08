package pl.java.scalatech.config.job;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.core.ItemProcessListener;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SimpleConfigListener {
    @Bean
    public ItemProcessListener<String, String> itemProcessListener() {
        return new ItemProcessListener<String, String>() {
            @Override
            public void beforeProcess(String item) {
                log.info("+++ before Process +++  {}", item);
            }

            @Override
            public void afterProcess(String item, String result) {
                log.info("+++ after Process +++  {}", item);
            }

            @Override
            public void onProcessError(String item, Exception e) {
                log.info("+++ onProcessError Process +++  {}", item);
            }

        };
    }

    @Bean
    public ItemReadListener<String> itemReaderListener() {
        return new ItemReadListener<String>() {

            @Override
            public void beforeRead() {
                log.info("+++ before reader +++ ");

            }

            @Override
            public void afterRead(String item) {
                log.info("+++ afterReader : {}", item);

            }

            @Override
            public void onReadError(Exception ex) {
                log.info("+++  onReadError {}", ex);

            }
        };
    }
}
