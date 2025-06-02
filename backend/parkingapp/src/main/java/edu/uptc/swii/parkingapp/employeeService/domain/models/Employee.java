package edu.uptc.swii.parkingapp.employeeService.domain.models;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;

@Getter
@Document(collection = "Employee")
public class Employee {
    @Id
    private final String document;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private boolean status;

    public Employee(String document, String firstname, String lastname, 
                   String email, String phone, boolean status) {
        this.document = Objects.requireNonNull(document);
        this.firstname = Objects.requireNonNull(firstname);
        this.lastname = Objects.requireNonNull(lastname);
        this.email = Objects.requireNonNull(email);
        this.phone = Objects.requireNonNull(phone);
        this.status = status;
    }

    public void update(String firstname, String lastname, String email, String phone) {
        this.firstname = Objects.requireNonNull(firstname);
        this.lastname = Objects.requireNonNull(lastname);
        this.email = Objects.requireNonNull(email);
        this.phone = Objects.requireNonNull(phone);
    }

    public void disable() {
        this.status = false;
    }
    
    public void enable() {
        this.status = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return document.equals(employee.document);
    }

    @Override
    public int hashCode() {
        return Objects.hash(document);
    }
}