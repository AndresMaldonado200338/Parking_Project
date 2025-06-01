package edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.mongodb;

import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;
import edu.uptc.swii.parkingapp.employeeService.domain.ports.EmployeeQueryPort;
import edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.mongodb.documents.EmployeeDocument;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class MongoEmployeeRepository implements EmployeeQueryPort {
    
    private final MongoEmployeeRepositoryImpl mongoRepository;

    public MongoEmployeeRepository(MongoEmployeeRepositoryImpl mongoRepository) {
        this.mongoRepository = mongoRepository;
    }

@Override
public Employee save(Employee employee) {
    try {
        EmployeeDocument document = new EmployeeDocument();
        document.setDocument(employee.getDocument());
        document.setFirstname(employee.getFirstname());
        document.setLastname(employee.getLastname());
        document.setEmail(employee.getEmail());
        document.setPhone(employee.getPhone());
        document.setStatus(employee.isStatus());
        
        EmployeeDocument saved = mongoRepository.save(document);
        return toDomain(saved);
    } catch (Exception e) {
        log.error("Error guardando empleado en MongoDB", e);
        throw new RuntimeException("Error guardando empleado en MongoDB", e);
    }
}

    @Override
    public Optional<Employee> findByDocument(String document) {
        return mongoRepository.findByDocument(document)
            .map(this::toDomain);
    }

    @Override
    public List<Employee> findAllEmployees() {
        return mongoRepository.findAll()
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Employee updateEmployee(Employee employee) {
        EmployeeDocument document = mongoRepository.findByDocument(employee.getDocument())
            .orElseThrow(() -> new RuntimeException("Employee not found in MongoDB"));
        
        updateDocumentFromDomain(document, employee);
        return toDomain(mongoRepository.save(document));
    }

    @Override
    public Employee disableEmployee(String document) {
        EmployeeDocument entity = mongoRepository.findByDocument(document)
            .orElseThrow(() -> new RuntimeException("Employee not found in MongoDB"));
        
        entity.setStatus(false);
        return toDomain(mongoRepository.save(entity));
    }

    private EmployeeDocument toDocument(Employee employee) {
        EmployeeDocument document = new EmployeeDocument();
        document.setDocument(employee.getDocument());
        document.setFirstname(employee.getFirstname());
        document.setLastname(employee.getLastname());
        document.setEmail(employee.getEmail());
        document.setPhone(employee.getPhone());
        document.setStatus(employee.isStatus());
        return document;
    }

    private Employee toDomain(EmployeeDocument document) {
        return new Employee(
            document.getDocument(),
            document.getFirstname(),
            document.getLastname(),
            document.getEmail(),
            document.getPhone(),
            document.isStatus()
        );
    }

    private void updateDocumentFromDomain(EmployeeDocument document, Employee employee) {
        document.setFirstname(employee.getFirstname());
        document.setLastname(employee.getLastname());
        document.setEmail(employee.getEmail());
        document.setPhone(employee.getPhone());
        document.setStatus(employee.isStatus());
    }
}