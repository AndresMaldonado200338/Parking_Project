package edu.uptc.swii.parkingapp.employeeService.infraestructure.persistence.mongodb.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Employee")
public class EmployeeDocument {
    @Id
    private String document;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private boolean status;
}