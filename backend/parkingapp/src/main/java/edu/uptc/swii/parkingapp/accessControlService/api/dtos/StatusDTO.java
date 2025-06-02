package edu.uptc.swii.parkingapp.accessControlService.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StatusDTO {
    private boolean status;
    private String message;
    
    // Additional constructor for simplicity
    public StatusDTO(boolean status) {
        this.status = status;
        this.message = status ? "Employee is active" : "Employee is inactive";
    }
}