package edu.uptc.swii.parkingapp.accessControlService.api.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessResponseDTO {
    private String employeeId;
    private String eventType; // "ENTRADA" o "SALIDA"
    private LocalDateTime accessDateTime;
    private String message;
}