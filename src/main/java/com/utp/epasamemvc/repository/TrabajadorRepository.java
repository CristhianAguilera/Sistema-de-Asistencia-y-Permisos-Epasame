/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.utp.epasamemvc.repository;

import com.utp.epasamemvc.model.Trabajador;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;

/**
 *
 * @author ADVANCE
 */
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {
    List<Trabajador> findAll();
    Trabajador findByCorreo(String correo);
    List<Trabajador> findByEstado(String estado);
    List<Trabajador> findByNdocumento(String ndocumento);
    List<Trabajador> findByTelefono(String telefono);
    List<Trabajador> findByContraseña(String contrasena);
    Trabajador findById(Integer Trabajadorid);
    
}
