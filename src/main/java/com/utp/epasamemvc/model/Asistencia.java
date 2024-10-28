/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * @author ADVANCE
 */
@Entity
@Table(name = "asistencias")
public class Asistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private LocalDate fecha;
    private LocalTime horaingreso;
    private LocalTime horasalida;
    private String estado;
    private String justificacion;
    private String evidencia;

    
    @ManyToOne
    @JoinColumn(name = "trabajador_id")
    private Trabajador trabajador;
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraingreso() {
        return horaingreso;
    }

    public void setHoraingreso(LocalTime horaingreso) {
        this.horaingreso = horaingreso;
    }

    public LocalTime getHorasalida() {
        return horasalida;
    }

    public void setHorasalida(LocalTime horasalida) {
        this.horasalida = horasalida;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }
    
    public String getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(String evidencia) {
        this.evidencia = evidencia;
    }

}
