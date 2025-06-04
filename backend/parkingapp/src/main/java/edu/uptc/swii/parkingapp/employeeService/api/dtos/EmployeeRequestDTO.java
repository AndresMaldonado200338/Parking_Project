package edu.uptc.swii.parkingapp.employeeService.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "EmployeeRequest", description = "DTO para solicitudes de empleados")
public class EmployeeRequestDTO {
    @NotBlank(message = "Document cannot be blank")
    @Size(min = 5, max = 20, message = "Document must be between 5 and 20 characters")
    @Schema(description = "Identificador del empleado. Debe tener entre 5 y 20 caracteres.", example = "EMP12345", required = true)
    private String document;
    
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    @Schema(description = "Nombre del empleado. No puede estar vacío y no debe exceder los 50 caracteres.", example = "John", required = true)
    private String firstname;
    
    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    @Schema(description = "Apellido del empleado. No puede estar vacío y no debe exceder los 50 caracteres.", example = "Doe", required = true)
    private String lastname;
    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Schema(description = "Correo electrónico del empleado. Debe ser un correo electrónico válido.", example = "johndoe@email.com", required = true)
    private String email;
    
    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be between 10-15 digits and may include '+'")
    @Schema(description = "Número de teléfono del empleado. Debe tener entre 10 y 15 dígitos y puede incluir el símbolo '+'.", example = "+1234567890", required = true)
    private String phone;
}