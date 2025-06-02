package edu.uptc.swii.parkingapp.accessControlService.application.commands;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GenerateReportCommand {
    private String employeeId;  // Para reportes individuales (opcional)
    private String date;        // Para reportes por fecha específica
    private String startDate;   // Para reportes por rango
    private String endDate;     // Para reportes por rango
    private ReportType reportType;

    public enum ReportType {
        BY_DATE,
        BY_EMPLOYEE,
        BY_EMPLOYEE_DATE_RANGE
    }
}