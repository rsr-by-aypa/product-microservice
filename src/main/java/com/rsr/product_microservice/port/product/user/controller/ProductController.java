package com.rsr.product_microservice.port.product.user.controller;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.impl.ProductService;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import com.rsr.product_microservice.port.product.user.exceptions.NoProductsException;
import com.rsr.product_microservice.port.product.user.exceptions.ProductIdAlreadyInUseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class ProductController {

    @Autowired
    private IProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @PostMapping("/product")
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Product createProduct(@RequestBody Product product) throws ProductIdAlreadyInUseException {
        LOGGER.info(String.format("Received Product -> %s", product));
        return productService.createProduct(product);
    }

    @GetMapping("/products")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Product> getAllProducts() throws NoProductsException {
        List<Product> products = productService.getAllProducts();
        LOGGER.info(String.format("Returning all Products. Number of Products -> %s", products.size()));
        return products;
    }

    @GetMapping("/product/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Product getProductById(@PathVariable("productId") UUID productId) {
           return productService.getProductById(productId);
    }
}
