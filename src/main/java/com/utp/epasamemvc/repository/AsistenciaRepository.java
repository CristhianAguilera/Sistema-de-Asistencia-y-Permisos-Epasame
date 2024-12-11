/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.utp.epasamemvc.repository;

import com.utp.epasamemvc.model.Asistencia;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para manejar las operaciones de acceso a datos de la entidad
 * Asistencia.
 * <p>
 * Esta interfaz extiende JpaRepository, lo que permite realizar operaciones
 * CRUD sin necesidad de implementar métodos adicionales.
 * </p>
 *
 * @author Cristhian Aguilera
 */
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    /**
     * Obtiene una página de asistencias asociadas a un trabajador por su ID.
     * Este método devuelve una página de registros de asistencia asociados a un
     * trabajador específico, paginados según los parámetros proporcionados.
     *
     * @param idTrabajador el ID del trabajador
     * @param p la información de paginación
     * @return una página de asistencias para el trabajador especificado
     */
    Page<Asistencia> findByTrabajadorId(Integer idTrabajador, Pageable p);

    /**
     * Obtiene una lista de todas las asistencias asociadas a un trabajador por
     * su ID. Este método devuelve todas las asistencias que están asociadas a
     * un trabajador específico.
     *
     * @param idTrabajador el ID del trabajador
     * @return una lista de asistencias para el trabajador especificado
     */
    List<Asistencia> findByTrabajadorId(Integer idTrabajador);

    /**
     * Obtiene una asistencia específica asociada a un trabajador por su ID y
     * fecha. Este método busca una asistencia para un trabajador en una fecha
     * específica.
     *
     * @param trabajadorId el ID del trabajador
     * @param fecha la fecha de la asistencia
     * @return un objeto Optional con la asistencia encontrada, o vacío si no se
     * encuentra
     */
    Optional<Asistencia> findByTrabajadorIdAndFecha(Integer trabajadorId, LocalDate fecha);

    /**
     * Obtiene una asistencia específica por su ID. Este método busca una
     * asistencia por su ID único.
     *
     * @param idAsistencia el ID de la asistencia
     * @return la asistencia encontrada
     */
    Asistencia findById(Integer idAsistencia);

    /**
     * Obtiene todas las asistencias ordenadas por fecha de creación en orden
     * descendente. Este método devuelve todas las asistencias, ordenadas por la
     * fecha de creación, de la más reciente a la más antigua.
     *
     * @param p la información de paginación
     * @return una página de asistencias ordenadas por fecha de creación
     */
    Page<Asistencia> findAllByOrderByCreatedAtDesc(Pageable p);

    /**
     * Obtiene una lista de asistencias asociadas a un trabajador con un estado
     * específico. Este método devuelve una lista de asistencias para un
     * trabajador que se encuentran en uno de los estados proporcionados.
     *
     * @param trabajadorId el ID del trabajador
     * @param estados la lista de estados para filtrar las asistencias
     * @return una lista de asistencias con los estados especificados
     */
    List<Asistencia> findByTrabajadorIdAndEstadoIn(Integer trabajadorId, List<String> estados);

    /**
     * Obtiene una página de asistencias que se encuentren dentro de un rango de
     * fechas. Este método devuelve una página de asistencias que han ocurrido
     * entre las fechas proporcionadas.
     *
     * @param desde la fecha de inicio del rango
     * @param hasta la fecha de fin del rango
     * @param pageable la información de paginación
     * @return una página de asistencias dentro del rango de fechas
     */
    Page<Asistencia> findByFechaBetween(LocalDate desde, LocalDate hasta, Pageable pageable);

    /**
     * Obtiene una página de asistencias para un trabajador dentro de un rango
     * de fechas. Este método devuelve una página de asistencias de un
     * trabajador específico que ocurrieron entre las fechas proporcionadas.
     *
     * @param trabajadorId el ID del trabajador
     * @param desde la fecha de inicio del rango
     * @param hasta la fecha de fin del rango
     * @param pageable la información de paginación
     * @return una página de asistencias del trabajador dentro del rango de
     * fechas
     */
    Page<Asistencia> findByTrabajadorIdAndFechaBetween(Integer trabajadorId, LocalDate desde, LocalDate hasta, Pageable pageable);

}
