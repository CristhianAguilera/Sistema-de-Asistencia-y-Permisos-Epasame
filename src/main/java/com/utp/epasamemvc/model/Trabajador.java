/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.model;

import jakarta.persistence.*;
import java.sql.Date;
import java.util.List;

/**
 *
 * @author ADVANCE
 */
@Entity
@Table(name = "trabajadores")
public class Trabajador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombres;
    private String apellidos;
    private String cargo;
    private String rol;
    private String estado; //Activo o Inactivo
    private String tipodocumento;
    private String ndocumento;
    private String telefono;
    private String correo;
    private String contraseña;
    private Date fechaentrada;
    private int diaspermiso;
    private String estadodiario;
    private boolean tieneSolicitud;

    @OneToMany(mappedBy = "trabajador")
    private List<Asistencia> asistencias;
    
    @OneToMany(mappedBy = "trabajador")
    private List<Permiso> permisos;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipodocumento() {
        return tipodocumento;
    }

    public void setTipodocumento(String tipodocumento) {
        this.tipodocumento = tipodocumento;
    }

    public String getNdocumento() {
        return ndocumento;
    }

    public void setNdocumento(String ndocumento) {
        this.ndocumento = ndocumento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public Date getFechaentrada() {
        return fechaentrada;
    }

    public void setFechaentrada(Date fechaentrada) {
        this.fechaentrada = fechaentrada;
    }

    public List<Asistencia> getAsistencias() {
        return asistencias;
    }

    public void setAsistencias(List<Asistencia> asistencias) {
        this.asistencias = asistencias;
    }

    public List<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(List<Permiso> permisos) {
        this.permisos = permisos;
    }

    public int getDiaspermiso() {
        return diaspermiso;
    }

    public void setDiaspermiso(int diaspermiso) {
        this.diaspermiso = diaspermiso;
    }

    public String getEstadodiario() {
        return estadodiario;
    }

    public void setEstadodiario(String estadodiario) {
        this.estadodiario = estadodiario;
    }

    public boolean isTieneSolicitud() {
        return tieneSolicitud;
    }

    public void setTieneSolicitud(boolean tieneSolicitud) {
        this.tieneSolicitud = tieneSolicitud;
    }
    
}
