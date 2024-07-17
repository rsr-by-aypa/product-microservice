package com.rsr.product_microservice.port.admin.controller;

import com.rsr.product_microservice.core.domain.model.Product;
import com.rsr.product_microservice.core.domain.service.interfaces.IProductService;
import com.rsr.product_microservice.port.shopping_cart.producer.ProductUpdateProducer;
import com.rsr.product_microservice.port.user.controller.ProductController;
import com.rsr.product_microservice.port.utils.exceptions.NoProductsException;
import com.rsr.product_microservice.port.utils.exceptions.ProductIdAlreadyInUseException;
import com.rsr.product_microservice.port.utils.exceptions.UnknownProductIdException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/admin/product")
@Tag(name = "Product Admin API", description = "API für Produktoperationen")
public class ProductAdminController {

    @Autowired
    private IProductService productService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductController.class);

    @Operation(summary = "Erstellt ein neues Produkt", description = "Erstellt ein neues Produkt in der Datenbank")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produkt erstellt", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "409", description = "Produkt-ID bereits in Gebrauch", content = @Content)
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public @ResponseBody Product createProduct(@RequestBody Product product) throws ProductIdAlreadyInUseException {
        LOGGER.info(String.format("Received Product in Rest-Controller -> %s", product));
        Product createdProduct = productService.createProduct(product);
        productService.sendCreatedMessage(product);
        return createdProduct;
    }

    @Operation(summary = "Gibt alle Produkte zurück", description = "Gibt eine Liste aller Produkte in der Datenbank zurück")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste aller Produkte", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Keine Produkte gefunden", content = @Content)
    })
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Product> getAllProducts() throws NoProductsException {
        List<Product> products = productService.getAllProducts();
        LOGGER.info(String.format("Returning all Products. Number of Products -> %s", products.size()));
        return products;
    }

    @Operation(summary = "Gibt ein Produkt nach ID zurück", description = "Gibt ein Produkt anhand der angegebenen ID zurück")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produkt gefunden", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Produkt nicht gefunden", content = @Content)
    })
    @GetMapping("/{productId}")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Product getProductById(@PathVariable("productId") UUID productId) {
        return productService.getProductById(productId);
    }

    @Operation(summary = "Aktualisiert ein Produkt", description = "Aktualisiert die Details eines bestehenden Produkts")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produkt aktualisiert", content = @Content(schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Produkt nicht gefunden", content = @Content)
    })
    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Product update(@RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(product);
        productService.sendUpdatedMessage(product);
        return updatedProduct;
    }

    @Operation(summary = "Löscht ein Produkt", description = "Löscht ein Produkt anhand der angegebenen ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produkt gelöscht", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produkt nicht gefunden", content = @Content)
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteProduct (@RequestBody UUID productId)  throws UnknownProductIdException {
        productService.deleteProduct(productId);
    }

}
