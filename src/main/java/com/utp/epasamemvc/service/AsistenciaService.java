/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.service;

import com.utp.epasamemvc.model.Asistencia;
import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.repository.AsistenciaRepository;
import com.utp.epasamemvc.repository.TrabajadorRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para manejar la lógica de negocio relacionada con la asistencia de
 * los trabajadores.
 * <p>
 * Esta clase proporciona métodos para guardar, obtener y gestionar las
 * asistencias.
 * </p>
 *
 * @author Cristhian Aguilera
 */
@Service
public class AsistenciaService {

    /**
     * Crea una nueva instancia del servicio de Asistencia.
     */
    public AsistenciaService() {
    }
    
    

    @Autowired
    private AsistenciaRepository asistenciarepository;

    /**
     * Guarda la asistencia proporcionada en la base de datos.
     *
     * @param asistencia la asistencia que se desea guardar
     * @return la asistencia guardada, que puede incluir un ID asignado
     */
    @Transactional
    public Asistencia saveAsistencia(Asistencia asistencia) {
        return asistenciarepository.save(asistencia);
    }

    /**
     * Obtiene la asistencia del día actual para un trabajador específico.
     *
     * @param trabajadorId el ID del trabajador cuya asistencia se desea obtener
     * @return la asistencia del trabajador correspondiente a la fecha actual, o
     * null si no existe
     */
    public Asistencia obtenerAsistenciaDelDia(Integer trabajadorId) {
        return asistenciarepository.findByTrabajadorIdAndFecha(trabajadorId, LocalDate.now()).orElse(null);
    }

    /**
     * Obtiene todas las asistencias registradas en la base de datos.
     *
     * @return una lista de todas las asistencias
     */
    public List<Asistencia> obtenerTodasLasAsistencias() {
        return asistenciarepository.findAll();
    }

//para obtener las asistencias segun el id del trabajador
    /*public List<Asistencia> findAsistenciasByTrabajadorId(Long trabajadorId) {  
        return asistenciarepository.findByTrabajadorId(trabajadorId);
    }*/
//Para obtener las asistencias en un rango de fecha 
    /*public List<Asistencia> findAsistenciasByDateRange(LocalDate fechainicio, LocalDate fechafin) {
        return asistenciarepository.findByFechaBetween(fechainicio, fechafin);

    }*/
}
