package edu.uptc.swii.parkingapp.accessControlService.api.dtos;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class AccessEventDTO {
    private String eventType;  // "ENTRADA" o "SALIDA"
    private String employeeId;
    private LocalDateTime accessDateTime;
    private String timestamp;
}