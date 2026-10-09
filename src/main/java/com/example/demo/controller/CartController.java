package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.example.demo.model.Producto;
import com.example.demo.service.CartService;
import com.example.demo.service.ProductoService;

@Controller
@RequestMapping("/cart")
@SessionAttributes("cartItems")
public class CartController {

    private final CartService cartService;
    private final ProductoService productoService;

    public CartController(CartService cartService, ProductoService productoService) {
        this.cartService = cartService;
        this.productoService = productoService;
    }

    @ModelAttribute("cartItems")
    public List<Producto> cartItems() {
        return new ArrayList<>();
    }

    @GetMapping
    public String getCartPage(@ModelAttribute("cartItems") List<Producto> cartItems, Model model) {
        model.addAttribute("cartItems", cartService.getCartItems(cartItems));
        model.addAttribute("cartTotal", cartService.getTotal(cartItems));
        return "cart";
    }

    @PostMapping("/add/{id}")
    public String addProductToCart(@PathVariable Long id,
                                   @ModelAttribute("cartItems") List<Producto> cartItems,
                                   RedirectAttributes redirectAttributes) {
        Producto product = productoService.getProductoById(id);

        if (product != null) {
            cartService.addProduct(cartItems, product);
            redirectAttributes.addFlashAttribute("successMessage", "Producto añadido al carrito.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "El producto ya no está disponible.");
        }

        return "redirect:/cart";
    }

    @PostMapping("/remove/{id}")
    public String removeProductFromCart(@PathVariable Long id,
                                        @ModelAttribute("cartItems") List<Producto> cartItems,
                                   RedirectAttributes redirectAttributes) {
        if (cartService.removeProduct(cartItems, id)) {
            redirectAttributes.addFlashAttribute("successMessage", "Producto retirado del carrito.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "El producto ya no está en el carrito.");
        }
        return "redirect:/cart";
    }
    
    // Para limpiar el carrito
    @PostMapping("/clear")
    public String clearCart(@ModelAttribute("cartItems") List<Producto> cartItems,
                            RedirectAttributes redirectAttributes) {
        cartService.clearCart(cartItems);
        redirectAttributes.addFlashAttribute("successMessage", "Carrito vaciado correctamente.");
        return "redirect:/cart";
    }
}
