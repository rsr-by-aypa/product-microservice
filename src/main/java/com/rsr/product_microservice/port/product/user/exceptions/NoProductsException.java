package com.rsr.product_microservice.port.product.user.exceptions;

public class NoProductsException extends RuntimeException {
    public NoProductsException() {
        super("There seem to be no Products");
    }
}
