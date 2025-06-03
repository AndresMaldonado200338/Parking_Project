package edu.uptc.swii.parkingapp.accessControlService.application.queries;

import java.time.LocalDate;

public class EmployeeByDatesQuery {
    private String employeeId;
    private LocalDate startDate;
    private LocalDate endDate;

    public EmployeeByDatesQuery() {
        this.employeeId = "";
        this.startDate = LocalDate.now().minusDays(7); 
        this.endDate = LocalDate.now();
    }

    public EmployeeByDatesQuery(String employeeId, String startDate, String endDate) {
        this.employeeId = employeeId;
        this.startDate = LocalDate.parse(startDate);
        this.endDate = LocalDate.parse(endDate);
    }

    // Getters
    public String getEmployeeId() {
        return employeeId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}