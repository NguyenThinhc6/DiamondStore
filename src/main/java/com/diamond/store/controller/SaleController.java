package com.diamond.store.controller;

import com.diamond.store.dto.request.SaleRequest;
import com.diamond.store.dto.response.ApiResponse;
import com.diamond.store.model.Sale;
import com.diamond.store.service.ProductAttributeService.SaleService;
import com.diamond.store.util.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales")
@RequiredArgsConstructor
public class SaleController {

    private final SaleService saleService;


    @GetMapping
    public ApiResponse<List<Sale>> findAll() {

        return ApiResponse.<List<Sale>>builder()
                .code(HttpStatus.OK.value())
                .data(saleService.findAll())
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<Sale> findById(@PathVariable String id) {

        return ApiResponse.<Sale>builder()
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.GET_DATA_SUCCESS)
                .data(saleService.findById(id))
                .build();
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<Void>> createSale(@RequestBody SaleRequest saleRequest) {

        Sale sale = Sale.builder()
                .enable(saleRequest.isEnable())
                .saleType(saleRequest.getSaleType())
                .saleName(saleRequest.getSaleName())
                .value(saleRequest.getValue())
                .description(saleRequest.getDescription())
                .timeStart(saleRequest.getTimeStart())
                .timeEnd(saleRequest.getTimeEnd())
                .build();

            saleService.create(sale);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.<Void>builder()
                .code(HttpStatus.CREATED.value())
                .message(ApplicationMessage.CREATE_DATA_SUCCESS)
                .build());
    }

    @DeleteMapping("/{id}/delete")
    public ApiResponse<Void> deleteSale(@PathVariable String id) {
        saleService.delete(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.DELETE_DATA_SUCCESS)
                .build();
    }

    @PutMapping("/{id}/update")
    public ApiResponse<Void> updateSale(@PathVariable String id, @RequestBody SaleRequest saleRequest) {
        Sale sale = Sale.builder()
                .saleId(id)
                .enable(saleRequest.isEnable())
                .saleType(saleRequest.getSaleType())
                .saleName(saleRequest.getSaleName())
                .value(saleRequest.getValue())
                .description(saleRequest.getDescription())
                .timeStart(saleRequest.getTimeStart())
                .timeEnd(saleRequest.getTimeEnd())
                .build();

        saleService.update(sale, id);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message(ApplicationMessage.UPDATE_DATA_SUCCESS)
                .build();
    }
}
