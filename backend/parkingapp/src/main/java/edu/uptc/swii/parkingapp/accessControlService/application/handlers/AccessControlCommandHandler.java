package edu.uptc.swii.parkingapp.accessControlService.application.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.uptc.swii.parkingapp.accessControlService.api.dtos.AccessResponseDTO;
import edu.uptc.swii.parkingapp.accessControlService.application.commands.UserCheckInCommand;
import edu.uptc.swii.parkingapp.accessControlService.application.commands.UserCheckOutCommand;
import edu.uptc.swii.parkingapp.accessControlService.application.services.AccessEventService;
import edu.uptc.swii.parkingapp.accessControlService.domain.models.AccessRecord;
import edu.uptc.swii.parkingapp.accessControlService.domain.ports.AccessCommandPort;
import edu.uptc.swii.parkingapp.accessControlService.domain.services.AccessDomainService;

@Service
@Transactional
public class AccessControlCommandHandler {

    private final AccessCommandPort commandPort;
    private final AccessDomainService domainService;
    private final AccessEventService eventService;

    public AccessControlCommandHandler(AccessCommandPort commandPort,
                                    AccessDomainService domainService,
                                    AccessEventService eventService) {
        this.commandPort = commandPort;
        this.domainService = domainService;
        this.eventService = eventService;
    }

    public AccessResponseDTO handleCheckIn(UserCheckInCommand command) {
        // 1. Verificar que el empleado existe y está activo
        domainService.validateEmployeeActive(command.getEmployeeId());

        // 2. Crear registro de acceso
        AccessRecord access = new AccessRecord(
            command.getEmployeeId(),
            command.getAccessDateTime(),
            "ENTRADA"
        );

        // 3. Validar reglas de negocio (ej: no entrada duplicada)
        domainService.validateCheckInRules(access);

        // 4. Guardar en MySQL
        AccessRecord saved = commandPort.saveAccess(access);

        // 5. Publicar evento
        eventService.publishAccessEvent(saved);

        return toResponseDTO(saved);
    }

    public AccessResponseDTO handleCheckOut(UserCheckOutCommand command) {
        // 1. Verificar que el empleado existe y está activo
        domainService.validateEmployeeActive(command.getEmployeeId());

        // 2. Crear registro de acceso
        AccessRecord access = new AccessRecord(
            command.getEmployeeId(),
            command.getAccessDateTime(),
            "SALIDA"
        );

        // 3. Validar reglas de negocio (ej: debe existir entrada previa)
        domainService.validateCheckOutRules(access);

        // 4. Guardar en MySQL
        AccessRecord saved = commandPort.saveAccess(access);

        // 5. Publicar evento
        eventService.publishAccessEvent(saved);

        return toResponseDTO(saved);
    }

    private AccessResponseDTO toResponseDTO(AccessRecord access) {
        return new AccessResponseDTO(
            access.getEmployeeId(),
            access.getEventType(),
            access.getAccessDateTime(),
            access.getEventType().equals("ENTRADA") 
                ? "Check-in registered successfully" 
                : "Check-out registered successfully"
        );
    }
}