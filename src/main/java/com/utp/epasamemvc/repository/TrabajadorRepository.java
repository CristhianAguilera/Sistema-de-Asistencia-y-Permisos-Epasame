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
 * Repositorio para manejar las operaciones de acceso a datos de la entidad
 * Trabajador.
 * <p>
 * Esta interfaz extiende JpaRepository, lo que permite realizar operaciones
 * CRUD sin necesidad de implementar métodos adicionales.
 * </p>
 *
 * @author Cristhian Aguilera
 */
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {

    /**
     * Encuentra todos los trabajadores.
     *
     * @return una lista de todos los trabajadores registrados en la base de
     * datos.
     */
    @Override
    List<Trabajador> findAll();

    /**
     * Encuentra un trabajador por su correo electrónico.
     *
     * @param correo el correo electrónico del trabajador que se desea buscar
     * @return el trabajador asociado al correo electrónico especificado
     */
    Trabajador findByCorreo(String correo);

    /**
     * Encuentra todos los trabajadores con un estado específico.
     *
     * @param estado el estado de los trabajadores que se desean buscar
     * @return una lista de trabajadores que coinciden con el estado
     * especificado
     */
    List<Trabajador> findByEstado(String estado);

    /**
     * Encuentra todos los trabajadores por su número de documento.
     *
     * @param ndocumento el número de documento del trabajador que se desea
     * buscar
     * @return una lista de trabajadores que coinciden con el número de
     * documento especificado
     */
    List<Trabajador> findByNdocumento(String ndocumento);

    /**
     * Encuentra todos los trabajadores por su número de teléfono.
     *
     * @param telefono el número de teléfono del trabajador que se desea buscar
     * @return una lista de trabajadores que coinciden con el número de teléfono
     * especificado
     */
    List<Trabajador> findByTelefono(String telefono);

    /**
     * Encuentra todos los trabajadores por su contraseña.
     *
     * @param contrasena la contraseña del trabajador que se desea buscar
     * @return una lista de trabajadores que coinciden con la contraseña
     * especificada
     */
    List<Trabajador> findByContraseña(String contrasena);

    /**
     * Encuentra un trabajador por su ID.
     *
     * @param Trabajadorid el ID del trabajador que se desea buscar
     * @return el trabajador correspondiente al ID especificado
     */
    Trabajador findById(Integer Trabajadorid);

}
