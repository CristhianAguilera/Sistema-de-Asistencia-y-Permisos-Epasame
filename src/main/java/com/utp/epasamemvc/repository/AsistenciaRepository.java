/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.utp.epasamemvc.repository;

import com.utp.epasamemvc.model.Asistencia;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    
    

    /* List<Asistencia> findByTrabajadorId(Long trabajadorId);*/
    /**
     * Encuentra una asistencia por el ID del trabajador.
     *
     * @param idTrabajador el ID del trabajador cuyas asistencias se desean
     * buscar
     * @return la asistencia del trabajador con el ID especificado
     */
    Asistencia findByTrabajadorId(Integer idTrabajador);

    /**
     * Encuentra una asistencia por el ID del trabajador y la fecha.
     *
     * @param trabajadorId el ID del trabajador
     * @param fecha la fecha de la asistencia que se desea buscar
     * @return un objeto Optional que puede contener la asistencia encontrada, o
     * estar vacío si no se encuentra ninguna
     */
    Optional<Asistencia> findByTrabajadorIdAndFecha(Integer trabajadorId, LocalDate fecha);

}
