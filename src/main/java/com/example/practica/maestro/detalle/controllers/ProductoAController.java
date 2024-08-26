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
        // Recuperar la categoría usando el id proporcionado
        CategoriaA categoriaA = categoriaAService.buscarPorId(categoriaA_id).orElse(null);

        if (categoriaA != null) {
            productoA.setCategoriaA(categoriaA);
        } else {
            attributes.addFlashAttribute("error", "La categoría seleccionada no es válida.");
            return "productos/create";
        }
        if (productoA.getEtiquetas() != null) {
            for (EtiquetaA item : productoA.getEtiquetas()) {
                if (item.getId() != null && item.getId() < 0)
                    item.setId(null);
                item.setProductoA(productoA);
            }
        }
        if (productoA.getId() != null && productoA.getId() > 0) {
            // funcionalidad para cuando es modificar un registro
            ProductoA productoAUpdate = productoAService.buscarPorId(productoA.getId()).get();
            // almacenar en un dicionario los telefono que estan
            // guardados en la base de datos para mejor acceso a ellos
            Map<Integer, EtiquetaA> etiquetasData = new HashMap<>();
            if (productoAUpdate.getEtiquetas() != null) {
                for (EtiquetaA item : productoAUpdate.getEtiquetas()) {
                    etiquetasData.put(item.getId(), item);
                }
            }
            // actualizar los registro que viene de la vista hacia el que se encuentra por
            // id
            productoAUpdate.setNombreA(productoA.getNombreA());
            productoAUpdate.setPrecioA(productoA.getPrecioA());
            productoAUpdate.setExistenciaA(productoA.getExistenciaA());
            // recorrer los telefonos obtenidos desde la vista y actualizar
            // ProductoAiUpdate para que implemente los cambios
            if (productoA.getEtiquetas() != null) {
                for (EtiquetaA item : productoA.getEtiquetas()) {
                    if (item.getId() == null) {
                        if (productoAUpdate.getEtiquetas() == null)
                            productoAUpdate.setEtiquetas(new ArrayList<>());
                        item.setProductoA(productoAUpdate);
                        productoAUpdate.getEtiquetas().add(item);
                    } else {
                        if (etiquetasData.containsKey(item.getId())) {
                            EtiquetaA telefonoUpdate = etiquetasData.get(item.getId());
                            // actualizar las propiedades de EtiquetaA
                            // si ya existe en la base de datos
                            telefonoUpdate.setNombreA(item.getNombreA());
                            // remover del dicionario los telefonos datas para
                            // saber que cuales se van eliminar que serian
                            // todos aquellos que no se lograron remove porque no viene desde
                            // la vista
                            etiquetasData.remove(item.getId());
                        }
                    }
                }
            }
            if (etiquetasData.isEmpty() == false) {
                for (Map.Entry<Integer, EtiquetaA> item : etiquetasData.entrySet()) {
                    productoAUpdate.getEtiquetas().removeIf(elemento -> elemento.getId() == item.getKey());
                }

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

}
