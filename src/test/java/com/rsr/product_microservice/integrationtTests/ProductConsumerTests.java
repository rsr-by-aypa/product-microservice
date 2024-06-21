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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;

import java.util.UUID;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ProductConsumerTests {

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

    @Value("${rabbitmq.amount_change.routing.key}")
    private String routingKey;

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

        Thread.sleep(5000);

        Mockito.verify(spyProductConsumer).consume(productChangedDTO);
    }
}
