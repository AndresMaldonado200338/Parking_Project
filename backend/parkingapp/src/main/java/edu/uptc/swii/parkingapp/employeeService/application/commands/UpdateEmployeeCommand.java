package edu.uptc.swii.parkingapp.employeeService.application.commands;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEmployeeCommand {
    private String document;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
}