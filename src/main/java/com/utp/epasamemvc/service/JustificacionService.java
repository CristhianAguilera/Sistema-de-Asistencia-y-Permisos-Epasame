/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.service;

import com.utp.epasamemvc.model.Justificacion;
import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.repository.JustificacionRepository;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio que gestiona las justificaciones de los trabajadores. Incluye
 * operaciones como guardar, consultar y verificar el estado de las
 * justificaciones.
 *
 * @author Cristhian Aguilera
 */
@Service
public class JustificacionService {

    @Autowired
    private JustificacionRepository justificacionRepository;

    /**
     * Guarda una justificación en la base de datos.
     *
     * @param justificacion La justificación que se desea guardar.
     * @return La justificación guardada.
     */
    @Transactional
    public Justificacion savrJustificacion(Justificacion justificacion) {
        return justificacionRepository.save(justificacion);
    }

    /**
     * Verifica si un trabajador tiene una justificación pendiente.
     *
     * @param trabajador El trabajador cuyo estado de justificación se desea
     * verificar.
     * @return true si el trabajador tiene una justificación pendiente, false en
     * caso contrario.
     */
    public boolean tieneJustificacionPendiente(Trabajador trabajador) {
        return justificacionRepository.findByTrabajadorAndEstado(trabajador, "Pendiente").isPresent();
    }

    /**
     * Obtiene una justificación de un trabajador por su ID y estado.
     *
     * @param trabajadorId El ID del trabajador.
     * @param estado El estado de la justificación.
     * @return La justificación encontrada, o null si no se encuentra ninguna.
     */
    public Justificacion findJustificacionByTrabajadorIdAndEstado(Integer trabajadorId, String estado) {
        return justificacionRepository.findByTrabajadorIdAndEstado(trabajadorId, estado);
    }

    /**
     * Obtiene todas las justificaciones de un trabajador por su ID.
     *
     * @param trabajadorId El ID del trabajador.
     * @return Una lista de justificaciones asociadas al trabajador.
     */
    public List<Justificacion> findJustificacionesByTrabajadorId(Integer trabajadorId) {
        return justificacionRepository.findByTrabajadorId(trabajadorId);
    }

    /**
     * Obtiene una página de justificaciones asociadas a un trabajador.
     *
     * @param trabajadorId El ID del trabajador.
     * @param p Los parámetros de paginación.
     * @return Una página de justificaciones del trabajador.
     */
    public Page<Justificacion> findPermisosByTrabajadorIdPage(Integer trabajadorId, Pageable p) {
        return justificacionRepository.findByTrabajadorId(trabajadorId, p);
    }

    /**
     * Obtiene una justificación por su ID.
     *
     * @param idJustificacion El ID de la justificación.
     * @return La justificación correspondiente al ID, o null si no se
     * encuentra.
     */
    public Justificacion findJustificacionById(Integer idJustificacion) {
        return justificacionRepository.findById(idJustificacion);
    }

    /**
     * Obtiene todas las justificaciones registradas en la base de datos.
     *
     * @return Una lista de todas las justificaciones.
     */
    public List<Justificacion> getAllJustificaciones() {
        return justificacionRepository.findAll();
    }

    /**
     * Obtiene una página de todas las justificaciones.
     *
     * @param p Los parámetros de paginación.
     * @return Una página de todas las justificaciones.
     */
    public Page<Justificacion> getAllJustificacionesPage(Pageable p) {
        return justificacionRepository.findAll(p);
    }

    /**
     * Obtiene las justificaciones registradas entre dos fechas.
     *
     * @param desde La fecha de inicio.
     * @param hasta La fecha de fin.
     * @param pageable Los parámetros de paginación.
     * @return Una página de justificaciones dentro del rango de fechas.
     */
    public Page<Justificacion> obtenerJustificacionPorFechas(LocalDate desde, LocalDate hasta, Pageable pageable) {
        return justificacionRepository.findByFechaBetween(desde, hasta, pageable);
    }

    /**
     * Obtiene las justificaciones de un trabajador entre dos fechas.
     *
     * @param trabajadorid El ID del trabajador.
     * @param desde La fecha de inicio.
     * @param hasta La fecha de fin.
     * @param pageable Los parámetros de paginación.
     * @return Una página de justificaciones del trabajador dentro del rango de
     * fechas.
     */
    public Page<Justificacion> obtenerJustificacionPorTrabajadorIdAndFechas(Integer trabajadorid, LocalDate desde, LocalDate hasta, Pageable pageable) {
        return justificacionRepository.findByTrabajadorIdAndFechaBetween(trabajadorid, desde, hasta, pageable);
    }
}
