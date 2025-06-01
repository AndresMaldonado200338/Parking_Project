package edu.uptc.swii.parkingapp.employeeService.domain.ports;

import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeQueryPort {
    Employee save(Employee employee);
    Optional<Employee> findByDocument(String document);
    List<Employee> findAllEmployees();
    Employee updateEmployee(Employee employee);
    Employee disableEmployee(String document);
}