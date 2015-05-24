package pl.java.scalatech.processor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.batch.item.ItemProcessor;

import pl.java.scalatech.domain.BatchUser;
@Slf4j
public class BatchUserProcessor implements ItemProcessor<BatchUser, BatchUser> {

    @Override
    public BatchUser process(BatchUser item) throws Exception {
        log.info("+++ item");
        return item;
    }

}
