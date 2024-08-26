package com.example.practica.maestro.detalle.services.interfaces;

import java.util.*;
import org.springframework.data.domain.*;
import com.example.practica.maestro.detalle.models.CategoriaA;

public interface ICategoriaAService {

    Page<CategoriaA> buscarTodosPaginados(Pageable pageable);

    List<CategoriaA> obtenerTodos();

    Optional<CategoriaA> buscarPorId(Integer id);

    CategoriaA crearOEditar(CategoriaA grupo);
    
    void eliminarPorId(Integer id);
}
