package edu.uptc.swii.parkingapp.accessControlService.api.exceptions;

public class InvalidAccessOperationException extends RuntimeException {
    public InvalidAccessOperationException(String message) {
        super(message);
    }
}