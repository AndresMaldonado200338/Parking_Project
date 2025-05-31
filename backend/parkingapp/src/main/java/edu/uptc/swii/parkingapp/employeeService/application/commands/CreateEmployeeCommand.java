package edu.uptc.swii.parkingapp.employeeService.application.commands;


import lombok.Data;

@Data
public class CreateEmployeeCommand {
    private String document;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
}