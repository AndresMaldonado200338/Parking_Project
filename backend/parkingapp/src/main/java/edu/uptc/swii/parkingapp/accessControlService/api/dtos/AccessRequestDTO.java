package edu.uptc.swii.parkingapp.accessControlService.api.dtos;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AccessRequest", description = "DTO para solicitudes de acceso al parqueadero")
public class AccessRequestDTO {
    @NotBlank(message = "Employee ID cannot be blank")
    @Size(min = 5, max = 20, message = "Employee ID must be between 5 and 20 characters")
    @Schema(description = "Identificador del empleado. Debe tener entre 5 y 20 caracteres.", example = "EMP12345", required = true)
    private String employeeId;
    
    @Schema(description = "Fecha y hora del acceso en formato ISO. Si se omite, se usa la hora actual del servidor", example = "2025-06-02T09:00:00", required = false)
    private LocalDateTime accessDateTime; // Opcional, si no se envía se usa la hora actual
}