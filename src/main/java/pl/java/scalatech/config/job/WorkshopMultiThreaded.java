package pl.java.scalatech.config.job;

import java.util.Collections;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;

import pl.java.scalatech.config.concurrent.ThreadConfig;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;

@Configuration
@Import(ThreadConfig.class)
@ImportResource("classpath:multiThreadedSteps.xml")
@Slf4j
public class WorkshopMultiThreaded {
    @Bean
    public ItemReader<String> reader() {
        final List<Integer> list = Collections.synchronizedList(Lists.newArrayList(ContiguousSet.create(Range.closed(1, 100), DiscreteDomain.integers())));
        return () -> {
            if (list.isEmpty()) { return null; }
            Integer item = list.remove(0);
            log.info("Reading {} from {}", item, Thread.currentThread());
            return item.toString();
        };
    }

    @Bean
    public ItemProcessor<String, String> processor() {
        return item -> {
            log.info("Processing {} from {}", item, Thread.currentThread());
            return item;
        };
    }

    @Bean
    public ItemWriter<String> writer() {
        return items -> log.info("Writing {} from {}", items, Thread.currentThread());
    }
}
