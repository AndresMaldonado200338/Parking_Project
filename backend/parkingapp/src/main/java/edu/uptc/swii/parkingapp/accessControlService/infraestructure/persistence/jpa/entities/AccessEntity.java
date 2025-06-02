package edu.uptc.swii.parkingapp.accessControlService.infraestructure.persistence.jpa.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "access")
public class AccessEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "employeeID", nullable = false)
    private String employeeId;
    
    @Column(name = "accessdatetime", nullable = false)
    private LocalDateTime accessDateTime;
    
    @Column(name = "event_type", nullable = false)
    private String eventType; // "ENTRADA" o "SALIDA"
}