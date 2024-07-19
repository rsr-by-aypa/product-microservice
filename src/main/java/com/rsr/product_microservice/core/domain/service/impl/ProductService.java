package com.rsr.product_microservice.core.domain.service.impl;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductRepository;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import com.rsr.product_microservice.port.shopping_cart.producer.ProductUpdateProducer;
import com.rsr.product_microservice.port.user.producer.ProductProducer;
import com.rsr.product_microservice.port.utils.exceptions.NoProductsException;
import com.rsr.product_microservice.port.utils.exceptions.ProductIdAlreadyInUseException;
import com.rsr.product_microservice.port.utils.exceptions.UnknownProductIdException;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class ProductService implements IProductService {

    private final IProductRepository productRepository;

    @Autowired
    private final ProductProducer productProducer;

    @Autowired
    private final ProductUpdateProducer productUpdateProducer;

    @Override
    public Product createProduct(Product product) throws ProductIdAlreadyInUseException {
        ProductValidator.validate(product);
        try {
            Product persistedProduct = productRepository.save(product);
            log.info(String.format("Persisted Product -> %s", persistedProduct));

            return persistedProduct;
        } catch (EntityExistsException e) {
            throw new ProductIdAlreadyInUseException(product.getId());
        }
    }

    @Override
    public List<Product> getAllProducts() throws NoProductsException {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new NoProductsException();
        }
        return products;
    }

    @Override
    public Product getProductById(UUID productId) throws UnknownProductIdException {
        if (productId == null) {
            throw new IllegalArgumentException("Invalid Product ID");
        }

        return productRepository.findById(productId)
                .orElseThrow(UnknownProductIdException::new);
    }

    @Override
    public void deleteProduct(UUID productId) throws UnknownProductIdException {
        if (productId == null) {
            throw new IllegalArgumentException("Invalid Product ID");
        }
        productRepository.findById(productId).orElseThrow(UnknownProductIdException::new);
        productRepository.deleteById(productId);
        log.info("Deleted Product: {}", productId);
    }

    @Override
    public Product updateProduct(Product product) throws UnknownProductIdException {
        ProductValidator.validate(product);
        productRepository.findById(product.getId()).orElseThrow(UnknownProductIdException::new);
        Product updatedProduct = productRepository.save(product);
        log.info("Updated Product: {}", updatedProduct);
        return updatedProduct;
    }

    @Override
    public Product changeProductAmount(UUID productId, int amountChange) throws UnknownProductIdException {
        if (productId == null) {
            throw new IllegalArgumentException("Product ID must not be null");
        }

        Product productToChange = productRepository.findById(productId).orElseThrow(UnknownProductIdException::new);
        productToChange.setNumberInStock(productToChange.getNumberInStock() + amountChange);
        Product changedProduct = productRepository.save(productToChange);
        log.info("Changed Product Amount for: {}", changedProduct);
        return changedProduct;
    }

    @Override
    public void sendCreatedMessage(Product product) {
        productProducer.sendMessage(product);
    }

    @Override
    public void sendUpdatedMessage(Product product) {
        productUpdateProducer.sendMessage(product);
    }
}

