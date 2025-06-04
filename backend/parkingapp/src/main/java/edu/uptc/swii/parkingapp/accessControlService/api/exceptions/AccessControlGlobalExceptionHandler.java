package edu.uptc.swii.parkingapp.accessControlService.api.exceptions;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import edu.uptc.swii.parkingapp.accessControlService.api.dtos.ErrorResponseDTO;
import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@ControllerAdvice
public class AccessControlGlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers,
        HttpStatusCode status, WebRequest request) {

        List<ErrorResponseDTO.ValidationError> errors = ex.getBindingResult()
            .getAllErrors()
            .stream()
            .map(error -> {
                ErrorResponseDTO.ValidationError validationError = new ErrorResponseDTO.ValidationError();
                
                if (error instanceof FieldError) {
                    FieldError fieldError = (FieldError) error;
                    validationError.setField(fieldError.getField());
                    validationError.setRejectedValue(fieldError.getRejectedValue() != null ? 
                        fieldError.getRejectedValue().toString() : null);
                }
                
                validationError.setMessage(error.getDefaultMessage());
                validationError.setObjectName(error.getObjectName());
                
                if (error.getCode() != null) {
                    validationError.setCode(error.getCode());
                }
                
                return validationError;
            })
            .collect(Collectors.toList());

        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("Validation Error");
        errorResponse.setMessage("Invalid access control data");
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        errorResponse.setValidationErrors(errors);

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setError("Access Control Service Error");
        errorResponse.setMessage(ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred in access control service");
        errorResponse.setPath(getRequestPath(request));

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse);
    }

    // Podemos añadir excepciones específicas del dominio de control de acceso
    @ExceptionHandler(InvalidAccessOperationException.class)
    public ResponseEntity<ErrorResponseDTO> handleInvalidAccessOperation(InvalidAccessOperationException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("Invalid Access Operation");
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(getRequestPath(request));

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse);
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmployeeNotFound(EmployeeNotFoundException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.NOT_FOUND.value());
        errorResponse.setError("Employee Not Found");
        errorResponse.setMessage(ex.getMessage());
        errorResponse.setPath(getRequestPath(request));

        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse);
    }

    private String getRequestPath(WebRequest request) {
        String path = request.getDescription(false);
        if (path.startsWith("uri=")) {
            path = path.substring(4);
        }
        return path;
    }
}