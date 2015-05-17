package pl.java.scalatech.domain;

import java.math.BigDecimal;
// fistName,surname,age,salary,city,email,birthdate,login
import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
public class Customer {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String surname;
    private int age;
    private BigDecimal salary;
    private String city;
    private String email;
    private LocalDate birthDate;
    private String login;
}
