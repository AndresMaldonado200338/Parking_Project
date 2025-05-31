package edu.uptc.swii.parkingapp.employeeService.api.exceptions;

import edu.uptc.swii.parkingapp.employeeService.api.dtos.ErrorResponseDTO;
import org.springframework.http.*;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.validation.ObjectError;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


@Override
protected ResponseEntity<Object> handleMethodArgumentNotValid(
    MethodArgumentNotValidException ex, HttpHeaders headers,
    HttpStatusCode status, WebRequest request) {

    // Mapear errores de validación
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
            
            // setCode solo si existe
            if (error.getCode() != null) {
                validationError.setCode(error.getCode());
            }
            
            return validationError;
        })
        .collect(Collectors.toList());

    // Construir respuesta de error
    ErrorResponseDTO errorResponse = new ErrorResponseDTO();
    errorResponse.setTimestamp(LocalDateTime.now());
    errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
    errorResponse.setError("Validation Error");
    errorResponse.setMessage("Invalid request data");
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
        errorResponse.setError("Internal Server Error");
        errorResponse.setMessage(ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");
        errorResponse.setPath(getRequestPath(request));

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(errorResponse);
    }

    private ErrorResponseDTO.ValidationError mapFieldError(FieldError fieldError) {
        ErrorResponseDTO.ValidationError validationError = new ErrorResponseDTO.ValidationError();
        validationError.setField(fieldError.getField());
        validationError.setMessage(fieldError.getDefaultMessage());
        validationError.setRejectedValue(fieldError.getRejectedValue() != null ? 
            fieldError.getRejectedValue().toString() : null);
        validationError.setObjectName(fieldError.getObjectName());
        validationError.setCode(fieldError.getCode());
        return validationError;
    }

    private ErrorResponseDTO.ValidationError mapGlobalError(ObjectError globalError) {
        ErrorResponseDTO.ValidationError validationError = new ErrorResponseDTO.ValidationError();
        validationError.setMessage(globalError.getDefaultMessage());
        validationError.setObjectName(globalError.getObjectName());
        validationError.setCode(globalError.getCode());
        return validationError;
    }

    private String getRequestPath(WebRequest request) {
        String path = request.getDescription(false);
        if (path.startsWith("uri=")) {
            path = path.substring(4);
        }
        return path;
    }
}