package com.andreastrap.strapan_y.product_backend.product.controller;

import com.andreastrap.strapan_y.product_backend.product.Services.ProductoService;
import com.andreastrap.strapan_y.product_backend.product.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {
    
    @Autowired
    private ProductoService productoService;
    
    // Constructor con inyección de dependencias
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }
    
    /**
     * GET /api/productos - Obtiene todos los productos
     * @return Lista de todos los productos
     */
    @GetMapping
    public ResponseEntity<List<Producto>> getAllProductos() {
        try {
            List<Producto> productos = productoService.findAllProductos();
            if (productos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * GET /api/productos/{id} - Obtiene un producto por ID
     * @param id ID del producto
     * @return Producto encontrado o 404 si no existe
     */
    @GetMapping("/{id}")
    public ResponseEntity<Producto> getProductoById(@PathVariable("id") Long id) {
        try {
            Optional<Producto> producto = productoService.findProductoById(id);
            if (producto.isPresent()) {
                return new ResponseEntity<>(producto.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * POST /api/productos - Crea un nuevo producto
     * @param producto Datos del producto a crear
     * @return Producto creado
     */
    @PostMapping
    public ResponseEntity<Producto> createProducto(@RequestBody Producto producto) {
        try {
            // Validaciones básicas
            if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            if (producto.getPrecio() == null || producto.getPrecio() < 0) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            // Verificar si ya existe un producto con el mismo nombre
            if (productoService.existsByNombre(producto.getNombre())) {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
            
            Producto nuevoProducto = productoService.saveProducto(producto);
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * PUT /api/productos/{id} - Actualiza un producto existente
     * @param id ID del producto a actualizar
     * @param producto Nuevos datos del producto
     * @return Producto actualizado o 404 si no existe
     */
    @PutMapping("/{id}")
    public ResponseEntity<Producto> updateProducto(@PathVariable("id") Long id, @RequestBody Producto producto) {
        try {
            Optional<Producto> productoExistente = productoService.findProductoById(id);
            if (productoExistente.isPresent()) {
                // Validaciones básicas
                if (producto.getNombre() == null || producto.getNombre().trim().isEmpty()) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                if (producto.getPrecio() == null || producto.getPrecio() < 0) {
                    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
                }
                
                // Actualizar los datos
                Producto productoActualizar = productoExistente.get();
                productoActualizar.setNombre(producto.getNombre());
                productoActualizar.setPrecio(producto.getPrecio());
                
                Producto productoActualizado = productoService.saveProducto(productoActualizar);
                return new ResponseEntity<>(productoActualizado, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * DELETE /api/productos/{id} - Elimina un producto por ID
     * @param id ID del producto a eliminar
     * @return 204 si se eliminó correctamente, 404 si no existe
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteProducto(@PathVariable("id") Long id) {
        try {
            boolean eliminado = productoService.deleteProductoById(id);
            if (eliminado) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Endpoints adicionales para búsquedas específicas
    
    /**
     * GET /api/productos/buscar/nombre/{nombre} - Busca productos por nombre
     * @param nombre Nombre del producto a buscar
     * @return Lista de productos que coinciden
     */
    @GetMapping("/buscar/nombre/{nombre}")
    public ResponseEntity<List<Producto>> getProductosByNombre(@PathVariable("nombre") String nombre) {
        try {
            List<Producto> productos = productoService.findByNombreContaining(nombre);
            if (productos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * GET /api/productos/buscar/precio - Busca productos en un rango de precios
     * @param min Precio mínimo
     * @param max Precio máximo
     * @return Lista de productos en el rango
     */
    @GetMapping("/buscar/precio")
    public ResponseEntity<List<Producto>> getProductosByPrecioRange(
            @RequestParam("min") Double min, 
            @RequestParam("max") Double max) {
        try {
            if (min < 0 || max < 0 || min > max) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            
            List<Producto> productos = productoService.findByPrecioRange(min, max);
            if (productos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * GET /api/productos/ordenar/precio - Obtiene productos ordenados por precio
     * @return Lista de productos ordenados por precio ascendente
     */
    @GetMapping("/ordenar/precio")
    public ResponseEntity<List<Producto>> getProductosOrderByPrecio() {
        try {
            List<Producto> productos = productoService.findAllOrderByPrecio();
            if (productos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * GET /api/productos/ordenar/nombre - Obtiene productos ordenados por nombre
     * @return Lista de productos ordenados por nombre
     */
    @GetMapping("/ordenar/nombre")
    public ResponseEntity<List<Producto>> getProductosOrderByNombre() {
        try {
            List<Producto> productos = productoService.findAllOrderByNombre();
            if (productos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(productos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    /**
     * GET /api/productos/count - Obtiene el número total de productos
     * @return Número total de productos
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getProductosCount() {
        try {
            long count = productoService.countProductos();
            return new ResponseEntity<>(count, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
