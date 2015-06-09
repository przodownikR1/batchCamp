package pl.java.scalatech.config.jpa;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "pl.java.scalatech.repository")
@EntityScan(basePackages = "pl.java.scalatech.domain")
@EnableTransactionManagement
@EnableAutoConfiguration
@ComponentScan(basePackages = { "pl.java.scalatech.service", "pl.java.scalatech.repository" })
public class JpaConfig {

}
