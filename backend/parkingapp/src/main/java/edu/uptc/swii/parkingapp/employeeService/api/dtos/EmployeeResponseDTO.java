package edu.uptc.swii.parkingapp.employeeService.api.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "EmployeeResponse", description = "DTO for respuestas de empleados")
public class EmployeeResponseDTO {
    @Schema(description = "Identificador del empleado", example = "EMP12345")
    private String document;
    @Schema(description = "Nombre del empleado", example = "John")
    private String firstname;
    @Schema(description = "Apellido del empleado", example = "Doe")
    private String lastname;
    @Schema(description = "Correo electrónico del empleado", example = "johndoe@email.com")
    private String email;
    @Schema(description = "Número de teléfono del empleado", example = "+1234567890")
    private String phone;
    @Schema(description = "Estado del empleado", example = "true")
    private boolean status;
    
}