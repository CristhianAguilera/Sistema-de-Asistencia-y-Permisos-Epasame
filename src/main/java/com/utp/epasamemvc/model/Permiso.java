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
 * La clase Permiso representa una solicitud de permiso realizada por un
 * trabajador, incluyendo detalles como fechas de petición e inicio, tipo de
 * permiso, descripción y evidencia adjunta.
 * <p>
 * Cada permiso está asociado a un trabajador específico y tiene un estado que
 * indica su aprobación o rechazo.
 * </p>
 *
 * @author Cristhian Aguilera
 */
@Entity
@Table(name = "permisos")
public class Permiso {

    /**
     * Crea una nueva instancia del permiso.
     */
    public Permiso() {
    }
    
    

    
    /**
     * Identificador único del permiso.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Fecha en la que se solicita el permiso.
     */
    private LocalDate fechapeticion;

    /**
     * Fecha de inicio del permiso.
     */
    private LocalDate fechainicio;

    /**
     * Fecha de fin del permiso.
     */
    private LocalDate fechafin;

    /**
     * Tipo de permiso ("Vacaciones", "Enfermedad","Personal").
     */
    private String tipo;

    /**
     * Descripción del motivo del permiso.
     */
    private String descripcion;

    /**
     * Evidencia en formato de bytes asociada al permiso, si es proporcionada.
     */
    @Lob
    private byte[] evidencia;

    /**
     * Estado del permiso ("Pendiente", "Aceptado", "Rechazado").
     */
    private String estado;

    /**
     * Fecha en la que el permiso fue aprobado o rechazado.
     */
    private LocalDate fechaaprobado;

    /**
     * Trabajador al que se asocia este permiso.
     */
    @ManyToOne
    @JoinColumn(name = "trabajador_id")
    private Trabajador trabajador;

    /**
     * Obtiene el ID de la solicitud.
     *
     * @return el ID de la solicitud.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID de la solicitud.
     *
     * @param id el ID de la solicitud a establecer.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene el tipo de la solicitud.
     *
     * @return el tipo de la solicitud.
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de la solicitud.
     *
     * @param tipo el tipo de la solicitud a establecer.
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene la fecha de petición de la solicitud.
     *
     * @return la fecha de petición de la solicitud.
     */
    public LocalDate getFechapeticion() {
        return fechapeticion;
    }

    /**
     * Establece la fecha de petición de la solicitud.
     *
     * @param fechapeticion la fecha de petición a establecer.
     */
    public void setFechapeticion(LocalDate fechapeticion) {
        this.fechapeticion = fechapeticion;
    }

    /**
     * Obtiene la fecha de inicio de la solicitud.
     *
     * @return la fecha de inicio de la solicitud.
     */
    public LocalDate getFechainicio() {
        return fechainicio;
    }

    /**
     * Establece la fecha de inicio de la solicitud.
     *
     * @param fechainicio la fecha de inicio a establecer.
     */
    public void setFechainicio(LocalDate fechainicio) {
        this.fechainicio = fechainicio;
    }

    /**
     * Obtiene la fecha de fin de la solicitud.
     *
     * @return la fecha de fin de la solicitud.
     */
    public LocalDate getFechafin() {
        return fechafin;
    }

    /**
     * Establece la fecha de fin de la solicitud.
     *
     * @param fechafin la fecha de fin a establecer.
     */
    public void setFechafin(LocalDate fechafin) {
        this.fechafin = fechafin;
    }

    /**
     * Obtiene la evidencia de la solicitud.
     *
     * @return la evidencia de la solicitud en forma de byte array.
     */
    public byte[] getEvidencia() {
        return evidencia;
    }

    /**
     * Establece la evidencia de la solicitud.
     *
     * @param evidencia la evidencia a establecer en forma de byte array.
     */
    public void setEvidencia(byte[] evidencia) {
        this.evidencia = evidencia;
    }

    /**
     * Obtiene el estado de la solicitud.
     *
     * @return el estado de la solicitud.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la solicitud.
     *
     * @param estado el estado de la solicitud a establecer.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el trabajador asociado a la solicitud.
     *
     * @return el trabajador asociado.
     */
    public Trabajador getTrabajador() {
        return trabajador;
    }

    /**
     * Establece el trabajador asociado a la solicitud.
     *
     * @param trabajador el trabajador a establecer.
     */
    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    /**
     * Obtiene la descripción de la solicitud.
     *
     * @return la descripción de la solicitud.
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la solicitud.
     *
     * @param descripcion la descripción a establecer.
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene la fecha en que se aprobó la solicitud.
     *
     * @return la fecha de aprobación de la solicitud.
     */
    public LocalDate getFechaaprobado() {
        return fechaaprobado;
    }

    /**
     * Establece la fecha en que se aprobó la solicitud.
     *
     * @param fechaaprobado la fecha de aprobación a establecer.
     */
    public void setFechaaprobado(LocalDate fechaaprobado) {
        this.fechaaprobado = fechaaprobado;
    }

}
