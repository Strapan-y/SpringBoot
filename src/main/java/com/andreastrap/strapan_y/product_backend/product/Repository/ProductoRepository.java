package com.andreastrap.strapan_y.product_backend.product.Repository;

import com.andreastrap.strapan_y.product_backend.product.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
}
