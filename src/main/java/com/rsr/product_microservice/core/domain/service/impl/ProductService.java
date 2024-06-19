package com.rsr.product_microservice.core.domain.service.impl;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductRepository;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import com.rsr.product_microservice.port.product.user.exceptions.NoProductsException;
import com.rsr.product_microservice.port.product.user.exceptions.ProductIdAlreadyInUseException;
import com.rsr.product_microservice.port.product.user.exceptions.UnknownIdException;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ProductService implements IProductService {

    private final IProductRepository productRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductService.class);

    @Override
    public Product createProduct(Product product) throws ProductIdAlreadyInUseException {
        ProductValidator.validate(product);
        try {
            Product persistedProduct = productRepository.save(product);
            LOGGER.info(String.format("Persisted Product -> %s", persistedProduct));
            return persistedProduct;
        } catch (EntityExistsException e){
            throw new ProductIdAlreadyInUseException(product.getId());
        }
    }

    @Override
    public List<Product> getAllProducts()  throws NoProductsException {
        List<Product> products = productRepository.findAll();
        if (products.isEmpty()) {
            throw new NoProductsException();
        }
        return products;
    }

    @Override
    public Product getProductById(UUID productId) throws UnknownIdException {
        if (productId == null) {
            throw new IllegalArgumentException("Invalid Product ID");
        }

        return productRepository.findById(productId)
                .orElseThrow(UnknownIdException::new);
    }


}
