/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.controller;

import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.properties.UnitValue;
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
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.utp.epasamemvc.config.ConfiguracionUbicacion;
import com.utp.epasamemvc.service.HoraServidorService;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador para gestionar la asistencia de los trabajadores en la
 * aplicación. Proporciona rutas para registrar asistencias y visualizar el
 * historial de asistencias.
 *
 * <p>
 * Incluye métodos para mostrar la página principal de trabajadores, cargar las
 * asistencias en la vista de administración, y registrar el ingreso o salida de
 * los trabajadores.</p>
 *
 * @author Cristhian Aguilera
 */
@Controller
public class AsistenciaController {

    /**
     * Crea una nueva instancia del controlador de asistencias.
     */
    public AsistenciaController() {
    }

    /**
     * Servicio para manejar la lógica de negocio relacionada con las
     * asistencias.
     */
    @Autowired
    private AsistenciaService asistenciaService;

    /**
     * Servicio para obtener la hora actual del servidor.
     */
    @Autowired
    private HoraServidorService horaServidorService;

    /**
     * Muestra la vista principal para el trabajador, cargando su información
     * desde la sesión.
     *
     * @param model El modelo utilizado para pasar datos a la vista.
     * @param session La sesión HTTP donde se almacena el trabajador
     * autenticado.
     * @return La vista "PrincipalTrabajador".
     */
    @GetMapping("/PrincipalTrabajador")
    public String MuestraVistaPrincipalTrabajador(Model model, HttpSession session) {
        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");
        model.addAttribute("trabajador", trabajador);
        return "PrincipalTrabajador";
    }

    /**
     * Carga todas las asistencias en la vista de administración para el
     * administrador. Genera enlaces de Google Maps para las ubicaciones de
     * ingreso y salida.
     *
     * @param model El modelo utilizado para pasar datos a la vista.
     * @param session La sesión HTTP donde se almacena el trabajador
     * autenticado.
     * @param page Número de página para la paginación de asistencias.
     * @param desde Fecha inicial para filtrar asistencias.
     * @param hasta Fecha final para filtrar asistencias.
     * @return La vista "AsistenciasAdmin" con la lista de asistencias.
     */
    @GetMapping("/AsistenciasAdmin")
    public String CargaAsistenciaAdmin(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

        Page<Asistencia> asistencias;

        // Filtrar si las fechas están presentes
        if (desde != null && hasta != null) {
            asistencias = asistenciaService.obtenerAsistenciasPorFechas(desde, hasta, PageRequest.of(page, 5));
        } else {
            asistencias = asistenciaService.obtenerTodasLasAsistencias(PageRequest.of(page, 5));
        }

        // Generar enlace de Google Maps para cada asistencia
        for (Asistencia asistencia : asistencias) {
            if (asistencia.getLatitudIngreso() != null && asistencia.getLongitudIngreso() != null) {
                String mapsUrlIngreso = "https://www.google.com/maps?q=" + asistencia.getLatitudIngreso() + "," + asistencia.getLongitudIngreso();
                asistencia.setLinkUbicacionIngreso(mapsUrlIngreso);
            }
            if (asistencia.getLatitudSalida() != null && asistencia.getLongitudSalida() != null) {
                String mapsUrlSalida = "https://www.google.com/maps?q=" + asistencia.getLatitudSalida() + "," + asistencia.getLongitudSalida();
                asistencia.setLinkUbicacionSalida(mapsUrlSalida);
            }
        }

        model.addAttribute("asistencias", asistencias);
        model.addAttribute("trabajador", trabajador);
        model.addAttribute("desde", desde); // Mantener las fechas en el formulario
        model.addAttribute("hasta", hasta);
        return "AsistenciasAdmin";
    }

    /**
     * Carga las asistencias del trabajador autenticado, con la opción de
     * filtrar por fechas. Genera enlaces de Google Maps para las ubicaciones de
     * ingreso y salida.
     *
     * @param model El modelo utilizado para pasar datos a la vista.
     * @param session La sesión HTTP donde se almacena el trabajador
     * autenticado.
     * @param page Número de página para la paginación de asistencias.
     * @param desde Fecha inicial para filtrar asistencias.
     * @param hasta Fecha final para filtrar asistencias.
     * @return La vista "VerAsistenciasTrabajador" con las asistencias
     * filtradas.
     */
    @GetMapping("/VerAsistenciasTrabajador")
    public String CargaAsistenciaTrabajador(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

        Page<Asistencia> asistencias;

        // Filtrar asistencias si las fechas están presentes
        if (desde != null && hasta != null) {
            asistencias = asistenciaService.findAsistenciasByTrabajadorIdAndFechas(
                    trabajador.getId(), desde, hasta, PageRequest.of(page, 5));
        } else {
            asistencias = asistenciaService.findAsistenciasByTrabajadorId(
                    trabajador.getId(), PageRequest.of(page, 5));
        }

        // Generar enlace de Google Maps para cada asistencia
        for (Asistencia asistencia : asistencias) {
            if (asistencia.getLatitudIngreso() != null && asistencia.getLongitudIngreso() != null) {
                String mapsUrlIngreso = "https://www.google.com/maps?q=" + asistencia.getLatitudIngreso() + "," + asistencia.getLongitudIngreso();
                asistencia.setLinkUbicacionIngreso(mapsUrlIngreso);
            }
            if (asistencia.getLatitudSalida() != null && asistencia.getLongitudSalida() != null) {
                String mapsUrlSalida = "https://www.google.com/maps?q=" + asistencia.getLatitudSalida() + "," + asistencia.getLongitudSalida();
                asistencia.setLinkUbicacionSalida(mapsUrlSalida);
            }
        }

        model.addAttribute("asistencias", asistencias);
        model.addAttribute("trabajador", trabajador);
        model.addAttribute("desde", desde); // Mantener las fechas en el formulario
        model.addAttribute("hasta", hasta);

        return "VerAsistenciasTrabajador";

    }

    /**
     * Genera un reporte PDF con la información de asistencia de un trabajador.
     *
     * <p>
     * El reporte incluye información como fecha, trabajador, cargo, hora de
     * ingreso, hora de salida, asistencia, inasistencia, tardanza y
     * justificaciones.</p>
     *
     * @param response objeto {@link HttpServletResponse} utilizado para generar
     * el archivo PDF como respuesta.
     * @param session objeto {@link HttpSession} para obtener el trabajador en
     * sesión.
     *
     */
    @GetMapping("/imprimirTodoTrabajador")
    public void imprimirReporteTrabajador(HttpServletResponse response, HttpSession session) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_asistencias_Trabajador.pdf");

        try {
            Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4.rotate());

            // Configurar márgenes
            document.setMargins(10, 20, 10, 20);

            // Agregar contenido al PDF
            document.add(new Paragraph("Reporte de Asistencia").setBold().setFontSize(18));

            // Crear la tabla con 8 columnas (agregamos una nueva columna para la fecha)
            Table table = new Table(new float[]{3, 3, 2, 2, 2, 2, 2, 2, 2});
            table.setFontSize(8);
            table.setWidth(UnitValue.createPercentValue(100));

            // Encabezados de la tabla con tamaño de fuente aumentado
            table.addHeaderCell(new Paragraph("Fecha").setFontSize(10));
            table.addHeaderCell(new Paragraph("Trabajador").setFontSize(10));
            table.addHeaderCell(new Paragraph("Cargo").setFontSize(10));
            table.addHeaderCell(new Paragraph("Hora de ingreso").setFontSize(10));
            table.addHeaderCell(new Paragraph("Hora de salida").setFontSize(10));
            table.addHeaderCell(new Paragraph("Asistencia").setFontSize(10));
            table.addHeaderCell(new Paragraph("Inasistencia").setFontSize(10));
            table.addHeaderCell(new Paragraph("Tardanza").setFontSize(10));
            table.addHeaderCell(new Paragraph("Justificaciones").setFontSize(10));

            // Iterar sobre las asistencias y agregar las filas correspondientes
            for (Asistencia asistencia : asistenciaService.findAsistenciasByTrabajadorId2(trabajador.getId())) {
                // Agregar la fecha al principio de la fila
                table.addCell(asistencia.getFecha().toString());

                // Agregar datos del trabajador y otros campos
                table.addCell(asistencia.getTrabajador().getNombres());
                table.addCell(asistencia.getTrabajador().getCargo());

                // Verificar la asistencia y añadir celdas de asistencia, inasistencia o tardanza
                if (asistencia.getHoraingreso() != null && asistencia.getHorasalida() != null) {
                    // Si el trabajador tiene hora de ingreso y salida, se considera presente
                    if (asistencia.getEstado().equals("Tardanza")) {
                        // Si la hora de ingreso es después de las 9 AM, es una tardanza
                        table.addCell(asistencia.getHoraingreso().toString());
                        table.addCell(asistencia.getHorasalida().toString());
                        table.addCell("").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                        table.addCell("!").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                    } else if (asistencia.getEstado().equals("Justificada")) {
                        // Si el trabajador llegó a tiempo, marcar como presente
                        table.addCell(asistencia.getHoraingreso().toString());
                        table.addCell(asistencia.getHorasalida().toString());
                        table.addCell("").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                        table.addCell("");
                        table.addCell("V").setTextAlignment(TextAlignment.CENTER);
                    } else {
                        table.addCell(asistencia.getHoraingreso().toString());
                        table.addCell(asistencia.getHorasalida().toString());
                        table.addCell("V").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                        table.addCell("");
                        table.addCell("");
                    }
                } else {
                    // Si no tiene hora de entrada o salida, marcar como inasistencia
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                    table.addCell("X").setTextAlignment(TextAlignment.CENTER);
                    table.addCell("");
                    table.addCell("");
                }
            }

            // Añadir la tabla al documento y cerrar
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera un reporte PDF filtrado por un rango de fechas con la información
     * de asistencia de un trabajador.
     *
     * <p>
     * El reporte incluye información como fecha, trabajador, cargo, hora de
     * ingreso, hora de salida, asistencia, inasistencia, tardanza y
     * justificaciones.</p>
     *
     * @param fechaInicio cadena que representa la fecha de inicio del filtro en
     * formato ISO (yyyy-MM-dd). Si es nulo o vacío, no se aplica filtro por
     * fecha de inicio.
     * @param fechaFin cadena que representa la fecha de fin del filtro en
     * formato ISO (yyyy-MM-dd). Si es nulo o vacío, no se aplica filtro por
     * fecha de fin.
     * @param response objeto {@link HttpServletResponse} utilizado para generar
     * el archivo PDF como respuesta.
     * @param session objeto {@link HttpSession} para obtener el trabajador en
     * sesión.
     *
     */
    @GetMapping("/imprimirFiltradoTrabajador")
    public void imprimirReporteFiltradoTrabajador(@RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin,
            HttpServletResponse response, HttpSession session) throws IOException {
        try {
            Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");

            // Verificar si ambos parámetros son nulos o vacíos antes de filtrar
            if ((fechaInicio == null || fechaInicio.isEmpty()) && (fechaFin == null || fechaFin.isEmpty())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Debe seleccionar al menos una fecha para filtrar el reporte.\"}");
                return;
            }

            // Convertir las fechas de parámetros a LocalDate
            LocalDate inicio = (fechaInicio != null && !fechaInicio.isEmpty()) ? LocalDate.parse(fechaInicio) : LocalDate.MIN;
            LocalDate fin = (fechaFin != null && !fechaFin.isEmpty()) ? LocalDate.parse(fechaFin) : LocalDate.MAX;

            // Filtrar asistencias
            List<Asistencia> asistenciasFiltradas = asistenciaService.findAsistenciasByTrabajadorId2(trabajador.getId()).stream()
                    .filter(asistencia -> {
                        LocalDate fechaAsistencia = asistencia.getFecha();
                        return (fechaAsistencia.isAfter(inicio) || fechaAsistencia.isEqual(inicio))
                                && (fechaAsistencia.isBefore(fin) || fechaAsistencia.isEqual(fin));
                    })
                    .toList();

            if (asistenciasFiltradas.isEmpty()) {
                throw new IllegalArgumentException("No se encontraron asistencias para el rango de fechas seleccionado.");
            }

            // Generar PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_asistencias_filtrado.pdf");

            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4.rotate());
            document.setMargins(10, 20, 10, 20);
            document.add(new Paragraph("Reporte de Asistencia - Filtrado").setBold().setFontSize(18));

            Table table = new Table(new float[]{3, 3, 2, 2, 2, 2, 2, 2, 2});
            table.setFontSize(8);
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell(new Paragraph("Fecha").setFontSize(10));
            table.addHeaderCell(new Paragraph("Trabajador").setFontSize(10));
            table.addHeaderCell(new Paragraph("Cargo").setFontSize(10));
            table.addHeaderCell(new Paragraph("Hora de ingreso").setFontSize(10));
            table.addHeaderCell(new Paragraph("Hora de salida").setFontSize(10));
            table.addHeaderCell(new Paragraph("Asistencia").setFontSize(10));
            table.addHeaderCell(new Paragraph("Inasistencia").setFontSize(10));
            table.addHeaderCell(new Paragraph("Tardanza").setFontSize(10));
            table.addHeaderCell(new Paragraph("Justificaciones").setFontSize(10));

            for (Asistencia asistencia : asistenciasFiltradas) {
                table.addCell(asistencia.getFecha().toString());
                table.addCell(asistencia.getTrabajador().getNombres());
                table.addCell(asistencia.getTrabajador().getCargo());
                if (asistencia.getHoraingreso() != null && asistencia.getHorasalida() != null) {
                    if (asistencia.getEstado().equals("Tardanza")) {
                        // Si la hora de ingreso es después de las 9 AM, es una tardanza
                        table.addCell(asistencia.getHoraingreso().toString());
                        table.addCell(asistencia.getHorasalida().toString());
                        table.addCell("").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                        table.addCell("!").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                    } else if (asistencia.getEstado().equals("Justificada")) {
                        // Si el trabajador llegó a tiempo, marcar como presente
                        table.addCell(asistencia.getHoraingreso().toString());
                        table.addCell(asistencia.getHorasalida().toString());
                        table.addCell("").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                        table.addCell("");
                        table.addCell("V").setTextAlignment(TextAlignment.CENTER);
                    } else {
                        table.addCell(asistencia.getHoraingreso().toString());
                        table.addCell(asistencia.getHorasalida().toString());
                        table.addCell("V").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                        table.addCell("");
                        table.addCell("");
                    }
                } else {
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                    table.addCell("X").setTextAlignment(TextAlignment.CENTER);
                    table.addCell("");
                    table.addCell("");
                }
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            // Manejar el error en el servidor de forma controlada
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Registra la asistencia de un trabajador, ya sea para el ingreso o la
     * salida, y realiza las validaciones necesarias.
     *
     * @param tipo El tipo de asistencia a registrar, que puede ser "Ingreso" o
     * "Salida".
     * @param latitud La latitud de la ubicación desde donde se registra la
     * asistencia.
     * @param longitud La longitud de la ubicación desde donde se registra la
     * asistencia.
     * @param redirectAttributes Los atributos que se utilizarán para redirigir
     * con mensajes de éxito o error.
     * @param session La sesión HTTP que contiene la información del trabajador.
     * @return La vista a la que se redirige después de registrar la asistencia,
     * con el mensaje adecuado.
     *
     * Este método verifica que el trabajador esté dentro del rango permitido de
     * ubicación para registrar la asistencia, valida la hora de ingreso y
     * salida y maneja los casos de tardanza y ausencia. También gestiona los
     * permisos de un administrador para ver o modificar las asistencias de los
     * trabajadores.
     */
    @PostMapping("/registrarAsistencia")
    public String registrarAsistencia(
            @RequestParam("tipo") String tipo,// Detecta si el valor es Ingreso o Salida
            @RequestParam("latitud") Double latitud,
            @RequestParam("longitud") Double longitud,
            RedirectAttributes redirectAttributes,
            HttpSession session) {
        //Cargamos al trabaajdor que se guardo en la sesion
        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");
        //obtenemos la asistencia del dia con el trabajador
        Asistencia asistencia = asistenciaService.obtenerAsistenciaDelDia(trabajador.getId());

        ZonedDateTime ahora = horaServidorService.obtenerHoraDelServidor();

        // Extrae la hora y fecha
        LocalTime horaActualServidor = ahora.toLocalTime();
        LocalDate fechaActualServidor = ahora.toLocalDate();

        //limite de hora 9Am
        LocalTime horaLimiteTardanza = LocalTime.of(9, 0);

        // Verificar si el trabajador está dentro del rango permitido de 100 metros
        boolean dentroDelRango = asistenciaService.estaDentroDelRango(
                latitud, longitud,
                ConfiguracionUbicacion.UBICACION_LATITUD,
                ConfiguracionUbicacion.UBICACION_LONGITUD,
                ConfiguracionUbicacion.RANGO_ASISTENCIA
        );

        if (!dentroDelRango) {
            // Si está fuera del rango, redirigir con un mensaje de advertencia
            if (trabajador.getRol().equals("Administrador")) {
                redirectAttributes.addAttribute("errorfueraDeRango", true);
                return "redirect:/AsistenciasAdmin";
            } else {
                redirectAttributes.addAttribute("errorfueraDeRango", true);
                return "redirect:/PrincipalTrabajador";
            }
        }

        if (asistencia != null && asistencia.getEstado().equals("Inasistencia")) {
            redirectAttributes.addAttribute("erroringreso", true);
            if (trabajador.getRol().equals("Administrador")) {
                return "redirect:/AsistenciasAdmin";
            } else {
                return "redirect:/PrincipalTrabajador";
            }
        }

        //Asistencia pal administrador
        if (trabajador.getRol().equals("Administrador")) {
            if (tipo.equals("Ingreso")) {

                if (asistencia != null && asistencia.getHoraingreso() != null) {
                    redirectAttributes.addAttribute("erroringreso", true);
                    return "redirect:/AsistenciasAdmin";
                }
                // si no hay asistencia registrada para el dia actual, crea una nueva entrada
                if (asistencia == null) {
                    // Crear nueva asistencia
                    asistencia = new Asistencia();
                    asistencia.setFecha(fechaActualServidor);
                    asistencia.setTrabajador(trabajador);
                }

                asistencia.setHoraingreso(horaActualServidor);

                // Determinar estado según la hora de ingreso
                if (horaActualServidor.isAfter(horaLimiteTardanza)) {
                    asistencia.setEstado("Tardanza");
                } else {
                    asistencia.setEstado("Presente");
                }

                asistencia.setLatitudIngreso(latitud);
                asistencia.setLongitudIngreso(longitud);

            } else if (tipo.equals("Salida")) {

                //si asistencia y hora de salida no estan vacios manda error
                if (asistencia == null) {
                    redirectAttributes.addAttribute("errorsalida2", true);
                    return "redirect:/AsistenciasAdmin";
                }

                if (asistencia != null && asistencia.getHorasalida() != null) {
                    redirectAttributes.addAttribute("errorsalida", true);
                    return "redirect:/AsistenciasAdmin";
                }
                // si es salida actualiza la hora de salida en la asistencia del dia actual
                if (asistencia != null && "Tardanza".equals(asistencia.getEstado())) {
                    // Si el estado es tardanza, no cambiarlo
                    asistencia.setHorasalida(horaActualServidor);
                    asistencia.setLatitudSalida(latitud);
                    asistencia.setLongitudSalida(longitud);
                } else if (asistencia != null) {
                    asistencia.setHorasalida(horaActualServidor);
                    asistencia.setEstado("Salida");
                    asistencia.setLatitudSalida(latitud);
                    asistencia.setLongitudSalida(longitud);
                }
            }

            asistenciaService.saveAsistencia(asistencia);
            redirectAttributes.addAttribute("success", true);
            return "redirect:/AsistenciasAdmin";
        } else {
            //verifica el tipo 
            if (tipo.equals("Ingreso")) {

                //si asistencia y hora de ingreso no estan vacios manda error
                if (asistencia != null && asistencia.getHoraingreso() != null) {
                    redirectAttributes.addAttribute("erroringreso", true);
                    return "redirect:/PrincipalTrabajador";
                }
                // si no hay asistencia registrada para el dia actual, crea una nueva entrada
                if (asistencia == null) {
                    // Crear nueva asistencia
                    asistencia = new Asistencia();
                    asistencia.setFecha(fechaActualServidor);
                    asistencia.setTrabajador(trabajador);
                }

                asistencia.setHoraingreso(horaActualServidor);

                // Determinar estado según la hora de ingreso
                if (horaActualServidor.isAfter(horaLimiteTardanza)) {
                    asistencia.setEstado("Tardanza");
                } else {
                    asistencia.setEstado("Presente");
                }

                asistencia.setLatitudIngreso(latitud);
                asistencia.setLongitudIngreso(longitud);

            } else if (tipo.equals("Salida")) {

                if (asistencia == null) {
                    redirectAttributes.addAttribute("errorsalida2", true);
                    return "redirect:/PrincipalTrabajador";
                }

                //si asistencia y hora de salida no estan vacios manda error
                if (asistencia != null && asistencia.getHorasalida() != null) {
                    redirectAttributes.addAttribute("errorsalida", true);
                    return "redirect:/PrincipalTrabajador";
                }
                // si es salida actualiza la hora de salida en la asistencia del dia actual
                if (asistencia != null && "Tardanza".equals(asistencia.getEstado())) {
                    // Si el estado es tardanza, no cambiarlo
                    asistencia.setHorasalida(horaActualServidor);
                    asistencia.setLatitudSalida(latitud);
                    asistencia.setLongitudSalida(longitud);
                } else if (asistencia != null) {
                    asistencia.setHorasalida(horaActualServidor);
                    asistencia.setEstado("Salida");
                    asistencia.setLatitudSalida(latitud);
                    asistencia.setLongitudSalida(longitud);
                }
            }

            //guarda la asisitencia y recarga la pagina
            asistenciaService.saveAsistencia(asistencia);
            redirectAttributes.addAttribute("success", true);
            return "redirect:/PrincipalTrabajador";

        }
    }

    /**
     * Muestra la vista de reportes de asistencia, con la opción de filtrar por
     * un rango de fechas.
     *
     * @param model El modelo que contiene los atributos que se enviarán a la
     * vista.
     * @param session La sesión HTTP que contiene la información del trabajador
     * actual.
     * @param page El número de página para la paginación de los resultados
     * (valor por defecto es 0).
     * @param desde La fecha de inicio del rango de fechas para filtrar las
     * asistencias (opcional).
     * @param hasta La fecha de fin del rango de fechas para filtrar las
     * asistencias (opcional).
     * @return El nombre de la vista que muestra los reportes de asistencia.
     *
     * Este método recupera las asistencias de los trabajadores y las muestra en
     * una vista de reportes, permitiendo filtrarlas por un rango de fechas si
     * se proporciona. También incluye la paginación de resultados. Se añade al
     * modelo la información de las asistencias, las fechas seleccionadas y el
     * trabajador actual.
     */
    @GetMapping("/Reportes")
    public String MuestraVistaReportes(
            Model model,
            HttpSession session,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta) {

        Page<Asistencia> asistencias;

        // Filtrar si las fechas están presentes
        if (desde != null && hasta != null) {
            asistencias = asistenciaService.obtenerAsistenciasPorFechas(desde, hasta, PageRequest.of(page, 5));
        } else {
            asistencias = asistenciaService.obtenerTodasLasAsistencias(PageRequest.of(page, 5));
        }

        model.addAttribute("asistencias", asistencias);

        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");
        model.addAttribute("trabajador", trabajador);

        // Pasar las fechas al modelo para mantenerlas en los campos del formulario
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);

        return "Reportes";
    }

    /**
     * Genera y envía un reporte en formato PDF con las asistencias de los
     * trabajadores. El reporte incluye una tabla con la fecha, trabajador,
     * cargo, hora de ingreso, hora de salida, estado de la asistencia
     * (presente, inasistencia, tardanza, justificada) y justificaciones.
     *
     * @param response La respuesta HTTP donde se generará y enviará el archivo
     * PDF.
     *
     * Este método crea un archivo PDF que se descarga como
     * "reporte_asistencias.pdf". El reporte contiene una tabla que muestra los
     * registros de asistencia de todos los trabajadores, incluyendo las fechas
     * de ingreso, las horas de entrada y salida, el estado de la asistencia
     * (presente, inasistencia, tardanza, justificada) y cualquier justificación
     * asociada. Se maneja la configuración de márgenes y el estilo del PDF.
     */
    @GetMapping("/imprimirTodo")
    public void imprimirReporte(HttpServletResponse response) {
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte_asistencias.pdf");

        try {
            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4.rotate());

            // Configurar márgenes
            document.setMargins(10, 20, 10, 20);

            // Agregar contenido al PDF
            document.add(new Paragraph("Reporte de Asistencia").setBold().setFontSize(18));

            // Crear la tabla con 8 columnas (agregamos una nueva columna para la fecha)
            Table table = new Table(new float[]{3, 3, 2, 2, 2, 2, 2, 2, 2});
            table.setFontSize(8);
            table.setWidth(UnitValue.createPercentValue(100));

            // Encabezados de la tabla con tamaño de fuente aumentado
            table.addHeaderCell(new Paragraph("Fecha").setFontSize(10));
            table.addHeaderCell(new Paragraph("Trabajador").setFontSize(10));
            table.addHeaderCell(new Paragraph("Cargo").setFontSize(10));
            table.addHeaderCell(new Paragraph("Hora de ingreso").setFontSize(10));
            table.addHeaderCell(new Paragraph("Hora de salida").setFontSize(10));
            table.addHeaderCell(new Paragraph("Asistencia").setFontSize(10));
            table.addHeaderCell(new Paragraph("Inasistencia").setFontSize(10));
            table.addHeaderCell(new Paragraph("Tardanza").setFontSize(10));
            table.addHeaderCell(new Paragraph("Justificaciones").setFontSize(10));

            // Iterar sobre las asistencias y agregar las filas correspondientes
            for (Asistencia asistencia : asistenciaService.obtenerTodasLasAsistencias()) {
                // Agregar la fecha al principio de la fila
                table.addCell(asistencia.getFecha().toString());

                // Agregar datos del trabajador y otros campos
                table.addCell(asistencia.getTrabajador().getNombres());
                table.addCell(asistencia.getTrabajador().getCargo());

                // Verificar la asistencia y añadir celdas de asistencia, inasistencia o tardanza
                if (asistencia.getHoraingreso() != null && asistencia.getHorasalida() != null) {
                    // Si el trabajador tiene hora de ingreso y salida, se considera presente
                    if (asistencia.getEstado().equals("Tardanza")) {
                        // Si la hora de ingreso es después de las 9 AM, es una tardanza
                        table.addCell(asistencia.getHoraingreso().toString());
                        table.addCell(asistencia.getHorasalida().toString());
                        table.addCell("").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                        table.addCell("!").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                    } else if (asistencia.getEstado().equals("Justificada")) {
                        // Si el trabajador llegó a tiempo, marcar como presente
                        table.addCell(asistencia.getHoraingreso().toString());
                        table.addCell(asistencia.getHorasalida().toString());
                        table.addCell("").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                        table.addCell("");
                        table.addCell("V").setTextAlignment(TextAlignment.CENTER);
                    } else {
                        table.addCell(asistencia.getHoraingreso().toString());
                        table.addCell(asistencia.getHorasalida().toString());
                        table.addCell("V").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                        table.addCell("");
                        table.addCell("");
                    }
                } else {
                    // Si no tiene hora de entrada o salida, marcar como inasistencia
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                    table.addCell("X").setTextAlignment(TextAlignment.CENTER);
                    table.addCell("");
                    table.addCell("");
                }
            }

            // Añadir la tabla al documento y cerrar
            document.add(table);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Genera y envía un reporte en formato PDF de las asistencias de los
     * trabajadores, filtrado por un rango de fechas.
     *
     * Si los parámetros de fecha de inicio y fecha de fin son proporcionados,
     * se filtran las asistencias que caen dentro de este rango. Si no se
     * proporcionan fechas, se devuelve un error de solicitud incorrecta.
     *
     * @param fechaInicio La fecha de inicio para filtrar las asistencias
     * (formato ISO: "yyyy-MM-dd").
     * @param fechaFin La fecha de fin para filtrar las asistencias (formato
     * ISO: "yyyy-MM-dd").
     * @param response La respuesta HTTP donde se generará y enviará el archivo
     * PDF.
     *
     * Este método crea un archivo PDF que se descarga como
     * "reporte_asistencias_filtrado.pdf". El reporte incluye una tabla con las
     * fechas de asistencia, nombres de los trabajadores, cargos, horas de
     * ingreso y salida, el estado de la asistencia (presente, inasistencia,
     * tardanza, justificada) y cualquier justificación asociada. En caso de que
     * no se encuentren asistencias dentro del rango de fechas o si ocurre un
     * error en el proceso, se devolverá un mensaje de error en formato JSON.
     */
    @GetMapping("/imprimirFiltrado")
    public void imprimirReporteFiltrado(@RequestParam(name = "fechaInicio", required = false) String fechaInicio,
            @RequestParam(name = "fechaFin", required = false) String fechaFin,
            HttpServletResponse response) throws IOException {
        try {
            // Verificar si ambos parámetros son nulos o vacíos antes de filtrar
            if ((fechaInicio == null || fechaInicio.isEmpty()) && (fechaFin == null || fechaFin.isEmpty())) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                response.getWriter().write("{\"message\": \"Debe seleccionar al menos una fecha para filtrar el reporte.\"}");
                return;
            }

            // Convertir las fechas de parámetros a LocalDate
            LocalDate inicio = (fechaInicio != null && !fechaInicio.isEmpty()) ? LocalDate.parse(fechaInicio) : LocalDate.MIN;
            LocalDate fin = (fechaFin != null && !fechaFin.isEmpty()) ? LocalDate.parse(fechaFin) : LocalDate.MAX;

            // Filtrar asistencias
            List<Asistencia> asistenciasFiltradas = asistenciaService.obtenerTodasLasAsistencias().stream()
                    .filter(asistencia -> {
                        LocalDate fechaAsistencia = asistencia.getFecha();
                        return (fechaAsistencia.isAfter(inicio) || fechaAsistencia.isEqual(inicio))
                                && (fechaAsistencia.isBefore(fin) || fechaAsistencia.isEqual(fin));
                    })
                    .toList();

            if (asistenciasFiltradas.isEmpty()) {
                throw new IllegalArgumentException("No se encontraron asistencias para el rango de fechas seleccionado.");
            }

            // Generar PDF
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=reporte_asistencias_filtrado.pdf");

            PdfWriter writer = new PdfWriter(response.getOutputStream());
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc, PageSize.A4.rotate());
            document.setMargins(10, 20, 10, 20);
            document.add(new Paragraph("Reporte de Asistencia - Filtrado").setBold().setFontSize(18));

            Table table = new Table(new float[]{3, 3, 2, 2, 2, 2, 2, 2, 2});
            table.setFontSize(8);
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell(new Paragraph("Fecha").setFontSize(10));
            table.addHeaderCell(new Paragraph("Trabajador").setFontSize(10));
            table.addHeaderCell(new Paragraph("Cargo").setFontSize(10));
            table.addHeaderCell(new Paragraph("Hora de ingreso").setFontSize(10));
            table.addHeaderCell(new Paragraph("Hora de salida").setFontSize(10));
            table.addHeaderCell(new Paragraph("Asistencia").setFontSize(10));
            table.addHeaderCell(new Paragraph("Inasistencia").setFontSize(10));
            table.addHeaderCell(new Paragraph("Tardanza").setFontSize(10));
            table.addHeaderCell(new Paragraph("Justificaciones").setFontSize(10));

            for (Asistencia asistencia : asistenciasFiltradas) {
                table.addCell(asistencia.getFecha().toString());
                table.addCell(asistencia.getTrabajador().getNombres());
                table.addCell(asistencia.getTrabajador().getCargo());
                if (asistencia.getHoraingreso() != null && asistencia.getHorasalida() != null) {
                    if (asistencia.getEstado().equals("Tardanza")) {
                        // Si la hora de ingreso es después de las 9 AM, es una tardanza
                        table.addCell(asistencia.getHoraingreso().toString());
                        table.addCell(asistencia.getHorasalida().toString());
                        table.addCell("").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                        table.addCell("!").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                    } else if (asistencia.getEstado().equals("Justificada")) {
                        // Si el trabajador llegó a tiempo, marcar como presente
                        table.addCell(asistencia.getHoraingreso().toString());
                        table.addCell(asistencia.getHorasalida().toString());
                        table.addCell("").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                        table.addCell("");
                        table.addCell("V").setTextAlignment(TextAlignment.CENTER);
                    } else {
                        table.addCell(asistencia.getHoraingreso().toString());
                        table.addCell(asistencia.getHorasalida().toString());
                        table.addCell("V").setTextAlignment(TextAlignment.CENTER);
                        table.addCell("");
                        table.addCell("");
                        table.addCell("");
                    }
                } else {
                    table.addCell("");
                    table.addCell("");
                    table.addCell("");
                    table.addCell("X").setTextAlignment(TextAlignment.CENTER);
                    table.addCell("");
                    table.addCell("");
                }
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            // Manejar el error en el servidor de forma controlada
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"" + e.getMessage() + "\"}");
        }
    }

}
