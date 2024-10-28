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
 *
 * @author ADVANCE
 */
@Service
public class TrabajadorService {

    @Autowired
    private TrabajadorRepository trabajadorrepository;

    //gurada un nuevo trabajador
    @Transactional
    public Trabajador saveTrabajador(Trabajador trabajador) {
        return trabajadorrepository.save(trabajador);
    }

    //actualiza al trabajador
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

    /*@Transactional
    public void deleteTrabajador(Long id) {
        Trabajador trabajador = trabajadorrepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trabajador no encontrado con el ID: " + id));

        trabajadorrepository.delete(trabajador);
    }*/

    //obtiene trabajador por estado
    public List<Trabajador> findByEstado(String estado) {
        return trabajadorrepository.findByEstado(estado);
    }

    //obtiene trabajdor por id
    public Optional<Trabajador> findTrabajadorById(Long id) {
        return trabajadorrepository.findById(id);
    }
    
    //obtiene trabajdor por id version2
    public Trabajador findTrabajadorById2(Integer id) {
        return trabajadorrepository.findById(id);
    }
    
    //obtiene trabajdor por correo
    public Trabajador findByCorreo(String correo) {
        return trabajadorrepository.findByCorreo(correo);
                
    }
    
    //obtiene trabajdor por nuemro de documento
    public List<Trabajador> findByNdocumento(String ndocumento) {
        return trabajadorrepository.findByNdocumento(ndocumento);
    }
    
    public List<Trabajador> findByTelefono(String telefono) {
        return trabajadorrepository.findByTelefono(telefono);
    }
    
    public List<Trabajador> findByContraseña(String contrasena) {
        return trabajadorrepository.findByContraseña(contrasena);
    }

    //obtiene todos los trabajadores 
    public List<Trabajador> getAllTrabajadores() {
        return trabajadorrepository.findAll();
    }

}
