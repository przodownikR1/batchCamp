package pl.java.scalatech.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.java.scalatech.domain.BatchUser;


public interface BatchUserRepository extends JpaRepository<BatchUser, Long>{

}
