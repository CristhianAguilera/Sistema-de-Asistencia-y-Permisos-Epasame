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
 * Servicio para manejar la lógica de negocio relacionada con los permisos de
 * los trabajadores.
 * <p>
 * Esta clase proporciona métodos para guardar, obtener y gestionar permisos.
 * </p>
 *
 * @author Cristhian Aguilera
 */
@Service
public class PermisoService {

    /**
     * Crea una nueva instancia del servicio de permisos.
     */
    public PermisoService() {
    }

    @Autowired
    private PermisoRepository permisorepository;

    /**
     * Guarda un nuevo permiso en la base de datos.
     *
     * @param permiso el permiso que se desea guardar
     * @return el permiso guardado, que puede incluir un ID asignado
     */
    @Transactional
    public Permiso savePermiso(Permiso permiso) {
        return permisorepository.save(permiso);
    }

    /**
     * Obtiene todos los permisos asociados a un trabajador específico.
     *
     * @param trabajadorId el ID del trabajador cuyas solicitudes de permiso se
     * desean obtener
     * @return el permiso correspondiente al trabajador, o null si no existe
     */
    public Permiso findPermisosByTrabajadorId(Integer trabajadorId) {
        return permisorepository.findByTrabajadorId(trabajadorId);
    }

    /**
     * Verifica si un trabajador tiene un permiso pendiente.
     *
     * @param trabajador el trabajador que se desea verificar
     * @return true si el trabajador tiene un permiso en estado 'Pendiente',
     * false en caso contrario
     */
    public boolean tienePermisoPendiente(Trabajador trabajador) {
        return permisorepository.findByTrabajadorAndEstado(trabajador, "Pendiente").isPresent();
    }

    /**
     * Obtiene un permiso específico por su ID.
     *
     * @param idPermiso el ID del permiso que se desea obtener
     * @return el permiso correspondiente al ID proporcionado, o null si no
     * existe
     */
    public Permiso findPermisoById(Integer idPermiso) {
        return permisorepository.findById(idPermiso);
    }

}
