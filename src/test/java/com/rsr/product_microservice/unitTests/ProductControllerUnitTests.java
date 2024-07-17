package com.rsr.product_microservice.unitTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsr.product_microservice.ProductFactory;
import com.rsr.product_microservice.TestContainerConfiguration;
import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.impl.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@Import(TestContainerConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductControllerUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService mockProductService;

    @Autowired
    private ObjectMapper objectMapper;

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:13.3")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");



    @DynamicPropertySource
    static void dynamicProperties(DynamicPropertyRegistry registry) {
        String dbUrl = postgresqlContainer.getJdbcUrl();
        String username = postgresqlContainer.getUsername();
        String password = postgresqlContainer.getPassword();

        registry.add("spring.datasource.url",
                () -> dbUrl);
        registry.add("spring.datasource.username",
                () -> username);
        registry.add("spring.datasource.password",
                () -> password);
    }

    @Nested
    @DisplayName("Test cases for getting a product or multiple products via RestController - White Box Test")
    class GetProductTests {

        @Test
        public void getAllProductsTest() throws Exception {
            Product product_rock = ProductFactory.getValidExampleProduct();
            Product product_stone = ProductFactory.getValidExampleProduct();
            product_rock.setName("Rock");
            product_stone.setName("Stone");

            List<Product> products = Arrays.asList(
                    product_rock,
                    product_stone
            );

            Mockito.when(mockProductService.getAllProducts()).thenReturn(products);

            mockMvc.perform(MockMvcRequestBuilders.get("/product/all")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(products.size()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Rock"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Stone"));
        }

        @Test
        void getProductByIdTest() throws Exception {
            Product productRock = ProductFactory.getValidExampleProduct();
            UUID rockId = UUID.randomUUID();
            productRock.setName("Rock");
            productRock.setId(rockId);

            Product productStone = ProductFactory.getValidExampleProduct();
            UUID stoneId = UUID.randomUUID();
            productStone.setName("Stone");
            productStone.setId(stoneId);

            List<Product> products = Arrays.asList(
                    productRock,
                    productStone
            );

            Mockito.when(mockProductService.getAllProducts()).thenReturn(products);
            Mockito.when(mockProductService.getProductById(rockId)).thenReturn(productRock);
            Mockito.when(mockProductService.getProductById(stoneId)).thenReturn(productStone);

            mockMvc.perform(MockMvcRequestBuilders.get("/product/" + rockId.toString())
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath(".name").value("Rock"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(rockId.toString()));

        }
    }

    @Nested
    @DisplayName("Test Cases for deleting a product")
    class DeleteProductTests {

        @Test
        @DisplayName("Properley Deleting an existing product")
        void deleteProductProperlyTest() throws Exception {
            //Product product = ProductFactory.getExampleValidProduct();
            UUID productId = UUID.randomUUID();
            //product.setId(productId);

            String requestBody = objectMapper.writeValueAsString(productId);

            mockMvc.perform(MockMvcRequestBuilders.delete("/admin/product").
                            contentType(MediaType.APPLICATION_JSON).content(requestBody)).
                    andExpect(MockMvcResultMatchers.status().isOk());

            Mockito.verify(mockProductService, Mockito.times(1)).deleteProduct(productId);
        }
    }

    @Nested
    @DisplayName("Test Cases for updating a product")
    class UpdateProductTests {

        @Test
        @DisplayName("Properly updating an existing product - White Box")
        void updateProductProperlyTest() throws Exception {
            Product updatedProduct = ProductFactory.getValidExampleProduct();

            String requestBody = objectMapper.writeValueAsString(updatedProduct);

            mockMvc.perform(MockMvcRequestBuilders.put("/admin/product").
                            contentType(MediaType.APPLICATION_JSON).content(requestBody)).
                    andExpect(MockMvcResultMatchers.status().isOk());

            Mockito.verify(mockProductService, Mockito.times(1)).updateProduct(updatedProduct);
        }
    }

}
