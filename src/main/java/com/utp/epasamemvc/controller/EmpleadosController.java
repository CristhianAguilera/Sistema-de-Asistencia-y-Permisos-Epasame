/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.controller;

import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.service.TrabajadorService;
import jakarta.persistence.EntityNotFoundException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author ADVANCE
 */
@Controller
public class EmpleadosController {

    @Autowired
    private TrabajadorService trabajadorservice;
    

    //Cargamos a todos los empleados en la vista EmpleadosAdmin
    @GetMapping("/EmpleadosAdmin")
    public String home(Model model) {
        List<Trabajador> trabajadoresList = trabajadorservice.getAllTrabajadores();
        model.addAttribute("trabajadores", trabajadoresList);
        return "EmpleadosAdmin";
    }

    //Permitimos el acceso ala vista PrincipalAdmin
    @GetMapping("/PrincipalAdmin")
    public String iniciarSesion() {
        return "PrincipalAdmin";
    }

    //Registramos un nuevo trabajador
    @PostMapping("/registertrabajador")
    public String registratrabajador(
            @RequestParam("Nombres") String nombres,//Recibimos los parametros a registrar
            @RequestParam("Apellidos") String apellidos,
            @RequestParam("Correo") String correo,
            @RequestParam("Telefono") String telefono,
            @RequestParam("NDocumento") String numeroDocumento,
            @RequestParam("FechaEntrada") String FechaEntrada,
            @RequestParam("contrasena") String contrasena,
            @RequestParam("cargo") String cargo,
            @RequestParam("tipoDocumento") String tipoDocumento,
            @RequestParam("rol") String rol) {

        try {
            //Verificamos si los datos existen ya en la bd
            if (trabajadorservice.findByCorreo(correo) != null) {
                return "redirect:/PrincipalAdmin?error1=true";
            }

            if (!trabajadorservice.findByNdocumento(numeroDocumento).isEmpty()) {
                return "redirect:/PrincipalAdmin?error1=true";
            }

            if (!trabajadorservice.findByTelefono(telefono).isEmpty()) {
                return "redirect:/PrincipalAdmin?error1=true";
            }

            if (!trabajadorservice.findByContraseña(contrasena).isEmpty()) {
                return "redirect:/PrincipalAdmin?error1=true";
            }

            // Crea un nuevo objeto Trabajador
            //y le asignamos los valores de los parametros recogidos
            Trabajador trabajador = new Trabajador();
            trabajador.setNombres(nombres);
            trabajador.setApellidos(apellidos);
            trabajador.setCorreo(correo);
            trabajador.setTelefono(telefono);
            trabajador.setNdocumento(numeroDocumento);

            // Conversion de la fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
            LocalDate fechaEntradaLocalDate = LocalDate.parse(FechaEntrada, formatter);
            Date fechaEntradaSql = Date.valueOf(fechaEntradaLocalDate);
            trabajador.setFechaentrada(fechaEntradaSql);

            trabajador.setContraseña(contrasena);
            trabajador.setCargo(cargo);
            trabajador.setTipodocumento(tipoDocumento);
            trabajador.setRol(rol);
            trabajador.setEstado("Activo");
            trabajador.setDiaspermiso(15);
            trabajador.setEstadodiario("Presente");
            trabajador.setTieneSolicitud(false);

            // guardamos el trabajador 
            trabajadorservice.saveTrabajador(trabajador);

            return "redirect:/PrincipalAdmin?success=true";

        } catch (Exception e) {

            return "redirect:/PrincipalAdmin?error=true";

        }

    }

    @GetMapping("/getTrabajador/{id}")
    @ResponseBody
    public ResponseEntity<Trabajador> getTrabajador(@PathVariable Long id) {
        Optional<Trabajador> trabajador = trabajadorservice.findTrabajadorById(id);
        return trabajador.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/trabajadores/actualizar")
    @ResponseBody
    public ResponseEntity<String> updateTrabajador(@RequestBody Trabajador trabajador) {
        try {
            
            if (trabajador.getId() == null) {
                return ResponseEntity.badRequest().body("El ID del trabajador es requerido.");
            }

            trabajadorservice.updateTrabajador(trabajador.getId(), trabajador);
            return ResponseEntity.ok("Trabajador actualizado exitosamente.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Trabajador no encontrado.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el trabajador: " + e.getMessage());
        }
    }
  
}
