package com.diamond.store.service.CartService;

import com.diamond.store.model.CartItem;

import java.util.List;

public interface CartService {

    void addProductToCart(int cartId, int productId, int quantity);
    List<CartItem> getCartItems(int cartId);
}
