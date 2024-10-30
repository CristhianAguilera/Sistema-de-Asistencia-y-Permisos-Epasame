/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.service;

import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.repository.TrabajadorRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para manejar la lógica de negocio relacionada con los trabajadores.
 * <p>
 * Esta clase proporciona métodos para guardar, actualizar y obtener información
 * sobre trabajadores.
 * </p>
 *
 * @author Cristhian Aguilera
 */
@Service
public class TrabajadorService {

    /**
     * Crea una nueva instancia del servicio de Trabajador.
     */
    public TrabajadorService() {
    }
    
    

    @Autowired
    private TrabajadorRepository trabajadorrepository;

    /**
     * Guarda un nuevo trabajador en la base de datos.
     *
     * @param trabajador el trabajador que se desea guardar
     * @return el trabajador guardado, que puede incluir un ID asignado
     */
    @Transactional
    public Trabajador saveTrabajador(Trabajador trabajador) {
        return trabajadorrepository.save(trabajador);
    }

    /**
     * Actualiza los detalles de un trabajador existente.
     *
     * @param id el ID del trabajador a actualizar
     * @param trabajadorDetails los nuevos detalles del trabajador
     * @return el trabajador actualizado
     * @throws EntityNotFoundException si no se encuentra un trabajador con el
     * ID proporcionado
     */
    @Transactional
    public Trabajador updateTrabajador(long id, Trabajador trabajadorDetails) {
        Trabajador trabajador = trabajadorrepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trabajador no encontrado con el ID: " + id));

        trabajador.setNombres(trabajadorDetails.getNombres());
        trabajador.setApellidos(trabajadorDetails.getApellidos());
        trabajador.setTelefono(trabajadorDetails.getTelefono());
        trabajador.setCorreo(trabajadorDetails.getCorreo());
        trabajador.setCargo(trabajadorDetails.getCargo());
        trabajador.setRol(trabajadorDetails.getRol());
        trabajador.setTipodocumento(trabajadorDetails.getTipodocumento());
        trabajador.setNdocumento(trabajadorDetails.getNdocumento());
        trabajador.setTelefono(trabajadorDetails.getTelefono());
        trabajador.setCorreo(trabajadorDetails.getCorreo());
        trabajador.setContraseña(trabajadorDetails.getContraseña());
        trabajador.setFechaentrada(trabajadorDetails.getFechaentrada());

        return trabajadorrepository.save(trabajador);
    }

    /**
     * Obtiene una lista de trabajadores según su estado.
     *
     * @param estado el estado de los trabajadores que se desean obtener
     * @return una lista de trabajadores con el estado especificado
     */
    public List<Trabajador> findByEstado(String estado) {
        return trabajadorrepository.findByEstado(estado);
    }

    /**
     * Obtiene un trabajador por su ID.
     *
     * @param id el ID del trabajador a buscar
     * @return un {@link Optional} que contiene el trabajador si existe, o vacío
     * si no
     */
    public Optional<Trabajador> findTrabajadorById(Long id) {
        return trabajadorrepository.findById(id);
    }

    /**
     * Obtiene un trabajador por su ID (versión alternativa).
     *
     * @param id el ID del trabajador a buscar
     * @return el trabajador correspondiente al ID proporcionado, o null si no
     * existe
     */
    public Trabajador findTrabajadorById2(Integer id) {
        return trabajadorrepository.findById(id);
    }

    /**
     * Obtiene un trabajador por su correo electrónico.
     *
     * @param correo el correo del trabajador a buscar
     * @return el trabajador correspondiente al correo proporcionado
     */
    public Trabajador findByCorreo(String correo) {
        return trabajadorrepository.findByCorreo(correo);
    }

    /**
     * Obtiene una lista de trabajadores según su número de documento.
     *
     * @param ndocumento el número de documento de los trabajadores a buscar
     * @return una lista de trabajadores con el número de documento especificado
     */
    public List<Trabajador> findByNdocumento(String ndocumento) {
        return trabajadorrepository.findByNdocumento(ndocumento);
    }

    /**
     * Obtiene una lista de trabajadores según su número de teléfono.
     *
     * @param telefono el número de teléfono de los trabajadores a buscar
     * @return una lista de trabajadores con el número de teléfono especificado
     */
    public List<Trabajador> findByTelefono(String telefono) {
        return trabajadorrepository.findByTelefono(telefono);
    }

    /**
     * Obtiene una lista de trabajadores según su contraseña.
     *
     * @param contrasena la contraseña de los trabajadores a buscar
     * @return una lista de trabajadores con la contraseña especificada
     */
    public List<Trabajador> findByContraseña(String contrasena) {
        return trabajadorrepository.findByContraseña(contrasena);
    }

    /**
     * Obtiene todos los trabajadores de la base de datos.
     *
     * @return una lista de todos los trabajadores
     */
    public List<Trabajador> getAllTrabajadores() {
        return trabajadorrepository.findAll();
    }
    
    /*@Transactional
    public void deleteTrabajador(Long id) {
        Trabajador trabajador = trabajadorrepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trabajador no encontrado con el ID: " + id));

        trabajadorrepository.delete(trabajador);
    }*/

}
