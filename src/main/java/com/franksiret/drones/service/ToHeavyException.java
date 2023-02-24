package com.franksiret.drones.service;

public class ToHeavyException extends RuntimeException {

    private static final long serialVersionUID = -1873881683285597465L;

    public ToHeavyException(String message) {
        super(message);
    }

    public ToHeavyException(String message, Throwable cause) {
        super(message, cause);
    }
}
