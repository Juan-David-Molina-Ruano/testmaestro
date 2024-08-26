package com.example.practica.maestro.detalle.controllers;

import java.util.*;
import java.util.stream.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import com.example.practica.maestro.detalle.models.EtiquetaA;
import com.example.practica.maestro.detalle.models.ProductoA;
import com.example.practica.maestro.detalle.services.interfaces.IProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoAController {

    @Autowired
    private IProductoService productoAService;

    @GetMapping("/index")
    public String index(Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        PageRequest pageable = PageRequest.of(currentPage - 1, pageSize);

        Page<ProductoA> productoslista = productoAService.buscarTodosPaginados(pageable);
        model.addAttribute("productoslista", productoslista);

        if (productoslista.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, productoslista.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "/productos/index";
    }

    @GetMapping("/create")
    public String create(ProductoA productoA, Model model) {
        model.addAttribute(productoA);
        return "/productos/create";
    }

    @PostMapping("/addetiquetas")
    public String addPhone(ProductoA productoA, Model model) {
        if (productoA.getEtiquetas() == null)
        productoA.setEtiquetas(new ArrayList<>());
        productoA.getEtiquetas().add(new EtiquetaA(productoA, ""));

        if (productoA.getEtiquetas() != null) {
            Integer idDet = (int) 0l;
            for (EtiquetaA item : productoA.getEtiquetas()) {
                if (item.getId() == null || item.getId() < 1) {
                    idDet--;
                    item.setId(idDet);
                }
            }
        }

        model.addAttribute(productoA);
        if (productoA.getId() != null && productoA.getId() > 0)
            return "/productos/edit";
        else
            return "/productos/create";
    }

    @PostMapping("/deletiquetas/{id}")
    public String delPhone(@PathVariable("id") Integer id, ProductoA productoA, Model model) {
        productoA.getEtiquetas().removeIf(elemento -> elemento.getId() == id);
        model.addAttribute(productoA);
        if (productoA.getId() != null && productoA.getId() > 0)
            return "/productos/edit";
        else
            return "/productos/create";
    }

}
