package com.rsr.product_microservice.port.user.controller;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.port.shopping_cart.producer.ProductUpdateProducer;
import com.rsr.product_microservice.port.user.producer.ProductProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestProducerController {

    @Autowired
    ProductProducer productProducer;

    @Autowired
    ProductUpdateProducer productUpdateProducer;

    private static final Logger LOGGER = LoggerFactory.getLogger(TestProducerController.class);

    @GetMapping("/product-queue")
    public void triggerProductQueue() {
        Product product = getExampleProduct();
        try {
            productProducer.sendMessage(product);
        } catch (Exception e) {
            LOGGER.error("Failed to send Product-Created-Message");
        }
    }

    @GetMapping("product-update-queue")
    public void triggerUpdateProductQueue() {
        Product product = getExampleProduct();
        try {
            productUpdateProducer.sendMessage(product);
        } catch (Exception e) {
            LOGGER.error("Failed to send Product-Updated-Message");
        }
    }

    private Product getExampleProduct() {
        return new Product("Stein", "Ein runder Stein", 23.45, 54,
                "http://link", 3.45, "green", 4.3);
    }


}
