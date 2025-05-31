package edu.uptc.swii.parkingapp.employeeService.api.dtos;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReportResponseDTO {
    private String document;
    private String employeeName;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private String duration;
}