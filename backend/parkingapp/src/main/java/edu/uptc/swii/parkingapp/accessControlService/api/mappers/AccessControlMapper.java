package edu.uptc.swii.parkingapp.accessControlService.api.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import edu.uptc.swii.parkingapp.accessControlService.api.dtos.AccessRequestDTO;
import edu.uptc.swii.parkingapp.accessControlService.api.dtos.AccessResponseDTO;
import edu.uptc.swii.parkingapp.accessControlService.application.commands.UserCheckInCommand;
import edu.uptc.swii.parkingapp.accessControlService.application.commands.UserCheckOutCommand;
import edu.uptc.swii.parkingapp.accessControlService.domain.models.AccessRecord;

@Component
@Mapper(componentModel = "spring")
public interface AccessControlMapper {

    // Mapeo básico para el comando de entrada
    @Mapping(target = "employeeId", source = "employeeId")
    @Mapping(target = "accessDateTime", 
             expression = "java(request.getAccessDateTime() != null ? request.getAccessDateTime() : java.time.LocalDateTime.now())")
    UserCheckInCommand toCheckInCommand(AccessRequestDTO request);

    // Mapeo básico para el comando de salida
    @Mapping(target = "employeeId", source = "employeeId")
    @Mapping(target = "accessDateTime", 
             expression = "java(request.getAccessDateTime() != null ? request.getAccessDateTime() : java.time.LocalDateTime.now())")
    UserCheckOutCommand toCheckOutCommand(AccessRequestDTO request);

    // Mapeo básico para la respuesta
    @Mapping(target = "employeeId", source = "employeeId")
    @Mapping(target = "eventType", source = "eventType")
    @Mapping(target = "accessDateTime", source = "accessDateTime")
    @Mapping(target = "message", 
             expression = "java(access.getEventType().equals(\"ENTRADA\") ? \"Check-in registered successfully\" : \"Check-out registered successfully\")")
    AccessResponseDTO toDto(AccessRecord access);
}