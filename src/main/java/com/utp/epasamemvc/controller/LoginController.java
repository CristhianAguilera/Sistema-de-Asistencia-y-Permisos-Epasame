/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


/**
 *
 * @author Cristhian Aguilera
 */
@Controller
public class LoginController {

    /**
     * Crea una nueva instancia del controlador del login.
     */
    public LoginController() {
    }


    /**
     * Carga la página de inicio de sesión cuando se inicia el proyecto.
     *
     * @return La vista "login".
     */
    @GetMapping("/")
    public String MuestraVistaLogin() {
        return "login";
    }
    
}
