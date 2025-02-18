package com.diamond.store.controller;

import com.diamond.store.dto.request.AddCartRequest;
import com.diamond.store.dto.response.ApiResponse;
import com.diamond.store.dto.response.CartItemResponse;
import com.diamond.store.service.CartService.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;


    @PostMapping("/add-product")
    public ApiResponse<Void> addProduct(@RequestBody AddCartRequest addCartRequest) {

        cartService.addProductToCart(addCartRequest.getCartId(), addCartRequest.getProductId(), addCartRequest.getQuantity());

        return ApiResponse.<Void>builder()
                .message("success")
                .code(HttpStatus.OK.value())
                .build();
    }

    @GetMapping("/{cartId}")
    public ApiResponse<List<CartItemResponse>> getCart(@PathVariable Integer cartId) {

        return ApiResponse.<List<CartItemResponse>>builder()
                .message("success")
                .code(HttpStatus.OK.value())
                .data(cartService.getCartItems(cartId).stream().map(cartItem -> CartItemResponse.builder()
                        .id(cartItem.getId())
                        .quantity(cartItem.getQuantity())
                        .productId(cartItem.getProduct().getProductId())
                        .build()).collect(Collectors.toList()))
                .build();
    }

}
