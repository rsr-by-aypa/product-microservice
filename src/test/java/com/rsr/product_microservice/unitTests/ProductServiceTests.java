package com.rsr.product_microservice.unitTests;

import com.rsr.product_microservice.ProductFactory;
import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.impl.ProductService;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductRepository;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import org.junit.jupiter.api.*;

import static org.mockito.Mockito.*;

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
            Product product = ProductFactory.getExampleValidProduct();
            productService.createProduct(product);
            verify(productRepository).save(product);
        }

        @Test
        @DisplayName("create Product with no name (bad case) - White Box Test")
        void createProductWithInvalidNameTest() {
            Product product = ProductFactory.getExampleValidProduct();
            product.setName("");
            Assertions.assertThrows(IllegalArgumentException.class, () -> productService.createProduct(product));
            verify(productRepository, never()).save(product);
        }

        @Test
        @DisplayName("create Product with negative price (bad case) - White Box Test")
        void createProductWithNegativePriceTest() {
            Product product = ProductFactory.getExampleValidProduct();
            product.setPriceInEuro(-2.5);
            Assertions.assertThrows(IllegalArgumentException.class, () -> productService.createProduct(product));
            verify(productRepository, never()).save(product);
        }

        @Test
        @DisplayName("create Product with more than 2 decimal places (bad case) - White Box Test")
        void createProductWithWrongPriceFormatTest() {
            Product product = ProductFactory.getExampleValidProduct();
            product.setPriceInEuro(2.333);
            Assertions.assertThrows(IllegalArgumentException.class, () -> productService.createProduct(product));
            verify(productRepository, never()).save(product);
        }

        @Test
        @DisplayName("create Product with negative amount (bad case) - White Box Test")
        void createProductWithNegativeAmountTest() {
            Product product = ProductFactory.getExampleValidProduct();
            product.setAmount(-3);
            Assertions.assertThrows(IllegalArgumentException.class, () -> productService.createProduct(product));
            verify(productRepository, never()).save(product);
        }

        @Test
        @DisplayName("create null-Product (bad case) - White Box Test")
        void createNullProductTest() {
            Product product = null;
            Assertions.assertThrows(IllegalArgumentException.class, () -> productService.createProduct(product));
            verify(productRepository, never()).save(product);
        }

    }
}
