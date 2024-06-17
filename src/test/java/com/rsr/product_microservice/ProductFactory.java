package com.rsr.product_microservice;

import com.rsr.product_microservice.core.domain.model.Product;

public class ProductFactory {

    public static Product getExampleValidProduct() {
        return new Product("Rock", "It is a Rock", 199.99, 5,
                "http://link", 564.5, "blue", 2.5);
    }

}
