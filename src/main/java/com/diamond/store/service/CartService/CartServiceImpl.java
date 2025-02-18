package com.diamond.store.service.CartService;

import com.diamond.store.exception.ResourceNotFoundException;
import com.diamond.store.model.Cart;
import com.diamond.store.model.CartItem;
import com.diamond.store.model.Product;
import com.diamond.store.repository.CartItemRepository;
import com.diamond.store.repository.CartRepository;
import com.diamond.store.service.ProductService.ProductService;
import com.diamond.store.util.ApplicationMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;


    @Override
    public void addProductToCart(int cartId, int productId, int quantity) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException(ApplicationMessage.CART_NOTFOUND));
        Product product = productService.findById(productId);

        cartItemRepository.findCartItemByCartAndProduct(cart, product).ifPresentOrElse(cartItem1 -> {
            cartItem1.setQuantity(cartItem1.getQuantity() + quantity);
            cartItemRepository.save(cartItem1);
        }, () -> {
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(quantity)
                    .build();

            cartItemRepository.save(cartItem);
        });

    }

    @Override
    public List<CartItem> getCartItems(int cartId) {

        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new ResourceNotFoundException(ApplicationMessage.CART_NOTFOUND));

        return cartItemRepository.findCartItemsByCart(cart);
    }
}
