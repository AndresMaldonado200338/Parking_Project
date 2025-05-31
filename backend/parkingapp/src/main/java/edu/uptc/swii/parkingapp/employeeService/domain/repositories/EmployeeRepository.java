package edu.uptc.swii.parkingapp.employeeService.domain.repositories;

import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;
import edu.uptc.swii.parkingapp.employeeService.domain.ports.EmployeeCommandPort;
import java.util.Optional;

public interface EmployeeRepository extends EmployeeCommandPort {
    Optional<Employee> findById(String document);
}