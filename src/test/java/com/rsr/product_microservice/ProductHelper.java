package com.rsr.product_microservice;

import com.rsr.product_microservice.core.domain.model.Product;

import java.util.List;

public class ProductHelper {

    public static boolean compareProductListsByName(List<Product> productList1, List<Product> productList2) {
        boolean sameLength = productList1.size() == productList2.size();
        boolean sameContent = productList1.stream().map(Product::getName).toList()
                .containsAll(productList2.stream().map(Product::getName).toList());
        return sameLength && sameContent;
    }
}
