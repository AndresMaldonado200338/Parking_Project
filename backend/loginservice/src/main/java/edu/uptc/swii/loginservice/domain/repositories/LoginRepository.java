package edu.uptc.swii.loginservice.domain.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.uptc.swii.loginservice.domain.models.Login;

public interface LoginRepository extends JpaRepository<Login, Long> {
    Optional<Login> findByUserID(Long userID);
}