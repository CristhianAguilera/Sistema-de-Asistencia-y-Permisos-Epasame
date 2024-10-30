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
     * Encuentra todos los permisos con el estado especificado.
     *
     * @param estadoPermiso el estado de los permisos que se desean buscar
     * @return una lista de permisos que coinciden con el estado especificado
     */
    List<Permiso> findByEstado(String estadoPermiso);

    /**
     * Encuentra un permiso por el ID del trabajador.
     *
     * @param idTrabajador el ID del trabajador cuyas permisos se desean buscar
     * @return el permiso del trabajador con el ID especificado
     */
    Permiso findByTrabajadorId(Integer idTrabajador);

    /**
     * Encuentra un permiso por su ID.
     *
     * @param idPermiso el ID del permiso que se desea buscar
     * @return el permiso correspondiente al ID especificado
     */
    Permiso findById(Integer idPermiso);

    /**
     * Encuentra un permiso por el trabajador y el estado.
     *
     * @param trabajador el trabajador asociado al permiso
     * @param estado el estado del permiso que se desea buscar
     * @return un objeto Optional que puede contener el permiso encontrado, o
     * estar vacío si no se encuentra ninguno
     */
    Optional<Permiso> findByTrabajadorAndEstado(Trabajador trabajador, String estado);

}
