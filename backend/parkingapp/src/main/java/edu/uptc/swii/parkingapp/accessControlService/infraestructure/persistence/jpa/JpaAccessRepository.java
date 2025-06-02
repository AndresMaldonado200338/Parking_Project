package edu.uptc.swii.parkingapp.accessControlService.infraestructure.persistence.jpa;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import edu.uptc.swii.parkingapp.accessControlService.domain.models.AccessRecord;
import edu.uptc.swii.parkingapp.accessControlService.domain.ports.AccessCommandPort;
import edu.uptc.swii.parkingapp.accessControlService.infraestructure.persistence.jpa.entities.AccessEntity;
import edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.jpa.JpaEmployeeRepositoryImpl;
import edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.jpa.entities.EmployeeEntity;

@Repository
public class JpaAccessRepository implements AccessCommandPort {
    
    private final JpaAccessRepositoryImpl jpaRepository;
    private final JpaEmployeeRepositoryImpl employeeRepository; // To check employee status

    public JpaAccessRepository(JpaAccessRepositoryImpl jpaRepository, JpaEmployeeRepositoryImpl employeeRepository) {
        this.jpaRepository = jpaRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public AccessRecord saveAccess(AccessRecord access) {
        AccessEntity entity = toEntity(access);
        AccessEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<AccessRecord> findLastEntry(String employeeId) {
        return jpaRepository.findTopByEmployeeIdAndEventTypeOrderByAccessDateTimeDesc(
            employeeId, "ENTRADA").map(this::toDomain);
    }

    @Override
    public Optional<AccessRecord> findLastExit(String employeeId) {
        return jpaRepository.findTopByEmployeeIdAndEventTypeOrderByAccessDateTimeDesc(
            employeeId, "SALIDA").map(this::toDomain);
    }

    @Override
    public boolean isEmployeeActive(String employeeId) { // Implement this method
        Optional<EmployeeEntity> employee = employeeRepository.findByDocument(employeeId);
        return employee.isPresent() && employee.get().isStatus();
    }

    private AccessEntity toEntity(AccessRecord access) {
        AccessEntity entity = new AccessEntity();
        entity.setEmployeeId(access.getEmployeeId());
        entity.setAccessDateTime(access.getAccessDateTime());
        entity.setEventType(access.getEventType());
        return entity;
    }

    private AccessRecord toDomain(AccessEntity entity) {
        return new AccessRecord(
            entity.getEmployeeId(),
            entity.getAccessDateTime(),
            entity.getEventType()
        );
    }
}