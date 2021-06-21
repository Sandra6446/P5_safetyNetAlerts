package com.openclassrooms.safetyNetAlerts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AlreadyInDataFileException extends RuntimeException {
    public AlreadyInDataFileException(String s) {
        super(s);
    }
}
