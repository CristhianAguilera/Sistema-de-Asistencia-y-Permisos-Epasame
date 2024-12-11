/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Representa una justificación asociada a un trabajador. La justificación
 * incluye detalles sobre la fecha, tipo, descripción, estado, fecha de
 * aprobación, fecha de rechazo, motivo, evidencia y la asistencia relacionada.
 *
 * @author Cristhian Aguilera
 */
@Entity
@Table(name = "justificaciones")
public class Justificacion {

    public Justificacion() {
    }


    /**
     * Identificador único de la justificación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Fecha en la que se registró la justificación.
     */
    private LocalDate fecha;

    /**
     * Tipo de la justificación (por ejemplo, permiso, enfermedad, etc.).
     */
    private String tipo;

    /**
     * Descripción detallada de la justificación. Este campo se almacena como
     * texto largo en la base de datos.
     */
    @Column(columnDefinition = "TEXT")
    private String descripcion;

    /**
     * Estado actual de la justificación (aprobada, rechazada, pendiente, etc.).
     */
    private String estado;

    /**
     * Fecha en que la justificación fue aprobada, si corresponde.
     */
    private LocalDate fechaaprobada;

    /**
     * Fecha en que la justificación fue rechazada, si corresponde.
     */
    private LocalDate fecharechazada;

    /**
     * Motivo de la justificación, que puede proporcionar detalles adicionales.
     * Este campo se almacena como texto largo en la base de datos.
     */
    @Column(columnDefinition = "TEXT")
    private String motivo;

    /**
     * Evidencia asociada a la justificación (por ejemplo, un archivo adjunto).
     * El campo se almacena como un arreglo de bytes (Lob).
     */
    @Lob
    private byte[] evidencia;

    /**
     * Fecha y hora de creación de la justificación.
     */
    private LocalDateTime createdAt;

    /**
     * Asistencia asociada a esta justificación. La justificación puede estar
     * relacionada con una asistencia específica.
     */
    @ManyToOne
    @JoinColumn(name = "asistencia_id")
    private Asistencia asistencia;

    /**
     * Trabajador que solicita la justificación. Se establece mediante una
     * relación con la entidad Trabajador.
     */
    @ManyToOne
    @JoinColumn(name = "trabajador_id")
    private Trabajador trabajador;

    /**
     * Obtiene el identificador único de la justificación.
     *
     * @return el identificador de la justificación
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el identificador único de la justificación.
     *
     * @param id el identificador de la justificación
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene la fecha en la que se registró la justificación.
     *
     * @return la fecha de la justificación
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha en la que se registró la justificación.
     *
     * @param fecha la fecha de la justificación
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Obtiene el tipo de la justificación (por ejemplo, permiso, enfermedad,
     * etc.).
     *
     * @return el tipo de la justificación
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Establece el tipo de la justificación.
     *
     * @param tipo el tipo de la justificación
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene la descripción detallada de la justificación.
     *
     * @return la descripción de la justificación
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción detallada de la justificación.
     *
     * @param descripcion la descripción de la justificación
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el estado de la justificación (aprobada, rechazada, pendiente,
     * etc.).
     *
     * @return el estado de la justificación
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado de la justificación.
     *
     * @param estado el estado de la justificación
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene la fecha en la que la justificación fue aprobada, si corresponde.
     *
     * @return la fecha de aprobación de la justificación
     */
    public LocalDate getFechaaprobada() {
        return fechaaprobada;
    }

    /**
     * Establece la fecha en la que la justificación fue aprobada, si
     * corresponde.
     *
     * @param fechaaprobada la fecha de aprobación de la justificación
     */
    public void setFechaaprobada(LocalDate fechaaprobada) {
        this.fechaaprobada = fechaaprobada;
    }

    /**
     * Obtiene la fecha en la que la justificación fue rechazada, si
     * corresponde.
     *
     * @return la fecha de rechazo de la justificación
     */
    public LocalDate getFecharechazada() {
        return fecharechazada;
    }

    /**
     * Establece la fecha en la que la justificación fue rechazada, si
     * corresponde.
     *
     * @param fecharechazada la fecha de rechazo de la justificación
     */
    public void setFecharechazada(LocalDate fecharechazada) {
        this.fecharechazada = fecharechazada;
    }

    /**
     * Obtiene el motivo de la justificación, proporcionando detalles
     * adicionales.
     *
     * @return el motivo de la justificación
     */
    public String getMotivo() {
        return motivo;
    }

    /**
     * Establece el motivo de la justificación, proporcionando detalles
     * adicionales.
     *
     * @param motivo el motivo de la justificación
     */
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    /**
     * Obtiene la evidencia asociada a la justificación.
     *
     * @return la evidencia asociada a la justificación
     */
    public byte[] getEvidencia() {
        return evidencia;
    }

    /**
     * Establece la evidencia asociada a la justificación.
     *
     * @param evidencia la evidencia asociada a la justificación
     */
    public void setEvidencia(byte[] evidencia) {
        this.evidencia = evidencia;
    }

    /**
     * Obtiene la asistencia asociada a esta justificación.
     *
     * @return la asistencia relacionada con la justificación
     */
    public Asistencia getAsistencia() {
        return asistencia;
    }

    /**
     * Establece la asistencia asociada a esta justificación.
     *
     * @param asistencia la asistencia asociada a la justificación
     */
    public void setAsistencia(Asistencia asistencia) {
        this.asistencia = asistencia;
    }

    /**
     * Obtiene el trabajador que solicitó la justificación.
     *
     * @return el trabajador asociado con la justificación
     */
    public Trabajador getTrabajador() {
        return trabajador;
    }

    /**
     * Establece el trabajador que solicitó la justificación.
     *
     * @param trabajador el trabajador asociado con la justificación
     */
    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    /**
     * Obtiene la fecha y hora de creación de la justificación.
     *
     * @return la fecha y hora de creación de la justificación
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha y hora de creación de la justificación.
     *
     * @param createdAt la fecha y hora de creación de la justificación
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
