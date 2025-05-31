package edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.jpa.entities.EmployeeEntity;

import java.util.Optional;

@Repository
public interface JpaEmployeeRepositoryImpl extends JpaRepository<EmployeeEntity, String> {
    Optional<EmployeeEntity> findByDocument(String document);
}