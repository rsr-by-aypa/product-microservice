package com.rsr.product_microservice.unitTests;


import com.rsr.product_microservice.core.domain.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ProductTests {

    @Test
    void createProductWithNoArgsConstructor() {
        Product product = new Product();
    }

    @Test
    void setAndGetProductNameTest() {
        Product product = new Product();
        product.setName("Name");
        Assertions.assertEquals("Name", product.getName());
    }


}
