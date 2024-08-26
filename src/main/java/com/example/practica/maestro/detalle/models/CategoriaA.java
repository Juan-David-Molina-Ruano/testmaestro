package com.example.practica.maestro.detalle.models;

import java.util.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


@Entity
@Table(name = "categorias")
public class CategoriaA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // nombre
    @NotBlank(message = "El nombre es requerido")
    private String nombreA;

    // nombre
    @NotBlank(message = "la descripcion es requerida")
    private String descripcionA;

    @OneToMany(mappedBy = "categoriaA", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductoA> productos = new ArrayList<>();  // Inicializar la lista

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombreA() {
        return nombreA;
    }

    public void setNombreA(String nombreA) {
        this.nombreA = nombreA;
    }

    public List<ProductoA> getProductos() {
        return productos;
    }

    public void setProductos(List<ProductoA> productos) {
        this.productos = productos;
    }

    public String getDescripcionA() {
        return descripcionA;
    }

    public void setDescripcionA(String descripcionA) {
        this.descripcionA = descripcionA;
    }


}
