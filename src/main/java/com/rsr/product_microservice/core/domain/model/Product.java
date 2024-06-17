package com.rsr.product_microservice.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    private String name;

    private String description;

    private double priceInEuro;

    private int amount;

    private String imageLink;

    private double weightInGram;

    private String color;

    private double diameterInCm;

    public Product(String name, String description, double priceInEuro, int amount, String imageLink, double weightInGram,
                   String color, double diameterInCm) {
        this.name = name;
        this.description = description;
        this.priceInEuro = priceInEuro;
        this.amount = amount;
        this.imageLink = imageLink;
        this.weightInGram = weightInGram;
        this.color = color;
        this.diameterInCm = diameterInCm;
    }

}
