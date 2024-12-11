/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.config;

import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.service.TrabajadorService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Configuración de seguridad para la aplicación.
 * <p>
 * Proporciona la configuración necesaria para la autenticación y la
 * autorización en la aplicación, utilizando Spring Security.
 * </p>
 *
 * @author Cristhian Aguilera
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    public SecurityConfiguration() {
    }

    /**
     * Servicio para gestionar trabajadores.
     */
    @Autowired
    private TrabajadorService trabajadorservice;

    /**
     * Bean para codificar contraseñas usando BCrypt.
     *
     * @return instancia de BCryptPasswordEncoder.
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configuración del filtro de seguridad para definir las reglas de acceso y
     * los endpoints protegidos.
     *
     * @param http objeto HttpSecurity para configurar la seguridad web.
     * @return la cadena de filtros de seguridad configurada.
     * @throws Exception si ocurre un error en la configuración.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/login", "/css/**",
                        "/js/**", "/image/**", "/error").permitAll()
                .requestMatchers("/PrincipalAdmin/**", "/AsistenciasAdmin/**",
                        "/PermisosAdmin/**", "/EmpleadosAdmin/**",
                        "/Reportes/**", "/VerPermisosAdmin/**", "/JustificacionesAdmin/**",
                        "/VerJustificacionesAdmin/**", "/ConfiguracionAdmin/**").hasRole("Administrador")
                .requestMatchers("/PrincipalTrabajador/**",
                        "/VerPermisosTrabajador/**", "/PermisosTrabajador/**", "/JustificacionesTrabajador/**",
                        "/VerJustificacionesTrabajador/**").hasRole("Trabajador")
                .anyRequest().authenticated()
                )
                .formLogin(form -> form
                .loginPage("/")
                .failureUrl("/?error=true")
                .loginProcessingUrl("/login")
                .usernameParameter("correoelectronico")
                .passwordParameter("contrasena")
                .successHandler(new CustomAuthenticationSuccessHandler())
                .permitAll()
                )
                .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
                );

        return http.build();

    }

    /**
     * Clase interna para manejar el éxito de autenticación personalizada.
     */
    @Component
    public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

        /**
         * Maneja el evento de éxito en la autenticación.
         *
         * @param request la solicitud HTTP.
         * @param response la respuesta HTTP.
         * @param authentication objeto de autenticación.
         * @throws IOException si ocurre un error de entrada/salida.
         * @throws ServletException si ocurre un error relacionado con el
         * servlet.
         */
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                Authentication authentication) throws IOException, ServletException {
            // Obtener el nombre de usuario (correo electrónico) del autenticado
            String correo = authentication.getName();

            // Buscar el trabajador por correo
            Trabajador trabajador = trabajadorservice.findByCorreo(correo);

            // Si el trabajador se encuentra, guardarlo en la sesión
            if (trabajador != null) {
                // Guardar el trabajador en la sesión
                HttpSession session = request.getSession();
                session.setAttribute("trabajador", trabajador);

            }

            // Redirigir según el rol del trabajador
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {

                if (authority.getAuthority().equals("ROLE_Administrador")) {
                    response.sendRedirect("/PrincipalAdmin");
                    return;
                } else if (authority.getAuthority().equals("ROLE_Trabajador")) {
                    response.sendRedirect("/PrincipalTrabajador");
                    return;
                }
            }

            // Redirigir al principal si no se encuentra un rol adecuado
            response.sendRedirect("/");
        }
    }
}
