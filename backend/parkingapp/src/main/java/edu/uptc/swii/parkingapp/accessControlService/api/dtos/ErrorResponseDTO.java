package edu.uptc.swii.parkingapp.accessControlService.api.dtos;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

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
            throw new UnsupportedOperationException("Unimplemented method 'setObjectName'");
        }

        public void setCode(String code) {
            throw new UnsupportedOperationException("Unimplemented method 'setCode'");
        }
    }
}