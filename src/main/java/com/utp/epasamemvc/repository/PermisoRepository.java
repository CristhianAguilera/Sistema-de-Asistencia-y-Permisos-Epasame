/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.utp.epasamemvc.repository;


import com.utp.epasamemvc.model.Permiso;
import com.utp.epasamemvc.model.Trabajador;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author ADVANCE
 */
public interface PermisoRepository extends JpaRepository<Permiso, Long> {
    
    List<Permiso> findByEstado(String estadoPermiso);
    
    Permiso findByTrabajadorId(Integer idTrabajador);
    
    Permiso findById(Integer idPemriso);
    
    Optional<Permiso> findByTrabajadorAndEstado(Trabajador trabajador, String estado);



}
