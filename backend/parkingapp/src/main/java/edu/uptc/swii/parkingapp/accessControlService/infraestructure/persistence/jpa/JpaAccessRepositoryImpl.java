package edu.uptc.swii.parkingapp.accessControlService.infraestructure.persistence.jpa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.uptc.swii.parkingapp.accessControlService.infraestructure.persistence.jpa.entities.AccessEntity;

public interface JpaAccessRepositoryImpl extends JpaRepository<AccessEntity, Long> {
    Optional<AccessEntity> findTopByEmployeeIdAndEventTypeOrderByAccessDateTimeDesc(
        String employeeId, String eventType);
}