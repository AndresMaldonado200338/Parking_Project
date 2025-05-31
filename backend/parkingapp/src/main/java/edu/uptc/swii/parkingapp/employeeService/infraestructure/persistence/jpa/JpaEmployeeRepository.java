package edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.jpa;

import org.springframework.stereotype.Repository;

import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;
import edu.uptc.swii.parkingapp.employeeService.domain.ports.EmployeeCommandPort;
import edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.jpa.entities.EmployeeEntity;

import java.util.Optional;

@Repository
public class JpaEmployeeRepository implements EmployeeCommandPort {
    
    private final JpaEmployeeRepositoryImpl jpaRepository;

    public JpaEmployeeRepository(JpaEmployeeRepositoryImpl jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        // Verificar si el empleado ya existe para determinar si es creación o actualización
        Optional<EmployeeEntity> existingEntity = jpaRepository.findById(employee.getDocument());
        
        EmployeeEntity entity;
        if (existingEntity.isPresent()) {
            // Actualización
            entity = existingEntity.get();
            updateEntityFromDomain(entity, employee);
        } else {
            // Creación
            entity = toEntity(employee);
        }
        
        EmployeeEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        EmployeeEntity entity = jpaRepository.findById(employee.getDocument())
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        updateEntityFromDomain(entity, employee);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Employee disableEmployee(String document) {
        EmployeeEntity entity = jpaRepository.findById(document)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        entity.setStatus(false);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Employee> findById(String document) {
        return jpaRepository.findByDocument(document)  // Cambiado a findByDocument
            .map(this::toDomain);
    }

    private EmployeeEntity toEntity(Employee employee) {
        EmployeeEntity entity = new EmployeeEntity();
        entity.setDocument(employee.getDocument());
        entity.setFirstname(employee.getFirstname());
        entity.setLastname(employee.getLastname());
        entity.setEmail(employee.getEmail());
        entity.setPhone(employee.getPhone());
        entity.setStatus(employee.isStatus());
        return entity;
    }

    private Employee toDomain(EmployeeEntity entity) {
        return new Employee(
            entity.getDocument(),
            entity.getFirstname(),
            entity.getLastname(),
            entity.getEmail(),
            entity.getPhone(),
            entity.isStatus()
        );
    }

    private void updateEntityFromDomain(EmployeeEntity entity, Employee employee) {
        // No actualizamos el documento ya que es el ID
        entity.setFirstname(employee.getFirstname());
        entity.setLastname(employee.getLastname());
        entity.setEmail(employee.getEmail());
        entity.setPhone(employee.getPhone());
        entity.setStatus(employee.isStatus());
    }
}