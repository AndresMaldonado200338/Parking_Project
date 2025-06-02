package edu.uptc.swii.parkingapp.accessControlService.application.queries;

import java.time.LocalDate;

public class AllEmployeesByDateQuery {
    private LocalDate date;
    private boolean includeInactive;

    public AllEmployeesByDateQuery() {
        this.date = LocalDate.now();
        this.includeInactive = false;
    }

    public AllEmployeesByDateQuery(LocalDate date, boolean includeInactive) {
        this.date = date;
        this.includeInactive = includeInactive;
    }

    public AllEmployeesByDateQuery(String dateString) {
        this.date = LocalDate.parse(dateString);
        this.includeInactive = false;
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public boolean isIncludeInactive() {
        return includeInactive;
    }
}