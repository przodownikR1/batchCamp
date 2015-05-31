package pl.java.scalatech.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.file.FlatFileFooterCallback;
import org.springframework.batch.item.file.ResourceAwareItemWriterItemStream;
import org.springframework.core.io.Resource;

import pl.java.scalatech.domain.Customer;

public class CustomerWriterWithFooter implements ResourceAwareItemWriterItemStream<Customer>, FlatFileFooterCallback {
    private ResourceAwareItemWriterItemStream<Customer> delegate;
    private int itemsProcessedSoFar = 0;

    @Override
    public void writeFooter(Writer writer) throws IOException {
        writer.write("At the end of this file, you have written " + itemsProcessedSoFar + " items");
    }
    @Override
    public void write(List<? extends Customer> items) throws Exception {
        itemsProcessedSoFar += items.size();
        delegate.write(items);
    }

    public void setDelegate(ResourceAwareItemWriterItemStream<Customer> delegate) {
        this.delegate = delegate;
    }

    @Override
    public void close() throws ItemStreamException {
        delegate.close();
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if (executionContext.containsKey("records.processed")) {
            itemsProcessedSoFar = Integer.parseInt(executionContext.get("records.processed").toString());
        }
        delegate.open(executionContext);
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.put("records.processed", itemsProcessedSoFar);
        delegate.update(executionContext);
    }

    @Override
    public void setResource(Resource arg0) {
        itemsProcessedSoFar = 0;
        delegate.setResource(arg0);
    }
}