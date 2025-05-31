package edu.uptc.swii.parkingapp.employeeService.domain.services;

import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDomainService {
    
    public void validateEmployee(Employee employee) {
        if (employee.getDocument() == null || employee.getDocument().isEmpty()) {
            throw new IllegalArgumentException("Document is required");
        }
        if (employee.getFirstname() == null || employee.getFirstname().isEmpty()) {
            throw new IllegalArgumentException("Firstname is required");
        }
        if (employee.getEmail() == null || employee.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
    }
}