package com.rsr.product_microservice.port.user.controller;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.port.user.producer.ProductProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestProducerController {

    @Autowired
    ProductProducer productProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(TestProducerController.class);

    @GetMapping("/product-queue")
    public void triggerProductQueue() {
        Product product = new Product("Stein", "Ein runder, heilender Stein",
                23.45, 54, "https://link", 32.4, "green", 3.2);
        try {
            productProducer.sendMessage(product);
        } catch (Exception e) {
            LOGGER.error("Failed to send Product-Created-Message");
        }
    }



}
