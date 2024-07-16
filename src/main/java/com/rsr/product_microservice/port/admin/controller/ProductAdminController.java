package com.rsr.product_microservice.port.admin.controller;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import com.rsr.product_microservice.port.shopping_cart.producer.ProductUpdateProducer;
import com.rsr.product_microservice.port.user.controller.ProductController;
import com.rsr.product_microservice.port.utils.exceptions.NoProductsException;
import com.rsr.product_microservice.port.utils.exceptions.ProductIdAlreadyInUseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/product")
public class ProductAdminController {

    @Autowired
    private IProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Product createProduct(@RequestBody Product product) throws ProductIdAlreadyInUseException {
        LOGGER.info(String.format("Received Product in Rest-Controller -> %s", product));
        Product createdProduct = productService.createProduct(product);
        productService.sendCreatedMessage(product);
        return createdProduct;
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Product> getAllProducts() throws NoProductsException {
        List<Product> products = productService.getAllProducts();
        LOGGER.info(String.format("Returning all Products. Number of Products -> %s", products.size()));
        return products;
    }

    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Product getProductById(@PathVariable("productId") UUID productId) {
        return productService.getProductById(productId);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Product update(@RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(product);
        productService.sendUpdatedMessage(product);
        return updatedProduct;
    }

}
