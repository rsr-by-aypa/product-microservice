package com.rsr.product_microservice.integrationtTests;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsr.product_microservice.ProductFactory;
import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import com.rsr.product_microservice.port.user.consumer.ProductConsumer;
import com.rsr.product_microservice.port.user.dto.ProductChangedDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.UUID;

@SpringBootTest
@Testcontainers
@ExtendWith(SpringExtension.class)
public class ProductConsumerTests {

    @Container
    public static RabbitMQContainer rabbitMQContainer = new RabbitMQContainer("rabbitmq:3.13.3");

    @Container
    private static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>("postgres:13.3")
            .withDatabaseName("test")
            .withUsername("test")
            .withPassword("test");


    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ProductConsumer productConsumer;

    @SpyBean
    private ProductConsumer spyProductConsumer;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IProductService mockProductService;

    //RabbitMQ Values

    @Value("${rabbitmq.product.queue.name}")
    private String queue;

    @Value("${rabbitmq.product.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.amount_change.binding.key}")
    private String routingKey;

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
    public void receiveValidChangeProductTest() throws InterruptedException, JsonProcessingException {
        UUID productChangeId = UUID.randomUUID();
        int changeAmount = 4;
        Product actualProduct = ProductFactory.getValidExampleProduct();
        actualProduct.setId(productChangeId);
        actualProduct.setAmount(actualProduct.getAmount() - changeAmount);

        ProductChangedDTO productChangedDTO = new ProductChangedDTO(productChangeId, changeAmount);
        Mockito.when(mockProductService.changeProductAmount(productChangeId, changeAmount)).thenReturn(actualProduct);

        rabbitTemplate.convertAndSend(exchange, routingKey,  productChangedDTO);

        Thread.sleep(3000);

        Mockito.verify(spyProductConsumer).consume(productChangedDTO);
        Mockito.verify(mockProductService, Mockito.times(1))
                .changeProductAmount(productChangeId, changeAmount);
    }
}
