package pl.java.scalatech.utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import pl.java.scalatech.domain.Person;
import pl.java.scalatech.domain.Profession;
import pl.java.scalatech.domain.Project;
import pl.java.scalatech.domain.Technology;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public final class EntitiesFactory {

    private static List<String> techs = Lists.newArrayList("java", "maven", "gradle", "spring", "hibernate", "rest", "ws", "monitoring", "nosql", "jpa",
            "groovy", "spring-security", "batch", "boot", "jenkins", "sonar", "eclipse", "idea");
    private static List<String> projectNames = Lists.newArrayList("eb", "arimr", "idea", "touk", "bocian", "koala", "bsp", "igolf", "filmweb", "mm",
            "gaminator", "trip", "dziennik", "testament", "jpaPlugin", "restPoc", "social warehouse", "ogw", "pn", "payu", "terra");
    HashFunction hf = Hashing.md5();

    public static Person createPerson(List<Profession> proffessions, int i, List<Project> projects, Random r) {
        String passwd = Hashing.md5().hashString("passwd" + i, Charsets.UTF_8).toString();
        int prof = r.nextInt(proffessions.size() - 1);
        if (prof < 0) {
            prof = 0;
        }
        Person p = Person.builder().city("Warsaw").login("login:" + i).passwd(passwd).salary(new BigDecimal(r.nextInt(100000))).projects(projects)
                .profession(proffessions.get(prof)).build();
        return p;
    }

    public static void createProject(List<Project> projects, Random r) {
        List<Technology> techList = Lists.newArrayList();

        for (int t = 0; t < 4; t++) {
            int tch = r.nextInt(techs.size() - 1);
            if (tch < 0) {
                tch = 0;
            }
            techList.add(Technology.builder().name(techs.get(tch)).build());
        }
        for (int j = 0; j < r.nextInt(5); j++) {
            int pn = r.nextInt(projectNames.size() - 1);
            if (pn < 0) {
                pn = 0;
            }
            projects.add(Project.builder().name(projectNames.get(pn)).technologies(techList).description("aaa").build());
        }
    }

}
