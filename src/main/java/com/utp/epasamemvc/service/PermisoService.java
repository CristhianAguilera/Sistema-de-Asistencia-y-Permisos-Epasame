/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.service;

import com.utp.epasamemvc.model.Permiso;
import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.repository.PermisoRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Obtiene todos los permisos de un trabajador por su ID.
     *
     * @param trabajadorId El ID del trabajador.
     * @return Una lista de permisos asociados al trabajador.
     */
    public List<Permiso> findPermisosByTrabajadorId(Integer trabajadorId) {
        return permisorepository.findByTrabajadorId(trabajadorId);
    }

    /**
     * Obtiene los permisos de un trabajador por su ID en formato paginado.
     *
     * @param trabajadorId El ID del trabajador.
     * @param p Los parámetros de paginación.
     * @return Una página de permisos asociados al trabajador.
     */
    public Page<Permiso> findPermisosByTrabajadorIdPage(Integer trabajadorId, Pageable p) {
        return permisorepository.findByTrabajadorId(trabajadorId, p);
    }

    /**
     * Obtiene un permiso de un trabajador por su ID y estado.
     *
     * @param trabajadorId El ID del trabajador.
     * @param estado El estado del permiso.
     * @return El permiso encontrado, o null si no se encuentra ninguno.
     */
    public Permiso findPermisosByTrabajadorIdAndEstado(Integer trabajadorId, String estado) {
        return permisorepository.findByTrabajadorIdAndEstado(trabajadorId, estado);
    }

    /**
     * Verifica si un trabajador tiene un permiso pendiente.
     *
     * @param trabajador El trabajador cuyo estado de permiso se desea
     * verificar.
     * @return true si el trabajador tiene un permiso pendiente, false en caso
     * contrario.
     */
    public boolean tienePermisoPendiente(Trabajador trabajador) {
        return permisorepository.findByTrabajadorAndEstado(trabajador, "Pendiente").isPresent();
    }

    /**
     * Obtiene un permiso por su ID.
     *
     * @param idPermiso El ID del permiso.
     * @return El permiso correspondiente al ID, o null si no se encuentra.
     */
    public Permiso findPermisoById(Integer idPermiso) {
        return permisorepository.findById(idPermiso);
    }

    /**
     * Obtiene todos los permisos registrados en la base de datos.
     *
     * @return Una lista de todos los permisos.
     */
    public List<Permiso> getAllPermisos() {
        return permisorepository.findAll();
    }

    /**
     * Obtiene todos los permisos registrados en formato paginado.
     *
     * @param p Los parámetros de paginación.
     * @return Una página de todos los permisos.
     */
    public Page<Permiso> getAllPermisosPage(Pageable p) {
        return permisorepository.findAll(p);
    }

    /**
     * Obtiene los permisos registrados entre dos fechas.
     *
     * @param desde La fecha de inicio.
     * @param hasta La fecha de fin.
     * @param pageable Los parámetros de paginación.
     * @return Una página de permisos dentro del rango de fechas.
     */
    public Page<Permiso> obtenerPermisosPorFechas(LocalDate desde, LocalDate hasta, Pageable pageable) {
        return permisorepository.findByFechapeticionBetween(desde, hasta, pageable);
    }

    /**
     * Obtiene los permisos de un trabajador entre dos fechas.
     *
     * @param trabajadorid El ID del trabajador.
     * @param desde La fecha de inicio.
     * @param hasta La fecha de fin.
     * @param pageable Los parámetros de paginación.
     * @return Una página de permisos del trabajador dentro del rango de fechas.
     */
    public Page<Permiso> obtenerPermisosPorTrabajadorIdAndFechas(Integer trabajadorid, LocalDate desde, LocalDate hasta, Pageable pageable) {
        return permisorepository.findByTrabajadorIdAndFechapeticionBetween(trabajadorid, desde, hasta, pageable);
    }

}
