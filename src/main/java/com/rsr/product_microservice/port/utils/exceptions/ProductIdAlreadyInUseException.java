package com.rsr.product_microservice.port.utils.exceptions;

import java.util.UUID;

public class ProductIdAlreadyInUseException extends IllegalArgumentException {
    public ProductIdAlreadyInUseException(UUID uuid) {
        super("Product Id already exists: " + uuid);
    }
}
