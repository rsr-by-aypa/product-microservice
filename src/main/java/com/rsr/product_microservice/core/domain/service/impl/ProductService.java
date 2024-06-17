package com.rsr.product_microservice.core.domain.service.impl;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductRepository;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import com.rsr.product_microservice.port.product.user.exceptions.ProductIdAlreadyInUseException;
import jakarta.persistence.EntityExistsException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService implements IProductService {

    private final IProductRepository productRepository;

    @Override
    public Product createProduct(Product product) throws ProductIdAlreadyInUseException {
        ProductValidator.validate(product);
        try {
            return productRepository.save(product);
        } catch (EntityExistsException e){
            throw new ProductIdAlreadyInUseException(product.getId());
        }
    }
}
