package pl.java.scalatech.reader;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class SimpleStringReader implements ItemReader<String>{

    
    List<String> strs = newArrayList("yamaha","kawaski","suzuki","ducatti","honda","aprila","junak","romet","mz","ktm","triumph","bmw","java");
    
    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
                     
        return !strs.isEmpty() ? strs.remove(0) : null;
    }

}
