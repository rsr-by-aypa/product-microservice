package com.rsr.product_microservice.port.user.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductAmountChangedDTO {

    private UUID productId;

    private int amountChange;

    @JsonCreator
    public ProductAmountChangedDTO(@JsonProperty("productId") UUID productId,
                                   @JsonProperty("amountChange") int amountChange) {
        this.productId = productId;
        this.amountChange = amountChange;
    }

}
