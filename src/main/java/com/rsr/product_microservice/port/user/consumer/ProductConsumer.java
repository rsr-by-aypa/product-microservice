package com.rsr.product_microservice.port.user.consumer;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import com.rsr.product_microservice.port.user.dto.ProductChangedDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class ProductConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductConsumer.class);

    @Autowired
    private IProductService productService;

    private CountDownLatch latch = new CountDownLatch(1);

    @RabbitListener(queues = {"${rabbitmq.product.queue.name}"})
    public void consume(ProductChangedDTO changedProduct){
        try {
            Product updatedProduct = productService.changeProductAmount(
                    changedProduct.getProductId(), changedProduct.getAmountChange());
            LOGGER.info(String.format("Changed Product Amount -> For %s to %x",
                    updatedProduct.getId().toString(), updatedProduct.getAmount()));
        } catch (ListenerExecutionFailedException listenerExecutionFailedException) {
            LOGGER.error(listenerExecutionFailedException.getMessage());
        }
    }

}
