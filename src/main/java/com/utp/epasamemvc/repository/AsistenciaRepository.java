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
 *
 * @author ADVANCE
 */
public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    /* List<Asistencia> findByTrabajadorId(Long trabajadorId);

   */
    
    Asistencia findByTrabajadorId(Integer idTrabajador);
    
    Optional<Asistencia> findByTrabajadorIdAndFecha(Integer trabajadorId, LocalDate fecha);

}
