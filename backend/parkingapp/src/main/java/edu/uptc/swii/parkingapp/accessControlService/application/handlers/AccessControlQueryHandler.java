package edu.uptc.swii.parkingapp.accessControlService.application.handlers;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import edu.uptc.swii.parkingapp.accessControlService.api.dtos.ReportByDateDTO;
import edu.uptc.swii.parkingapp.accessControlService.api.dtos.ReportByEmployeeDTO;
import edu.uptc.swii.parkingapp.accessControlService.application.queries.AllEmployeesByDateQuery;
import edu.uptc.swii.parkingapp.accessControlService.application.queries.EmployeeByDatesQuery;
import edu.uptc.swii.parkingapp.accessControlService.domain.models.AccessRecord;
import edu.uptc.swii.parkingapp.accessControlService.domain.ports.AccessQueryPort;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class AccessControlQueryHandler {

    private final AccessQueryPort queryPort;

    public AccessControlQueryHandler(AccessQueryPort queryPort) {
        this.queryPort = queryPort;
    }

    public List<ReportByDateDTO> handle(AllEmployeesByDateQuery query) {
        if (query == null || query.getDate() == null) {
            throw new IllegalArgumentException("Query parameters cannot be null");
        }
        
        List<AccessRecord> accesses = queryPort.findAccessesByDate(query.getDate());
        
        return accesses.stream()
            .map(this::toReportByDateDTO)
            .collect(Collectors.toList());
    }

    public List<ReportByEmployeeDTO> handle(EmployeeByDatesQuery query) {
        if (query == null || query.getEmployeeId() == null || 
            query.getStartDate() == null || query.getEndDate() == null) {
            throw new IllegalArgumentException("Query parameters cannot be null");
        }
        
        if (query.getStartDate().isAfter(query.getEndDate())) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        List<AccessRecord> accesses = queryPort.findAccessesByEmployeeAndDateRange(
            query.getEmployeeId(),
            query.getStartDate(),
            query.getEndDate()
        );

        return groupAccessRecords(accesses);
    }

    private ReportByDateDTO toReportByDateDTO(AccessRecord access) {
        if (access == null || access.getEmployeeId() == null || 
            access.getAccessDateTime() == null || access.getEventType() == null) {
            throw new IllegalArgumentException("Invalid access record data");
        }
        
        boolean isEntry = "ENTRADA".equals(access.getEventType());
        return new ReportByDateDTO(
            access.getEmployeeId(),
            "Employee: " + access.getEmployeeId(),
            isEntry ? access.getAccessDateTime() : null,
            !isEntry ? access.getAccessDateTime() : null,
            calculateDuration(access)
        );
    }

    private List<ReportByEmployeeDTO> groupAccessRecords(List<AccessRecord> accesses) {
        Map<String, List<AccessRecord>> groupedByEmployee = accesses.stream()
            .collect(Collectors.groupingBy(AccessRecord::getEmployeeId));

        return groupedByEmployee.entrySet().stream()
            .map(entry -> createEmployeeReport(entry.getKey(), entry.getValue()))
            .collect(Collectors.toList());
    }

    private ReportByEmployeeDTO createEmployeeReport(String employeeId, List<AccessRecord> records) {
        List<ReportByEmployeeDTO.AccessRecordDTO> recordDTOs = records.stream()
            .map(this::toAccessRecordDTO)
            .collect(Collectors.toList());

        return new ReportByEmployeeDTO(
            employeeId,
            "Employee: " + employeeId,
            recordDTOs,
            calculateTotalDuration(recordDTOs)
        );
    }

    private ReportByEmployeeDTO.AccessRecordDTO toAccessRecordDTO(AccessRecord access) {
        boolean isEntry = "ENTRADA".equals(access.getEventType());
        
        return new ReportByEmployeeDTO.AccessRecordDTO(
            isEntry ? access.getAccessDateTime() : null,
            !isEntry ? access.getAccessDateTime() : null,
            calculateDuration(access)
        );
    }

    private String calculateDuration(AccessRecord access) {
        if ("ENTRADA".equals(access.getEventType())) {
            Optional<AccessRecord> exitRecord = queryPort.findMatchingExit(access);
            if (exitRecord.isPresent()) {
                Duration duration = Duration.between(
                    access.getAccessDateTime(), 
                    exitRecord.get().getAccessDateTime());
                return formatDuration(duration);
            }
        }
        return "00:00";
    }

    private String calculateTotalDuration(List<ReportByEmployeeDTO.AccessRecordDTO> records) {
        long totalMinutes = records.stream()
            .filter(r -> r.getDuration() != null && !r.getDuration().equals("00:00"))
            .mapToLong(r -> {
                String[] parts = r.getDuration().split(":");
                return Long.parseLong(parts[0]) * 60 + Long.parseLong(parts[1]);
            })
            .sum();
        
        return formatDuration(Duration.ofMinutes(totalMinutes));
    }

    private String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.minusHours(hours).toMinutes();
        return String.format("%02d:%02d", hours, minutes);
    }
}