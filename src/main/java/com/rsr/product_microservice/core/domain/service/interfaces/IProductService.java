package com.rsr.product_microservice.core.domain.service.interfaces;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.port.user.exceptions.NoProductsException;
import com.rsr.product_microservice.port.user.exceptions.ProductIdAlreadyInUseException;
import com.rsr.product_microservice.port.user.exceptions.UnknownProductIdException;

import java.util.List;
import java.util.UUID;

public interface IProductService {

    Product createProduct(Product product) throws ProductIdAlreadyInUseException;

    List<Product> getAllProducts() throws NoProductsException;

    Product getProductById(UUID productId) throws UnknownProductIdException;

    void deleteProduct(UUID productId) throws UnknownProductIdException;

    Product updateProduct(Product product) throws UnknownProductIdException;

    Product changeProductAmount(UUID productId, int subtractFromAmount) throws UnknownProductIdException;
}
