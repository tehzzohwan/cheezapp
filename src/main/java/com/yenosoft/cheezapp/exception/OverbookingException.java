package com.yenosoft.cheezapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OverbookingException extends RuntimeException {

    public OverbookingException(String message) {
        super(message);
    }
}
