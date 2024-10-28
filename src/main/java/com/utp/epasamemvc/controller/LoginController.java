/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.controller;

import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.service.TrabajadorService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADVANCE
 */
@Controller
public class LoginController {

    @Autowired
    private TrabajadorService trabajadorservice;

    //Carga la pagina del login una vez se inicie el proyecto
    @GetMapping("/")
    public String iniciarSesion() {
        return "login";
    }

    //Ingresamos al sistema 
    @PostMapping("/login")
    public String procesarLogin(@RequestParam("correoelectronico") String correo,//recogemos parametros
            @RequestParam("contrasena") String contrasena,
            HttpSession session) {
        //verificamos si el trabajdor existe en la bd mediante el correo
        Trabajador trabajador = trabajadorservice.findByCorreo(correo);

        //si no existe retorna el login con un mensaje de error
        if (trabajador == null) {
            return "redirect:/?error=true";
        }  

        //si existe verifica que el correo y la contraseña sean igfuales 
        if (correo.equals(trabajador.getCorreo()) && contrasena.equals(trabajador.getContraseña())) {
            //guardamos al trabajador en la  sesion 
            session.setAttribute("trabajador", trabajador);

            //redirige a las distintas vistas segun su rol 
            if (trabajador.getRol().equals("Administrador")) {
                return "redirect:/PrincipalAdmin";
            }

            return "redirect:/PrincipalTrabajador";
        } else {
            return "redirect:/?error=true";
        }
    }


}
