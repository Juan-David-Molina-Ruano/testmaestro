package com.example.practica.maestro.detalle.models;


import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "etiquetas")
public class EtiquetaA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // nombre
    @NotBlank(message = "El nombre es requerido")
    private String nombreA;

    @Nonnull
    @ManyToOne
    @JoinColumn(name = "productoA_id", referencedColumnName = "id")
    private ProductoA productoA; 

    public Integer getId() {
        return id;
    }

    public EtiquetaA() {
    }

    

    public EtiquetaA(ProductoA productoA ,@NotBlank(message = "El nombre es requerido") String nombreA) {
        this.nombreA = nombreA;
        this.productoA = productoA;
    }

    public ProductoA getProductoA() {
        return productoA;
    }

    public void setProductoA(ProductoA productoA) {
        this.productoA = productoA;
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

}
