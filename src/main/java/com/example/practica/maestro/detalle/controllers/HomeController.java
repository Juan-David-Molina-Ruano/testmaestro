package com.example.practica.maestro.detalle.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

    @RequestMapping("/")
    public String index() {
        return "/home/index";
    }
}
