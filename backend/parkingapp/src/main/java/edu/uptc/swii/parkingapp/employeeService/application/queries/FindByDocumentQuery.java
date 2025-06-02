package edu.uptc.swii.parkingapp.employeeService.application.queries;

public class FindByDocumentQuery {
    private final String document;

    public FindByDocumentQuery(String document) {
        this.document = document;
    }

    public String getDocument() {
        return document;
    }
}