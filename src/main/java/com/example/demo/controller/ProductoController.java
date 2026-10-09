package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;

@Controller
@RequestMapping("/products")
public class ProductoController {

    @Autowired
    private ProductoService productoService;
    
    // Metodo actualizado con la búsqueda y el filtrado
    @GetMapping
    public String getProductsPage(@RequestParam(defaultValue = "") String search, @RequestParam(required = false) Double minPrice, 
    								@RequestParam (required = false) Double maxPrice,
                                    @RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "id") String sort,
                                    @RequestParam(defaultValue = "asc") String direction, Model model) {
    	
    	// Para la ordenación de productos
    	if ("name".equals(sort) || "price".equals(sort)) {
    	    sort = sort;
    	} else {
    	    sort = "id";
    	}
    	if ("desc".equals(direction)) {
    	    direction = "desc";
    	} else {
    	    direction = "asc";
    	}
        Page<Producto> productPage = productoService.filterProducts(search, minPrice, maxPrice, page, sort, direction);
        model.addAttribute("sort", sort);
        model.addAttribute("direction", direction);
    	model.addAttribute("products", productPage.getContent());
        model.addAttribute("productPage", productPage);
    	model.addAttribute("search", search);
    	model.addAttribute("minPrice", minPrice);
    	model.addAttribute("maxPrice", maxPrice);
    	
    	return "products";
    }
    
    @PostMapping("/add")
    public String addProduct(@RequestParam String name, @RequestParam double price, RedirectAttributes redirectAttributes) {
    	Producto product = new Producto();
    	product.setName(name);
    	product.setPrice(price);
    	
    	productoService.saveProduct(product);
        redirectAttributes.addFlashAttribute("successMessage", "Producto añadido correctamente.");
    	
    	return "redirect:/products";
    }
    
    @GetMapping("/add")
    public String showAddForm(Model model) {
    	model.addAttribute("product", new Producto());
    	return "product_form";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
    	Producto product = productoService.getProductoById(id);
    	
    	if (product == null) {
    		return "redirect:/products";
    	}
    	model.addAttribute("product", product);
    	return "product_form";
    }
    
    @PostMapping("/edit/{id}")
    	public String editProduct(@PathVariable Long id, @RequestParam String name, @RequestParam double price, RedirectAttributes redirectAttributes) {
    		Producto product = productoService.getProductoById(id);
    		if (product != null) {
    			product.setName(name);
    			product.setPrice(price);
    			productoService.saveProduct(product);
                redirectAttributes.addFlashAttribute("successMessage", "Producto actualizado correctamente.");
    		} else {
                redirectAttributes.addFlashAttribute("errorMessage", "El producto ya no está disponible.");
            }
    		return "redirect:/products";
    }
    
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        if (productoService.getProductoById(id) != null) {
            productoService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("successMessage", "Producto eliminado correctamente.");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "El producto ya no está disponible.");
        }
    	return "redirect:/products";
    }

}
