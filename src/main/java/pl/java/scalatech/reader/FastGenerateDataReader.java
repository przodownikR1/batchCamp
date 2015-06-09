package pl.java.scalatech.reader;

import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import pl.java.scalatech.domain.Person;
import pl.java.scalatech.domain.Profession;
import pl.java.scalatech.domain.Project;
import pl.java.scalatech.utils.EntitiesFactory;

import com.google.common.collect.Lists;

public class FastGenerateDataReader implements ItemReader<Person> {
    private static List<Person> persons = Lists.newArrayList();

    @PostConstruct
    public void init() {
        List<Profession> proffessions = Lists.newArrayList(Profession.values());
        for (int i = 0; i < 1000; i++) {
            List<Project> projects = Lists.newArrayList();
            Random r = new Random();
            EntitiesFactory.createProject(projects, r);
            persons.add(EntitiesFactory.createPerson(proffessions, i, projects, r));

        }
    }

    @Override
    public Person read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return !persons.isEmpty() ? persons.remove(0) : null;
    }

}
