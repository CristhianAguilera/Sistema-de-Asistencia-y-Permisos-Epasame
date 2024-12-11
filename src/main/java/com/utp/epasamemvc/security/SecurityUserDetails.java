/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.security;

import com.utp.epasamemvc.model.Trabajador;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementación personalizada de {@link UserDetails} para representar los
 * detalles de seguridad de un trabajador. Esta clase se utiliza para integrar
 * la autenticación de un trabajador dentro del sistema de seguridad de Spring
 * Security.
 *
 * @author Cristhian Aguilera
 */
public class SecurityUserDetails implements UserDetails {

    // El trabajador asociado con los detalles de seguridad
    private final Trabajador trabajador;

    /**
     * Constructor que inicializa la instancia con un trabajador.
     *
     * @param trabajador el trabajador asociado con los detalles de seguridad
     */
    public SecurityUserDetails(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    /**
     * Obtiene las autoridades (roles) del trabajador. El rol del trabajador se
     * convierte en una autoridad con prefijo "ROLE_" para ser compatible con
     * Spring Security.
     *
     * @return una colección de las autoridades (roles) del trabajador
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Agregar el rol del trabajador como una autoridad
        authorities.add(new SimpleGrantedAuthority("ROLE_" + trabajador.getRol()));

        return authorities;
    }

    /**
     * Obtiene la contraseña del trabajador.
     *
     * @return la contraseña del trabajador
     */
    @Override
    public String getPassword() {
        return trabajador.getContraseña();
    }

    /**
     * Obtiene el nombre de usuario del trabajador, que en este caso es su
     * correo electrónico.
     *
     * @return el correo electrónico del trabajador
     */
    @Override
    public String getUsername() {
        return trabajador.getCorreo();
    }

    /**
     * Verifica si la cuenta del trabajador no ha expirado. La cuenta se
     * considera no expirada si el trabajador tiene el estado "Activo".
     *
     * @return true si la cuenta no ha expirado, false en caso contrario
     */
    @Override
    public boolean isAccountNonExpired() {
        return trabajador.getEstado().equalsIgnoreCase("Activo");
    }

    /**
     * Verifica si la cuenta del trabajador no está bloqueada. La cuenta se
     * considera no bloqueada si el trabajador tiene el estado "Activo".
     *
     * @return true si la cuenta no está bloqueada, false en caso contrario
     */
    @Override
    public boolean isAccountNonLocked() {
        return trabajador.getEstado().equalsIgnoreCase("Activo");
    }

    /**
     * Verifica si las credenciales del trabajador no han expirado. En este
     * caso, se considera que las credenciales nunca expiran.
     *
     * @return true, ya que las credenciales no tienen fecha de expiración
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Verifica si la cuenta del trabajador está habilitada. La cuenta se
     * considera habilitada si el trabajador tiene el estado "Activo".
     *
     * @return true si la cuenta está habilitada, false en caso contrario
     */
    @Override
    public boolean isEnabled() {
        return trabajador.getEstado().equalsIgnoreCase("Activo");
    }

    /**
     * Obtiene el trabajador asociado con los detalles de seguridad. Este método
     * es opcional y permite acceder a toda la información del trabajador si es
     * necesario.
     *
     * @return el trabajador asociado con los detalles de seguridad
     */
    public Trabajador getTrabajador() {
        return trabajador;
    }
}
