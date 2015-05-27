package pl.java.scalatech;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.batch.BatchAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Import;

import pl.java.scalatech.config.web.MainConfiguration;

@SpringBootApplication
/*@Import(MainConfiguration.class)
@EnableAutoConfiguration(exclude = { 
        WebMvcAutoConfiguration.class })*/
public class BatchCampApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(BatchCampApplication.class, args);
    }
  /*  @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BatchCampApplication.class);
    }*/
}
