package edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.mongodb.documents.EmployeeDocument;

import java.util.Optional;

public interface MongoEmployeeRepositoryImpl extends MongoRepository<EmployeeDocument, String> {
    Optional<EmployeeDocument> findByDocument(String document);
}