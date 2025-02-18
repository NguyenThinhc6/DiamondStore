package com.diamond.store.service.ProductService;

import com.diamond.store.dto.request.ProductRequest;
import com.diamond.store.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product findById(int id);

    void createProduct(ProductRequest productRequest);

    void updateProduct(ProductRequest productRequest, int id);

    void deleteProduct(int id);

    void createProductWithImage(ProductRequest productRequest);
}
