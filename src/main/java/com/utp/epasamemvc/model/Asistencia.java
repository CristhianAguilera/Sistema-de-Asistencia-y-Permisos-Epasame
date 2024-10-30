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
 * La clase Asistencia representa la asistencia de un trabajador en una fecha
 * específica, registrando la hora de ingreso y salida, así como el estado y
 * cualquier justificación.
 * <p>
 * Cada asistencia está asociada a un trabajador específico.
 * </p>
 *
 * @author Cristhian Aguilera
 */
@Entity
@Table(name = "asistencias")
public class Asistencia {

    /**
     * Crea una nueva instancia de la asistencia
     */
    public Asistencia() {
    }
    
    

    /**
     * Identificador único de la asistencia.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Fecha de la asistencia.
     */
    private LocalDate fecha;

    /**
     * Hora de ingreso del trabajador.
     */
    private LocalTime horaingreso;

    /**
     * Hora de salida del trabajador.
     */
    private LocalTime horasalida;

    /**
     * Estado de la asistencia ("Presente", "Ausente", "De Salida").
     */
    private String estado;

    /**
     * Justificación en caso de que el trabajador no haya cumplido con su
     * asistencia normal.
     */
    private String justificacion;

    /**
     * Evidencia asociada con la asistencia, si se proporciona.
     */
    private String evidencia;

    /**
     * Trabajador al que se asocia esta asistencia.
     */
    @ManyToOne
    @JoinColumn(name = "trabajador_id")
    private Trabajador trabajador;

    /**
     * Obtiene el ID del permiso.
     *
     * @return el ID del permiso.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID del permiso.
     *
     * @param id el ID del permiso a establecer.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene la fecha del permiso.
     *
     * @return la fecha del permiso.
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha del permiso.
     *
     * @param fecha la fecha del permiso a establecer.
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene la hora de ingreso del trabajador.
     *
     * @return la hora de ingreso.
     */
    public LocalTime getHoraingreso() {
        return horaingreso;
    }

    /**
     * Establece la hora de ingreso del trabajador.
     *
     * @param horaingreso la hora de ingreso a establecer.
     */
    public void setHoraingreso(LocalTime horaingreso) {
        this.horaingreso = horaingreso;
    }

    /**
     * Obtiene la hora de salida del trabajador.
     *
     * @return la hora de salida.
     */
    public LocalTime getHorasalida() {
        return horasalida;
    }

    /**
     * Establece la hora de salida del trabajador.
     *
     * @param horasalida la hora de salida a establecer.
     */
    public void setHorasalida(LocalTime horasalida) {
        this.horasalida = horasalida;
    }

    /**
     * Obtiene el estado del permiso.
     *
     * @return el estado del permiso.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del permiso.
     *
     * @param estado el estado del permiso a establecer.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la justificación del permiso.
     *
     * @return la justificación del permiso.
     */
    public String getJustificacion() {
        return justificacion;
    }

    /**
     * Establece la justificación del permiso.
     *
     * @param justificacion la justificación a establecer.
     */
    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    /**
     * Obtiene el trabajador asociado al permiso.
     *
     * @return el trabajador asociado.
     */
    public Trabajador getTrabajador() {
        return trabajador;
    }

    /**
     * Establece el trabajador asociado al permiso.
     *
     * @param trabajador el trabajador a establecer.
     */
    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    /**
     * Obtiene la evidencia del permiso.
     *
     * @return la evidencia del permiso.
     */
    public String getEvidencia() {
        return evidencia;
    }

    /**
     * Establece la evidencia del permiso.
     *
     * @param evidencia la evidencia a establecer.
     */
    public void setEvidencia(String evidencia) {
        this.evidencia = evidencia;

    }

}
