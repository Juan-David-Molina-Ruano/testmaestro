package com.example.practica.maestro.detalle.services.interfaces;

import java.util.*;
import org.springframework.data.domain.*;
import com.example.practica.maestro.detalle.models.EtiquetaA;

public interface IEtiquetaAService {

    Page<EtiquetaA> buscarTodosPaginados(Pageable pageable);

    List<EtiquetaA> obtenerTodos();

    Optional<EtiquetaA> buscarPorId(Integer id);

    EtiquetaA crearOEditar(EtiquetaA grupo);
    
    void eliminarPorId(Integer id);

}
