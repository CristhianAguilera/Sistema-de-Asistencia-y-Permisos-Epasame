/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.service;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Servicio encargado de enviar correos electrónicos y verificar la validez de
 * correos electrónicos.
 *
 * @author Cristhian Aguilera
 */
@Service
public class EmailService {

    public EmailService() {
    }
    
    

    @Autowired
    private JavaMailSender mailSender;  // Enviar correos electrónicos

    private static final String API_URL = "https://api.hunter.io/v2/email-verifier"; // URL de la API para verificación de correo
    private static final String API_KEY = "c9dbb60f747e407547ebcca0c21d1e5f4fe0f7aa"; // Clave API para acceso a la API

    /**
     * Verifica si un correo electrónico es válido utilizando la API de Hunter.
     * La verificación se realiza para determinar si el correo electrónico es
     * "entregable" según la API.
     *
     * @param correo El correo electrónico a verificar.
     * @return true si el correo electrónico es válido y entregable, false si no
     * lo es o si ocurre un error.
     */
    public boolean verificarCorreo(String correo) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = API_URL + "?email=" + correo + "&api_key=" + API_KEY;  // URL con parámetros para la API

            // Realiza la solicitud GET a la API y obtiene la respuesta
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            Map<String, Object> body = response.getBody();

            // Verifica si el correo es "entregable"
            return body != null && body.get("data") != null
                    && "deliverable".equals(((Map<String, Object>) body.get("data")).get("result"));
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Si ocurre algún error, se asume que el correo no es válido
        }
    }

    /**
     * Envía un correo electrónico a un destinatario con un asunto y contenido
     * especificado.
     *
     * @param destinatario El correo electrónico del destinatario.
     * @param asunto El asunto del correo.
     * @param contenido El contenido del correo.
     */
    public void enviarCorreo(String destinatario, String asunto, String contenido) {
        // Crea un objeto SimpleMailMessage para el correo
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);  // Establece el destinatario
        mensaje.setSubject(asunto);  // Establece el asunto
        mensaje.setText(contenido);  // Establece el contenido
        mensaje.setFrom("epasame29@gmail.com");  // Establece la dirección de correo remitente

        // Envía el correo utilizando el mailSender
        mailSender.send(mensaje);
    }
}
