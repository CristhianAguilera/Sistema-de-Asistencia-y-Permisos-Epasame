/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.controller;

import com.utp.epasamemvc.model.Permiso;
import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.service.PermisoService;
import com.utp.epasamemvc.service.TrabajadorService;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controlador que gestiona las solicitudes de permisos de los trabajadores.
 * Permite listar trabajadores activos, registrar permisos, mostrar detalles de
 * permisos pendientes, y aceptar o rechazar solicitudes de permisos.
 *
 * <p>
 * El controlador proporciona redirección y gestión de errores en caso de que no
 * se encuentren registros o existan problemas en el proceso de registro o
 * actualización de datos.</p>
 *
 * <p>
 * Incluye operaciones relacionadas con la sesión y manipulación de archivos de
 * evidencia en permisos.</p>
 *
 * @author Cristhian Aguilera
 */
@Controller
public class PermisosAController {

    /**
     * Crea una nueva instancia del controlador de permisos.
     */
    public PermisosAController() {
    }

    
    
    @Autowired
    private TrabajadorService trabajadorservice;

    @Autowired
    private PermisoService permisoservice;

    /**
     * Carga la lista de trabajadores activos para su visualización en la vista
     * de administración de permisos.
     *
     * @param model Modelo para almacenar y transportar datos a la vista.
     * @return La vista "PermisosAdmin" con la lista de trabajadores activos.
     */
    @GetMapping("/PermisosAdmin")
    public String TrabajadoresActivos(Model model) {
        List<Trabajador> trabajadoresList = trabajadorservice.findByEstado("Activo");
        model.addAttribute("trabajadores", trabajadoresList);
        return "PermisosAdmin";
    }

    /**
     * Carga la vista para que el trabajador pueda ver y solicitar permisos.
     *
     * @return La vista "PermisosTrabajador".
     */
    @GetMapping("/PermisosTrabajador")
    public String MuestaVistaPermisosTrabajador() {
        return "PermisosTrabajador";
    }

    /**
     * Registra una nueva solicitud de permiso para el trabajador autenticado.
     * Incluye verificación de permisos pendientes y almacenamiento de
     * evidencia.
     *
     * @param correo El correo del trabajador que solicita el permiso.
     * @param fechaInicio Fecha de inicio del permiso.
     * @param fechaFin Fecha de finalización del permiso.
     * @param nDocumento Número de documento del trabajador.
     * @param tipo Tipo de permiso solicitado.
     * @param descripcion Descripción de la razón del permiso.
     * @param evidencia Archivo de evidencia (si aplica).
     * @param session Sesión HTTP para obtener el trabajador autenticado.
     * @return Redirección a la vista "PermisosTrabajador" con parámetros de
     * éxito o error.
     */
    @PostMapping("/registerpermiso")
    public String registrarPermiso(
            @RequestParam("Correo") String correo,//Obtenemos los parametros a utilizar
            @RequestParam("fechainicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam("fechafin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin,
            @RequestParam("NDocumento") String nDocumento,
            @RequestParam("tipo") String tipo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("evidencia") MultipartFile evidencia,
            HttpSession session
    ) {
        try {

            //Cargamos al trabajdor que se guardo en la sesion
            Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

            if (trabajador != null) {

                // Verifica si el trabajdor tiene un permiso en estado pendiente 
                //si es asi no deja registrar un nuevo permiso
                if (permisoservice.tienePermisoPendiente(trabajador)) {
                    return "redirect:/PermisosTrabajador?errorpermiso=true";
                }

                // Crear un nuevo objeto Permiso
                Permiso nuevoPermiso = new Permiso();
                nuevoPermiso.setTrabajador(trabajador);
                nuevoPermiso.setFechainicio(fechaInicio);
                nuevoPermiso.setFechafin(fechaFin);
                nuevoPermiso.setTipo(tipo);
                nuevoPermiso.setDescripcion(descripcion);
                nuevoPermiso.setEstado("Pendiente");
                nuevoPermiso.setFechapeticion(LocalDate.now());

                // Si se proporciona un archivo de evidencia se guarda
                if (!evidencia.isEmpty()) {
                    nuevoPermiso.setEvidencia(evidencia.getBytes());
                }

                // guardamos el permiso
                permisoservice.savePermiso(nuevoPermiso);

                //Actualiza el campo tienesolicutud del trabajador
                trabajador.setTieneSolicitud(true);
                trabajadorservice.saveTrabajador(trabajador);
            }
        } catch (IOException e) {
            //e.printStackTrace();

        }

        return "redirect:/PermisosTrabajador?success=true";
    }

    /**
     * Muestra los detalles de un permiso pendiente de un trabajador específico.
     *
     * @param trabajadorId ID del trabajador cuyo permiso se desea ver.
     * @return Los datos del permiso en formato JSON si se encuentra y está
     * pendiente, o un error 404 si no se encuentra o no está pendiente.
     */
    @GetMapping("/permisosAdmin/{trabajadorId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPermisoByTrabajadorId(@PathVariable Integer trabajadorId) {
        //con el id del trabajdor buscamos su permiso ligado a el 
        Permiso permiso = permisoservice.findPermisosByTrabajadorId(trabajadorId);

        //verificamos que permiso no sea nulo y que tenga el estado de Pendiente
        if (permiso != null && permiso.getEstado().equalsIgnoreCase("Pendiente")) {
            //creamos un mapa con su clave y valor para facilitar la obtencion de los datos
            Map<String, Object> permisoData = new HashMap<>();
            permisoData.put("trabajadorNombre", permiso.getTrabajador().getNombres() + " " + permiso.getTrabajador().getApellidos());
            permisoData.put("fechaInicio", permiso.getFechainicio().toString());
            permisoData.put("fechaFin", permiso.getFechafin().toString());
            permisoData.put("motivo", permiso.getTipo());
            permisoData.put("descripcion", permiso.getDescripcion());
            permisoData.put("permisoId", permiso.getId());

            if (permiso.getEvidencia() != null) {
                //si evidencia esta presente la convertimos en un cadena compatible con JSON
                String evidenciaBase64 = Base64.getEncoder().encodeToString(permiso.getEvidencia());
                permisoData.put("evidencia", evidenciaBase64);
            }
            //se devuelve la respuesta juston con la data del permiso
            return ResponseEntity.ok(permisoData);
        } else {
            //retorna un error si no se enuentra el permiso
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No se encontró un permiso para este trabajador."));
        }
    }

    /**
     * Acepta o rechaza un permiso específico, actualizando el estado del
     * permiso y el trabajador.
     *
     * @param permisoId ID del permiso a actualizar.
     * @param accion Acción a realizar (Aceptar o Rechazar).
     * @return Redirección a la vista "PermisosAdmin".
     */
    @PostMapping("/AceptarRechazarPermiso")
    public String AceptarRechazarPermiso(
            @RequestParam("permisoId") Integer permisoId,//recojemos parametros
            @RequestParam("accion") String accion) {
        try {
            // buscamos el permiso por su id
            Permiso permiso = permisoservice.findPermisoById(permisoId);

            //verifica si no es nulo
            if (permiso != null) {
                // si lo encuentra actualiza el estado del permiso segun la accion 
                if ("Aceptar".equalsIgnoreCase(accion)) {
                    permiso.setEstado("Aceptado");
                    permiso.setFechaaprobado(LocalDate.now());
                } else if ("Rechazar".equalsIgnoreCase(accion)) {
                    permiso.setEstado("Rechazado");
                }

                // Guardar el cambio de estado del permiso
                permisoservice.savePermiso(permiso);

                // Cambia el campo dtieneSolicitud del trabajador a false
                Trabajador trabajador = permiso.getTrabajador();
                trabajador.setTieneSolicitud(false);
                trabajadorservice.saveTrabajador(trabajador);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return "redirect:/PermisosAdmin";
    }

}
