package edu.uptc.swii.parkingapp.employeeService.api.dtos;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {
    @NotBlank(message = "Document cannot be blank")
    @Size(min = 5, max = 20, message = "Document must be between 5 and 20 characters")
    private String document;
    
    @NotBlank(message = "First name cannot be blank")
    @Size(max = 50, message = "First name cannot exceed 50 characters")
    private String firstname;
    
    @NotBlank(message = "Last name cannot be blank")
    @Size(max = 50, message = "Last name cannot exceed 50 characters")
    private String lastname;
    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    
    @NotBlank(message = "Phone cannot be blank")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Phone number must be between 10-15 digits and may include '+'")
    private String phone;
}