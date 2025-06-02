package edu.uptc.swii.parkingapp.accessControlService.infraestructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration // Bean name will be accessControlMongoConfig
public class AccessControlMongoConfig extends AbstractMongoClientConfiguration { // Renamed class

    @Value("${spring.data.mongodb.host:localhost}") // Added default value
    private String host;

    @Value("${spring.data.mongodb.port:27017}") // Added default value
    private int port;

    // It's crucial that if this service has its own DB or specific collections,
    // this value reflects that. If it shares the 'GestionEmpleadosDB', it's fine.
    @Value("${spring.data.mongodb.database.accesscontrol:GestionEmpleadosDB}") // Consider a specific property or ensure it's correct
    private String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Override
    public MongoClient mongoClient() {
        // Consider using a more complete connection string if needed (e.g., with credentials)
        return MongoClients.create(String.format("mongodb://%s:%d", host, port));
    }

    // If you need to scan for MongoDB repositories in a specific package for this service:
    // @Override
    // protected Collection<String> getMappingBasePackages() {
    //     return Collections.singleton("edu.uptc.swii.parkingapp.accessControlService.infraestructure.persistence.mongodb");
    // }
}