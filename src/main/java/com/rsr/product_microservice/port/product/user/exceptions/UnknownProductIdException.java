package com.rsr.product_microservice.port.product.user.exceptions;

import java.util.NoSuchElementException;

public class UnknownProductIdException extends NoSuchElementException {
    public UnknownProductIdException() {
        super("Could not find the passed id. Such Element does not exist.");
    }
}
