package edu.uptc.swii.parkingapp.employeeService.api.dtos;

import lombok.Data;

@Data
public class EmployeeEventDTO {
    private String eventType;
    private String document;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private boolean status;
    private String timestamp;
}