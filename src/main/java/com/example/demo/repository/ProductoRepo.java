package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Producto;

public interface ProductoRepo extends JpaRepository<Producto, Long> {
	
	@Query("SELECT p FROM Producto p WHERE UPPER(p.name) LIKE UPPER(:name) ESCAPE '\\'")
	List<Producto> findByNameContainingIgnoreCase(String name);

    @Query("""
        select p from Producto p
        where lower(p.name) like lower(concat('%', :search, '%'))
          and (:minPrice is null or p.price >= :minPrice)
          and (:maxPrice is null or p.price <= :maxPrice)
        """)
    Page<Producto> filterProducts(@Param("search") String search,
            @Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice,
            Pageable pageable);

}
