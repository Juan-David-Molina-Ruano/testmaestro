package com.example.practica.maestro.detalle.services.implementations;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.*;
import com.example.practica.maestro.detalle.models.ProductoA;
import com.example.practica.maestro.detalle.repositories.IProductoARepository;
import com.example.practica.maestro.detalle.services.interfaces.IProductoService;

@Service
public class ProductoAService implements IProductoService{

    @Autowired
    private IProductoARepository productoARepository;

    @Override
    public Page<ProductoA> buscarTodosPaginados(Pageable pageable) {
        return productoARepository.findAll(pageable);
    }

    @Override
    public List<ProductoA> obtenerTodos() {
        return productoARepository.findAll();
    }

    @Override
    public Optional<ProductoA> buscarPorId(Integer id) {
        return productoARepository.findById(id);
    }    
    @Override
    public ProductoA crearOEditar(ProductoA categoriaA) {
        return productoARepository.save(categoriaA);
    }

    @Override
    public void eliminarPorId(Integer id) {
        productoARepository.deleteById(id);
    }
}
