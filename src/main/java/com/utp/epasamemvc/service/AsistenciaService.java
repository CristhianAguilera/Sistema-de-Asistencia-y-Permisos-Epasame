/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.service;

import com.utp.epasamemvc.model.Asistencia;
import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.repository.AsistenciaRepository;
import com.utp.epasamemvc.repository.TrabajadorRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para manejar la lógica de negocio relacionada con la asistencia de
 * los trabajadores.
 * <p>
 * Esta clase proporciona métodos para guardar, obtener y gestionar las
 * asistencias.
 * </p>
 *
 * @author Cristhian Aguilera
 */
@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciarepository;

    @Autowired
    private TrabajadorService trabajadorservice;

    @Autowired
    private HoraServidorService horaServidorService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    /**
     * Constructor del servicio que inicia la tarea programada para registrar
     * faltas. La tarea se ejecutará periódicamente para verificar si se debe
     * registrar una falta.
     */
    public AsistenciaService() {
        iniciarTareaProgramada();
    }

    /**
     * Método que inicia una tarea programada que se ejecuta cada minuto para
     * verificar si es exactamente las 11:00 PM y registrar las faltas de los
     * trabajadores si es necesario.
     */
    private void iniciarTareaProgramada() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // Obtener la hora del servidor
                ZonedDateTime horaServidor = horaServidorService.obtenerHoraDelServidor();

                // Si es exactamente las 11:00 PM, ejecutar la lógica
                if (horaServidor.getHour() == 23 && horaServidor.getMinute() == 0) {
                    registrarFaltas();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 1, TimeUnit.MINUTES); // Verificar cada minuto
    }

    /**
     * Método que registra las faltas de los trabajadores para el día actual. Si
     * un trabajador no tiene registrada asistencia, se registra como
     * "Inasistencia".
     */
    public void registrarFaltas() {
        // Usamos el servicio para obtener la fecha del servidor
        ZonedDateTime fechaServidor = horaServidorService.obtenerHoraDelServidor();
        LocalDate hoy = fechaServidor.toLocalDate(); // Convertimos a LocalDate

        List<Trabajador> trabajadores = trabajadorservice.getAllTrabajadores();

        for (Trabajador trabajador : trabajadores) {
            Asistencia asistencia = obtenerAsistenciaDelDia(trabajador.getId());
            if (asistencia == null) {
                asistencia = new Asistencia();
                asistencia.setFecha(hoy);
                asistencia.setTrabajador(trabajador);
                asistencia.setEstado("Inasistencia");
                asistenciarepository.save(asistencia);
            }
        }
    }

    /**
     * Guarda la asistencia proporcionada en la base de datos.
     *
     * @param asistencia la asistencia que se desea guardar
     * @return la asistencia guardada, que puede incluir un ID asignado
     */
    @Transactional
    public Asistencia saveAsistencia(Asistencia asistencia) {
        return asistenciarepository.save(asistencia);
    }

    /**
     * Obtiene la asistencia del día actual para un trabajador específico.
     *
     * @param trabajadorId el ID del trabajador cuya asistencia se desea obtener
     * @return la asistencia del trabajador correspondiente a la fecha actual, o
     * null si no existe
     */
    public Asistencia obtenerAsistenciaDelDia(Integer trabajadorId) {
        ZonedDateTime fechaServidor = horaServidorService.obtenerHoraDelServidor();
        LocalDate hoy = fechaServidor.toLocalDate();
        return asistenciarepository.findByTrabajadorIdAndFecha(trabajadorId, hoy).orElse(null);
    }

    /**
     * Obtiene todas las asistencias registradas en la base de datos.
     *
     * @param pageable
     * @return una lista de todas las asistencias
     */
    public Page<Asistencia> obtenerTodasLasAsistencias(Pageable pageable) {
        return asistenciarepository.findAllByOrderByCreatedAtDesc(pageable);
    }

    /**
     * Obtiene todas las asistencias de la base de datos.
     *
     * @return Una lista con todas las asistencias registradas.
     */
    public List<Asistencia> obtenerTodasLasAsistencias() {
        return asistenciarepository.findAll();
    }

    /**
     * Obtiene las asistencias de un trabajador específico filtradas por su
     * estado.
     *
     * @param trabajadorId El ID del trabajador.
     * @param estados Lista de estados de las asistencias que se desean obtener.
     * @return Una lista con las asistencias que coinciden con el ID del
     * trabajador y los estados proporcionados.
     */
    public List<Asistencia> obtenerAsistenciasPorEstado(Integer trabajadorId, List<String> estados) {
        return asistenciarepository.findByTrabajadorIdAndEstadoIn(trabajadorId, estados);
    }

    /**
     * Busca una asistencia específica por su ID.
     *
     * @param idAsistencia El ID de la asistencia a buscar.
     * @return La asistencia correspondiente al ID proporcionado.
     */
    public Asistencia findAsistenciasById(Integer idAsistencia) {
        return asistenciarepository.findById(idAsistencia);
    }

    /**
     * Obtiene las asistencias de un trabajador específico, con paginación.
     *
     * @param trabajadorId El ID del trabajador.
     * @param pageable Objeto que contiene la información de paginación.
     * @return Un objeto Page con las asistencias del trabajador.
     */
    public Page<Asistencia> findAsistenciasByTrabajadorId(Integer trabajadorId, Pageable pageable) {
        return asistenciarepository.findByTrabajadorId(trabajadorId, pageable);
    }

    /**
     * Obtiene todas las asistencias de un trabajador específico sin paginación.
     *
     * @param trabajadorId El ID del trabajador.
     * @return Una lista con todas las asistencias del trabajador.
     */
    public List<Asistencia> findAsistenciasByTrabajadorId2(Integer trabajadorId) {
        return asistenciarepository.findByTrabajadorId(trabajadorId);
    }

    /**
     * Obtiene las asistencias dentro de un rango de fechas, con paginación.
     *
     * @param desde La fecha de inicio del rango.
     * @param hasta La fecha final del rango.
     * @param pageable Objeto que contiene la información de paginación.
     * @return Un objeto Page con las asistencias dentro del rango de fechas.
     */
    public Page<Asistencia> obtenerAsistenciasPorFechas(LocalDate desde, LocalDate hasta, Pageable pageable) {
        return asistenciarepository.findByFechaBetween(desde, hasta, pageable);
    }

    /**
     * Obtiene las asistencias de un trabajador dentro de un rango de fechas,
     * con paginación.
     *
     * @param trabajadorId El ID del trabajador.
     * @param desde La fecha de inicio del rango.
     * @param hasta La fecha final del rango.
     * @param pageable Objeto que contiene la información de paginación.
     * @return Un objeto Page con las asistencias del trabajador dentro del
     * rango de fechas.
     */
    public Page<Asistencia> findAsistenciasByTrabajadorIdAndFechas(
            Integer trabajadorId, LocalDate desde, LocalDate hasta, Pageable pageable) {
        return asistenciarepository.findByTrabajadorIdAndFechaBetween(trabajadorId, desde, hasta, pageable);
    }

    /**
     * Calcula la distancia en metros entre dos puntos geográficos utilizando
     * sus coordenadas GPS.
     *
     * @param lat1 Latitud del primer punto.
     * @param lon1 Longitud del primer punto.
     * @param lat2 Latitud del segundo punto.
     * @param lon2 Longitud del segundo punto.
     * @return La distancia entre los dos puntos en metros.
     */
    public double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int RADIO_TIERRA = 6371000; // Radio de la Tierra en metros
        double latDistancia = Math.toRadians(lat2 - lat1);
        double lonDistancia = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistancia / 2) * Math.sin(latDistancia / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistancia / 2) * Math.sin(lonDistancia / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIO_TIERRA * c;  // La distancia en metros
    }

    /**
     * Verifica si las coordenadas actuales están dentro de un rango de
     * distancia desde la ubicación de la oficina.
     *
     * @param latitudActual La latitud de la ubicación actual.
     * @param longitudActual La longitud de la ubicación actual.
     * @param latitudOficina La latitud de la oficina.
     * @param longitudOficina La longitud de la oficina.
     * @param rangoMetros El rango de distancia en metros.
     * @return true si la distancia es menor o igual al rango permitido, false
     * en caso contrario.
     */
    public boolean estaDentroDelRango(double latitudActual, double longitudActual, double latitudOficina, double longitudOficina, double rangoMetros) {
        final int RADIO_TIERRA = 6371000; // Radio de la Tierra en metros

        double dLat = Math.toRadians(latitudOficina - latitudActual);
        double dLon = Math.toRadians(longitudOficina - longitudActual);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(latitudActual)) * Math.cos(Math.toRadians(latitudOficina))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distancia = RADIO_TIERRA * c;  // Distancia en metros

        // Verifica si la distancia está dentro del rango permitido
        return distancia <= rangoMetros;
    }
}
