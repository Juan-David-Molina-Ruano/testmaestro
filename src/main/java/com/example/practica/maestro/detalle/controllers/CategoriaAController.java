package com.example.practica.maestro.detalle.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.practica.maestro.detalle.models.CategoriaA;
import com.example.practica.maestro.detalle.services.interfaces.ICategoriaAService;


@Controller
@RequestMapping("/categorias")
public class CategoriaAController {

    @Autowired
    private ICategoriaAService categoriaAService;

    @GetMapping("/index")
    public String index(Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        PageRequest pageable = PageRequest.of(currentPage - 1, pageSize);

        Page<CategoriaA> categorias = categoriaAService.buscarTodosPaginados(pageable);
        model.addAttribute("categorias", categorias);

        if (categorias.getTotalPages() > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, categorias.getTotalPages())
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return "/categorias/index";
    }

    @GetMapping("/create")
    public String create(CategoriaA categoriaA) {
        return "/categorias/create";
    }

    @PostMapping("/save")
    public String save(CategoriaA categoriaA, BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            model.addAttribute(categoriaA);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "/categorias/create";
        }

        categoriaAService.crearOEditar(categoriaA);
        attributes.addFlashAttribute("msg", "Categoria guardada correctamente");
        return "redirect:/categorias/index";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        CategoriaA categoriaA = categoriaAService.buscarPorId(id).get();
        model.addAttribute("categoriaA", categoriaA);
        return "/categorias/edit";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        Optional<CategoriaA> cateOptional = categoriaAService.buscarPorId(id);
        if (cateOptional.isPresent()) {
            CategoriaA categoriaA = cateOptional.get();
            model.addAttribute("categoriaA", categoriaA);
            return "/categorias/details"; // Asegúrate de que esta ruta coincide con la estructura de carpetas
        } else {
            // Manejo de error si el producto no existe
            return "redirect:/categorias/index"; // Redirige a una página de error o lista
        }
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable("id") Integer id, Model model) {
        Optional<CategoriaA> cOptional = categoriaAService.buscarPorId(id);
        if (cOptional.isPresent()) {
            CategoriaA categoriaA = cOptional.get();
            model.addAttribute("categoriaA", categoriaA);
            return "/categorias/delete";
        } else {
            return "redirect:/categorias/index";
        }
    }

    @PostMapping("/delete")
    public String delete(CategoriaA categoriaA, RedirectAttributes attributes) {
        categoriaAService.eliminarPorId(categoriaA.getId());
        attributes.addFlashAttribute("msg", "Categoria eliminada correctamente");
        return "redirect:/categorias/index";
    }

}
