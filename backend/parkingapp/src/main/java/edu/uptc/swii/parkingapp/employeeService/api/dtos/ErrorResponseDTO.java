package edu.uptc.swii.parkingapp.employeeService.api.dtos;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ErrorResponseDTO {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<ValidationError> validationErrors;
    
    @Data
    public static class ValidationError {
        private String field;
        private String message;
        private String rejectedValue;
        public void setObjectName(String objectName) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'setObjectName'");
        }
        public void setCode(String code) {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'setCode'");
        }
    }
}