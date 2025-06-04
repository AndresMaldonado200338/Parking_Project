package edu.uptc.swii.parkingapp.employeeService.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "DTO que representa el estado de un empleado")
public class StatusDTO {
    @Schema(description = "Estado del empleado", example = "true")
    private boolean status;
    @Schema(description = "Mensaje descriptivo del estado del empleado", example = "Employee is active")
    private String message;
    
    // Additional constructor for simplicity
    public StatusDTO(boolean status) {
        this.status = status;
        this.message = status ? "Employee is active" : "Employee is inactive";
    }
}