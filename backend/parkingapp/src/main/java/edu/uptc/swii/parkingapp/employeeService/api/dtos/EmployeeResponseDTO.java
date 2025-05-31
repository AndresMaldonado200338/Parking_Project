package edu.uptc.swii.parkingapp.employeeService.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {
    private String document;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private boolean status;
    
}