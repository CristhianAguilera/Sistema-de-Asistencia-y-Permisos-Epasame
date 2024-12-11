/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.utp.epasamemvc.repository;

import com.utp.epasamemvc.model.Justificacion;
import com.utp.epasamemvc.model.Trabajador;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio de justificaciones que proporciona métodos para acceder a la base
 * de datos relacionadas con las justificaciones. Este repositorio extiende
 * JpaRepository, lo que permite realizar operaciones CRUD básicas y consultas
 * personalizadas sobre la entidad {@link Justificacion}.
 *
 * @author Cristhian Aguilera
 */
public interface JustificacionRepository extends JpaRepository<Justificacion, Long> {

    /**
     * Busca una justificación por trabajador y estado. Este método busca una
     * justificación para un trabajador específico y un estado específico.
     *
     * @param trabajador el trabajador asociado a la justificación
     * @param estado el estado de la justificación (por ejemplo, aprobado,
     * rechazado)
     * @return un objeto Optional con la justificación encontrada, o vacío si no
     * se encuentra
     */
    Optional<Justificacion> findByTrabajadorAndEstado(Trabajador trabajador, String estado);

    /**
     * Obtiene una lista de todas las justificaciones asociadas a un trabajador
     * por su ID. Este método devuelve todas las justificaciones relacionadas
     * con un trabajador específico.
     *
     * @param idTrabajador el ID del trabajador
     * @return una lista de justificaciones asociadas al trabajador
     */
    List<Justificacion> findByTrabajadorId(Integer idTrabajador);

    /**
     * Obtiene una página de justificaciones asociadas a un trabajador por su
     * ID. Este método devuelve una página de justificaciones relacionadas con
     * un trabajador específico, paginadas según los parámetros proporcionados.
     *
     * @param idTrabajador el ID del trabajador
     * @param p la información de paginación
     * @return una página de justificaciones para el trabajador especificado
     */
    Page<Justificacion> findByTrabajadorId(Integer idTrabajador, Pageable p);

    /**
     * Busca una justificación para un trabajador y un estado específico. Este
     * método busca una justificación asociada a un trabajador dado y un estado
     * específico.
     *
     * @param idTrabajador el ID del trabajador
     * @param estado el estado de la justificación (por ejemplo, aprobado,
     * rechazado)
     * @return la justificación encontrada
     */
    Justificacion findByTrabajadorIdAndEstado(Integer idTrabajador, String estado);

    /**
     * Busca una justificación por su ID. Este método busca una justificación
     * específica a partir de su ID único.
     *
     * @param idJustificacion el ID de la justificación
     * @return la justificación encontrada
     */
    Justificacion findById(Integer idJustificacion);

    /**
     * Obtiene todas las justificaciones. Este método devuelve todas las
     * justificaciones presentes en la base de datos.
     *
     * @return una lista de todas las justificaciones
     */
    @Override
    List<Justificacion> findAll();

    /**
     * Obtiene una página de todas las justificaciones. Este método devuelve una
     * página de todas las justificaciones, paginadas según los parámetros
     * proporcionados.
     *
     * @param p la información de paginación
     * @return una página de todas las justificaciones
     */
    Page<Justificacion> findAll(Pageable p);

    /**
     * Obtiene una página de justificaciones dentro de un rango de fechas. Este
     * método devuelve una página de justificaciones que se encuentran entre las
     * fechas proporcionadas.
     *
     * @param desde la fecha de inicio del rango
     * @param hasta la fecha de fin del rango
     * @param pageable la información de paginación
     * @return una página de justificaciones dentro del rango de fechas
     */
    Page<Justificacion> findByFechaBetween(LocalDate desde, LocalDate hasta, Pageable pageable);

    /**
     * Obtiene una página de justificaciones para un trabajador dentro de un
     * rango de fechas. Este método devuelve una página de justificaciones
     * asociadas a un trabajador específico que se encuentren entre las fechas
     * proporcionadas.
     *
     * @param trabajadorId el ID del trabajador
     * @param desde la fecha de inicio del rango
     * @param hasta la fecha de fin del rango
     * @param pageable la información de paginación
     * @return una página de justificaciones del trabajador dentro del rango de
     * fechas
     */
    Page<Justificacion> findByTrabajadorIdAndFechaBetween(Integer trabajadorId, LocalDate desde, LocalDate hasta, Pageable pageable);
}
