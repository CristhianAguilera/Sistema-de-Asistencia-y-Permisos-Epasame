/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase Asistencia representa la asistencia de un trabajador en una fecha
 * específica, registrando la hora de ingreso y salida, así como el estado.
 * <p>
 * Cada asistencia está asociada a un trabajador específico y una justificacion.
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
     * La latitud de la ubicación donde el trabajador ingresó. Representa la
     * coordenada geográfica en la que el trabajador marcó su entrada.
     */
    private Double latitudIngreso;

    /**
     * La longitud de la ubicación donde el trabajador ingresó. Representa la
     * coordenada geográfica en la que el trabajador marcó su entrada.
     */
    private Double longitudIngreso;

    /**
     * La latitud de la ubicación donde el trabajador salió. Representa la
     * coordenada geográfica en la que el trabajador marcó su salida.
     */
    private Double latitudSalida;

    /**
     * La longitud de la ubicación donde el trabajador salió. Representa la
     * coordenada geográfica en la que el trabajador marcó su salida.
     */
    private Double longitudSalida;

    /**
     * Un enlace a la ubicación de entrada del trabajador. Este enlace puede
     * mostrar un mapa o la ubicación exacta de ingreso.
     */
    private String linkUbicacionIngreso;

    /**
     * Un enlace a la ubicación de salida del trabajador. Este enlace puede
     * mostrar un mapa o la ubicación exacta de salida.
     */
    private String linkUbicacionSalida;

    /**
     * Indica si el trabajador se encontraba dentro de las instalaciones al
     * momento de su ingreso. Si es verdadero, el trabajador marcó su entrada
     * dentro del local; si es falso, no estaba dentro del local.
     */
    private boolean dentroDeLocalIngreso;

    /**
     * Getter para el atributo `dentroDeLocalIngreso`. Devuelve si el trabajador
     * estaba dentro del local en el momento de su ingreso.
     *
     * @return true si el trabajador estaba dentro del local al ingresar; false
     * si no.
     */
    public boolean isDentroDeLocalIngreso() {
        return dentroDeLocalIngreso;
    }

    private LocalDateTime createdAt;

    /**
     * Trabajador al que se asocia esta asistencia.
     */
    @ManyToOne
    @JoinColumn(name = "trabajador_id")
    private Trabajador trabajador;

    /**
     * Lista de justificaciones asociadas a la asistencia.
     */
    @OneToMany(mappedBy = "asistencia")
    private List<Justificacion> justificaciones;

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
     * Obtiene la lista de justificaciones asociadas.
     *
     * @return La lista de objetos Justificacion.
     */
    public List<Justificacion> getJustificaciones() {
        return justificaciones;
    }

    /**
     * Establece la lista de justificaciones asociadas.
     *
     * @param justificaciones La lista de objetos Justificacion a establecer.
     */
    public void setJustificaciones(List<Justificacion> justificaciones) {
        this.justificaciones = justificaciones;
    }

    /**
     * Obtiene la longitud de ingreso del trabajador. Representa la coordenada
     * geográfica de la ubicación donde el trabajador marcó su entrada.
     *
     * @return La longitud de la ubicación de ingreso.
     */
    public Double getLongitudIngreso() {
        return longitudIngreso;
    }

    /**
     * Obtiene la latitud de ingreso del trabajador. Representa la coordenada
     * geográfica de la ubicación donde el trabajador marcó su entrada.
     *
     * @return La latitud de la ubicación de ingreso.
     */
    public Double getLatitudIngreso() {
        return latitudIngreso;
    }

    /**
     * Establece la latitud de ingreso del trabajador. Representa la coordenada
     * geográfica de la ubicación donde el trabajador marcó su entrada.
     *
     * @param latitudIngreso La latitud de la ubicación de ingreso a establecer.
     */
    public void setLatitudIngreso(Double latitudIngreso) {
        this.latitudIngreso = latitudIngreso;
    }

    /**
     * Establece la longitud de ingreso del trabajador. Representa la coordenada
     * geográfica de la ubicación donde el trabajador marcó su entrada.
     *
     * @param longitudIngreso La longitud de la ubicación de ingreso a
     * establecer.
     */
    public void setLongitudIngreso(Double longitudIngreso) {
        this.longitudIngreso = longitudIngreso;
    }

    /**
     * Obtiene la latitud de salida del trabajador. Representa la coordenada
     * geográfica de la ubicación donde el trabajador marcó su salida.
     *
     * @return La latitud de la ubicación de salida.
     */
    public Double getLatitudSalida() {
        return latitudSalida;
    }

    /**
     * Establece la latitud de salida del trabajador. Representa la coordenada
     * geográfica de la ubicación donde el trabajador marcó su salida.
     *
     * @param latitudSalida La latitud de la ubicación de salida a establecer.
     */
    public void setLatitudSalida(Double latitudSalida) {
        this.latitudSalida = latitudSalida;
    }

    /**
     * Obtiene la longitud de salida del trabajador. Representa la coordenada
     * geográfica de la ubicación donde el trabajador marcó su salida.
     *
     * @return La longitud de la ubicación de salida.
     */
    public Double getLongitudSalida() {
        return longitudSalida;
    }

    /**
     * Establece la longitud de salida del trabajador. Representa la coordenada
     * geográfica de la ubicación donde el trabajador marcó su salida.
     *
     * @param longitudSalida La longitud de la ubicación de salida a establecer.
     */
    public void setLongitudSalida(Double longitudSalida) {
        this.longitudSalida = longitudSalida;
    }

    /**
     * Obtiene el enlace a la ubicación de ingreso del trabajador. Este enlace
     * puede redirigir a un mapa o mostrar más detalles de la ubicación de
     * ingreso.
     *
     * @return El enlace a la ubicación de ingreso.
     */
    public String getLinkUbicacionIngreso() {
        return linkUbicacionIngreso;
    }

    /**
     * Establece el enlace a la ubicación de ingreso del trabajador. Este enlace
     * puede redirigir a un mapa o mostrar más detalles de la ubicación de
     * ingreso.
     *
     * @param linkUbicacionIngreso El enlace de ubicación de ingreso a
     * establecer.
     */
    public void setLinkUbicacionIngreso(String linkUbicacionIngreso) {
        this.linkUbicacionIngreso = linkUbicacionIngreso;
    }

    /**
     * Obtiene el enlace a la ubicación de salida del trabajador. Este enlace
     * puede redirigir a un mapa o mostrar más detalles de la ubicación de
     * salida.
     *
     * @return El enlace a la ubicación de salida.
     */
    public String getLinkUbicacionSalida() {
        return linkUbicacionSalida;
    }

    /**
     * Establece el enlace a la ubicación de salida del trabajador. Este enlace
     * puede redirigir a un mapa o mostrar más detalles de la ubicación de
     * salida.
     *
     * @param linkUbicacionSalida El enlace de ubicación de salida a establecer.
     */
    public void setLinkUbicacionSalida(String linkUbicacionSalida) {
        this.linkUbicacionSalida = linkUbicacionSalida;
    }

    /**
     * Obtiene la fecha y hora en que se creó el registro de entrada o salida
     * del trabajador.
     *
     * @return La fecha y hora de creación del registro.
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha y hora en que se creó el registro de entrada o salida
     * del trabajador.
     *
     * @param createdAt La fecha y hora de creación a establecer.
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
