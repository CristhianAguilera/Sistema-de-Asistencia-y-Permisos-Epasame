/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Clase de configuración para el manejo de recursos estáticos en la aplicación.
 * Implementa la interfaz {@link WebMvcConfigurer} para personalizar la
 * configuración de Spring MVC.
 * <p>
 * Esta clase permite la configuración de rutas de acceso para recursos
 * estáticos como archivos subidos, estilos CSS, e imágenes.</p>
 *
 * @author Cristhian Aguilera
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Crea una nueva instancia del WebConfig.
     */
    public WebConfig() {
    }
    
    /**
     * Configura los manejadores de recursos estáticos. Se definen rutas
     * específicas para acceder a los directorios de recursos como "/uploads",
     * "/css" y "/image", para que estos estén disponibles desde la web.
     *
     * @param registry Registro de manejadores de recursos donde se agregan las
     * rutas personalizadas.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        registry.addResourceHandler("/css/**")
                .addResourceLocations("file:css/");

        registry.addResourceHandler("/image/**")
                .addResourceLocations("file:image/");
    }

}
