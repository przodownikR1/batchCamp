package pl.java.scalatech.config.reader;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import pl.java.scalatech.domain.Domain;

@Configuration
public class ReaderConfig<T extends Domain> {
    
        @Value("${customer.input.file}")
        private String targetFile;
        
        @Autowired
        private FieldSetMapper<T> fieldSetMapper;
        
        @Resource
        private List<String> columns;
             
        @Bean
        public ItemReader<T> genericReader() {
            FlatFileItemReader<T> bean = new FlatFileItemReader<>();
            bean.setLinesToSkip(1);
            bean.setResource(new FileSystemResource(targetFile));
            DefaultLineMapper<T> lineMapper = new DefaultLineMapper<>();
            DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
            lineTokenizer.setNames(columns.toArray(new String[columns.size()]));
            lineMapper.setLineTokenizer(lineTokenizer);
            lineMapper.setFieldSetMapper(fieldSetMapper);
            bean.setLineMapper(lineMapper);
            return bean;
        }
}

