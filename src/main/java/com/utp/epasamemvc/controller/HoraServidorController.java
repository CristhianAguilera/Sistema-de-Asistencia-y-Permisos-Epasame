/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.controller;

import com.utp.epasamemvc.service.HoraServidorService;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST que maneja la obtención de la hora del servidor.
 *
 * Este controlador expone un endpoint para consultar la hora actual del
 * servidor en formato ISO 8601. Utiliza el servicio `HoraServidorService` para
 * obtener la hora y devolverla en una respuesta HTTP.
 *
 * @author Cristhian Aguilera
 */
@RestController
@RequestMapping("/api/hora")
public class HoraServidorController {

    /**
     * Servicio encargado de gestionar la hora del servidor
     */
    @Autowired
    private HoraServidorService horaServidorService;

    
    /**
     * Endpoint que retorna la hora actual del servidor.
     * 
     * Este método maneja las solicitudes GET en el endpoint "/api/hora" y devuelve la hora del servidor
     * en formato ISO 8601 dentro de un mapa JSON.
     * 
     * @return ResponseEntity con un mapa que contiene la hora del servidor en formato ISO 8601.
     */
    @GetMapping
    public ResponseEntity<Map<String, String>> obtenerHoraDelServidor() {
        ZonedDateTime horaServidor = horaServidorService.obtenerHoraDelServidor();
        Map<String, String> response = new HashMap<>();
        response.put("horaServidor", horaServidor.toString());
        return ResponseEntity.ok(response);
    }
}
