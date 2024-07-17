package com.rsr.product_microservice.core.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String description;

    private double priceInEuro;

    private int numberInStock;

    private String imageLink;

    private double weightInGram;

    private String color;

    private double diameterInCm;

    public Product(String name, String description, double priceInEuro, int numberInStock, String imageLink, double weightInGram,
                   String color, double diameterInCm) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.priceInEuro = priceInEuro;
        this.numberInStock = numberInStock;
        this.imageLink = imageLink;
        this.weightInGram = weightInGram;
        this.color = color;
        this.diameterInCm = diameterInCm;
    }

    @PrePersist
    public void prePersist() {
        if (id == null) {
            id = UUID.randomUUID();
        }
    }

    @Override
    public String toString() {
        if (id == null) {
            return String.format("Product[name=%s, description=%s, price=%s, picture=%s]",
                    name, description, priceInEuro, imageLink);
        } else {
            return String.format("Product[id=%s, name=%s, description=%s, price=%s, picture=%s]",
                    id, name, description, priceInEuro, imageLink);
        }

    }
}
