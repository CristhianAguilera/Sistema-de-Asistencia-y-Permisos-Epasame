/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.controller;

import com.utp.epasamemvc.model.Permiso;
import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.service.EmailService;
import com.utp.epasamemvc.service.HoraServidorService;
import com.utp.epasamemvc.service.PermisoService;
import com.utp.epasamemvc.service.TrabajadorService;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    /**
     * Servicio que maneja las operaciones relacionadas con los trabajadores.
     * Permite obtener, crear, actualizar y eliminar trabajadores en el sistema.
     */
    @Autowired
    private TrabajadorService trabajadorservice;

    /**
     * Servicio que maneja las operaciones relacionadas con los permisos. Se
     * encarga de registrar, consultar y procesar las solicitudes de pemrisos de
     * los trabajadores.
     */
    @Autowired
    private PermisoService permisoservice;

    /**
     * Servicio encargado del envío de correos electrónicos. Se utiliza para
     * enviar notificaciones y correos relacionados con las justificaciones y
     * asistencias.
     */
    @Autowired
    private EmailService emailService;

    /**
     * Servicio que proporciona la hora actual del servidor. Se utiliza para
     * obtener la hora exacta del servidor en formato de zona horaria.
     */
    @Autowired
    private HoraServidorService horaServidorService;

    /**
     * Método que maneja la solicitud GET para mostrar la lista de trabajadores
     * activos. El resultado se presenta en una página con paginación, con un
     * límite de 5 trabajadores por página.
     *
     * @param model El objeto que contiene los atributos que serán pasados a la
     * vista.
     * @param session La sesión HTTP que contiene al trabajador actual.
     * @param page El número de página para la paginación de resultados (valor
     * predeterminado es 0).
     *
     * @return El nombre de la vista que muestra la lista de trabajadores
     * activos.
     */
    @GetMapping("/PermisosAdmin")
    public String TrabajadoresActivos(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page) {
        Page<Trabajador> trabajadoresPpage = trabajadorservice.findByEstadoPage("Activo", PageRequest.of(page, 5));
        model.addAttribute("trabajadores", trabajadoresPpage);

        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");
        model.addAttribute("trabajador", trabajador);
        return "PermisosAdmin";
    }

    /**
     * Método que maneja la solicitud GET para mostrar la vista de permisos del
     * trabajador. Obtiene los datos del trabajador de la sesión y los pasa al
     * modelo para ser utilizados en la vista.
     *
     * @param model El objeto que contiene los atributos que serán pasados a la
     * vista.
     * @param session La sesión HTTP que contiene al trabajador actual.
     *
     * @return El nombre de la vista que muestra la información de permisos del
     * trabajador.
     */
    @GetMapping("/PermisosTrabajador")
    public String MuestaVistaPermisosTrabajador(Model model, HttpSession session) {
        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");
        model.addAttribute("trabajador", trabajador);
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
     * @param redirectAttributes
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
            RedirectAttributes redirectAttributes,
            HttpSession session
    ) {
        try {

            ZonedDateTime ahora = horaServidorService.obtenerHoraDelServidor();

            // Extrae la hora y fecha
            LocalTime horaActualServidor = ahora.toLocalTime();
            LocalDate fechaActualServidor = ahora.toLocalDate();

            //Cargamos al trabajdor que se guardo en la sesion
            Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

            if (trabajador != null) {

                // Verifica si el trabajdor tiene un permiso en estado pendiente 
                //si es asi no deja registrar un nuevo permiso
                if (permisoservice.tienePermisoPendiente(trabajador)) {
                    redirectAttributes.addAttribute("errorpermiso", true);
                    return "redirect:/PermisosTrabajador";
                }

                // Crear un nuevo objeto Permiso
                Permiso nuevoPermiso = new Permiso();
                nuevoPermiso.setTrabajador(trabajador);
                nuevoPermiso.setFechainicio(fechaInicio);
                nuevoPermiso.setFechafin(fechaFin);
                nuevoPermiso.setTipo(tipo);
                nuevoPermiso.setDescripcion(descripcion);
                nuevoPermiso.setEstado("Pendiente");
                nuevoPermiso.setFechapeticion(fechaActualServidor);

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
        redirectAttributes.addAttribute("success", true);
        return "redirect:/PermisosTrabajador";
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
        Permiso permiso = permisoservice.findPermisosByTrabajadorIdAndEstado(trabajadorId, "Pendiente");

        //verificamos que permiso no sea nulo y que tenga el estado de Pendiente
        if (permiso != null) {
            //creamos un mapa con su clave y valor para facilitar la obtencion de los datos
            Map<String, Object> permisoData = new HashMap<>();
            permisoData.put("trabajadorNombre", permiso.getTrabajador().getNombres() + " " + permiso.getTrabajador().getApellidos());
            permisoData.put("fechaInicio", permiso.getFechainicio().toString());
            permisoData.put("fechaFin", permiso.getFechafin().toString());
            permisoData.put("motivo1", permiso.getTipo());
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
     * Método que maneja la aceptación o rechazo de un permiso por parte de un
     * administrador. Dependiendo de la acción (Aceptar o Rechazar), se
     * actualiza el estado del permiso, se envía un correo al trabajador con el
     * resultado de su solicitud y se actualizan los días de permiso disponibles
     * del trabajador si es aceptado.
     *
     * @param permisoId El ID del permiso que se está gestionando.
     * @param accion La acción que se tomará sobre el permiso (Aceptar o
     * Rechazar).
     * @param motivo El motivo proporcionado para aceptar o rechazar el permiso.
     * @param fechaInicio La fecha de inicio del permiso solicitado.
     * @param fechaFin La fecha de fin del permiso solicitado.
     * @param redirectAttributes Los atributos que se pasan para mostrar
     * mensajes al usuario después de procesar la acción.
     *
     * @return La redirección a la vista de administración de permisos con el
     * estado actualizado del permiso.
     */
    @PostMapping("/AceptarRechazarPermiso")
    public String AceptarRechazarPermiso(
            @RequestParam("permisoId") Integer permisoId,//recojemos parametros
            @RequestParam("accion") String accion, RedirectAttributes redirectAttributes,
            @RequestParam("motivo") String motivo,
            @RequestParam("fechaInicio") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin) {
        try {
            // buscamos el permiso por su id
            Permiso permiso = permisoservice.findPermisoById(permisoId);
            Trabajador trabajador = permiso.getTrabajador();

            ZonedDateTime ahora = horaServidorService.obtenerHoraDelServidor();

            // Extrae la hora y fecha
            LocalTime horaActualServidor = ahora.toLocalTime();
            LocalDate fechaActualServidor = ahora.toLocalDate();

            //verifica si no es nulo
            if (permiso != null) {

                // Calcular días entre fechaInicio y fechaFin
                long diasEntre = DAYS.between(fechaInicio, fechaFin);
                int diasPermisoDisponibles = trabajador.getDiaspermiso();

                // si lo encuentra actualiza el estado del permiso segun la accion 
                if ("Aceptar".equalsIgnoreCase(accion)) {

                    if (diasPermisoDisponibles >= diasEntre) {
                        permiso.setEstado("Aceptado");
                        permiso.setFechaaprobado(fechaActualServidor);
                        permiso.setMotivo(motivo);

                        // Actualizar días de permiso del trabajador
                        trabajador.setDiaspermiso(diasPermisoDisponibles - (int) diasEntre);
                        redirectAttributes.addAttribute("Aceptado", true);

                        String asunto = "Bienvenido al sistema de Epasame";
                        String contenido = String.format("Hola estimado, %s %s,\n\n"
                                + "Con respecto al Permiso Solicitado el dia, %s\n\n"
                                + "Se le Informa que fue Aceptada su Solicitud\n\n"
                                + "Motivo: %s\n\n"
                                + "Saludos,\nEl equipo de gestión.",
                                trabajador.getNombres(), trabajador.getApellidos(),
                                permiso.getFechapeticion(), motivo);

                        emailService.enviarCorreo(trabajador.getCorreo(), asunto, contenido);

                    } else {
                        redirectAttributes.addAttribute("sinDias", true);
                        return "redirect:/PermisosAdmin";
                    }

                } else if ("Rechazar".equalsIgnoreCase(accion)) {
                    permiso.setEstado("Rechazado");
                    permiso.setFecharechazado(fechaActualServidor);
                    permiso.setMotivo(motivo);

                    redirectAttributes.addAttribute("Rechazado", true);

                    String asunto = "Bienvenido al sistema de Epasame";
                    String contenido = String.format("Hola estimado, %s %s,\n\n"
                            + "Con respecto al Permiso Solicitado el dia, %s\n\n"
                            + "Se le Informa que fue Rechazada su Solicitud\n\n"
                            + "Motivo: %s\n\n"
                            + "Saludos,\nEl equipo de gestión.",
                            trabajador.getNombres(), trabajador.getApellidos(),
                            permiso.getFechapeticion(), motivo);

                    emailService.enviarCorreo(trabajador.getCorreo(), asunto, contenido);

                }

                // Guardar el cambio de estado del permiso
                permisoservice.savePermiso(permiso);

                // Cambia el campo dtieneSolicitud del trabajador a false
                trabajador.setTieneSolicitud(false);
                trabajadorservice.saveTrabajador(trabajador);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return "redirect:/PermisosAdmin";
    }

    /**
     * Método que maneja la visualización de los permisos solicitados por el
     * trabajador. Permite filtrar los permisos por un rango de fechas
     * (desde-hasta) y paginar los resultados.
     *
     * @param model El modelo de la vista, utilizado para pasar los datos a la
     * plantilla.
     * @param page El número de página para la paginación de los permisos. El
     * valor por defecto es 0.
     * @param desde La fecha de inicio para filtrar los permisos (opcional).
     * @param hasta La fecha de fin para filtrar los permisos (opcional).
     * @param session La sesión HTTP, que contiene los datos del trabajador
     * logueado.
     *
     * @return El nombre de la vista ("VerPermisosTrabajador") con los permisos
     * filtrados y paginados.
     */
    @GetMapping("/VerPermisosTrabajador")
    public String misPermisosTrabajador(Model model, @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpSession session) {

        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

        Page<Permiso> permisosPage;

        if (desde != null && hasta != null) {
            // Obtener permisos filtrados por rango de fechas
            permisosPage = permisoservice.obtenerPermisosPorTrabajadorIdAndFechas(trabajador.getId(), desde, hasta, PageRequest.of(page, 5));
        } else {
            // Obtener todos los permisos
            permisosPage = permisoservice.findPermisosByTrabajadorIdPage(trabajador.getId(), PageRequest.of(page, 5));
        }

        List<Map<String, Object>> permisosData = new ArrayList<>();

        for (Permiso permiso : permisosPage.getContent()) {
            Map<String, Object> permisoData = new HashMap<>();
            permisoData.put("fechapeticion", permiso.getFechapeticion());
            permisoData.put("tipo", permiso.getTipo());
            permisoData.put("descripcion", permiso.getDescripcion());
            permisoData.put("fechainicio", permiso.getFechainicio());
            permisoData.put("fechafin", permiso.getFechafin());
            permisoData.put("estado", permiso.getEstado());
            permisoData.put("fechaaprobado", permiso.getFechaaprobado());
            permisoData.put("fecharechazado", permiso.getFecharechazado());
            permisoData.put("motivo", permiso.getMotivo());

            if (permiso.getEvidencia() != null) {
                // Convertir la evidencia a Base64
                String evidenciaBase64 = Base64.getEncoder().encodeToString(permiso.getEvidencia());
                permisoData.put("evidencia", evidenciaBase64);
            } else {
                permisoData.put("evidencia", null);
            }

            permisosData.add(permisoData);
        }

        model.addAttribute("permisos", permisosData);
        model.addAttribute("trabajador", trabajador);
        model.addAttribute("permisosPage", permisosPage);
        model.addAttribute("desde", desde); // Pasar fechas al modelo
        model.addAttribute("hasta", hasta);

        return "VerPermisosTrabajador";
    }

    /**
     * Método que maneja la visualización de los permisos solicitados por todos
     * los trabajadores desde el punto de vista del administrador. Permite
     * filtrar los permisos por un rango de fechas (desde-hasta) y paginar los
     * resultados.
     *
     * @param model El modelo de la vista, utilizado para pasar los datos a la
     * plantilla.
     * @param page El número de página para la paginación de los permisos. El
     * valor por defecto es 0.
     * @param desde La fecha de inicio para filtrar los permisos (opcional).
     * @param hasta La fecha de fin para filtrar los permisos (opcional).
     * @param session La sesión HTTP, que contiene los datos del trabajador
     * logueado (usado para obtener datos del administrador).
     *
     * @return El nombre de la vista ("VerPermisosAdmin") con los permisos
     * filtrados y paginados.
     */
    @GetMapping("/VerPermisosAdmin")
    public String PermisosAdministrador(Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpSession session) {

        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

        Page<Permiso> permisosPage;

        if (desde != null && hasta != null) {
            // Obtener permisos filtrados por rango de fechas
            permisosPage = permisoservice.obtenerPermisosPorFechas(desde, hasta, PageRequest.of(page, 5));
        } else {
            // Obtener todos los permisos
            permisosPage = permisoservice.getAllPermisosPage(PageRequest.of(page, 5));
        }

        List<Map<String, Object>> permisosData = new ArrayList<>();

        for (Permiso permiso : permisosPage.getContent()) {
            Map<String, Object> permisoData = new HashMap<>();
            permisoData.put("trabajador", permiso.getTrabajador().getNombres() + " " + permiso.getTrabajador().getApellidos());
            permisoData.put("fechapeticion", permiso.getFechapeticion());
            permisoData.put("tipo", permiso.getTipo());
            permisoData.put("descripcion", permiso.getDescripcion());
            permisoData.put("fechainicio", permiso.getFechainicio());
            permisoData.put("fechafin", permiso.getFechafin());
            permisoData.put("estado", permiso.getEstado());
            permisoData.put("fechaaprobado", permiso.getFechaaprobado());
            permisoData.put("fecharechazado", permiso.getFecharechazado());

            if (permiso.getEvidencia() != null) {
                // Convertir la evidencia a Base64
                String evidenciaBase64 = Base64.getEncoder().encodeToString(permiso.getEvidencia());
                permisoData.put("evidencia", evidenciaBase64);
            } else {
                permisoData.put("evidencia", null);
            }

            permisosData.add(permisoData);
        }

        model.addAttribute("permisos", permisosData);
        model.addAttribute("trabajador", trabajador);
        model.addAttribute("permisosPage", permisosPage);
        model.addAttribute("desde", desde); // Pasar fechas al modelo
        model.addAttribute("hasta", hasta);

        return "VerPermisosAdmin";
    }

}
