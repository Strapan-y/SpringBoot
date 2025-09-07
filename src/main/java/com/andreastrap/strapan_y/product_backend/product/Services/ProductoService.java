package com.andreastrap.strapan_y.product_backend.product.Services;

import com.andreastrap.strapan_y.product_backend.product.Repository.ProductoRepository;
import com.andreastrap.strapan_y.product_backend.product.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    // Constructor con inyección de dependencias (recomendado)
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }
    
    /**
     * Obtiene todos los productos
     * @return Lista de todos los productos
     */
    public List<Producto> findAllProductos() {
        return productoRepository.findAll();
    }
    
    /**
     * Busca un producto por su ID
     * @param id ID del producto a buscar
     * @return Optional con el producto si existe, vacío si no existe
     */
    public Optional<Producto> findProductoById(Long id) {
        return productoRepository.findById(id);
    }
    
    /**
     * Guarda un nuevo producto o actualiza uno existente
     * @param producto Producto a guardar
     * @return Producto guardado
     */
    public Producto saveProducto(Producto producto) {
        return productoRepository.save(producto);
    }
    
    /**
     * Elimina un producto por su ID
     * @param id ID del producto a eliminar
     * @return true si el producto existía y fue eliminado, false si no existía
     */
    public boolean deleteProductoById(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    /**
     * Verifica si existe un producto con el ID especificado
     * @param id ID del producto
     * @return true si existe, false si no existe
     */
    public boolean existsById(Long id) {
        return productoRepository.existsById(id);
    }
    
    /**
     * Cuenta el total de productos
     * @return Número total de productos
     */
    public long countProductos() {
        return productoRepository.count();
    }
    
    // Métodos adicionales usando los métodos personalizados del repositorio
    
    /**
     * Busca productos por nombre (ignorando mayúsculas/minúsculas)
     * @param nombre Nombre del producto
     * @return Lista de productos con ese nombre
     */
    public List<Producto> findByNombre(String nombre) {
        return productoRepository.findByNombreIgnoreCase(nombre);
    }
    
    /**
     * Busca productos que contengan una cadena en el nombre
     * @param nombre Cadena a buscar en el nombre
     * @return Lista de productos que contienen la cadena
     */
    public List<Producto> findByNombreContaining(String nombre) {
        return productoRepository.findByNombreContainingIgnoreCase(nombre);
    }
    
    /**
     * Busca productos en un rango de precios
     * @param precioMin Precio mínimo
     * @param precioMax Precio máximo
     * @return Lista de productos en el rango de precios
     */
    public List<Producto> findByPrecioRange(Double precioMin, Double precioMax) {
        return productoRepository.findByPrecioBetween(precioMin, precioMax);
    }
    
    /**
     * Obtiene todos los productos ordenados por precio ascendente
     * @return Lista de productos ordenados por precio
     */
    public List<Producto> findAllOrderByPrecio() {
        return productoRepository.findAllOrderByPrecioAsc();
    }
    
    /**
     * Obtiene todos los productos ordenados por nombre
     * @return Lista de productos ordenados por nombre
     */
    public List<Producto> findAllOrderByNombre() {
        return productoRepository.findAllOrderByNombreAsc();
    }
    
    /**
     * Verifica si existe un producto con el nombre especificado
     * @param nombre Nombre del producto
     * @return true si existe, false si no existe
     */
    public boolean existsByNombre(String nombre) {
        return productoRepository.existsByNombreIgnoreCase(nombre);
    }
}
