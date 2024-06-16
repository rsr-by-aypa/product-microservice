package com.rsr.product_microservice.unitTests;


import com.rsr.product_microservice.core.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class ProductTests {

    @Test
    void createProductWithNoArgsConstructor() {
        Product product = new Product();
    }


}
