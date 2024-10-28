/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

/**
 *
 * @author ADVANCE
 */
@Entity
@Table(name = "permisos")
public class Permiso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDate fechapeticion;
    private LocalDate fechainicio;
    private LocalDate fechafin;
    private String tipo;
    private String descripcion;
    
    @Lob
    private byte[] evidencia;
    
    private String estado;
    private Date fechaaprobado;

    @ManyToOne
    @JoinColumn(name = "trabajador_id")
    private Trabajador trabajador;

   
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechapeticion() {
        return fechapeticion;
    }

    public void setFechapeticion(LocalDate fechapeticion) {
        this.fechapeticion = fechapeticion;
    }

    public LocalDate getFechainicio() {
        return fechainicio;
    }

    public void setFechainicio(LocalDate fechainicio) {
        this.fechainicio = fechainicio;
    }

    public LocalDate getFechafin() {
        return fechafin;
    }

    public void setFechafin(LocalDate fechafin) {
        this.fechafin = fechafin;
    }

  
    public byte[] getEvidencia() {
        return evidencia;
    }

    public void setEvidencia(byte[] evidencia) {
        this.evidencia = evidencia;
    }

    

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaaprobado() {
        return fechaaprobado;
    }

    public void setFechaaprobado(Date fechaaprobado) {
        this.fechaaprobado = fechaaprobado;
    }

}
