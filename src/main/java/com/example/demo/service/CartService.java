package com.example.demo.service;

import java.util.Iterator;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.model.Producto;

@Service
public class CartService {

    public void addProduct(List<Producto> cartItems, Producto product) {
        cartItems.add(product);
    }

    public boolean removeProduct(List<Producto> cartItems, Long productId) {
        Iterator<Producto> iterator = cartItems.iterator();

        while (iterator.hasNext()) {
            Producto product = iterator.next();
            if (product.getId().equals(productId)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    public List<Producto> getCartItems(List<Producto> cartItems) {
        return cartItems;
    }
    // Sumamos los precios de todos los artículos, incluidos los repetidos.
    public BigDecimal getTotal(List<Producto> cartItems) {
        BigDecimal total = BigDecimal.ZERO;
        for (Producto product : cartItems) {
            total = total.add(BigDecimal.valueOf(product.getPrice()));
        }
        return total;
    }

    public void clearCart(List<Producto> cartItems) {
        cartItems.clear();
    }
}
