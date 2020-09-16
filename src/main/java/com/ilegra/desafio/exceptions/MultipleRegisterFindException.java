package com.ilegra.desafio.exceptions;

public class MultipleRegisterFindException extends RuntimeException {

    public MultipleRegisterFindException(String message) {
        super(message);
    }

    public MultipleRegisterFindException(String message, Throwable cause) {
        super(message, cause);
    }
}
