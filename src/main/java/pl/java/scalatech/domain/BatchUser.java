package pl.java.scalatech.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BatchUser {
    @Id @GeneratedValue(strategy=GenerationType.AUTO) 
    private Long id;
    private String login;
    private Date loginDate;
    private BigDecimal salary;
    private String phone;
    private boolean logged;
    
    
}
