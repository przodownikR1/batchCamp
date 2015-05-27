package pl.java.scalatech.config.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/org/springframework/batch/admin/web/resources/servlet-config.xml")
public class ServletConfiguration {

}