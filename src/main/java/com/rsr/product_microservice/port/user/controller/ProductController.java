package com.rsr.product_microservice.port.user.controller;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import com.rsr.product_microservice.port.utils.exceptions.NoProductsException;
import com.rsr.product_microservice.port.utils.exceptions.UnknownProductIdException;
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

    @GetMapping("/product/all")
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

    @DeleteMapping(path="/product")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct (@RequestBody UUID productId)  throws UnknownProductIdException {
        productService.deleteProduct(productId);
    }


}
