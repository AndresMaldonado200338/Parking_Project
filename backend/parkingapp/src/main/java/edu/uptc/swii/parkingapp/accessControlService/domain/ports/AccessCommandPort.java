package edu.uptc.swii.parkingapp.accessControlService.domain.ports;

import java.util.Optional;

import edu.uptc.swii.parkingapp.accessControlService.domain.models.AccessRecord;

public interface AccessCommandPort {
    AccessRecord saveAccess(AccessRecord access);
    Optional<AccessRecord> findLastEntry(String employeeId);
    Optional<AccessRecord> findLastExit(String employeeId);
    boolean isEmployeeActive(String employeeId);
}