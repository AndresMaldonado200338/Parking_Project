package edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.mongodb;

import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;
import edu.uptc.swii.parkingapp.employeeService.domain.ports.EmployeeQueryPort;
import edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.mongodb.documents.EmployeeDocument;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MongoEmployeeRepository implements EmployeeQueryPort {
    
    private final MongoEmployeeRepositoryImpl mongoRepository;

    public MongoEmployeeRepository(MongoEmployeeRepositoryImpl mongoRepository) {
        this.mongoRepository = mongoRepository;
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
}