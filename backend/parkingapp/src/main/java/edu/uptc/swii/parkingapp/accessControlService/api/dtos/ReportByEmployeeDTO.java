package edu.uptc.swii.parkingapp.accessControlService.api.dtos;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor; // Add this
import lombok.Data;
import lombok.NoArgsConstructor; // Add this

@Data
@NoArgsConstructor // Add this
@AllArgsConstructor // Add this
@Schema(name = "ReportByEmployee", description = "DTO para reportes de acceso por empleado")
public class ReportByEmployeeDTO {
    @Schema(description = "Id del empleado", example = "EMP12345")
    private String employeeId;
    @Schema(description = "Nombre del empleado", example = "Employee: EMP12345")
    private String employeeName;
    @Schema(description = "Lista de reportes", example = "{\r\n" + //
            "        \"entryTime\": \"2025-06-02T09:00:00\",\r\n" + //
            "        \"exitTime\": \"2025-06-02T17:30:00\",\r\n" + //
            "        \"duration\": \"08:30\"\r\n" + //
            "      }")
    private List<AccessRecordDTO> accessRecords;
    @Schema(description = "Duración total en el período", example = "17:00")
    private String totalDuration; // Duración total en el período

    @Data
    @NoArgsConstructor // Add this
    @AllArgsConstructor // Add this
    public static class AccessRecordDTO {
        @Schema(description = "Fecha y hora de entrada", example = "2025-06-02T09:00:00")
        private LocalDateTime entryTime;
        @Schema(description = "Fecha y hora de salida", example = "2025-06-02T17:30:00")
        private LocalDateTime exitTime;
        @Schema(description = "Duración formateada", example = "08:30")
        private String duration;
    }
}