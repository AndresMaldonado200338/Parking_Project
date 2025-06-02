package edu.uptc.swii.parkingapp.accessControlService.api.dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor; // Add this
import lombok.Data;
import lombok.NoArgsConstructor; // Add this

@Data
@NoArgsConstructor // Add this
@AllArgsConstructor // Add this
public class ReportByDateDTO {
    private String employeeId;
    private String employeeName;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private String duration; // Formato "HH:mm"
}