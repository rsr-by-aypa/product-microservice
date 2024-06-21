package com.rsr.product_microservice.port.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
public class ProductChangedDTO {

    private UUID productId;

    private int amountChange;

    @JsonCreator
    public ProductChangedDTO(@JsonProperty("productId") UUID productId,
                             @JsonProperty("amountChange") int amountChange) {
        this.productId = productId;
        this.amountChange = amountChange;
    }

}
