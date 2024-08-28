package com.example.practica.maestro.detalle.controllers;

import java.util.*;
import java.util.stream.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import com.example.practica.maestro.detalle.models.CategoriaA;
import com.example.practica.maestro.detalle.models.EtiquetaA;
import com.example.practica.maestro.detalle.models.ProductoA;
import com.example.practica.maestro.detalle.services.interfaces.ICategoriaAService;
import com.example.practica.maestro.detalle.services.interfaces.IProductoService;

@Controller
@RequestMapping("/productos")
public class ProductoAController {

    @Autowired
    private IProductoService productoAService;

    @Autowired
    private ICategoriaAService categoriaAService;

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
        model.addAttribute("categorias", categoriaAService.obtenerTodos());
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

        model.addAttribute("categorias", categoriaAService.obtenerTodos());
        model.addAttribute(productoA);
        if (productoA.getId() != null && productoA.getId() > 0)
            return "/productos/edit";
        else
            return "/productos/create";
    }

    @PostMapping("/deletiquetas/{id}")
    public String delPhone(@PathVariable("id") Integer id, ProductoA productoA, Model model) {
        productoA.getEtiquetas().removeIf(elemento -> elemento.getId() == id);
        model.addAttribute("categorias", categoriaAService.obtenerTodos());
        model.addAttribute(productoA);
        if (productoA.getId() != null && productoA.getId() > 0)
            return "/productos/edit";
        else
            return "/productos/create";
    }

    @PostMapping("/save")
    public String save(ProductoA productoA, BindingResult result, Model model, RedirectAttributes attributes,
            @RequestParam Integer categoriaA_id) {
        if (result.hasErrors()) {
            model.addAttribute(productoA);
            attributes.addFlashAttribute("error", "No se pudo guardar debido a un error.");
            return "productos/create";
        }

        CategoriaA categoriaA = categoriaAService.buscarPorId(categoriaA_id).orElse(null);
        if (categoriaA == null) {
            attributes.addFlashAttribute("error", "La categoría seleccionada no es válida.");
            return productoA.getId() != null ? "redirect:/productos/edit/" + productoA.getId()
                    : "redirect:/productos/create";
        }

        productoA.setCategoriaA(categoriaA);
        if (productoA.getEtiquetas() != null) {
            for (EtiquetaA item : productoA.getEtiquetas()) {
                if (item.getId() != null && item.getId() < 0)
                    item.setId(null);
                item.setProductoA(productoA);
            }
        }

        if (productoA.getId() != null && productoA.getId() > 0) {
            // Modificar un registro existente
            ProductoA productoAUpdate = productoAService.buscarPorId(productoA.getId()).orElse(null);
            if (productoAUpdate == null) {
                attributes.addFlashAttribute("error", "No se encontró el producto a actualizar.");
                return "redirect:/productos/index";
            }

            // Actualizar campos
            productoAUpdate.setNombreA(productoA.getNombreA());
            productoAUpdate.setPrecioA(productoA.getPrecioA());
            productoAUpdate.setExistenciaA(productoA.getExistenciaA());
            productoAUpdate.setCategoriaA(categoriaA);

            // Actualizar etiquetas
            Map<Integer, EtiquetaA> etiquetasData = new HashMap<>();
            if (productoAUpdate.getEtiquetas() != null) {
                for (EtiquetaA item : productoAUpdate.getEtiquetas()) {
                    etiquetasData.put(item.getId(), item);
                }
            }

            if (productoA.getEtiquetas() != null) {
                for (EtiquetaA item : productoA.getEtiquetas()) {
                    if (item.getId() == null) {
                        if (productoAUpdate.getEtiquetas() == null)
                            productoAUpdate.setEtiquetas(new ArrayList<>());
                        item.setProductoA(productoAUpdate);
                        productoAUpdate.getEtiquetas().add(item);
                    } else {
                        if (etiquetasData.containsKey(item.getId())) {
                            EtiquetaA etiquetaUpdate = etiquetasData.get(item.getId());
                            etiquetaUpdate.setNombreA(item.getNombreA());
                            etiquetasData.remove(item.getId());
                        }
                    }
                }
            }

            if (!etiquetasData.isEmpty()) {
                productoAUpdate.getEtiquetas().removeIf(elemento -> etiquetasData.containsKey(elemento.getId()));
            }

            productoA = productoAUpdate;
        } 

        // Guardar el producto
        productoAService.crearOEditar(productoA);
        attributes.addFlashAttribute("msg", "Producto guardado correctamente");
        return "redirect:/productos/index";
    }

    @GetMapping("/details/{id}")
    public String details(@PathVariable("id") Integer id, Model model) {
        // Recuperar el producto incluyendo la categoría
        ProductoA productoA = productoAService.buscarPorId(id).orElse(null);

        // Verificar si el producto existe
        if (productoA != null) {
            model.addAttribute("productoA", productoA);
        } else {
            // Manejar el caso donde el producto no existe, por ejemplo, redirigir a una
            // página de error
            model.addAttribute("error", "Producto no encontrado");
            return "error"; // O la vista de error que prefieras
        }

        return "/productos/details";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Integer id, Model model) {
        ProductoA productoA = productoAService.buscarPorId(id).get();
        model.addAttribute("categorias", categoriaAService.obtenerTodos());
        model.addAttribute("productoA", productoA);
        return "/productos/edit";
    }

}
