package edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "employee")
public class EmployeeEntity {
    @Id
    private String document;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private boolean status;
}