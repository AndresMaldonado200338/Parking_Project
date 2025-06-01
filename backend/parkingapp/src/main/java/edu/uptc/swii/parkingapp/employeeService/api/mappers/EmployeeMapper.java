package edu.uptc.swii.parkingapp.employeeService.api.mappers;

import org.mapstruct.*;
import org.springframework.data.repository.query.Param;
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
    UpdateEmployeeCommand toUpdateCommand(@Param("document") String document, 
                                       @Param("request") EmployeeRequestDTO request);
    
    EmployeeResponseDTO toDto(Employee employee);
}