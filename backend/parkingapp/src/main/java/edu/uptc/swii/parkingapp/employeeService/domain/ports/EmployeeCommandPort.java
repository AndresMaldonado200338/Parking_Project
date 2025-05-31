package edu.uptc.swii.parkingapp.employeeService.domain.ports;

import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;
import java.util.Optional;

public interface EmployeeCommandPort {
    Employee saveEmployee(Employee employee);
    Employee updateEmployee(Employee employee);
    Employee disableEmployee(String document);
    Optional<Employee> findById(String document);
}