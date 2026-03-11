package com.josveronez.desafio_astentask.business.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class ExternalAPIException extends RuntimeException {
    public ExternalAPIException(String message) {
        super(message);
    }
}
