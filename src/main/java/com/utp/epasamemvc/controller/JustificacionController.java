/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.controller;

import com.utp.epasamemvc.model.Asistencia;
import com.utp.epasamemvc.model.Justificacion;
import com.utp.epasamemvc.model.Permiso;
import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.service.AsistenciaService;
import com.utp.epasamemvc.service.EmailService;
import com.utp.epasamemvc.service.HoraServidorService;
import com.utp.epasamemvc.service.JustificacionService;
import com.utp.epasamemvc.service.TrabajadorService;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import static java.time.temporal.ChronoUnit.DAYS;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
 * Controlador que gestiona las solicitudes de justificaciones de los
 * trabajadores. Permite listar trabajadores activos, registrar justificaciones,
 * mostrar detalles de las justificaciones pendientes, y aceptar o rechazar
 * solicitudes de justificaciones.
 *
 * <p>
 * El controlador proporciona redirección y gestión de errores en caso de que no
 * se encuentren registros o existan problemas en el proceso de registro o
 * actualización de datos.</p>
 *
 * <p>
 * Incluye operaciones relacionadas con la sesión y manipulación de archivos de
 * evidencia en justificaciones.</p>
 *
 * @author Cristhian Aguilera
 */
@Controller
public class JustificacionController {

    /**
     * Servicio que maneja la lógica relacionada con las asistencias de los
     * trabajadores. Se utiliza para consultar, registrar y actualizar las
     * asistencias en el sistema.
     */
    @Autowired
    private AsistenciaService asistenciaService;

    /**
     * Servicio que maneja las operaciones relacionadas con las justificaciones
     * de asistencia. Se encarga de registrar, consultar y procesar las
     * solicitudes de justificación de asistencia de los trabajadores.
     */
    @Autowired
    private JustificacionService justificacionservice;

    /**
     * Servicio que maneja las operaciones relacionadas con los trabajadores.
     * Permite obtener, crear, actualizar y eliminar trabajadores en el sistema.
     */
    @Autowired
    private TrabajadorService trabajadorservice;

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
     * Controlador que maneja la visualización de los trabajadores activos para
     * la administración de justificaciones.
     *
     * @param model El modelo que se utiliza para pasar los datos a la vista.
     * @param session La sesión HTTP que almacena los datos del trabajador
     * actual.
     * @param page El número de página que se utiliza para la paginación de los
     * resultados de los trabajadores.
     * @return El nombre de la vista "JustificacionesAdmin", donde se muestran
     * los trabajadores activos.
     */
    @GetMapping("/JustificacionesAdmin")
    public String TrabajadoresActivos(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page) {
        Page<Trabajador> trabajadoresPpage = trabajadorservice.findByEstadoPage("Activo", PageRequest.of(page, 5));
        model.addAttribute("trabajadores", trabajadoresPpage);

        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");
        model.addAttribute("trabajador", trabajador);
        return "JustificacionesAdmin";
    }

    /**
     * Obtiene la justificación pendiente asociada a un trabajador específico
     * utilizando su ID.
     *
     * @param trabajadorId El ID del trabajador cuyo permiso se desea obtener.
     * @return Una respuesta HTTP con un mapa que contiene la información de la
     * justificación si se encuentra, o un mensaje de error si no se encuentra
     * una justificación pendiente.
     */
    @GetMapping("/JustificacionesAdmin/{trabajadorId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getJustificacionByTrabajadorId(@PathVariable Integer trabajadorId) {
        //con el id del trabajdor buscamos su permiso ligado a el 
        Justificacion justificacion = justificacionservice.findJustificacionByTrabajadorIdAndEstado(trabajadorId, "Pendiente");

        //verificamos que permiso no sea nulo y que tenga el estado de Pendiente
        if (justificacion != null) {
            //creamos un mapa con su clave y valor para facilitar la obtencion de los datos
            Map<String, Object> justificacionData = new HashMap<>();
            justificacionData.put("trabajadorNombre", justificacion.getTrabajador().getNombres() + " " + justificacion.getTrabajador().getApellidos());
            justificacionData.put("fecha", justificacion.getFecha().toString());
            justificacionData.put("asistencia", justificacion.getAsistencia().getFecha() + " - " + justificacion.getAsistencia().getEstado());
            justificacionData.put("descripcion", justificacion.getDescripcion());
            justificacionData.put("justificacionId", justificacion.getId());

            if (justificacion.getEvidencia() != null) {
                //si evidencia esta presente la convertimos en un cadena compatible con JSON
                String evidenciaBase64 = Base64.getEncoder().encodeToString(justificacion.getEvidencia());
                justificacionData.put("evidencia", evidenciaBase64);
            }
            //se devuelve la respuesta juston con la data del permiso
            return ResponseEntity.ok(justificacionData);
        } else {
            //retorna un error si no se enuentra el permiso
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No se encontró una justificacion para este trabajador."));
        }
    }

    /**
     * Maneja la acción de aceptar o rechazar una justificación de un
     * trabajador.
     *
     * @param justificacionId El ID de la justificación que se desea aceptar o
     * rechazar.
     * @param motivo El motivo por el cual se acepta o rechaza la justificación.
     * @param accion La acción a realizar, "Aceptar" o "Rechazar".
     * @param redirectAttributes Atributos de redirección para pasar mensajes de
     * éxito.
     * @return Redirige a la página de administración de justificaciones con el
     * estado de la acción.
     */
    @PostMapping("/AceptarRechazarJustificacion")
    public String AceptarRechazarJustificacion(
            @RequestParam("justificacionId") Integer justificacionId,
            @RequestParam("motivo") String motivo,//recojemos parametros
            @RequestParam("accion2") String accion, RedirectAttributes redirectAttributes) {
        try {
            // buscamos el permiso por su id
            Justificacion justificacion = justificacionservice.findJustificacionById(justificacionId);
            Trabajador trabajador = justificacion.getTrabajador();
            Asistencia asistencia = justificacion.getAsistencia();

            ZonedDateTime ahora = horaServidorService.obtenerHoraDelServidor();

            // Extrae la hora y fecha
            LocalTime horaActualServidor = ahora.toLocalTime();
            LocalDate fechaActualServidor = ahora.toLocalDate();

            //verifica si no es nulo
            if (justificacion != null) {

                // si lo encuentra actualiza el estado del permiso segun la accion 
                if ("Aceptar".equalsIgnoreCase(accion)) {

                    justificacion.setEstado("Aceptada");
                    justificacion.setFechaaprobada(fechaActualServidor);
                    justificacion.setMotivo(motivo);

                    String asunto = "Bienvenido al sistema de Epasame";
                    String contenido = String.format("Hola estimado, %s %s,\n\n"
                            + "Con respecto a la justificacion Solicitada el dia, %s\n\n"
                            + "De la Asistencia del día %s , cuyo estado es de %s\n\n"
                            + "Se le Informa que fue Aceptada su Solicitud\n\n"
                            + "Motivo: %s\n\n"
                            + "Saludos,\nEl equipo de gestión.",
                            trabajador.getNombres(), trabajador.getApellidos(),
                            justificacion.getFecha(), asistencia.getFecha(), asistencia.getEstado(), motivo);

                    emailService.enviarCorreo(trabajador.getCorreo(), asunto, contenido);

                    asistencia.setEstado("Justificada");
                    redirectAttributes.addAttribute("Aceptada", true);

                } else if ("Rechazar".equalsIgnoreCase(accion)) {

                    justificacion.setEstado("Rechazada");
                    justificacion.setFecharechazada(fechaActualServidor);
                    justificacion.setMotivo(motivo);

                    String asunto = "Bienvenido al sistema de Epasame";
                    String contenido = String.format("Hola estimado, %s %s,\n\n"
                            + "Con respecto a la justificacion Solicitada el dia, %s\n\n"
                            + "De la Asistencia del día %s , cuyo estado es de %s\n\n"
                            + "Se le Informa que fue Rechazada su Solicitud\n\n"
                            + "Motivo: %s\n\n"
                            + "Saludos,\nEl equipo de gestión.",
                            trabajador.getNombres(), trabajador.getApellidos(),
                            justificacion.getFecha(), asistencia.getFecha(), asistencia.getEstado(), motivo);

                    emailService.enviarCorreo(trabajador.getCorreo(), asunto, contenido);

                    redirectAttributes.addAttribute("Rechazada", true);
                }

                // Guardar el cambio de estado del permiso
                justificacionservice.savrJustificacion(justificacion);

                asistenciaService.saveAsistencia(asistencia);

                // Cambia el campo dtieneSolicitud del trabajador a false
                trabajador.setTienejustificacion(false);
                trabajadorservice.saveTrabajador(trabajador);
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        return "redirect:/JustificacionesAdmin";
    }

    /**
     * Método que maneja la solicitud GET para mostrar las justificaciones de
     * los trabajadores en la interfaz administrativa. Permite filtrar las
     * justificaciones por un rango de fechas y paginar los resultados.
     *
     * @param model El modelo de la vista que contiene los atributos para
     * mostrar en la página.
     * @param page Número de página para la paginación de los resultados. (Por
     * defecto es 0).
     * @param desde Fecha de inicio del rango de fechas para filtrar las
     * justificaciones (opcional).
     * @param hasta Fecha de fin del rango de fechas para filtrar las
     * justificaciones (opcional).
     * @param session La sesión HTTP que contiene el trabajador actual.
     *
     * @return Nombre de la vista que contiene las justificaciones de los
     * trabajadores.
     */
    @GetMapping("/VerJustificacionesAdmin")
    public String misJustificacionesAdmin(Model model, @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpSession session) {

        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

        Page<Justificacion> justificacionPage;

        if (desde != null && hasta != null) {
            // Obtener permisos filtrados por rango de fechas
            justificacionPage = justificacionservice.obtenerJustificacionPorFechas(desde, hasta, PageRequest.of(page, 5));
        } else {
            // Obtener todos los permisos
            justificacionPage = justificacionservice.getAllJustificacionesPage(PageRequest.of(page, 5));
        }

        List<Map<String, Object>> JustificacionesData = new ArrayList<>();

        for (Justificacion justificacion : justificacionPage.getContent()) {
            Map<String, Object> justificacionData = new HashMap<>();
            justificacionData.put("trabajador", justificacion.getTrabajador().getNombres() + " " + justificacion.getTrabajador().getApellidos());
            justificacionData.put("fecha", justificacion.getFecha());
            justificacionData.put("fechaasistencia", justificacion.getAsistencia().getFecha());
            justificacionData.put("estadoasistencia", justificacion.getAsistencia().getEstado());
            justificacionData.put("descripcion", justificacion.getDescripcion());
            justificacionData.put("estado", justificacion.getEstado());
            justificacionData.put("fechaaprobado", justificacion.getFechaaprobada());
            justificacionData.put("fecharechazado", justificacion.getFecharechazada());

            if (justificacion.getEvidencia() != null) {
                // Convertir la evidencia a Base64
                String evidenciaBase64 = Base64.getEncoder().encodeToString(justificacion.getEvidencia());
                justificacionData.put("evidencia", evidenciaBase64);
            } else {
                justificacionData.put("evidencia", null);
            }

            JustificacionesData.add(justificacionData);
        }

        model.addAttribute("justificaciones", JustificacionesData);
        model.addAttribute("trabajador", trabajador);
        model.addAttribute("justificacionesPage", justificacionPage);
        model.addAttribute("desde", desde); // Pasar fechas al modelo
        model.addAttribute("hasta", hasta);

        return "VerJustificacionesAdmin";
    }

    /**
     * Método que maneja la solicitud GET para mostrar la vista de
     * justificaciones del trabajador. Este método obtiene las asistencias del
     * trabajador con estado "Inasistencia" o "Tardanza" y las muestra en la
     * vista correspondiente.
     *
     * @param model El modelo de la vista que contiene los atributos para
     * mostrar en la página.
     * @param session La sesión HTTP que contiene el trabajador actual.
     *
     * @return Nombre de la vista que muestra las justificaciones del
     * trabajador.
     */
    @GetMapping("/JustificacionesTrabajador")
    public String MuestaVistaJustificacionesTrabajador(Model model, HttpSession session) {
        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");
        model.addAttribute("trabajador", trabajador);

        List<Asistencia> asistencias = asistenciaService.obtenerAsistenciasPorEstado(trabajador.getId(), Arrays.asList("Inasistencia", "Tardanza"));

        model.addAttribute("asistencias", asistencias);
        return "JustificacionesTrabajador";
    }

    /**
     * Método que maneja la solicitud POST para registrar una justificación de
     * un trabajador. Este método verifica si el trabajador ya tiene una
     * justificación pendiente, y en caso contrario, crea una nueva
     * justificación y la guarda en el sistema.
     *
     * @param correo El correo electrónico del trabajador que solicita la
     * justificación.
     * @param nDocumento El número de documento del trabajador.
     * @param asistenciaId El ID de la asistencia para la cual se solicita la
     * justificación.
     * @param descripcion La descripción proporcionada por el trabajador para la
     * justificación.
     * @param evidencia El archivo de evidencia asociado con la justificación,
     * si se proporciona.
     * @param redirectAttributes Atributos de redirección que permiten pasar
     * mensajes entre las solicitudes.
     * @param session La sesión HTTP que contiene al trabajador actual.
     *
     * @return La vista a la que se redirige tras el registro de la
     * justificación.
     */
    @PostMapping("/registerjustificacion")
    public String registrarJustificacion(
            @RequestParam("Correo") String correo,//Obtenemos los parametros a utilizar
            @RequestParam("NDocumento") String nDocumento,
            @RequestParam("asistenciaId") Integer asistenciaId,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("evidencia") MultipartFile evidencia,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        try {

            //Cargamos al trabajdor que se guardo en la sesion
            Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

            Asistencia asistencia = asistenciaService.findAsistenciasById(asistenciaId);

            ZonedDateTime ahora = horaServidorService.obtenerHoraDelServidor();

            // Extrae la hora y fecha
            LocalTime horaActualServidor = ahora.toLocalTime();
            LocalDate fechaActualServidor = ahora.toLocalDate();

            if (trabajador != null) {

                // Verifica si el trabajdor tiene un permiso en estado pendiente 
                //si es asi no deja registrar un nuevo permiso
                if (justificacionservice.tieneJustificacionPendiente(trabajador)) {
                    redirectAttributes.addAttribute("errorjustificacion", true);
                    return "redirect:/JustificacionesTrabajador";
                }

                // Crear un nuevo objeto Permiso
                Justificacion nuevajustificacion = new Justificacion();
                nuevajustificacion.setTrabajador(trabajador);
                nuevajustificacion.setAsistencia(asistencia);
                nuevajustificacion.setDescripcion(descripcion);
                nuevajustificacion.setEstado("Pendiente");
                nuevajustificacion.setFecha(fechaActualServidor);

                // Si se proporciona un archivo de evidencia se guarda
                if (!evidencia.isEmpty()) {
                    nuevajustificacion.setEvidencia(evidencia.getBytes());
                }

                // guardamos el permiso
                justificacionservice.savrJustificacion(nuevajustificacion);

                //Actualiza el campo tienesolicutud del trabajador
                trabajador.setTienejustificacion(true);
                trabajadorservice.saveTrabajador(trabajador);
            }
        } catch (IOException e) {
            //e.printStackTrace();

        }
        redirectAttributes.addAttribute("success", true);
        return "redirect:/JustificacionesTrabajador";
    }

    /**
     * Método que maneja la solicitud GET para mostrar las justificaciones de un
     * trabajador. Permite filtrar las justificaciones por un rango de fechas y
     * paginarlas. Si no se proporcionan fechas, se muestran todas las
     * justificaciones del trabajador.
     *
     * @param model El objeto que contiene los atributos que serán pasados a la
     * vista.
     * @param page El número de página para la paginación de resultados (valor
     * predeterminado es 0).
     * @param desde La fecha de inicio para filtrar las justificaciones
     * (opcional).
     * @param hasta La fecha de fin para filtrar las justificaciones (opcional).
     * @param session La sesión HTTP que contiene al trabajador actual.
     *
     * @return El nombre de la vista que muestra las justificaciones del
     * trabajador.
     */
    @GetMapping("/VerJustificacionesTrabajador")
    public String misJustificacionesTrabajador(Model model, @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
            HttpSession session) {

        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

        Page<Justificacion> justificacionesPage;

        if (desde != null && hasta != null) {
            // Obtener permisos filtrados por rango de fechas
            justificacionesPage = justificacionservice.obtenerJustificacionPorTrabajadorIdAndFechas(trabajador.getId(), desde, hasta, PageRequest.of(page, 5));
        } else {
            // Obtener todos los permisos
            justificacionesPage = justificacionservice.findPermisosByTrabajadorIdPage(trabajador.getId(), PageRequest.of(page, 5));
        }

        List<Map<String, Object>> JustificacionesData = new ArrayList<>();

        for (Justificacion justificacion : justificacionesPage.getContent()) {
            Map<String, Object> justificacionData = new HashMap<>();
            justificacionData.put("fecha", justificacion.getFecha());
            justificacionData.put("fechaasistencia", justificacion.getAsistencia().getFecha());
            justificacionData.put("estadoasistencia", justificacion.getAsistencia().getEstado());
            justificacionData.put("descripcion", justificacion.getDescripcion());
            justificacionData.put("estado", justificacion.getEstado());
            justificacionData.put("fechaaprobado", justificacion.getFechaaprobada());
            justificacionData.put("fecharechazado", justificacion.getFecharechazada());
            justificacionData.put("motivo", justificacion.getMotivo());

            if (justificacion.getEvidencia() != null) {
                // Convertir la evidencia a Base64
                String evidenciaBase64 = Base64.getEncoder().encodeToString(justificacion.getEvidencia());
                justificacionData.put("evidencia", evidenciaBase64);
            } else {
                justificacionData.put("evidencia", null);
            }

            JustificacionesData.add(justificacionData);
        }

        model.addAttribute("justificaciones", JustificacionesData);
        model.addAttribute("trabajador", trabajador);
        model.addAttribute("justificacionesPage", justificacionesPage);
        model.addAttribute("desde", desde); // Pasar fechas al modelo
        model.addAttribute("hasta", hasta);

        return "VerJustificacionesTrabajador";
    }

}
