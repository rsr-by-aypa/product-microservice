package com.rsr.product_microservice.integrationTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsr.product_microservice.ProductFactory;
import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Container
    public static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.13.3");

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:13.3")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IProductRepository productRepository;

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

        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    @Test
    void contextLoads() {
        //nischt
    }

    @Nested
    @DisplayName("Test cases for posting a product via RestController")
    class PostProductTests {

        @Test
        public void postValidProductCorrectResponseTest() throws Exception {

            Product product = ProductFactory.getValidExampleProduct();

            String requestBody = objectMapper.writeValueAsString(product);
            System.out.println(requestBody);

            mockMvc.perform(MockMvcRequestBuilders.post("/admin/product").
                    contentType(MediaType.APPLICATION_JSON).content(requestBody)).
                    andExpect(MockMvcResultMatchers.status().isCreated()).
                    andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName())).
                    andExpect(MockMvcResultMatchers.jsonPath("$.priceInEuro").value(product.getPriceInEuro())).
                    andExpect(MockMvcResultMatchers.jsonPath("$.description").value(product.getDescription()));

        }

        @Test
        public void postValidProductStoredTest() throws Exception {
            Product product = ProductFactory.getValidExampleProduct();
            product.setId(null);
            String requestBody = objectMapper.writeValueAsString(product);

            String productJson = mockMvc.perform(MockMvcRequestBuilders.post("/admin/product")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andReturn().getResponse().getContentAsString();

            Product responseProduct = objectMapper.readValue(productJson, Product.class);

            Product storedProduct = productRepository.findById(responseProduct.getId()).orElseThrow(AssertionError::new);
            storedProduct.setId(null);
            String storedProductJson = objectMapper.writeValueAsString(storedProduct);

            Assertions.assertEquals(requestBody, storedProductJson);
        }


    }


    @Nested
    @DisplayName("Test cases for posting a product via RestController")
    class GetProductTests {


        @Test
        void getAllProductsAllForRealTest() throws Exception {
            Product product1 = new Product();
            product1.setName("Rock");
            Product product2 = new Product();
            product2.setName("Stone");
            Product product3 = new Product();
            product3.setName("Emerald");

            List<Product> productList = List.of(
                    product1,
                    product2,
                    product3
            );
            productRepository.saveAll(productList);

            String productListJson = mockMvc.perform(MockMvcRequestBuilders.get("/product/all"))
                    .andReturn().getResponse().getContentAsString();

            List<Product> productListFromDB = objectMapper.readValue(productListJson, new TypeReference<List<Product>>() {});

            System.out.println(productList.stream().map(p -> p.getName()).toList());
            System.out.println(productListFromDB.stream().map(p -> p.getName()).toList());

            Assertions.assertTrue(productList.stream().map(p -> p.getName()).toList().containsAll(productListFromDB.stream().map(
                    p -> p.getName()
            ).toList()));
        }
    }


}
