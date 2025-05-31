package edu.uptc.swii.parkingapp.employeeService.application.commands;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DisableEmployeeCommand {
    private String document;
}
