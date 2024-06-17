package com.rsr.product_microservice.core.domain.service.interfaces;

import com.rsr.product_microservice.core.domain.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IProductRepository extends JpaRepository<Product, UUID> {

}
