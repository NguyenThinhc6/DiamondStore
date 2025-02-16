package com.diamond.store.controller;

import com.diamond.store.dto.request.ProductAttributeRequest;
import com.diamond.store.dto.response.ApiResponse;
import com.diamond.store.model.Category;
import com.diamond.store.service.ProductAttributeService.CategoryService;
import com.diamond.store.util.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ApiResponse<List<Category>> findAll() {

        return ApiResponse.<List<Category>>builder()
                .code(HttpStatus.OK.value())
                .data(categoryService.findAll())
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Category> findById(@PathVariable Integer id) {

        return ApiResponse.<Category>builder()
                .code(HttpStatus.OK.value())
                .data(categoryService.findById(id))
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .build();
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createCollection(@RequestBody ProductAttributeRequest attributeRequest) {

        Category category = Category.builder()
                .categoryName(attributeRequest.getAttributeName())
                .description(attributeRequest.getDescription())
                .build();

        categoryService.create(category);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Void>builder()
                .code(HttpStatus.CREATED.value())
                .message(ApplicationMessage.CREATE_DATA_SUCCESS)
                .build());


    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<Void>> deleteCollection(@PathVariable Integer id) {

        categoryService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(ApplicationMessage.DELETE_DATA_SUCCESS)
                .code(HttpStatus.OK.value())
                .build());
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<Void>> updateCollection(@PathVariable Integer id, @RequestBody ProductAttributeRequest attributeRequest) {

        Category category = Category.builder()
                .categoryId(id)
                .categoryName(attributeRequest.getAttributeName())
                .description(attributeRequest.getDescription())
                .build();
        categoryService.update(category, id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(ApplicationMessage.UPDATE_DATA_SUCCESS)
                .code(HttpStatus.OK.value())
                .build());
    }

}
