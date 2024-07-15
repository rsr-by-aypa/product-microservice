package com.rsr.product_microservice.core.domain.service.impl;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductRepository;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import com.rsr.product_microservice.port.user.exceptions.NoProductsException;
import com.rsr.product_microservice.port.user.exceptions.ProductIdAlreadyInUseException;
import com.rsr.product_microservice.port.user.exceptions.UnknownProductIdException;
import com.rsr.product_microservice.port.user.producer.ProductProducer;
import io.swagger.annotations.*;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Api(tags = "Product Service", description = "Operations pertaining to products in Product Microservice")
public class ProductService implements IProductService {

    private final IProductRepository productRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private final ProductProducer productProducer;

    @Override
    @ApiOperation(value = "Create a new product", response = Product.class)
    @ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully created product"),
        @ApiResponse(code = 409, message = "Product ID already in use")
    })
    public Product createProduct(Product product) throws ProductIdAlreadyInUseException {
        ProductValidator.validate(product);
        try {
            Product persistedProduct = productRepository.save(product);
            LOGGER.info(String.format("Persisted Product -> %s", persistedProduct));

            productProducer.sendMessage(persistedProduct);
            return persistedProduct;
        } catch (EntityExistsException e){
            throw new ProductIdAlreadyInUseException(product.getId());
        }
    }

    @Override
    @ApiOperation(value = "View a list of available products", response = List.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved list"),
        @ApiResponse(code = 404, message = "No products found")
    })
    public List<Product> getAllProducts()  throws NoProductsException {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new NoProductsException();
        }
        return products;
    }

    @Override
    @ApiOperation(value = "Get a product by Id", response = Product.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully retrieved product"),
        @ApiResponse(code = 404, message = "Product not found")
    })
    public Product getProductById(UUID productId) throws UnknownProductIdException {
        if (productId == null) {
            throw new IllegalArgumentException("Invalid Product ID");
        }

        return productRepository.findById(productId)
                .orElseThrow(UnknownProductIdException::new);
    }

    @Override
    @ApiOperation(value = "Delete a product")
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully deleted product"),
        @ApiResponse(code = 404, message = "Product not found")
    })
    public void deleteProduct(UUID productId) throws UnknownProductIdException {
        if (productId == null) {
            throw new IllegalArgumentException("Invalid Product ID");
        }
        productRepository.findById(productId).orElseThrow(UnknownProductIdException::new);
        productRepository.deleteById(productId);
    }

    @Override
    @ApiOperation(value = "Update an existing product", response = Product.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully updated product"),
        @ApiResponse(code = 404, message = "Product not found")
    })
    public Product updateProduct(Product product) throws UnknownProductIdException {
        ProductValidator.validate(product);
        productRepository.findById(product.getId()).orElseThrow(UnknownProductIdException::new);
        return productRepository.save(product);
    }

    @Override
    @ApiOperation(value = "Change the amount of a product", response = Product.class)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successfully changed product amount"),
        @ApiResponse(code = 404, message = "Product not found")
    })
    public Product changeProductAmount(UUID productId, int subtractFromAmount) throws UnknownProductIdException {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }

        Product productToChange = productRepository.findById(productId).orElseThrow(UnknownProductIdException::new);
        productToChange.setAmount(productToChange.getAmount() - subtractFromAmount);
        return productRepository.save(productToChange);
    }
}

