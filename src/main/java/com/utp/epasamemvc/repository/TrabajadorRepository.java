/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.utp.epasamemvc.repository;

import com.utp.epasamemvc.model.Trabajador;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.*;

/**
 * Repositorio para manejar las operaciones de acceso a datos de la entidad
 * Trabajador.
 * <p>
 * Esta interfaz extiende JpaRepository, lo que permite realizar operaciones
 * CRUD sin necesidad de implementar métodos adicionales.
 * </p>
 *
 * @author Cristhian Aguilera
 */
public interface TrabajadorRepository extends JpaRepository<Trabajador, Long> {

    /**
     * Encuentra todos los trabajadores registrados.
     *
     * @return una lista que contiene todos los trabajadores almacenados en la
     * base de datos.
     */
    @Override
    List<Trabajador> findAll();

    /**
     * Encuentra un trabajador a partir de su correo electrónico.
     *
     * @param correo el correo electrónico del trabajador que se desea buscar.
     * @return un objeto Trabajador que corresponde al correo electrónico
     * especificado. Si no se encuentra ningún trabajador, el método puede
     * retornar null.
     */
    Trabajador findByCorreo(String correo);

    /**
     * Encuentra todos los trabajadores que tienen un estado específico.
     *
     * @param estado el estado que se desea filtrar (por ejemplo: "Activo",
     * "Inactivo").
     * @return una lista que contiene los trabajadores que coinciden con el
     * estado especificado.
     */
    List<Trabajador> findByEstado(String estado);

    /**
     * Encuentra trabajadores con un estado específico y los retorna paginados.
     *
     * @param estado el estado de los trabajadores que se desea filtrar.
     * @param p el objeto Pageable que contiene la configuración de paginación.
     * @return una página que contiene los trabajadores que coinciden con el
     * estado especificado.
     */
    Page<Trabajador> findByEstado(String estado, Pageable p);

    /**
     * Encuentra todos los trabajadores que tienen un rol específico.
     *
     * @param rol el rol que se desea filtrar (por ejemplo: "Administrador",
     * "Empleado").
     * @return una lista que contiene los trabajadores que coinciden con el rol
     * especificado.
     */
    List<Trabajador> findByRol(String rol);

    /**
     * Encuentra trabajadores con un rol específico y los retorna paginados.
     *
     * @param rol el rol que se desea filtrar.
     * @param p el objeto Pageable que contiene la configuración de paginación.
     * @return una página que contiene los trabajadores que coinciden con el rol
     * especificado.
     */
    Page<Trabajador> findByRol(String rol, Pageable p);

    /**
     * Encuentra todos los trabajadores por su número de documento.
     *
     * @param ndocumento el número de documento que se desea buscar.
     * @return una lista que contiene los trabajadores que coinciden con el
     * número de documento especificado.
     */
    List<Trabajador> findByNdocumento(String ndocumento);

    /**
     * Encuentra todos los trabajadores por su número de teléfono.
     *
     * @param telefono el número de teléfono que se desea buscar.
     * @return una lista que contiene los trabajadores que coinciden con el
     * número de teléfono especificado.
     */
    List<Trabajador> findByTelefono(String telefono);

    /**
     * Encuentra todos los trabajadores por su contraseña.
     *
     * @param contrasena la contraseña que se desea buscar.
     * @return una lista que contiene los trabajadores que coinciden con la
     * contraseña especificada. Nota: Usar este método con precaución por temas
     * de seguridad.
     */
    List<Trabajador> findByContraseña(String contrasena);

    /**
     * Encuentra un trabajador por su ID único.
     *
     * @param Trabajadorid el identificador único del trabajador que se desea
     * buscar.
     * @return un objeto Trabajador correspondiente al ID especificado. Si no se
     * encuentra ningún trabajador, el método puede retornar null.
     */
    Trabajador findById(Integer Trabajadorid);

}
