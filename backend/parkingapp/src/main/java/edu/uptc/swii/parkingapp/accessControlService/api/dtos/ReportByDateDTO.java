package edu.uptc.swii.parkingapp.accessControlService.api.dtos;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; // Add this
import lombok.Data;
import lombok.NoArgsConstructor; // Add this

@Data
@NoArgsConstructor // Add this
@AllArgsConstructor // Add this
@Schema(name = "ReportByDate", description = "DTO para reportes de acceso por fecha")
public class ReportByDateDTO {
    @Schema(description = "Id del empleado", example = "EMP12345")
    private String employeeId;
    @Schema(description = "Nombre del empleado", example = "Employee: EMP12345")
    private String employeeName;
    @Schema(description = "Fecha inicio", example = "2025-06-02T09:00:00")
    private LocalDateTime entryTime;
    @Schema(description = "Fecha fin", example = "2025-06-02T17:30:00")
    private LocalDateTime exitTime;
    @Schema(description = "Duración total del acceso", example = "08:30")
    private String duration; // Formato "HH:mm"
}