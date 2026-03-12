package com.josveronez.desafio_astentask.business.exceptions;


public class ExternalAPIException extends RuntimeException {
    public ExternalAPIException(String message) {
        super(message);
    }
}
