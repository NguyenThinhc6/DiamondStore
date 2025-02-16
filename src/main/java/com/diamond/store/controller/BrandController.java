package com.diamond.store.controller;

import com.diamond.store.dto.request.ProductAttributeRequest;
import com.diamond.store.dto.response.ApiResponse;
import com.diamond.store.model.Brand;
import com.diamond.store.service.ProductAttributeService.BrandService;
import com.diamond.store.util.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;


    @GetMapping
    public ApiResponse<List<Brand>> findAll() {

        return ApiResponse.<List<Brand>>builder()
                .code(HttpStatus.OK.value())
                .data(brandService.findAll())
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Brand> findById(@PathVariable Integer id) {

        return ApiResponse.<Brand>builder()
                .code(HttpStatus.OK.value())
                .data(brandService.findById(id))
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .build();
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createBrand(@RequestBody ProductAttributeRequest attributeRequest) {

        Brand brand = Brand.builder()
                .brandName(attributeRequest.getAttributeName())
                .description(attributeRequest.getDescription())
                .build();

        brandService.create(brand);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Void>builder()
                .code(HttpStatus.CREATED.value())
                .message(ApplicationMessage.CREATE_DATA_SUCCESS)
                .build());


    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<Void>> deleteBrand(@PathVariable Integer id) {

        brandService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(ApplicationMessage.DELETE_DATA_SUCCESS)
                .code(HttpStatus.OK.value())
                .build());
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<Void>> updateBrand(@PathVariable Integer id, @RequestBody ProductAttributeRequest attributeRequest) {

        Brand brand = Brand.builder()
                .brandId(id)
                .brandName(attributeRequest.getAttributeName())
                .description(attributeRequest.getDescription())
                .build();
        brandService.update(brand, id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(ApplicationMessage.UPDATE_DATA_SUCCESS)
                .code(HttpStatus.OK.value())
                .build());
    }
}
