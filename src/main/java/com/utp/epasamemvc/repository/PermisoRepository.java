/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.utp.epasamemvc.repository;

import com.utp.epasamemvc.model.Permiso;
import com.utp.epasamemvc.model.Trabajador;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para manejar las operaciones de acceso a datos de la entidad
 * Permiso.
 * <p>
 * Esta interfaz extiende JpaRepository, lo que permite realizar operaciones
 * CRUD sin necesidad de implementar métodos adicionales.
 * </p>
 *
 * @author Cristhian Aguilera
 */
public interface PermisoRepository extends JpaRepository<Permiso, Long> {

    /**
     * Obtiene una lista de permisos con un estado específico. Este método
     * devuelve todos los permisos que tienen el estado especificado (por
     * ejemplo, "Pendiente", "Aprobado").
     *
     * @param estadoPermiso el estado de los permisos (por ejemplo, "Aprobado",
     * "Rechazado")
     * @return una lista de permisos con el estado indicado
     */
    List<Permiso> findByEstado(String estadoPermiso);

    /**
     * Obtiene una lista de permisos asociados a un trabajador por su ID. Este
     * método devuelve todos los permisos relacionados con un trabajador
     * específico.
     *
     * @param idTrabajador el ID del trabajador
     * @return una lista de permisos asociados al trabajador
     */
    List<Permiso> findByTrabajadorId(Integer idTrabajador);

    /**
     * Obtiene una página de permisos asociados a un trabajador por su ID. Este
     * método devuelve una página de permisos relacionadas con un trabajador
     * específico, paginadas según los parámetros proporcionados.
     *
     * @param idTrabajador el ID del trabajador
     * @param p la información de paginación
     * @return una página de permisos para el trabajador especificado
     */
    Page<Permiso> findByTrabajadorId(Integer idTrabajador, Pageable p);

    /**
     * Busca un permiso para un trabajador y un estado específico. Este método
     * busca un permiso para un trabajador determinado en un estado específico.
     *
     * @param idTrabajador el ID del trabajador
     * @param estado el estado del permiso (por ejemplo, aprobado, pendiente,
     * rechazado)
     * @return el permiso encontrado
     */
    Permiso findByTrabajadorIdAndEstado(Integer idTrabajador, String estado);

    /**
     * Busca un permiso por su ID. Este método busca un permiso específico
     * utilizando su ID único.
     *
     * @param idPermiso el ID del permiso
     * @return el permiso encontrado
     */
    Permiso findById(Integer idPermiso);

    /**
     * Busca un permiso por trabajador y estado. Este método permite encontrar
     * un permiso para un trabajador específico y en un estado determinado.
     *
     * @param trabajador el trabajador asociado al permiso
     * @param estado el estado del permiso
     * @return un objeto Optional con el permiso encontrado, o vacío si no se
     * encuentra
     */
    Optional<Permiso> findByTrabajadorAndEstado(Trabajador trabajador, String estado);

    /**
     * Obtiene todos los permisos. Este método devuelve todos los permisos
     * presentes en la base de datos.
     *
     * @return una lista de todos los permisos
     */
    @Override
    List<Permiso> findAll();

    /**
     * Obtiene una página de todos los permisos. Este método devuelve una página
     * de todos los permisos, paginados según los parámetros proporcionados.
     *
     * @param p la información de paginación
     * @return una página de todos los permisos
     */
    Page<Permiso> findAll(Pageable p);

    /**
     * Obtiene una página de permisos dentro de un rango de fechas. Este método
     * devuelve una página de permisos que fueron solicitados entre las fechas
     * proporcionadas.
     *
     * @param desde la fecha de inicio del rango
     * @param hasta la fecha de fin del rango
     * @param pageable la información de paginación
     * @return una página de permisos dentro del rango de fechas
     */
    Page<Permiso> findByFechapeticionBetween(LocalDate desde, LocalDate hasta, Pageable pageable);

    /**
     * Obtiene una página de permisos de un trabajador específico dentro de un
     * rango de fechas. Este método devuelve una página de permisos solicitados
     * por un trabajador específico, dentro de un rango de fechas determinado.
     *
     * @param trabajadorId el ID del trabajador
     * @param desde la fecha de inicio del rango
     * @param hasta la fecha de fin del rango
     * @param pageable la información de paginación
     * @return una página de permisos del trabajador dentro del rango de fechas
     */
    Page<Permiso> findByTrabajadorIdAndFechapeticionBetween(Integer trabajadorId, LocalDate desde, LocalDate hasta, Pageable pageable);

}
