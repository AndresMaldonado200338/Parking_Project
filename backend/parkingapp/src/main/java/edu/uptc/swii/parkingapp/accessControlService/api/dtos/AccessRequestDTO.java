package edu.uptc.swii.parkingapp.accessControlService.api.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestDTO {
    @NotBlank(message = "Employee ID cannot be blank")
    @Size(min = 5, max = 20, message = "Employee ID must be between 5 and 20 characters")
    private String employeeId;
    
    private LocalDateTime accessDateTime; // Opcional, si no se envía se usa la hora actual
}