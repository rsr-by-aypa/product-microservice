package com.rsr.product_microservice.port.user.consumer;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import com.rsr.product_microservice.port.user.dto.ProductAmountChangedDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.support.ListenerExecutionFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductConsumer {

    @Autowired
    private IProductService productService;

    @RabbitListener(queues = {"${rabbitmq.product.queue.name}"})
    public void consume(ProductAmountChangedDTO changedProduct) {
        try {
            Product updatedProduct = productService.changeProductAmount(
                    changedProduct.getProductId(), changedProduct.getAmountChange());
            log.info(String.format("Changed Product Amount -> For %s to %x",
                    updatedProduct.getId().toString(), updatedProduct.getNumberInStock()));
        } catch (ListenerExecutionFailedException listenerExecutionFailedException) {
            log.error(listenerExecutionFailedException.getMessage());
        }
    }

}
