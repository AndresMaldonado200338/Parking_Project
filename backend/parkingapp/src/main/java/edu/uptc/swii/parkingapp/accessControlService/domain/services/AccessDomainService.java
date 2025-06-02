package edu.uptc.swii.parkingapp.accessControlService.domain.services;

import org.springframework.stereotype.Service;
import edu.uptc.swii.parkingapp.accessControlService.domain.models.AccessRecord;
import edu.uptc.swii.parkingapp.accessControlService.domain.ports.AccessCommandPort;

@Service
public class AccessDomainService {
        private final AccessCommandPort commandPort;

    public AccessDomainService(AccessCommandPort commandPort) {
        this.commandPort = commandPort;
    }

    public void validateEmployeeActive(String employeeId) {
        if (!commandPort.isEmployeeActive(employeeId)) {
            throw new IllegalArgumentException("Employee is not active");
        }
    }

    public void validateCheckInRules(AccessRecord access) {
        // Validar que no haya una entrada previa sin salida
        commandPort.findLastEntry(access.getEmployeeId())
            .ifPresent(lastEntry -> {
                if (commandPort.findLastExit(access.getEmployeeId())
                    .map(exit -> exit.getAccessDateTime().isBefore(lastEntry.getAccessDateTime()))
                    .orElse(true)) {
                    throw new IllegalStateException("Employee has pending check-out");
                }
            });
    }

    public void validateCheckOutRules(AccessRecord access) {
        // Validar que exista una entrada previa sin salida
        commandPort.findLastEntry(access.getEmployeeId())
            .ifPresentOrElse(
                lastEntry -> commandPort.findLastExit(access.getEmployeeId())
                    .ifPresent(lastExit -> {
                        if (lastExit.getAccessDateTime().isAfter(lastEntry.getAccessDateTime())) {
                            throw new IllegalStateException("No pending check-in found");
                        }
                    }),
                () -> { throw new IllegalStateException("No check-in found for employee"); }
            );
    }
}