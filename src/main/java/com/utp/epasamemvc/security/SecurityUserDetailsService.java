/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.security;

import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.repository.TrabajadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Servicio de detalles de usuario personalizado para Spring Security. Esta
 * clase se encarga de cargar los detalles de un trabajador basado en su correo
 * electrónico durante el proceso de autenticación. Implementa la interfaz
 * {@link UserDetailsService} de Spring Security.
 *
 * @author Cristhian Aguilera
 */
@Service
public class SecurityUserDetailsService implements UserDetailsService {

    public SecurityUserDetailsService() {
    }

    @Autowired
    private TrabajadorRepository trabajadorRepository;

    /**
     * Carga los detalles del trabajador basándose en su correo electrónico. Si
     * no se encuentra un trabajador con el correo proporcionado, se lanza una
     * excepción {@link UsernameNotFoundException}.
     *
     * @param correo el correo electrónico del trabajador a autenticar
     * @return un objeto {@link UserDetails} que contiene los detalles del
     * trabajador si se encuentra
     * @throws UsernameNotFoundException si no se encuentra un trabajador con el
     * correo proporcionado
     */
    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        System.out.println("Intentando autenticar trabajador con correo: " + correo);

        // Buscar trabajador en la base de datos usando el correo electrónico
        Trabajador trabajador = trabajadorRepository.findByCorreo(correo);

        if (trabajador == null) {
            System.out.println("Trabajador no encontrado con correo: " + correo);
            throw new UsernameNotFoundException("Trabajador no encontrado con correo: " + correo);
        }

        System.out.println("Trabajador encontrado: " + correo);
        // Retorna un objeto SecurityUserDetails con los detalles del trabajador
        return new SecurityUserDetails(trabajador);
    }
}
