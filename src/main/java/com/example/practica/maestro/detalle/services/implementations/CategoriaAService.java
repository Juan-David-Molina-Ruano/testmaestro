package com.example.practica.maestro.detalle.services.implementations;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.example.practica.maestro.detalle.models.CategoriaA;
import com.example.practica.maestro.detalle.repositories.ICategoriaARepository;
import com.example.practica.maestro.detalle.services.interfaces.ICategoriaAService;

@Service
public class CategoriaAService implements ICategoriaAService {
    
    @Autowired
    private ICategoriaARepository categoriaARepository;

    @Override
    public Page<CategoriaA> buscarTodosPaginados(Pageable pageable) {
        return categoriaARepository.findAll(pageable);
    }

    @Override
    public List<CategoriaA> obtenerTodos() {
        return categoriaARepository.findAll();
    }

    @Override
    public Optional<CategoriaA> buscarPorId(Integer id) {
        return categoriaARepository.findById(id);
    }    
    @Override
    public CategoriaA crearOEditar(CategoriaA categoriaA) {
        return categoriaARepository.save(categoriaA);
    }

    @Override
    public void eliminarPorId(Integer id) {
        categoriaARepository.deleteById(id);
    }
}
