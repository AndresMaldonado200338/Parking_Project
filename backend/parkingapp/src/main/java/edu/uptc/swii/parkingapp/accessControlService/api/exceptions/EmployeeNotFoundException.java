package edu.uptc.swii.parkingapp.accessControlService.api.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(String employeeId) {
        super("Employee with ID " + employeeId + " not found or inactive");
    }
}