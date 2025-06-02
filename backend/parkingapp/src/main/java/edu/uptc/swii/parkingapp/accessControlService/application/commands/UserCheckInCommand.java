package edu.uptc.swii.parkingapp.accessControlService.application.commands;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCheckInCommand {
    private String employeeId;
    private LocalDateTime accessDateTime;
}