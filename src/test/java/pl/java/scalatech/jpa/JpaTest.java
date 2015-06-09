package pl.java.scalatech.jpa;

import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pl.java.scalatech.config.jpa.JpaConfig;
import pl.java.scalatech.domain.Person;
import pl.java.scalatech.domain.Profession;
import pl.java.scalatech.domain.Project;
import pl.java.scalatech.repository.PersonRepository;
import pl.java.scalatech.utils.EntitiesFactory;

import com.google.common.collect.Lists;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
@SpringApplicationConfiguration(classes = { JpaConfig.class })
public class JpaTest {

    private final static List<Person> persons = Lists.newArrayList();
    @Autowired
    private PersonRepository personRepository;

    @PostConstruct
    public void init() {
        List<Profession> proffessions = Lists.newArrayList(Profession.values());
        for (int i = 0; i < 10; i++) {
            List<Project> projects = Lists.newArrayList();
            Random r = new Random();
            EntitiesFactory.createProject(projects, r);
            persons.add(EntitiesFactory.createPerson(proffessions, i, projects, r));

        }
    }

    @Test
    public void shouldBootstrapWork() {
        persons.stream().forEach(p -> personRepository.save(p));
    }

}
