package com.example.practica.maestro.detalle.services.interfaces;

import java.util.*;
import org.springframework.data.domain.*;
import com.example.practica.maestro.detalle.models.ProductoA;

public interface IProductoService {

    Page<ProductoA> buscarTodosPaginados(Pageable pageable);

    List<ProductoA> obtenerTodos();

    Optional<ProductoA> buscarPorId(Integer id);

    ProductoA crearOEditar(ProductoA productoA);
    
    void eliminarPorId(Integer id);
}
