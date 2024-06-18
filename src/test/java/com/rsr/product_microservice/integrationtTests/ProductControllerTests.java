package com.rsr.product_microservice.integrationtTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsr.product_microservice.ProductFactory;
import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.impl.ProductService;
import com.rsr.product_microservice.port.product.user.controller.ProductController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;


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

    @Nested
    @DisplayName("Test cases for getting a product or multiple products via RestController - White Box Test")
    class GetProductTests {

        @Test
        public void getAllProductsTest() throws Exception {
            Product product_rock = ProductFactory.getExampleValidProduct();
            Product product_stone = ProductFactory.getExampleValidProduct();
            product_rock.setName("Rock");
            product_stone.setName("Stone");

            List<Product> products = Arrays.asList(
                    product_rock,
                    product_stone
            );

            Mockito.when(productService.getAllProducts()).thenReturn(products);

            mockMvc.perform(MockMvcRequestBuilders.get("/products")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(products.size()))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Rock"))
                    .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Stone"));
        }
    }
}
