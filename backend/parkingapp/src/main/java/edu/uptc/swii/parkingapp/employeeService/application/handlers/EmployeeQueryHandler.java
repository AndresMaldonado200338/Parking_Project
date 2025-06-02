package edu.uptc.swii.parkingapp.employeeService.application.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uptc.swii.parkingapp.employeeService.api.dtos.EmployeeResponseDTO;
import edu.uptc.swii.parkingapp.employeeService.application.queries.FindAllEmployeesQuery;
import edu.uptc.swii.parkingapp.employeeService.application.queries.FindByDocumentQuery;
import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;
import edu.uptc.swii.parkingapp.employeeService.domain.ports.EmployeeQueryPort;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class EmployeeQueryHandler {
    
    private final EmployeeQueryPort queryPort;

    public EmployeeQueryHandler(EmployeeQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    public List<EmployeeResponseDTO> handle(FindAllEmployeesQuery query) {
        List<Employee> employees = queryPort.findAllEmployees();
        return employees.stream()
            .map(this::toResponseDTO)
            .collect(Collectors.toList());
    }

    public EmployeeResponseDTO handle(FindByDocumentQuery query) {
       return queryPort.findByDocument(query.getDocument())
            .map(this::toResponseDTO)
            .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    private EmployeeResponseDTO toResponseDTO(Employee employee) {
        return new EmployeeResponseDTO(
            employee.getDocument(),
            employee.getFirstname(),
            employee.getLastname(),
            employee.getEmail(),
            employee.getPhone(),
            employee.isStatus()
        );
    }
}
