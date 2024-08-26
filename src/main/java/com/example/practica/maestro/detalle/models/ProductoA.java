package com.example.practica.maestro.detalle.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

import java.util.*;

import jakarta.annotation.*;

@Entity
@Table(name = "productos")
public class ProductoA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // nombre
    @NotBlank(message = "El nombre es requerido")
    private String nombreA;

    @NotNull(message = "El precio es requerido")
    private BigDecimal precioA;

    @NotNull(message = "La existencia es requerida")
    private int existenciaA;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "categoriaA_id", referencedColumnName = "id")
    private CategoriaA categoriaA; 

    @OneToMany(mappedBy = "productoA", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<EtiquetaA> etiquetas = new ArrayList<>(); 

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


    public int getExistenciaA() {
        return existenciaA;
    }

    public void setExistenciaA(int existenciaA) {
        this.existenciaA = existenciaA;
    }

    public CategoriaA getCategoriaA() {
        return categoriaA;
    }

    public void setCategoriaA(CategoriaA categoriaA) {
        this.categoriaA = categoriaA;
    }


    public BigDecimal getPrecioA() {
        return precioA;
    }

    public void setPrecioA(BigDecimal precioA) {
        this.precioA = precioA;
    }

    public List<EtiquetaA> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(List<EtiquetaA> etiquetas) {
        this.etiquetas = etiquetas;
    } 


    
}
