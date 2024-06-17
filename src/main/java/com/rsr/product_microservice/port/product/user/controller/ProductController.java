package com.rsr.product_microservice.port.product.user.controller;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import com.rsr.product_microservice.port.product.user.exceptions.ProductIdAlreadyInUseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    private IProductService productService;

    private static Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @PostMapping(path = "/product")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Product createProduct(@RequestBody Product product) throws ProductIdAlreadyInUseException {
        LOGGER.info(String.format("Received Product -> %s", product));
        return productService.createProduct(product);
    }
}
