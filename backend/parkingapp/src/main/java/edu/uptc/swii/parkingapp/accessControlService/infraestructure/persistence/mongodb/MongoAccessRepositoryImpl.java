package edu.uptc.swii.parkingapp.accessControlService.infraestructure.persistence.mongodb;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import edu.uptc.swii.parkingapp.accessControlService.infraestructure.persistence.mongodb.documents.AccessDocument;

public interface MongoAccessRepositoryImpl extends MongoRepository<AccessDocument, String> {
    List<AccessDocument> findByAccessDateTimeBetween(LocalDateTime start, LocalDateTime end);
    List<AccessDocument> findByEmployeeIdAndAccessDateTimeBetween(
        String employeeId, LocalDateTime start, LocalDateTime end);
    AccessDocument save(AccessDocument entity);
    List<AccessDocument> findByEmployeeIdAndEventTypeAndAccessDateTimeAfterOrderByAccessDateTimeAsc(
        String employeeId, String eventType, LocalDateTime accessDateTime);
}