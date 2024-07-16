package com.rsr.product_microservice.port.utils.exceptions;

public class NoProductsException extends RuntimeException {
    public NoProductsException() {
        super("There seem to be no Products");
    }
}
