package edu.uptc.swii.parkingapp.accessControlService.infraestructure.persistence.mongodb.documents;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document(collection = "Access")
public class AccessDocument {
    @Id
    private String id;
    private String employeeId;
    private LocalDateTime accessDateTime;
    private String eventType; // "ENTRADA" o "SALIDA"
}