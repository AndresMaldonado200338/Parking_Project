package edu.uptc.swii.parkingapp.accessControlService.domain.ports;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional; // Add this

import edu.uptc.swii.parkingapp.accessControlService.domain.models.AccessRecord;

public interface AccessQueryPort {
    List<AccessRecord> findAccessesByDate(LocalDate date);
    List<AccessRecord> findAccessesByEmployeeAndDateRange(
        String employeeId, 
        LocalDate startDate, 
        LocalDate endDate
    );
    Optional<AccessRecord> findMatchingExit(AccessRecord entryRecord); // Add this line
}