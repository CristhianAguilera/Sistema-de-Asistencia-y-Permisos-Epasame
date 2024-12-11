/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controlador que gestiona los erroes dentro del sistema
 *
 * @author Cristhian Aguilera
 */
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    /**
     * Maneja los errores HTTP y asigna los mensajes correspondientes.
     * 
     * @param request La solicitud HTTP que causó el error.
     * @param model El modelo donde se agregarán los detalles del error.
     * @return La vista de error con los detalles correspondientes al error.
     */
    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        String errorMessage = "Ha ocurrido un error";
        String errorDetails = "";

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            
            if (statusCode == HttpStatus.NOT_FOUND.value()) {

                errorMessage = "Página no encontrada";
                errorDetails = "La página que busca no existe";

            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {

                errorMessage = "Acceso denegado";
                errorDetails = "No tiene permisos para acceder a esta página";
                
            } else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {

                errorMessage = "Error interno";
                errorDetails = "Ha ocurrido un error interno en el servidor";
            }
        }

        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorDetails", errorDetails);

        return "error";
    }

}
