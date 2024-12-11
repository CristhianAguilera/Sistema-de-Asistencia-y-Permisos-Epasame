/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.utp.epasamemvc.controller;

import com.utp.epasamemvc.model.Trabajador;
import com.utp.epasamemvc.service.EmailService;
import com.utp.epasamemvc.service.TrabajadorService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpSession;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador que gestiona el registro, consulta y actualización de los
 * empleados. Proporciona rutas para acceder a la vista de administración,
 * registrar empleados, obtener datos de un trabajador específico y actualizar
 * la información de un trabajador.
 *
 * <p>
 * Incluye validaciones para evitar duplicidad de datos y manejo de excepciones
 * para garantizar una respuesta adecuada en caso de errores.</p>
 *
 * @author Cristhian Aguilera
 */
@Controller
public class EmpleadosController {

    /**
     * Crea una nueva instancia del controlador de permisos
     */
    public EmpleadosController() {
    }

    /**
     * Codificador de contraseñas utilizado para encriptar y validar las
     * contraseñas de los trabajadores.
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Servicio para gestionar las operaciones relacionadas con los
     * trabajadores.
     */
    @Autowired
    private TrabajadorService trabajadorservice;

    /**
     * Servicio encargado de gestionar el envío de correos electrónicos en el
     * sistema.
     */
    @Autowired
    private EmailService emailService;

    /**
     * Maneja la carga y visualización de los empleados para el administrador.
     *
     * Este método obtiene una lista paginada de trabajadores con el rol de
     * "Trabajador" y la agrega al modelo para que pueda ser visualizada en la
     * vista correspondiente. Además, obtiene los datos del trabajador
     * actualmente autenticado desde la sesión y los agrega al modelo.
     *
     * @param model El modelo que contiene los atributos a pasar a la vista.
     * @param session La sesión HTTP que contiene la información del trabajador
     * autenticado.
     * @param page El número de página para la paginación de los trabajadores
     * (por defecto es 0).
     * @return El nombre de la vista a mostrar, en este caso "EmpleadosAdmin".
     */
    @GetMapping("/EmpleadosAdmin")
    public String CargaEmpleados(Model model, HttpSession session, @RequestParam(defaultValue = "0") int page) {
        Page<Trabajador> trabajadoresPage = trabajadorservice.findByRolPage("Trabajador", PageRequest.of(page, 5));
        model.addAttribute("trabajadores", trabajadoresPage);

        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");
        model.addAttribute("trabajador", trabajador);
        return "EmpleadosAdmin";
    }

    /**
     * Permite el acceso a la vista principal del administrador.
     *
     * @param model El modelo que contiene los atributos a pasar a la vista.
     * @param session La sesión HTTP que contiene la información del trabajador
     * autenticado.
     * @return La vista "PrincipalAdmin".
     */
    @GetMapping("/PrincipalAdmin")
    public String MuestraVistaPrincipalAdmin(Model model, HttpSession session) {
        Trabajador trabajador = (Trabajador) session.getAttribute("trabajador");
        model.addAttribute("trabajador", trabajador);
        return "PrincipalAdmin";
    }

    /**
     * Registra un nuevo trabajador en el sistema, verificando que no haya datos
     * duplicados.
     *
     * @param nombres Nombres del trabajador.
     * @param apellidos Apellidos del trabajador.
     * @param correo Correo electrónico del trabajador.
     * @param telefono Número de teléfono del trabajador.
     * @param numeroDocumento Número de documento del trabajador.
     * @param FechaEntrada Fecha de entrada del trabajador.
     * @param contrasena Contraseña del trabajador.
     * @param cargo Cargo del trabajador.
     * @param tipoDocumento Tipo de documento del trabajador.
     * @return Redirección a la vista "PrincipalAdmin" con un mensaje de éxito o
     * error.
     */
    @PostMapping("/registertrabajador")
    @ResponseBody
    public Map<String, Object> registratrabajador(
            @RequestParam("Nombres") String nombres,//Recibimos los parametros a registrar
            @RequestParam("Apellidos") String apellidos,
            @RequestParam("Correo") String correo,
            @RequestParam("Telefono") String telefono,
            @RequestParam("NDocumento") String numeroDocumento,
            @RequestParam("FechaEntrada") String FechaEntrada,
            @RequestParam("contrasena") String contrasena,
            @RequestParam("cargo") String cargo,
            @RequestParam("tipoDocumento") String tipoDocumento) {

        Map<String, Object> response = new HashMap<>();
        try {

            if (!telefono.matches("\\d{9}")) {
                response.put("error", "Verifique el telefono Ingresado");
                return response;
            }

            //Verifica que sea un correo existente
            if (!emailService.verificarCorreo(correo)) {
                response.put("error", "Correo inválido");
                return response;
            }

            //Verificamos si los datos existen ya en la bd
            if (trabajadorservice.findByCorreo(correo) != null
                    || !trabajadorservice.findByNdocumento(numeroDocumento).isEmpty()
                    || !trabajadorservice.findByTelefono(telefono).isEmpty()
                    || !trabajadorservice.findByContraseña(contrasena).isEmpty()) {

                response.put("error", "Datos duplicados encontrados");
                return response;
            }

            // Crea un nuevo objeto Trabajador
            //y le asignamos los valores de los parametros recogidos
            Trabajador trabajador = new Trabajador();
            trabajador.setNombres(nombres);
            trabajador.setApellidos(apellidos);
            trabajador.setCorreo(correo);
            trabajador.setTelefono(telefono);
            trabajador.setNdocumento(numeroDocumento);

            // Conversion de la fecha
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
            LocalDate fechaEntradaLocalDate = LocalDate.parse(FechaEntrada, formatter);
            Date fechaEntradaSql = Date.valueOf(fechaEntradaLocalDate);
            trabajador.setFechaentrada(fechaEntradaSql);

            String contrasenaEncriptada = passwordEncoder.encode(contrasena);
            trabajador.setContraseña(contrasenaEncriptada);
            trabajador.setCargo(cargo);
            trabajador.setTipodocumento(tipoDocumento);
            trabajador.setRol("Trabajador");
            trabajador.setEstado("Activo");
            trabajador.setDiaspermiso(15);
            trabajador.setEstadodiario("Presente");
            trabajador.setTieneSolicitud(false);
            trabajador.setTienejustificacion(false);

            // guardamos el trabajador 
            trabajadorservice.saveTrabajador(trabajador);

            //Para enviar mensaje al correo
            String asunto = "Bienvenido al sistema de Epasame";
            String contenido = String.format("Hola %s %s,\n\n"
                    + "Se ha registrado exitosamente en el sistema. Sus credenciales son:\n\n"
                    + "Correo: %s\n"
                    + "Contraseña: %s\n\n"
                    + "Por favor, no comparta esta información con nadie.\n\n"
                    + "Saludos,\nEl equipo de gestión.",
                    nombres, apellidos, correo, contrasena);

            emailService.enviarCorreo(correo, asunto, contenido);

            response.put("success", true);
        } catch (Exception e) {

            response.put("error", "Error inesperado: " + e.getMessage());
        }
        return response;

    }

    @GetMapping("/editarTrabajadorModal/{trabajadorId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getTrabajadorById(@PathVariable Integer trabajadorId, Model model) {
        //con el id del trabajdor buscamos su permiso ligado a el 
        Trabajador trabajador = trabajadorservice.findTrabajadorById2(trabajadorId);

        //verificamos que permiso no sea nulo y que tenga el estado de Pendiente
        if (trabajador != null) {
            //creamos un mapa con su clave y valor para facilitar la obtencion de los datos
            Map<String, Object> trabajadorData = new HashMap<>();
            trabajadorData.put("nombres", trabajador.getNombres());
            trabajadorData.put("apellidos", trabajador.getApellidos());
            trabajadorData.put("correo", trabajador.getCorreo());
            trabajadorData.put("tipodoc", trabajador.getTipodocumento());
            trabajadorData.put("numerodoc", trabajador.getNdocumento());
            trabajadorData.put("telefono", trabajador.getTelefono());
            trabajadorData.put("rol", trabajador.getRol());
            trabajadorData.put("cargo", trabajador.getCargo());
            trabajadorData.put("fechaentrada", trabajador.getFechaentrada());
            trabajadorData.put("estado", trabajador.getEstado());
            trabajadorData.put("trabajadorId", trabajador.getId());

            //se devuelve la respuesta juston con la data del permiso
            return ResponseEntity.ok(trabajadorData);
        } else {
            //retorna un error si no se enuentra el permiso
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "No se encontró este trabajador."));
        }
    }

    /**
     * Permite editar los datos de un trabajador en el sistema.
     *
     * Este método recibe los datos del trabajador a editar (como su ID,
     * nombres, apellidos, teléfono, cargo, fecha de entrada y estado), valida
     * que el teléfono sea correcto y no esté duplicado, y luego actualiza la
     * información del trabajador en la base de datos. En caso de éxito,
     * redirige a la vista de administración de empleados, mostrando un mensaje
     * de éxito. Si ocurre algún error, redirige a la misma vista con un mensaje
     * de error.
     *
     * @param trabajadorId El ID del trabajador que se va a editar.
     * @param nombres El nombre del trabajador.
     * @param apellidos Los apellidos del trabajador.
     * @param telefono El teléfono del trabajador.
     * @param cargo El cargo del trabajador.
     * @param fechaentrada La fecha de entrada del trabajador (en formato
     * "yyyy-MM-dd").
     * @param estado El estado actual del trabajador (por ejemplo, "activo" o
     * "inactivo").
     * @param redirectAttributes Atributos para redirigir la respuesta con
     * mensajes de éxito o error.
     * @return La vista a la que se redirige después de procesar la solicitud,
     * en este caso "EmpleadosAdmin".
     */
    @PostMapping("/EditarTrabajador")
    public String EditarTrabajador(
            @RequestParam("trabajadorId") Integer trabajadorId,
            @RequestParam("nombres") String nombres,
            @RequestParam("apellidos") String apellidos,
            @RequestParam("telefono") String telefono,
            @RequestParam("cargo") String cargo,
            @RequestParam("fechaentrada") String fechaentrada,
            @RequestParam("estado") String estado,//recojemos parametros
            RedirectAttributes redirectAttributes) {

        try {
            Trabajador trabajador = trabajadorservice.findTrabajadorById2(trabajadorId);

            if (!telefono.matches("\\d{9}")) {
                redirectAttributes.addAttribute("error1", true);
                return "redirect:/EmpleadosAdmin";
            }

            // Verificamos si el teléfono ya está asignado a otro trabajador
            List<Trabajador> trabajadoresConMismoTelefono = trabajadorservice.findByTelefono(telefono);
            boolean telefonoDuplicado = trabajadoresConMismoTelefono.stream()
                    .anyMatch(t -> !t.getId().equals(trabajadorId));

            if (telefonoDuplicado) {
                redirectAttributes.addAttribute("error1", true);
                return "redirect:/EmpleadosAdmin";
            }

            // buscamos el permiso por su id
            trabajador.setNombres(nombres);
            trabajador.setApellidos(apellidos);
            trabajador.setTelefono(telefono);
            trabajador.setCargo(cargo);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyy-MM-dd");
            LocalDate fechaEntradaLocalDate = LocalDate.parse(fechaentrada, formatter);
            Date fechaEntradaSql = Date.valueOf(fechaEntradaLocalDate);
            trabajador.setFechaentrada(fechaEntradaSql);

            trabajador.setEstado(estado);

            trabajadorservice.saveTrabajador(trabajador);
            redirectAttributes.addAttribute("success", true);
            return "redirect:/EmpleadosAdmin";

        } catch (Exception e) {
            //e.printStackTrace();
            redirectAttributes.addAttribute("error", true);
            return "redirect:/EmpleadosAdmin";
        }
    }

    /**
     * Actualiza el estado de un trabajador a "Inactivo".
     *
     * Este método recibe el ID de un trabajador y actualiza su estado a
     * "Inactivo" en la base de datos. Si el trabajador no existe, se redirige a
     * la vista de administración de empleados con un mensaje de error. Si la
     * actualización es exitosa, se redirige a la misma vista con un mensaje de
     * éxito. En caso de un error durante el proceso, también se redirige con un
     * mensaje de error.
     *
     * @param trabajadorId El ID del trabajador cuyo estado se actualizará.
     * @param redirectAttributes Atributos para redirigir la respuesta con
     * mensajes de éxito o error.
     * @return La vista a la que se redirige después de procesar la solicitud,
     * en este caso "EmpleadosAdmin".
     */
    @PostMapping("/ActualizarEstadoTrabajador")
    public String ActualizarEstadoTrabajador(
            @RequestParam("trabajadorId") Integer trabajadorId,
            RedirectAttributes redirectAttributes) {

        try {
            // Verificamos si el trabajador existe
            Trabajador trabajador = trabajadorservice.findTrabajadorById2(trabajadorId);
            if (trabajador == null) {
                redirectAttributes.addAttribute("error2", true);
                return "redirect:/EmpleadosAdmin";
            }

            // Actualizamos el estado del trabajador
            trabajador.setEstado("Inactivo");
            trabajadorservice.saveTrabajador(trabajador);

            redirectAttributes.addAttribute("success1", true);
            return "redirect:/EmpleadosAdmin";

        } catch (Exception e) {
            // Manejo de errores
            redirectAttributes.addAttribute("error3", true);
            return "redirect:/EmpleadosAdmin";
        }
    }

}
