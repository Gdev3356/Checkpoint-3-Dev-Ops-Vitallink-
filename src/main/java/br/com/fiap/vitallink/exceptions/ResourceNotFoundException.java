package br.com.fiap.vitallink.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {super(message);
    }
}