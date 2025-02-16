package com.diamond.store.controller;

import com.diamond.store.dto.request.ProductAttributeRequest;
import com.diamond.store.dto.response.ApiResponse;
import com.diamond.store.model.Collection;
import com.diamond.store.service.ProductAttributeService.CollectionService;
import com.diamond.store.util.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;


    @GetMapping
    public ApiResponse<List<Collection>> findAll() {

        return ApiResponse.<List<Collection>>builder()
                .code(HttpStatus.OK.value())
                .data(collectionService.findAll())
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Collection> findById(@PathVariable Integer id) {

        return ApiResponse.<Collection>builder()
                .code(HttpStatus.OK.value())
                .data(collectionService.findById(id))
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .build();
    }


    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createCollection(@RequestBody ProductAttributeRequest attributeRequest) {

        Collection collection = Collection.builder()
                .collectionName(attributeRequest.getAttributeName())
                .description(attributeRequest.getDescription())
                .build();

        collectionService.create(collection);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Void>builder()
                .code(HttpStatus.CREATED.value())
                .message(ApplicationMessage.CREATE_DATA_SUCCESS)
                .build());


    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<ApiResponse<Void>> deleteCollection(@PathVariable Integer id) {

        collectionService.delete(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(ApplicationMessage.DELETE_DATA_SUCCESS)
                .code(HttpStatus.OK.value())
                .build());
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<ApiResponse<Void>> updateCollection(@PathVariable Integer id, @RequestBody ProductAttributeRequest attributeRequest) {

        Collection collection = Collection.builder()
                .collectionId(id)
                .collectionName(attributeRequest.getAttributeName())
                .description(attributeRequest.getDescription())
                .build();
        collectionService.update(collection, id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.<Void>builder()
                .message(ApplicationMessage.UPDATE_DATA_SUCCESS)
                .code(HttpStatus.OK.value())
                .build());
    }
}
