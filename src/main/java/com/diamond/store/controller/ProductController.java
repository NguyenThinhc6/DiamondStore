package com.diamond.store.controller;

import com.diamond.store.dto.request.ProductRequest;
import com.diamond.store.dto.response.ApiResponse;
import com.diamond.store.model.Product;
import com.diamond.store.service.ProductService.ProductService;
import com.diamond.store.util.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ApiResponse<List<Product>> getProducts() {

        return ApiResponse.<List<Product>>builder()
                .data(productService.findAll())
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Product> getProduct(@PathVariable Integer id) {

        return ApiResponse.<Product>builder()
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .data(productService.findById(id))
                .build();
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createProduct(@RequestBody ProductRequest productRequest) {

        productService.createProduct(productRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Void>builder()
                        .code(HttpStatus.CREATED.value())
                .message(ApplicationMessage.CREATE_DATA_SUCCESS)
                .build());
    }

    @DeleteMapping("/{id}/delete")
    public ApiResponse<Void> deleteProduct(@PathVariable Integer id) {

        productService.deleteProduct(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.DELETE_DATA_SUCCESS)
                .build();
    }

    @PutMapping("/{id}/update")
    public ApiResponse<Void> updateProduct(@PathVariable Integer id, @RequestBody ProductRequest productRequest) {

        productService.updateProduct(productRequest, id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.UPDATE_DATA_SUCCESS)
                .build();
    }
}
