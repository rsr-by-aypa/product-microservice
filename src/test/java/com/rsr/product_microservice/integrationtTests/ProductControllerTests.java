package com.rsr.product_microservice.integrationtTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsr.product_microservice.ProductFactory;
import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.port.product.user.controller.ProductController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Nested
    @DisplayName("Test cases for posting a product via RestController")
    class PostProductTests {

        @Test
        public void postValidProductTest() throws Exception {
            Product product = ProductFactory.getExampleValidProduct();
            String requestBody = objectMapper.writeValueAsString(product);

            mockMvc.perform(MockMvcRequestBuilders.post("/product").
                    contentType(MediaType.APPLICATION_JSON).content(requestBody)).
                    andExpect(MockMvcResultMatchers.status().isCreated()).
                    andExpect(MockMvcResultMatchers.jsonPath("$.name").value(product.getName())).
                    andExpect(MockMvcResultMatchers.jsonPath("$.priceInEuro").value(product.getPriceInEuro())).
                    andExpect(MockMvcResultMatchers.jsonPath("$.description").value(product.getDescription()));
        }


    }
}
