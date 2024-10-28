/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.controller;

import com.utp.epasamemvc.model.Asistencia;
import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.service.AsistenciaService;
import jakarta.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author ADVANCE
 */
@Controller
public class AsistenciaController {

    @Autowired
    private AsistenciaService asistenciaService;

    //Permite mostrar la vista de PrincipalTrabajdor
    @GetMapping("/PrincipalTrabajador")
    public String iniciarSesion() {
        return "PrincipalTrabajador";
    }

    //Carga todas las asistencias en la vista AsistenciasAdmin
    @GetMapping("/AsistenciasAdmin")
    public String iniciarSesion1(Model model) {
        List<Asistencia> asistencias = asistenciaService.obtenerTodasLasAsistencias();
        model.addAttribute("asistencias", asistencias);
        return "AsistenciasAdmin";
    }

    //Regsitra una nueva asistencia
    @PostMapping("/registrarAsistencia")
    public String registrarAsistencia(
            @RequestParam("tipo") String tipo, // Detecta si el valor es Ingreso o Salida
            HttpSession session) {
        //Cargamos al trabaajdor que se guardo en la sesion
        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");
        //obtenemos la asistencia del dia con el trabajador
        Asistencia asistencia = asistenciaService.obtenerAsistenciaDelDia(trabajador.getId());
        
        

        //verifica el tipo 
        if (tipo.equals("Ingreso")) {

            //si asistencia y hora de ingreso no estan vacios manda error
            if (asistencia != null && asistencia.getHoraingreso() != null) {
                return "redirect:/PrincipalTrabajador?erroringreso=true";
            }
            // si no hay asistencia registrada para el dia actual, crea una nueva entrada
            if (asistencia == null) {
                asistencia = new Asistencia();
                asistencia.setFecha(LocalDate.now());
                asistencia.setHoraingreso(LocalTime.now());
                asistencia.setEstado("Presente");
                asistencia.setTrabajador(trabajador);
            }
        } else if (tipo.equals("Salida")) {

            //si asistencia y hora de salida no estan vacios manda error
            if (asistencia != null && asistencia.getHorasalida() != null) {
                return "redirect:/PrincipalTrabajador?errorsalida=true";
            }
            // si es salida actualiza la hora de salida en la asistencia del dia actual
            if (asistencia != null) {
                asistencia.setHorasalida(LocalTime.now());
                asistencia.setEstado("Salida");
            }
        }

        //guarda la asisitencia y recarga la pagina
        asistenciaService.saveAsistencia(asistencia);
        return "redirect:/PrincipalTrabajador";
    }
}
