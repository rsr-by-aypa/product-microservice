package com.rsr.product_microservice.port.product.user.exceptions;

import java.util.NoSuchElementException;

public class UnknownIdException extends NoSuchElementException {
    public UnknownIdException() {
        super("Could not find the passed id. Such Element does not exist.");
    }
}
