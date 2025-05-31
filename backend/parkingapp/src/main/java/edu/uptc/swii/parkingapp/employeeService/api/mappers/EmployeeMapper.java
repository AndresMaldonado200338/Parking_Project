package edu.uptc.swii.parkingapp.employeeService.api.mappers;



import org.mapstruct.*;
import org.springframework.stereotype.Component;

import edu.uptc.swii.parkingapp.employeeService.api.dtos.EmployeeRequestDTO;
import edu.uptc.swii.parkingapp.employeeService.api.dtos.EmployeeResponseDTO;
import edu.uptc.swii.parkingapp.employeeService.application.commands.CreateEmployeeCommand;
import edu.uptc.swii.parkingapp.employeeService.application.commands.UpdateEmployeeCommand;
import edu.uptc.swii.parkingapp.employeeService.domain.models.Employee;

@Component
@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    
    CreateEmployeeCommand toCommand(EmployeeRequestDTO request);
    
    @Mapping(target = "document", source = "document")
    @Mapping(target = "firstname", source = "request.firstname")
    @Mapping(target = "lastname", source = "request.lastname")
    @Mapping(target = "email", source = "request.email")
    @Mapping(target = "phone", source = "request.phone")
    UpdateEmployeeCommand toUpdateCommand(String document, EmployeeRequestDTO request);
    
    @Mapping(target = "document", source = "document")
    @Mapping(target = "firstname", source = "firstname")
    @Mapping(target = "lastname", source = "lastname")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "phone", source = "phone")
    @Mapping(target = "status", source = "status")
    EmployeeResponseDTO toDto(Employee employee);
}