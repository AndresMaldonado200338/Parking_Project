package edu.uptc.swii.parkingapp.accessControlService.api.dtos;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "AccessResponse", description = "DTO para respuestas de acceso de empleados")
public class AccessResponseDTO {
    @Schema(description = "ID del empleado", example = "EMP12345")
    private String employeeId;
    @Schema(description = "Tipo de evento registrado", example = "ENTRADA")
    private String eventType; // "ENTRADA" o "SALIDA"
    @Schema(description = "Fecha y hora del acceso", example = "2025-06-02T09:00:00")
    private LocalDateTime accessDateTime;
    @Schema(description = "Mensaje de respuesta", example = "Check-in registered successfully")
    private String message;
}