/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.service;

import com.utp.epasamemvc.model.Permiso;
import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.repository.PermisoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ADVANCE
 */
@Service
public class PermisoService {

    @Autowired
    private PermisoRepository permisorepository;

    // Guardar un nuevo permiso 
    @Transactional
    public Permiso savePermiso(Permiso permiso) {
        return permisorepository.save(permiso);
    } 

    // Obtener todos los permisos por id del trabajador 
    public Permiso findPermisosByTrabajadorId(Integer trabajadorId) {
        return permisorepository.findByTrabajadorId(trabajadorId);
    }
    
    //obtiene los permisos del trabajdor en estado pendiente 
    public boolean tienePermisoPendiente(Trabajador trabajador) {
        return permisorepository.findByTrabajadorAndEstado(trabajador, "Pendiente").isPresent();
    }

    // Obtener un permiso por ID 
    public Permiso findPermisoById(Integer idPermiso) {
        return permisorepository.findById(idPermiso);
    }

}
