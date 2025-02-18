package com.diamond.store.repository;

import com.diamond.store.model.Cart;
import com.diamond.store.model.CartItem;
import com.diamond.store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findCartItemsByCart(Cart cart);

    Optional<CartItem> findCartItemByCartAndProduct(Cart cart, Product product);
}
