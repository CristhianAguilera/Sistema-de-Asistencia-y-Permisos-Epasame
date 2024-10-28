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
 *
 * @author ADVANCE
 */
@Service
public class AsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciarepository;

    //Guarda la asistencia
    @Transactional
    public Asistencia saveAsistencia(Asistencia asistencia) {
        return asistenciarepository.save(asistencia);
    }

    //obtiene la asistencia segun el id del trabajador y la fecha actual
    public Asistencia obtenerAsistenciaDelDia(Integer trabajadorId) {
        return asistenciarepository.findByTrabajadorIdAndFecha(trabajadorId, LocalDate.now()).orElse(null);
    }

    //obtiene todas las asistencias
    public List<Asistencia> obtenerTodasLasAsistencias(){
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
