package com.rsr.product_microservice.port.user.exceptions;

import java.util.NoSuchElementException;

public class UnknownProductIdException extends RuntimeException {
    public UnknownProductIdException() {
        super("Could not find the passed id. Such Element does not exist.");
    }
}
