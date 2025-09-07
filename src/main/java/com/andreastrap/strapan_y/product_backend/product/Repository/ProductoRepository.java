package com.andreastrap.strapan_y.product_backend.product.Repository;

import com.andreastrap.strapan_y.product_backend.product.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Método para buscar productos por nombre (ignorando mayúsculas/minúsculas)
    List<Producto> findByNombreIgnoreCase(String nombre);
    
    // Método para buscar productos que contengan una cadena en el nombre
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    
    // Método para buscar productos por rango de precio
    List<Producto> findByPrecioBetween(Double precioMin, Double precioMax);
    
    // Método para buscar productos con precio mayor a un valor específico
    List<Producto> findByPrecioGreaterThan(Double precio);
    
    // Método para buscar productos con precio menor a un valor específico
    List<Producto> findByPrecioLessThan(Double precio);
    
    // Método para verificar si existe un producto con un nombre específico
    boolean existsByNombreIgnoreCase(String nombre);
    
    // Consulta personalizada con JPQL para buscar productos ordenados por precio
    @Query("SELECT p FROM Producto p ORDER BY p.precio ASC")
    List<Producto> findAllOrderByPrecioAsc();
    
    // Consulta personalizada con JPQL para buscar productos ordenados por nombre
    @Query("SELECT p FROM Producto p ORDER BY p.nombre ASC")
    List<Producto> findAllOrderByNombreAsc();
    
    // Consulta personalizada con parámetros
    @Query("SELECT p FROM Producto p WHERE p.nombre LIKE %:nombre% AND p.precio BETWEEN :precioMin AND :precioMax")
    List<Producto> findByNombreAndPrecioRange(@Param("nombre") String nombre, 
                                            @Param("precioMin") Double precioMin, 
                                            @Param("precioMax") Double precioMax);
}
