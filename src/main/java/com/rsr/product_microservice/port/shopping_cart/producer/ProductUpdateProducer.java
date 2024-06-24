package com.rsr.product_microservice.port.shopping_cart.producer;

import com.rsr.product_microservice.core.domain.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ProductUpdateProducer {
    @Value("${rabbitmq.product.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.product.updated.routing_key}")
    private String routingKey;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductUpdateProducer.class);

    private final RabbitTemplate rabbitTemplate;

    public ProductUpdateProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Product product){
        LOGGER.info(String.format("Product-Update-Message sent -> %s", product.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, product);
    }
}
