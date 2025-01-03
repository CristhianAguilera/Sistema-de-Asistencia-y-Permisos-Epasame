/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

/**
 * La clase Trabajador representa a un empleado en el sistema de gestión de
 * trabajadores, incluyendo sus datos personales, cargo, estado, rol, y las
 * relaciones con las entidades Asistencia y Permiso.
 * <p>
 * Un trabajador puede tener múltiples asistencias y permisos asociados.
 * </p>
 *
 * @author Cristhian Aguilera
 */
@Entity
@Table(name = "trabajadores")
public class Trabajador {

    /**
     * Crea una nueva instancia del trabajador.
     */
    public Trabajador() {
    }

    /**
     * Identificador único del trabajador.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Nombres del trabajador.
     */
    private String nombres;

    /**
     * Apellidos del trabajador.
     */
    private String apellidos;

    /**
     * Cargo del trabajador dentro de la empresa.
     */
    private String cargo;

    /**
     * Rol del trabajador, como "Administrador" o "Empleado".
     */
    private String rol;

    /**
     * Estado actual del trabajador, como "Activo" o "Inactivo".
     */
    private String estado;

    /**
     * Tipo de documento de identidad del trabajador.
     */
    private String tipodocumento;

    /**
     * Número de documento de identidad del trabajador.
     */
    private String ndocumento;

    /**
     * Número de teléfono de contacto del trabajador.
     */
    private String telefono;

    /**
     * Correo electrónico del trabajador.
     */
    private String correo;

    /**
     * Contraseña del trabajador para autenticación.
     */
    private String contraseña;

    /**
     * Fecha de ingreso del trabajador a la empresa.
     */
    private Date fechaentrada;

    /**
     * Número de días de permiso disponibles para el trabajador.
     */
    private int diaspermiso;

    /**
     * Estado diario del trabajador, como "Presente", "Ausente" o "Vacaciones".
     */
    private String estadodiario;

    /**
     * Indica si el trabajador tiene una solicitud pendiente de permiso.
     */
    private boolean tieneSolicitud;

    /**
     * Indica si el trabajador tiene una justificación pendiente.
     */
    private boolean tienejustificacion;

    /**
     * Fecha y hora de creación del registro del trabajador.
     */
    private LocalDateTime createdAt;

    /**
     * Lista de asistencias registradas asociadas al trabajador.
     */
    @OneToMany(mappedBy = "trabajador")
    private List<Asistencia> asistencias;

    /**
     * Lista de permisos asignados al trabajador.
     */
    @OneToMany(mappedBy = "trabajador")
    private List<Permiso> permisos;

    /**
     * Lista de justificaciones relacionadas con el trabajador.
     */
    @OneToMany(mappedBy = "trabajador")
    private List<Justificacion> justificaciones;

    /**
     * Obtiene el ID del trabajador.
     *
     * @return el ID del trabajador.
     */
    public Integer getId() {
        return id;
    }

    /**
     * Establece el ID del trabajador.
     *
     * @param id el ID del trabajador a establecer.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Obtiene los nombres del trabajador.
     *
     * @return los nombres del trabajador.
     */
    public String getNombres() {
        return nombres;
    }

    /**
     * Establece los nombres del trabajador.
     *
     * @param nombres los nombres del trabajador a establecer.
     */
    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    /**
     * Obtiene los apellidos del trabajador.
     *
     * @return los apellidos del trabajador.
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Establece los apellidos del trabajador.
     *
     * @param apellidos los apellidos del trabajador a establecer.
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Obtiene el cargo del trabajador.
     *
     * @return el cargo del trabajador.
     */
    public String getCargo() {
        return cargo;
    }

    /**
     * Establece el cargo del trabajador.
     *
     * @param cargo el cargo del trabajador a establecer.
     */
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    /**
     * Obtiene el rol del trabajador.
     *
     * @return el rol del trabajador.
     */
    public String getRol() {
        return rol;
    }

    /**
     * Establece el rol del trabajador.
     *
     * @param rol el rol del trabajador a establecer.
     */
    public void setRol(String rol) {
        this.rol = rol;
    }

    /**
     * Obtiene el estado del trabajador.
     *
     * @return el estado del trabajador.
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Establece el estado del trabajador.
     *
     * @param estado el estado del trabajador a establecer.
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Obtiene el tipo de documento del trabajador.
     *
     * @return el tipo de documento del trabajador.
     */
    public String getTipodocumento() {
        return tipodocumento;
    }

    /**
     * Establece el tipo de documento del trabajador.
     *
     * @param tipodocumento el tipo de documento a establecer.
     */
    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    /**
     * Obtiene el número de documento del trabajador.
     *
     * @return el número de documento del trabajador.
     */
    public String getNdocumento() {
        return ndocumento;
    }

    /**
     * Establece el número de documento del trabajador.
     *
     * @param ndocumento el número de documento a establecer.
     */
    public void setNdocumento(String ndocumento) {
        this.ndocumento = ndocumento;
    }

    /**
     * Obtiene el teléfono del trabajador.
     *
     * @return el teléfono del trabajador.
     */
    public String getTelefono() {
        return telefono;
    }

    /**
     * Establece el teléfono del trabajador.
     *
     * @param telefono el teléfono a establecer.
     */
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    /**
     * Obtiene el correo electrónico del trabajador.
     *
     * @return el correo electrónico del trabajador.
     */
    public String getCorreo() {
        return correo;
    }

    /**
     * Establece el correo electrónico del trabajador.
     *
     * @param correo el correo electrónico a establecer.
     */
    public void setCorreo(String correo) {
        this.correo = correo;
    }

    /**
     * Obtiene la contraseña del trabajador.
     *
     * @return la contraseña del trabajador.
     */
    public String getContraseña() {
        return contraseña;
    }

    /**
     * Establece la contraseña del trabajador.
     *
     * @param contraseña la contraseña a establecer.
     */
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    /**
     * Obtiene la fecha de entrada del trabajador a la empresa.
     *
     * @return la fecha de entrada del trabajador.
     */
    public Date getFechaentrada() {
        return fechaentrada;
    }

    /**
     * Establece la fecha de entrada del trabajador a la empresa.
     *
     * @param fechaentrada la fecha de entrada a establecer.
     */
    public void setFechaentrada(Date fechaentrada) {
        this.fechaentrada = fechaentrada;
    }

    /**
     * Obtiene la lista de asistencias del trabajador.
     *
     * @return la lista de asistencias.
     */
    public List<Asistencia> getAsistencias() {
        return asistencias;
    }

    /**
     * Establece la lista de asistencias del trabajador.
     *
     * @param asistencias la lista de asistencias a establecer.
     */
    public void setAsistencias(List<Asistencia> asistencias) {
        this.asistencias = asistencias;
    }

    /**
     * Obtiene la lista de permisos del trabajador.
     *
     * @return la lista de permisos.
     */
    public List<Permiso> getPermisos() {
        return permisos;
    }

    /**
     * Establece la lista de permisos del trabajador.
     *
     * @param permisos la lista de permisos a establecer.
     */
    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }

    /**
     * Obtiene los días de permiso del trabajador.
     *
     * @return los días de permiso.
     */
    public int getDiaspermiso() {
        return diaspermiso;
    }

    /**
     * Establece los días de permiso del trabajador.
     *
     * @param diaspermiso los días de permiso a establecer.
     */
    public void setDiaspermiso(int diaspermiso) {
        this.diaspermiso = diaspermiso;
    }

    /**
     * Obtiene el estado diario del trabajador.
     *
     * @return el estado diario del trabajador.
     */
    public String getEstadodiario() {
        return estadodiario;
    }

    /**
     * Establece el estado diario del trabajador.
     *
     * @param estadodiario el estado diario a establecer.
     */
    public void setEstadodiario(String estadodiario) {
        this.estadodiario = estadodiario;
    }

    /**
     * Verifica si el trabajador tiene una solicitud pendiente.
     *
     * @return true si tiene una solicitud, false en caso contrario.
     */
    public boolean isTieneSolicitud() {
        return tieneSolicitud;
    }

    /**
     * Establece si el trabajador tiene una solicitud pendiente.
     *
     * @param tieneSolicitud true si tiene una solicitud, false en caso
     * contrario.
     */
    public void setTieneSolicitud(boolean tieneSolicitud) {
        this.tieneSolicitud = tieneSolicitud;
    }

    /**
     * Indica si el trabajador tiene una justificación asociada. Este método
     * devuelve un valor booleano que indica si el trabajador tiene alguna
     * justificación registrada en el sistema.
     *
     * @return true si el trabajador tiene una justificación, false en caso
     * contrario
     */
    public boolean isTienejustificacion() {
        return tienejustificacion;
    }

    /**
     * Obtiene la fecha y hora en la que fue creado el registro. Este método
     * devuelve la fecha y hora de creación del registro de justificación o
     * asistencia.
     *
     * @return la fecha y hora de creación del registro
     */
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    /**
     * Establece la fecha y hora en la que fue creado el registro. Este método
     * permite asignar la fecha y hora en la que se registró la justificación o
     * asistencia en el sistema.
     *
     * @param createdAt la fecha y hora de creación del registro
     */
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Establece si el trabajador tiene una justificación asociada. Este método
     * asigna un valor booleano que indica si el trabajador tiene alguna
     * justificación registrada en el sistema.
     *
     * @param tienejustificacion valor booleano que indica si el trabajador
     * tiene justificación
     */
    public void setTienejustificacion(boolean tienejustificacion) {
        this.tienejustificacion = tienejustificacion;
    }

    /**
     * Obtiene la lista de justificaciones asociadas al trabajador. Este método
     * devuelve una lista de objetos de tipo Justificación que están asociadas a
     * un trabajador.
     *
     * @return la lista de justificaciones del trabajador
     */
    public List<Justificacion> getJustificaciones() {
        return justificaciones;
    }

    /**
     * Establece la lista de justificaciones asociadas al trabajador. Este
     * método permite asignar una lista de objetos Justificación a un
     * trabajador.
     *
     * @param justificaciones la lista de justificaciones del trabajador
     */
    public void setJustificaciones(List<Justificacion> justificaciones) {
        this.justificaciones = justificaciones;
    }

}
