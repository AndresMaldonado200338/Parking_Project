package edu.uptc.swii.parkingapp.accessControlService.api.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor; // Add this
import lombok.Data;
import lombok.NoArgsConstructor; // Add this

@Data
@NoArgsConstructor // Add this
@AllArgsConstructor // Add this
public class ReportByEmployeeDTO {
    private String employeeId;
    private String employeeName;
    private List<AccessRecordDTO> accessRecords;
    private String totalDuration; // Duración total en el período
    
    @Data
    @NoArgsConstructor // Add this
    @AllArgsConstructor // Add this
    public static class AccessRecordDTO {
        private LocalDateTime entryTime;
        private LocalDateTime exitTime;
        private String duration;
    }
}