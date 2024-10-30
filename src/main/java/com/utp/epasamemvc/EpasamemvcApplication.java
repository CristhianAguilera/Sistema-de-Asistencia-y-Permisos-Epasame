package com.utp.epasamemvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación Spring Boot para Epasamemvc. Esta clase se
 * encarga de iniciar la aplicación utilizando Spring Boot.
 */
@SpringBootApplication
public class EpasamemvcApplication {

    /**
     * Crea una nueva instancia de EpasamemvcApplication.
     */
    public EpasamemvcApplication() {
    }
    

    /**
     * Método principal de la aplicación. Este método se ejecuta al iniciar la
     * aplicación, llamando a SpringApplication.run que lanza el contexto de la
     * aplicación Spring Boot.
     * 
     * @param args Argumentos de línea de comandos para personalizar el inicio de la aplicación.
     */
    public static void main(String[] args) {
        SpringApplication.run(EpasamemvcApplication.class, args);
    }

}
