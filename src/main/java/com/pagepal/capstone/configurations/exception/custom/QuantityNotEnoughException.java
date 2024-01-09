package com.pagepal.capstone.configurations.exception.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class QuantityNotEnoughException extends RuntimeException{
    public QuantityNotEnoughException(String message) {
        super(message);
    }

    public QuantityNotEnoughException(String message, Throwable cause) {
        super(message, cause);
    }
}
