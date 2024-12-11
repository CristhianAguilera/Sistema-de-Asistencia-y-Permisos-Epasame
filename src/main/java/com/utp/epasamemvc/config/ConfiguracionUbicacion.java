/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.config;

/**
 * Clase ConfiguracionUbicacion que almacena constantes relacionadas con la
 * ubicación y el rango permitido para la asistencia.
 *
 * @author Cristhian Aguilera
 */
public class ConfiguracionUbicacion {

    public ConfiguracionUbicacion() {
    }

    /**
     * Latitud de la ubicación central para la validación de asistencia.
     */
    public static final double UBICACION_LATITUD = -5.224747;
    
    /**
     * Longitud de la ubicación central para la validación de asistencia.
     */
    public static final double UBICACION_LONGITUD = -80.630393;
    
    
    /**
     * Rango de tolerancia (en metros) para la asistencia en relación a la
     * ubicación central.
     */
    public static final double RANGO_ASISTENCIA = 100.0;
}
