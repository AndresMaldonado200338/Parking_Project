package edu.uptc.swii.parkingapp.employeeService.application.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uptc.swii.parkingapp.employeeService.api.dtos.EmployeeResponseDTO;
import edu.uptc.swii.parkingapp.employeeService.api.dtos.StatusDTO;
import edu.uptc.swii.parkingapp.employeeService.application.commands.CreateEmployeeCommand;
import edu.uptc.swii.parkingapp.employeeService.application.commands.DisableEmployeeCommand;
import edu.uptc.swii.parkingapp.employeeService.application.commands.UpdateEmployeeCommand;
import edu.uptc.swii.parkingapp.employeeService.application.services.EmployeeEventService;
import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;
import edu.uptc.swii.parkingapp.employeeService.domain.ports.EmployeeCommandPort;
import edu.uptc.swii.parkingapp.employeeService.domain.services.EmployeeDomainService;

@Service
@Transactional
public class EmployeeCommandHandler {
    
    private final EmployeeCommandPort commandPort;
    private final EmployeeDomainService domainService;
    private final EmployeeEventService eventService;

    public EmployeeCommandHandler(EmployeeCommandPort commandPort,
                                EmployeeDomainService domainService,
                                EmployeeEventService eventService) {
        this.commandPort = commandPort;
        this.domainService = domainService;
        this.eventService = eventService;
    }

    public EmployeeResponseDTO handleCreateEmployee(CreateEmployeeCommand command) {
        Employee employee = new Employee(
            command.getDocument(),
            command.getFirstname(),
            command.getLastname(),
            command.getEmail(),
            command.getPhone(),
            true
        );
        
        domainService.validateEmployee(employee);
        Employee saved = commandPort.saveEmployee(employee);
        eventService.publishEmployeeCreated(saved);
        return toResponseDTO(saved);
    }

    public EmployeeResponseDTO handleUpdateEmployee(UpdateEmployeeCommand command) {
        Employee employee = commandPort.findById(command.getDocument())
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        employee.update(
            command.getFirstname(),
            command.getLastname(),
            command.getEmail(),
            command.getPhone()
        );
        
        domainService.validateEmployee(employee);
        Employee updated = commandPort.updateEmployee(employee);
        eventService.publishEmployeeUpdated(updated);
        return toResponseDTO(updated);
    }

    public StatusDTO handleDisableEmployee(DisableEmployeeCommand command) {
        Employee employee = commandPort.findById(command.getDocument())
            .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        employee.disable();
        Employee disabled = commandPort.disableEmployee(employee.getDocument());
        eventService.publishEmployeeDisabled(disabled);
        return new StatusDTO(disabled.isStatus());
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