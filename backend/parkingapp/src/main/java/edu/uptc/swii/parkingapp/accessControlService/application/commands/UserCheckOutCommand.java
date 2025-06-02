package edu.uptc.swii.parkingapp.accessControlService.application.commands;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCheckOutCommand {
    private String employeeId;
    private LocalDateTime accessDateTime;
}