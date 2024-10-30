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
 * Controlador que gestiona el inicio de sesión de los trabajadores. Permite la
 * autenticación mediante correo y contraseña, y redirige a vistas específicas
 * en función del rol del usuario autenticado.
 *
 * <p>
 * Incluye manejo de errores para credenciales incorrectas y almacenamiento de
 * la sesión para usuarios autenticados.</p>
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
    
    

    @Autowired
    private TrabajadorService trabajadorservice;

    /**
     * Carga la página de inicio de sesión cuando se inicia el proyecto.
     *
     * @return La vista "login".
     */
    @GetMapping("/")
    public String MuestraVistaLogin() {
        return "login";
    }

    /**
     * Procesa el inicio de sesión de un trabajador verificando el correo y la
     * contraseña. Si el trabajador existe y las credenciales son correctas, se
     * almacena en la sesión y se redirige a la vista correspondiente según el
     * rol del trabajador.
     *
     * @param correo El correo electrónico ingresado por el usuario.
     * @param contrasena La contraseña ingresada por el usuario.
     * @param session La sesión HTTP donde se almacena el trabajador
     * autenticado.
     * @return Redirección a la vista "PrincipalAdmin" si el usuario es
     * administrador, a "PrincipalTrabajador" si es trabajador, o de vuelta al
     * "login" con un mensaje de error si las credenciales no son válidas.
     */
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
