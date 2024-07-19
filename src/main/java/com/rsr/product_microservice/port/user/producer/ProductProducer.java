package com.rsr.product_microservice.port.user.producer;

import com.rsr.product_microservice.core.domain.model.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductProducer {

    @Value("${rabbitmq.product.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.product.created.routing_key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public ProductProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(Product createdProduct) {
        log.info(String.format("Message sent -> %s", createdProduct.toString()));
        rabbitTemplate.convertAndSend(exchange, routingKey, createdProduct);
    }
}
