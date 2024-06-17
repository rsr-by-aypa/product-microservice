package com.rsr.product_microservice.unitTests;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.impl.ProductService;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductRepository;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ProductServiceTests {

    private IProductRepository productRepository;

    private IProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(IProductRepository.class);
        productService = new ProductService(productRepository);
    }

    @Nested
    @DisplayName("Test cases for creating a product")
    class CreateProductTests {

        @Test
        @DisplayName("Check if valid Product is created - White Box Test")
        void createValidProductTest() {
            Product product = new Product("Rock", "It is a Rock", 199.99, 5,
                    "http://link", 564.5, "blue", 2.5);
            productService.createProduct(product);
            verify(productRepository).save(product);
        }

    }
}
