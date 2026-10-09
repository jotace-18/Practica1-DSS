package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import com.example.demo.model.Producto;
import com.example.demo.repository.ProductoRepo;

@Service
public class ProductoService {

    public Page<Producto> filterProducts(String search, Double minPrice, Double maxPrice, int page) {
        return filterProducts(search, minPrice, maxPrice, page, "id", "asc");
    }

    public Page<Producto> filterProducts(String search, Double minPrice, Double maxPrice, int page,
            String sort, String direction) {
        // Ordenamos por nombre o precio; por defecto usamos el identificador.
        String field;
        if ("name".equals(sort) || "price".equals(sort)) {
            field = sort;
        } else {
            field = "id";
        }

        // Elegimos el sentido de la ordenación.
        Sort.Direction sortDirection;
        if ("desc".equals(direction)) {
            sortDirection = Sort.Direction.DESC;
        } else {
            sortDirection = Sort.Direction.ASC;
        }
        Sort.Order order = new Sort.Order(sortDirection, field);

        // Para ordenar nombres, ignoramos las mayúsculas y minúsculas.
        Sort ordering;
        if ("name".equals(field)) {
            ordering = Sort.by(order.ignoreCase());
        } else {
            ordering = Sort.by(order);
        }
        if (!"id".equals(field)) {
            ordering = ordering.and(Sort.by("id"));
        }
        
        // Para la paginación
        Page<Producto> products = productoRepo.filterProducts(search.trim(), minPrice, maxPrice,
                PageRequest.of(Math.max(0, page), 10, ordering));
        if (products.getTotalPages() > 0 && products.getNumber() >= products.getTotalPages()) {
            return productoRepo.filterProducts(search.trim(), minPrice, maxPrice,
                    PageRequest.of(products.getTotalPages() - 1, 10, ordering));
        }
        return products;
    }

    @Autowired
    private ProductoRepo productoRepo;
    
    public List<Producto> getAllProducts(){
    	return productoRepo.findAll();
    }
    
    public void saveProduct(Producto product) {
    	productoRepo.save(product);
    }
    
    public Producto getProductoById(Long id) {
    	return productoRepo.findById(id).orElse(null); // el orElse sirve para que en caso de que no lo encuentre, devuelva null
    }
    
    public void deleteProduct(Long id) {
    	productoRepo.deleteById(id);
    }
    
    
    // Mejora opcional para la búsqueda
    public List<Producto> searchProducts(String search){
    	if (search == null || search.trim().isEmpty()) {
    		return productoRepo.findAll();
    	}
    	return productoRepo.findByNameContainingIgnoreCase(search.trim());
    }
    
    public List<Producto> filterProducts(String search, Double minPrice, Double maxPrice){
    	List<Producto> products = searchProducts(search);
    	List<Producto> filteredProducts = new ArrayList<>();
    	
    	for (Producto product : products) {
    		boolean cumpleMinimo = minPrice == null || product.getPrice() >= minPrice;
    		boolean cumpleMaximo = maxPrice == null || product.getPrice() <= maxPrice;
    		
    		if (cumpleMinimo && cumpleMaximo) {
    			filteredProducts.add(product);
    		}
    	}
    	return filteredProducts;
    }

}
