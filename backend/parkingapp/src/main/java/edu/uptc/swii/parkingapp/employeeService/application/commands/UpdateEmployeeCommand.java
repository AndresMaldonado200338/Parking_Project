package edu.uptc.swii.parkingapp.employeeService.application.commands;


import lombok.Data;

@Data
public class UpdateEmployeeCommand {
    private String document;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    
    public UpdateEmployeeCommand(String document, CreateEmployeeCommand command) {
        this.document = document;
        this.firstname = command.getFirstname();
        this.lastname = command.getLastname();
        this.email = command.getEmail();
        this.phone = command.getPhone();
    }
}