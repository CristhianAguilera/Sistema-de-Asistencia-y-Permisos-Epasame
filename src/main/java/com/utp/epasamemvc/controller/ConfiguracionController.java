/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.controller;

import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.service.EmailService;
import com.utp.epasamemvc.service.TrabajadorService;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para gestionar la configuración del administrador en el sistema.
 * Proporciona endpoints para visualizar y editar datos de perfil.
 *
 * @author Cristhian Aguilera
 */
@Controller
public class ConfiguracionController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TrabajadorService trabajadorservice;

    @Autowired
    private EmailService emailService;

    /**
     * Muestra la vista de configuración para el administrador.
     *
     * @param model Modelo para pasar datos a la vista.
     * @param session Sesión HTTP para obtener información del usuario actual.
     * @return Nombre de la vista "ConfiguracionAdmin".
     */
    @GetMapping("/ConfiguracionAdmin")
    public String MuestraVistaConfiguracionAdmin(Model model, HttpSession session) {
        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");
        model.addAttribute("trabajador", trabajador);
        return "ConfiguracionAdmin";
    }

    /**
     * Valida la contraseña actual del usuario.
     *
     * @param passwordIngresada Contraseña ingresada por el usuario.
     * @param session Sesión HTTP para obtener el usuario actual.
     * @return Mapa con la respuesta de validación.
     */
    @PostMapping("/validarContraseña")
    @ResponseBody
    public Map<String, Object> validarContraseña(@RequestParam("passwordActual") String passwordIngresada,
            HttpSession session) {
        Map<String, Object> response = new HashMap<>();

        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

        if (trabajador != null) {
            if (passwordEncoder.matches(passwordIngresada, trabajador.getContraseña())) {
                response.put("success", true);
            } else {
                response.put("error", "Contraseña Incorrecta");
                return response;
            }
        } else {
            response.put("error", "No se encontró Trabajador en la sesión");
        }
        return response;
    }

    /**
     * Edita los datos del perfil del administrador.
     *
     * @param nombres Nombres del administrador.
     * @param apellidos Apellidos del administrador.
     * @param correo Correo electrónico del administrador.
     * @param telefono Teléfono del administrador.
     * @param numeroDocumento Número de documento del administrador.
     * @param FechaEntrada Fecha de entrada del administrador en formato
     * "yyyy-MM-dd".
     * @param contrasena Nueva contraseña del administrador.
     * @param tipoDocumento Tipo de documento del administrador.
     * @param redirectAttributes Atributos para redirección con mensajes.
     * @param session Sesión HTTP para obtener el usuario actual.
     * @return Redirección a la vista de configuración del administrador.
     */
    @PostMapping("/EditarAdmin")
    public String EditarAdmin(@RequestParam("Nombres") String nombres,
            @RequestParam("Apellidos") String apellidos,
            @RequestParam("Correo") String correo,
            @RequestParam("Telefono") String telefono,
            @RequestParam("NDocumento") String numeroDocumento,
            @RequestParam("FechaEntrada") String FechaEntrada,
            @RequestParam("contrasena") String contrasena,
            @RequestParam("tipoDocumento") String tipoDocumento,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        try {
            Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

            // Verifica que el correo sea válido.
            if (!emailService.verificarCorreo(correo)) {
                redirectAttributes.addAttribute("error2", true);
                return "redirect:/ConfiguracionAdmin";
            }

            // Verifica que los datos no estén duplicados en la base de datos.
            Trabajador trabajadorPorCorreo = trabajadorservice.findByCorreo(correo);
            if (trabajadorPorCorreo != null && !trabajadorPorCorreo.getId().equals(trabajador.getId())) {
                redirectAttributes.addAttribute("error1", true);
                return "redirect:/ConfiguracionAdmin";
            }

            List<Trabajador> trabajadoresPorDocumento = trabajadorservice.findByNdocumento(numeroDocumento);
            boolean documentoDuplicado = trabajadoresPorDocumento.stream()
                    .anyMatch(t -> !t.getId().equals(trabajador.getId()));
            if (documentoDuplicado) {
                redirectAttributes.addAttribute("error1", true);
                return "redirect:/ConfiguracionAdmin";
            }

            List<Trabajador> trabajadoresPorTelefono = trabajadorservice.findByTelefono(telefono);
            boolean telefonoDuplicado = trabajadoresPorTelefono.stream()
                    .anyMatch(t -> !t.getId().equals(trabajador.getId()));
            if (telefonoDuplicado) {
                redirectAttributes.addAttribute("error1", true);
                return "redirect:/ConfiguracionAdmin";
            }

            List<Trabajador> trabajadoresPorContrasena = trabajadorservice.findByContraseña(contrasena);
            boolean contrasenaDuplicada = trabajadoresPorContrasena.stream()
                    .anyMatch(t -> !t.getId().equals(trabajador.getId()));
            if (contrasenaDuplicada) {
                redirectAttributes.addAttribute("error1", true);
                return "redirect:/ConfiguracionAdmin";
            }

            // Asigna los valores actualizados al trabajador.
            trabajador.setNombres(nombres);
            trabajador.setApellidos(apellidos);
            trabajador.setCorreo(correo);
            trabajador.setTelefono(telefono);
            trabajador.setNdocumento(numeroDocumento);

            // Convierte la fecha de entrada.
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate fechaEntradaLocalDate = LocalDate.parse(FechaEntrada, formatter);
            Date fechaEntradaSql = Date.valueOf(fechaEntradaLocalDate);
            trabajador.setFechaentrada(fechaEntradaSql);

            // Encripta la contraseña y actualiza el tipo de documento.
            String contrasenaEncriptada = passwordEncoder.encode(contrasena);
            trabajador.setContraseña(contrasenaEncriptada);
            trabajador.setTipodocumento(tipoDocumento);

            // Guarda los cambios en la base de datos.
            trabajadorservice.saveTrabajador(trabajador);

            // Envía un correo de confirmación.
            String asunto = "Actualización de datos en el sistema";
            String contenido = String.format("Hola %s %s,\n\n"
                    + "Sus datos se han actualizado correctamente.\n\n"
                    + "Correo: %s\n"
                    + "Contraseña: %s\n\n"
                    + "Por favor, no comparta esta información.\n\n"
                    + "Saludos,\nEl equipo de gestión.",
                    nombres, apellidos, correo, contrasena);

            emailService.enviarCorreo(correo, asunto, contenido);

            redirectAttributes.addAttribute("success", true);
            return "redirect:/ConfiguracionAdmin";

        } catch (Exception e) {
            redirectAttributes.addAttribute("error", true);
            return "redirect:/ConfiguracionAdmin";
        }
    }
}
