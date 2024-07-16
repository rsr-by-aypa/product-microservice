package com.rsr.product_microservice.integrationtTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsr.product_microservice.ProductFactory;
import com.rsr.product_microservice.core.domain.model.Product;
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

    @Nested
    @DisplayName("Test cases for posting a product via RestController")
    class PostProductTests {

        @Test
        public void postValidProductTest() throws Exception {

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


    }


}
