/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    @Column(columnDefinition = "TEXT")
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
     * Fecha en la que el permiso fue aprobado.
     */
    private LocalDate fechaaprobado;

    /**
     * Fecha en la que el permiso fue rechazado.
     */
    private LocalDate fecharechazado;

    /**
     * Motivo del permiso.
     */
    @Column(columnDefinition = "TEXT")
    private String motivo;

    private LocalDateTime createdAt;

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

    /**
     * Obtiene la fecha en que se rechazo la solicitud.
     *
     * @return la fecha de rechazo de la solicitud.
     */
    public LocalDate getFecharechazado() {
        return fecharechazado;
    }

    /**
     * Establece la fecha en que se rechazo la solicitud.
     *
     * @param fecharechazado la fecha de rechazo a establecer.
     */
    public void setFecharechazado(LocalDate fecharechazado) {
        this.fecharechazado = fecharechazado;
    }

    /**
     * Obtiene el motivo asociado a la justificación. El motivo puede ser
     * utilizado para explicar la razón de la justificación, por ejemplo, si fue
     * debido a una enfermedad o una emergencia.
     *
     * @return el motivo de la justificación
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * Establece el motivo asociado a la justificación. Este método permite
     * asignar una razón o explicación para la justificación, como una
     * enfermedad o algún otro evento que justifique la ausencia.
     *
     * @param motivo el motivo de la justificación
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /**
     * Obtiene la fecha y hora en la que fue creada la justificación. La fecha y
     * hora de creación indican cuándo fue registrada la justificación en el
     * sistema.
     *
     * @return la fecha y hora de creación de la justificación
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha y hora en la que fue creada la justificación. Este
     * método permite asignar la fecha y hora en la que se registró la
     * justificación en el sistema.
     *
     * @param createdAt la fecha y hora de creación de la justificación
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
