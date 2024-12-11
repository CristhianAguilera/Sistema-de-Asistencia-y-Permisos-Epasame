/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.service;

import java.time.ZonedDateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Servicio que obtiene la hora actual del servidor utilizando una API externa.
 * En caso de error, se devuelve la hora local del servidor.
 *
 * @author Cristhian Aguilera
 */
@Service
public class HoraServidorService {

    public HoraServidorService() {
    }


    // URL de la API externa que proporciona la hora actual
    private static final String API_URL = "http://worldtimeapi.org/api/timezone/America/Lima";

    /**
     * Obtiene la hora actual del servidor desde una API externa (WorldTimeAPI).
     * Si ocurre algún error durante la solicitud, se devuelve la hora local
     * como respaldo.
     *
     * @return La hora actual del servidor en formato ZonedDateTime.
     */
    public ZonedDateTime obtenerHoraDelServidor() {
        RestTemplate restTemplate = new RestTemplate();  // Instancia de RestTemplate para hacer solicitudes HTTP
        try {
            // Consumimos el API de WorldTimeAPI para obtener la hora actual en Lima
            WorldTimeResponse response = restTemplate.getForObject(API_URL, WorldTimeResponse.class);

            // Verificamos si la respuesta es válida y contiene la fecha y hora
            if (response != null && response.getDatetime() != null) {
                // Parseamos la fecha y hora recibida desde el API
                return ZonedDateTime.parse(response.getDatetime());
            }
        } catch (Exception e) {
            e.printStackTrace();  // Imprime el error en caso de fallo
        }
        // Si ocurre un error, devolvemos la hora local como respaldo
        return ZonedDateTime.now();
    }

    /**
     * Clase interna que mapea la respuesta de la API WorldTimeAPI. Se utiliza
     * para extraer el campo de la fecha y hora.
     */
    private static class WorldTimeResponse {

        private String datetime;  // Campo que almacena la fecha y hora en formato string

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }
    }
}
