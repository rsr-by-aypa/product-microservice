package com.rsr.product_microservice.core.domain.service.interfaces;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.port.product.user.exceptions.ProductIdAlreadyInUseException;

public interface IProductService {

    Product createProduct(Product product) throws ProductIdAlreadyInUseException;

}
