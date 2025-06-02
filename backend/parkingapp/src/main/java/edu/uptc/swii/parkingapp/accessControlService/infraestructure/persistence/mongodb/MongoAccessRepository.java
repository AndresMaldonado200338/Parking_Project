package edu.uptc.swii.parkingapp.accessControlService.infraestructure.persistence.mongodb;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional; // Make sure Optional is imported
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import edu.uptc.swii.parkingapp.accessControlService.domain.models.AccessRecord;
import edu.uptc.swii.parkingapp.accessControlService.domain.ports.AccessQueryPort;
import edu.uptc.swii.parkingapp.accessControlService.infraestructure.persistence.mongodb.documents.AccessDocument;

@Repository
public class MongoAccessRepository implements AccessQueryPort {
    
    // Using the original field name 'mongoRepository' for consistency with your initial upload
    private final MongoAccessRepositoryImpl mongoRepository; 
    

    public MongoAccessRepository(MongoAccessRepositoryImpl mongoRepository) { // Constructor uses 'mongoRepository'
        this.mongoRepository = mongoRepository;
    }

    // This 'save' method was added to allow AccessEventConsumer to save records.
    // It uses the 'mongoRepository' field.
    public AccessRecord save(AccessRecord accessRecord) {
        AccessDocument document = toDocument(accessRecord);
        AccessDocument savedDocument = this.mongoRepository.save(document); // Use 'this.mongoRepository'
        return toDomain(savedDocument);
    }

    @Override
    public List<AccessRecord> findAccessesByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        
        return this.mongoRepository.findByAccessDateTimeBetween(start, end) // Use 'this.mongoRepository'
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<AccessRecord> findAccessesByEmployeeAndDateRange(
        String employeeId, LocalDate startDate, LocalDate endDate) {
        
        LocalDateTime start = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay();
        
        return this.mongoRepository.findByEmployeeIdAndAccessDateTimeBetween( // Use 'this.mongoRepository'
            employeeId, start, end)
            .stream()
            .map(this::toDomain)
            .collect(Collectors.toList());
    }
    
    @Override // Add @Override annotation
    public Optional<AccessRecord> findMatchingExit(AccessRecord entryRecord) {
        // Ensure MongoAccessRepositoryImpl has this method defined:
        // List<AccessDocument> findByEmployeeIdAndEventTypeAndAccessDateTimeAfterOrderByAccessDateTimeAsc(
        //     String employeeId, String eventType, LocalDateTime accessDateTime);
        List<AccessDocument> potentialExits = this.mongoRepository.findByEmployeeIdAndEventTypeAndAccessDateTimeAfterOrderByAccessDateTimeAsc( // Use 'this.mongoRepository'
            entryRecord.getEmployeeId(), 
            "SALIDA", 
            entryRecord.getAccessDateTime()
        );
        if (potentialExits != null && !potentialExits.isEmpty()) {
            return Optional.of(toDomain(potentialExits.get(0)));
        }
        return Optional.empty();
    }

    private AccessDocument toDocument(AccessRecord access) {
        AccessDocument document = new AccessDocument();
        document.setEmployeeId(access.getEmployeeId());
        document.setAccessDateTime(access.getAccessDateTime());
        document.setEventType(access.getEventType());
        return document;
    }

    private AccessRecord toDomain(AccessDocument document) {
        if (document == null) return null; // Add null check
        return new AccessRecord(
            document.getEmployeeId(),
            document.getAccessDateTime(),
            document.getEventType()
        );
    }
}