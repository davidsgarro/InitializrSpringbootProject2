package com.example.ProyectoPOO2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import Entidades.CFisico;
import Entidades.CJuridico;
import Entidades.Cuenta;
import Entidades.Transaccion;
import Integracion.EnviarSMS;
import Integracion.TipoDeCambio;
import Integracion.TipoDeCambio.TipoDeCambioValores;
import PersistenciaXML.CuentaXMLManager;
import PersistenciaXML.ClienteFisicoXMLManager;
import PersistenciaXML.ClienteJuridicoXMLManager;
import PersistenciaXML.ClienteManager;
import java.text.SimpleDateFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller // Agrega esta anotación aquí
public class ProyectoPoo2Application {

    public static void main(String[] args) {
        SpringApplication.run(ProyectoPoo2Application.class, args);
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "dashboard"; // Nombre del archivo HTML sin la extensión
    }
    
    @GetMapping("/gestionClientes")
    public String showGestionClientes() {
        return "gestionClientes"; // Nombre del archivo HTML sin la extensión
    }
    
    @GetMapping("/gestionCuentas")
    public String showGestionCuentas() {
        return "gestionCuentas"; // Nombre del archivo HTML sin la extensión
    }
    
    @GetMapping("/gestionTransacciones")
    public String showGestionTransacciones() {
        return "gestionTransacciones"; // Nombre del archivo HTML sin la extensión
    }
    
    @GetMapping("/consultas")
    public String showConsultas() {
        return "consultas"; // Nombre del archivo HTML sin la extensión
    }
    
    @GetMapping("/crearClienteFisico")
    public String showCrearClienteFisico() {
        return "crearClienteFisico"; // Nombre del archivo HTML sin la extensión
    }
    
    private final ClienteFisicoXMLManager clienteFisicoXMLManager = new ClienteFisicoXMLManager();

    @RequestMapping("/crearClienteFisico")
    @PostMapping
    public ModelAndView crearClienteFisico(
            @RequestParam("nombreCompleto") String nombre,
            @RequestParam("categoria") String categoria,
            @RequestParam("telefono") String telefono,
            @RequestParam("email") String correo,
            @RequestParam("identificacion") String identificacion,
            @RequestParam("maxCuentas") int maxCuentas,
            @RequestParam("fechaNacimiento") String fechaNacimientoStr
    ) {
        try {
            CFisico nuevoCliente = new CFisico(nombre, categoria, telefono, correo, identificacion, maxCuentas, fechaNacimientoStr);
            clienteFisicoXMLManager.guardarClienteFisico(nuevoCliente);

            ModelAndView modelAndView = new ModelAndView("exitoCliente");
            modelAndView.addObject("codigoCliente", nuevoCliente.getCodigoCliente());
            modelAndView.addObject("nombre", nuevoCliente.getNombre());
            modelAndView.addObject("telefono", nuevoCliente.getTelefono());
            modelAndView.addObject("correo", nuevoCliente.getCorreo());
            modelAndView.addObject("identificacion", nuevoCliente.getIdentificacion());
            modelAndView.addObject("maxCuentas", nuevoCliente.getMaxCuentas());
            modelAndView.addObject("fechaNacimiento", nuevoCliente.getFechaNacimiento());
            return modelAndView;

        } catch (Exception e) {
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView("error");
            modelAndView.addObject("mensaje", "Ocurrió un error al crear el cliente físico.");
            return modelAndView;
        }
    }
    
    @GetMapping("/crearClienteJuridico")
    public String showCrearClienteJuridico() {
        return "crearClienteJuridico"; // Nombre del archivo HTML sin la extensión
    }
    
    private ClienteJuridicoXMLManager clienteJuridicoXMLManager = new ClienteJuridicoXMLManager();
    
    @RequestMapping("/crearClienteJuridico")
    @PostMapping
    public ModelAndView crearClienteJuridico(
            @RequestParam("nombreApoderado") String nombreApoderado,
            @RequestParam("categoria") String categoria,
            @RequestParam("telefono") String telefono,
            @RequestParam("email") String correo,
            @RequestParam("tipoNegocio") String tipoNegocio,
            @RequestParam("cedulaJuridica") String cedulaJuridica,
            @RequestParam("razonSocial") String razonSocial
    ) {
        try {
            // Crear instancia del cliente jurídico
            CJuridico nuevoCliente = new CJuridico(nombreApoderado, categoria, telefono, correo, nombreApoderado, tipoNegocio, cedulaJuridica, razonSocial);

            // Guardar cliente jurídico usando el XML manager
            clienteJuridicoXMLManager.guardarClienteJuridico(nuevoCliente);

            // Configurar el ModelAndView con la vista de éxito y pasar los datos del cliente
            ModelAndView modelAndView = new ModelAndView("exitoCJuridico");
            modelAndView.addObject("codigoCliente", nuevoCliente.getCodigoCliente());
            modelAndView.addObject("nombreApoderado", nuevoCliente.getNombre());
            modelAndView.addObject("telefono", nuevoCliente.getTelefono());
            modelAndView.addObject("correo", nuevoCliente.getCorreo());
            modelAndView.addObject("tipoNegocio", nuevoCliente.getTipoNegocio());
            modelAndView.addObject("cedulaJuridica", nuevoCliente.getCedulaJuridica());
            modelAndView.addObject("razonSocial", nuevoCliente.getRazonSocial());

            return modelAndView;

        } catch (Exception e) {
            e.printStackTrace();
            ModelAndView modelAndView = new ModelAndView("error"); // Vista de error
            modelAndView.addObject("mensaje", "Ocurrió un error al crear el cliente jurídico.");
            return modelAndView;
        }
    }

    // Método alternativo que acepta una instancia de CJuridico directamente
    @PostMapping("/crearClienteJuridicoDirecto")
    public String crearClienteJuridico(CJuridico cliente) {
        clienteJuridicoXMLManager.guardarClienteJuridico(cliente);
        return "exitoCJuridico"; // Nombre de la plantilla (sin .html)
    }
    
    
    
    @GetMapping("/actualizarTelefono")
    public String mostrarFormularioActualizarTelefono() {
        return "actualizarTelefono"; // Nombre de la vista HTML para el formulario
    }
    
    @RequestMapping("/actualizarTelefono")
    @PostMapping("/actualizarTelefono")
    public ModelAndView actualizarTelefono(
            @RequestParam("identidadCliente") String identidadCliente,
            @RequestParam("nuevoTelefono") String nuevoTelefono,
            @RequestParam("tipoCliente") String tipoCliente) {
        
        String nombreCompleto;
        String telefonoAnterior;
    
        CFisico clienteFisico = clienteFisicoXMLManager.buscarClientePorIdentidad(identidadCliente);
        CJuridico clienteJuridico = clienteJuridicoXMLManager.buscarClientePorIdentidad(identidadCliente);

        boolean actualizado;
        if ("fisico".equals(tipoCliente)) {
            telefonoAnterior = clienteFisico.getTelefono();
            actualizado = clienteFisicoXMLManager.actualizarTelefonoFisico(identidadCliente, nuevoTelefono);
            nombreCompleto = clienteFisico.getNombre();
        } else {
            telefonoAnterior = clienteJuridico.getTelefono();
            actualizado = clienteJuridicoXMLManager.actualizarTelefonoJuridico(identidadCliente, nuevoTelefono);
            nombreCompleto = clienteJuridico.getNombre();
        }
        
        ModelAndView modelAndView = new ModelAndView("resultadoActualizarTelefono");
        modelAndView.addObject("nombreCompleto", nombreCompleto);
        modelAndView.addObject("telefonoAnterior", telefonoAnterior);
        modelAndView.addObject("nuevoTelefono", nuevoTelefono);

        if (actualizado) {
            modelAndView.addObject("mensaje", "Teléfono actualizado exitosamente.");
        } else {
            modelAndView.addObject("mensaje", "Usuario no encontrado o error en la actualización.");
        }

        return modelAndView;
    }
    
    @GetMapping("/actualizarCorreo")
    public String mostrarFormularioActualizarCorreo() {
        return "actualizarCorreo"; // Nombre de la vista HTML para el formulario
    }
    
    @RequestMapping("/actualizarCorreo")
    @PostMapping("/actualizarCorreo")
    public ModelAndView actualizarCorreo(
            @RequestParam("tipoCliente") String tipoCliente,
            @RequestParam("identidadCliente") String identidadCliente,
            @RequestParam("nuevoCorreo") String nuevoCorreo) {
        
        String nombreCompleto = null;
        String correoAnterior = null;
        boolean actualizado = false;

        CFisico clienteFisico = clienteFisicoXMLManager.buscarClientePorIdentidad(identidadCliente);
        CJuridico clienteJuridico = clienteJuridicoXMLManager.buscarClientePorIdentidad(identidadCliente);
        
        if (tipoCliente.equals("fisico")) {
            correoAnterior = clienteFisico.getCorreo();
            actualizado = clienteFisicoXMLManager.actualizarCorreoFisico(identidadCliente, nuevoCorreo);
            nombreCompleto = clienteFisico.getNombre();
        } else if (tipoCliente.equals("juridico")) {
            correoAnterior = clienteJuridico.getCorreo();
            actualizado = clienteJuridicoXMLManager.actualizarCorreoJuridico(identidadCliente, nuevoCorreo);
            nombreCompleto = clienteJuridico.getNombre();
        }
        
        ModelAndView modelAndView = new ModelAndView("resultadoActualizarCorreo");
        modelAndView.addObject("nombreCompleto", nombreCompleto);
        modelAndView.addObject("correoAnterior", correoAnterior);
        modelAndView.addObject("nuevoCorreo", nuevoCorreo);

        if (actualizado) {
            modelAndView.addObject("mensaje", "Correo actualizado exitosamente.");
        } else {
            modelAndView.addObject("mensaje", "Usuario no encontrado o error en la actualización.");
        }

        return modelAndView;
    }
    
    private final ClienteManager clienteManager = new ClienteManager();
    
    @GetMapping("/listarClientes")
    public String listarClientes(Model model) {
        List<Map<String, String>> clientesConDetalles = clienteManager.obtenerClientesConDetalles();
        model.addAttribute("clientes", clientesConDetalles);
        return "listarClientes";  // Nombre de la vista donde mostrarás la lista
    }

    @GetMapping("/crearCuenta")
    public String mostrarFormularioCrearCuenta() {
        return "crearCuenta"; // Nombre de la vista HTML para el formulario
    }
    
    private final CuentaXMLManager cuentaXMLManager = new CuentaXMLManager();
    
    @RequestMapping("/crearCuenta")
    @PostMapping("/crearCuenta")
    public ModelAndView crearCuenta(
            @RequestParam("identidadCliente") String identidadCliente,
            @RequestParam("pinCuenta") String pinCuenta,
            @RequestParam("montoInicial") double montoInicial,
            @RequestParam("tipoCliente") String tipoCliente
    ) {
        String nombrePropietario;

        // Verificar si el cliente existe
        if ("fisico".equalsIgnoreCase(tipoCliente)) {
            CFisico clienteFisico = clienteFisicoXMLManager.buscarClientePorIdentidad(identidadCliente);
            if (clienteFisico == null) {
                ModelAndView modelAndView = new ModelAndView("resultadoCuenta");
                modelAndView.addObject("mensaje", "Cliente no encontrado. No se puede crear la cuenta.");
                return modelAndView;
            }
            nombrePropietario = clienteFisico.getNombre();
        } else {
            CJuridico clienteJuridico = clienteJuridicoXMLManager.buscarClientePorIdentidad(identidadCliente);
            if (clienteJuridico == null) {
                ModelAndView modelAndView = new ModelAndView("resultadoCuenta");
                modelAndView.addObject("mensaje", "Cliente no encontrado. No se puede crear la cuenta.");
                return modelAndView;
            }
            nombrePropietario = clienteJuridico.getNombre();
        }

        // Crear cuenta
        String numeroCuenta = cuentaXMLManager.generarNumeroCuenta();
        Cuenta cuenta = new Cuenta(identidadCliente, numeroCuenta, "Activa", montoInicial, pinCuenta);

        // Guardar cuenta en el XML
        cuentaXMLManager.guardarCuenta(cuenta);

        // Enviar los detalles de la cuenta a la vista
        ModelAndView modelAndView = new ModelAndView("resultadoCuenta");
        modelAndView.addObject("numeroCuenta", cuenta.getNumeroCuenta());
        modelAndView.addObject("estadoCuenta", cuenta.getEstadoCuenta());
        modelAndView.addObject("saldo", cuenta.getMontoInicial());
        modelAndView.addObject("tipoCliente", tipoCliente);
        modelAndView.addObject("nombrePropietario", nombrePropietario);
        modelAndView.addObject("mensaje", "Cuenta creada exitosamente.");
        return modelAndView;
    }
    
    @GetMapping("/cambiarPin")
    public String mostrarFormularioCambiarPIN() {
        return "cambiarPin"; // Nombre de la vista HTML para el formulario
    }
    
    @PostMapping("/cambiarPin")
    public ModelAndView cambiarPin(           
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("pinActual") String pinActual,
            @RequestParam("nuevoPin") String nuevoPin) {

        // Llamar al método de CuentaXMLManager para cambiar el PIN
        boolean cambioExitoso = cuentaXMLManager.cambiarPinCuenta(numeroCuenta, pinActual, nuevoPin);

        ModelAndView modelAndView = new ModelAndView("resultadoCambiarPin");
        if (cambioExitoso) {
            modelAndView.addObject("mensaje", "PIN cambiado exitosamente para la cuenta: " + numeroCuenta);
        } else {
            modelAndView.addObject("mensaje", "Error: No se pudo cambiar el PIN. Verifique el número de cuenta y el PIN actual.");
        }
        return modelAndView;
    }
    
    @GetMapping("/eliminarCuenta")
    public String mostrarFormularioEliminarCuenta() {
        return "eliminarCuenta"; // Nombre de la vista HTML para el formulario
    }
    
    @PostMapping("/eliminarCuenta")
    public ModelAndView eliminarCuenta(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("pinCuenta") String pinCuenta) {
        
        // Llamar al método para eliminar la cuenta
        String mensaje = cuentaXMLManager.eliminarCuenta(numeroCuenta, pinCuenta);

        // Redirigir a la vista de resultado con el mensaje
        ModelAndView modelAndView = new ModelAndView("resultadoEliminarCuenta");
        modelAndView.addObject("mensaje", mensaje);
        return modelAndView;
    }
    
    @GetMapping("/listarCuentas")
    public String listarCuentas(Model model) {
        List<Map<String, String>> cuentasConDetalles = cuentaXMLManager.obtenerCuentasConDetalles();
        model.addAttribute("cuentas", cuentasConDetalles);
        return "listarCuentas";  // Nombre de la vista donde mostrarás la lista
    }
    
    @GetMapping("/consultarCuentas")
    public String mostrarFormularioConsulta() {
        return "consultarCuentas";  // Nombre de la vista para el formulario de consulta
    }

    @PostMapping("/consultarCuentas")
    public ModelAndView procesarConsultaCuentas(@RequestParam("identidadCliente") String identidadCliente) {
        List<Cuenta> cuentas = cuentaXMLManager.obtenerCuentasPorIdentificacion(identidadCliente);

        ModelAndView modelAndView = new ModelAndView("resultadoConsultaCuentas");
        modelAndView.addObject("identidadCliente", identidadCliente);
        modelAndView.addObject("cuentas", cuentas);
        return modelAndView;
    }
    
    @GetMapping("/tipoCambio")
    public ModelAndView mostrarTipoDeCambio() {
        TipoDeCambio.TipoDeCambioValores tipoDeCambio = TipoDeCambio.consultarTipoDeCambio();
        ModelAndView modelAndView = new ModelAndView("tipoCambio");
        modelAndView.addObject("compra", tipoDeCambio.getCompra());
        modelAndView.addObject("venta", tipoDeCambio.getVenta());
        return modelAndView;
    }
    
    @GetMapping("/consultarSaldo")
    public String mostrarFormularioConsultaSaldo() {
        return "consultarSaldo"; // Nombre del archivo HTML para el formulario de consulta de saldo
    }

    @PostMapping("/consultarSaldo")
    public ModelAndView consultarSaldo(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("pinCuenta") String pinCuenta) {

        // Verificar si la cuenta existe
        Cuenta cuenta = cuentaXMLManager.buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null) {
            ModelAndView modelAndView = new ModelAndView("consultarSaldo");
            modelAndView.addObject("mensajeError", "El número de cuenta ingresado no está registrado.");
            return modelAndView;
        }

        // Verificar el PIN de la cuenta
        if (!cuenta.getPinCuenta().equals(pinCuenta)) {
            ModelAndView modelAndView = new ModelAndView("consultarSaldo");
            modelAndView.addObject("mensajeError", "El PIN ingresado es incorrecto.");
            return modelAndView;
        }
        String nombrePropietario = obtenerNombrePropietario(cuenta.getIdentidadCliente());
        // Preparar la vista de respuesta con el saldo y el nombre del propietario
        ModelAndView modelAndView = new ModelAndView("resultadoConsultaSaldo");
        modelAndView.addObject("numeroCuenta", cuenta.getNumeroCuenta());
        modelAndView.addObject("nombrePropietario", nombrePropietario);
        modelAndView.addObject("saldo", cuenta.getMontoInicial());

        return modelAndView;
    }
    
    //Consulta saldo cambio moneda 
    @GetMapping("/consultarSaldoCambioMoneda")
    public String mostrarFormularioConsultaSaldoCambioMoneda() {
        return "consultarSaldoCambioMoneda"; // Nombre del archivo HTML para el formulario de consulta de saldo con cambio de divisa
    }

    @PostMapping("/consultarSaldoCambioMoneda")
    public ModelAndView consultarSaldoCambioMoneda(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("pinCuenta") String pinCuenta) {

        // Verificar si la cuenta existe
        Cuenta cuenta = cuentaXMLManager.buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null) {
            ModelAndView modelAndView = new ModelAndView("consultarSaldoCambioMoneda");
            modelAndView.addObject("mensajeError", "El número de cuenta ingresado no está registrado.");
            return modelAndView;
        }

        // Verificar el PIN de la cuenta
        if (!cuenta.getPinCuenta().equals(pinCuenta)) {
            ModelAndView modelAndView = new ModelAndView("consultarSaldoCambioMoneda");
            modelAndView.addObject("mensajeError", "El PIN ingresado es incorrecto.");
            return modelAndView;
        }

        // Obtener el nombre del propietario
        String nombrePropietario = obtenerNombrePropietario(cuenta.getIdentidadCliente());

        // Obtener el tipo de cambio
        TipoDeCambio.TipoDeCambioValores tipoDeCambio = TipoDeCambio.consultarTipoDeCambio();
        double tipoCambioCompra = Double.parseDouble(tipoDeCambio.getCompra().replace(",", "."));

        // Convertir el saldo de colones a dólares
        double saldoDolares = cuenta.getMontoInicial() / tipoCambioCompra;

        // Preparar la vista de respuesta con el saldo en dólares y el nombre del propietario
        ModelAndView modelAndView = new ModelAndView("resultadoConsultaSaldoCambioMoneda");
        modelAndView.addObject("numeroCuenta", cuenta.getNumeroCuenta());
        modelAndView.addObject("nombrePropietario", nombrePropietario);
        modelAndView.addObject("saldoDolares", String.format("%.2f", saldoDolares));
        modelAndView.addObject("mensaje", "Estimado usuario: " + nombrePropietario + 
                               ", el saldo actual de su cuenta " + cuenta.getNumeroCuenta() +
                               " es de " + String.format("%.2f", saldoDolares) + " dólares.");

        return modelAndView;
    }
    
    private String obtenerNombrePropietario(String identidadCliente) {
        CFisico clienteFisico = clienteFisicoXMLManager.buscarClientePorIdentidad(identidadCliente);
        if (clienteFisico != null) {
            return clienteFisico.getNombre();
        }
        CJuridico clienteJuridico = clienteJuridicoXMLManager.buscarClientePorIdentidad(identidadCliente);
        if (clienteJuridico != null) {
            return clienteJuridico.getNombre();
        }
        return "Desconocido";
    }
    
    @GetMapping("/consultarEstatusCuenta")
    public String mostrarFormularioConsultarEstatusCuenta() {
        return "consultarEstatusCuenta"; // Nombre de la vista HTML para el formulario de consulta
    }

    @PostMapping("/consultarEstatusCuenta")
    public ModelAndView consultarEstatusCuenta(@RequestParam("numeroCuenta") String numeroCuenta) {
        // Buscar la cuenta por número
        Cuenta cuenta = cuentaXMLManager.buscarCuentaPorNumero(numeroCuenta);

        ModelAndView modelAndView;

        if (cuenta != null) {
            // Obtener el nombre del propietario
            String nombrePropietario = obtenerNombrePropietario(cuenta.getIdentidadCliente());

            // Configurar el ModelAndView con los datos de la cuenta
            modelAndView = new ModelAndView("resultadoConsultaEstatusCuenta");
            modelAndView.addObject("numeroCuenta", cuenta.getNumeroCuenta());
            modelAndView.addObject("nombrePropietario", nombrePropietario);
            modelAndView.addObject("estatus", cuenta.getEstadoCuenta());
        } else {
            // Si la cuenta no existe, retornar un mensaje de error en la misma vista
            modelAndView = new ModelAndView("consultarEstatusCuenta");
            modelAndView.addObject("mensajeError", "La cuenta ingresada no está registrada en el sistema.");
        }

        return modelAndView;
    }
    
    // Mostrar formulario de depósito
    @GetMapping("/realizarDeposito")
    public String mostrarFormularioDeposito() {
        return "realizarDeposito";  // Nombre de la plantilla HTML para el formulario de depósito
    }

    // Procesar el depósito
    @PostMapping("/realizarDeposito")
    public String realizarDeposito(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("montoDeposito") double montoDeposito,
            Model model) {

        boolean exito = cuentaXMLManager.registrarDeposito(numeroCuenta, montoDeposito);

        if (exito) {
            // Obtener el nombre del propietario de la cuenta
            String nombrePropietario = obtenerNombrePropietarioDeposito(numeroCuenta);
            model.addAttribute("mensaje", "Depósito realizado exitosamente.");
            model.addAttribute("numeroCuenta", numeroCuenta);
            model.addAttribute("montoDeposito", montoDeposito);
            model.addAttribute("nombrePropietario", nombrePropietario);
        } else {
            model.addAttribute("mensaje", "Error al realizar el depósito. Verifique el número de cuenta.");
        }

        return "resultadoDeposito";  // Vista de confirmación del depósito
    }
    
    private String obtenerNombrePropietarioDeposito(String numeroCuenta) {
        Cuenta cuenta = cuentaXMLManager.buscarCuentaPorNumero(numeroCuenta);
        if (cuenta != null) {
            String identidadCliente = cuenta.getIdentidadCliente();
            CFisico clienteFisico = clienteFisicoXMLManager.buscarClientePorIdentidad(identidadCliente);
            if (clienteFisico != null) {
                return clienteFisico.getNombre();
            }
            CJuridico clienteJuridico = clienteJuridicoXMLManager.buscarClientePorIdentidad(identidadCliente);
            if (clienteJuridico != null) {
                return clienteJuridico.getNombre();
            }
        }
        return "Desconocido";
    }
 
    
    @GetMapping("/realizarDepositoCambioMoneda")
    public String mostrarFormularioDepositoCambioMoneda() {
        return "realizarDepositoCambioMoneda";  // Nombre de la plantilla HTML para el formulario de depósito en moneda extranjera
    }

    @PostMapping("/realizarDepositoCambioMoneda")
    public ModelAndView realizarDepositoCambioMoneda(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("montoDeposito") double montoDeposito) { // Eliminado el parámetro 'moneda'

        // Configurar "dolares" como moneda predeterminada
        String moneda = "dolares";

        // Obtener el tipo de cambio actual
        TipoDeCambio.TipoDeCambioValores tipoDeCambio = TipoDeCambio.consultarTipoDeCambio();
        if (tipoDeCambio == null) {
            ModelAndView modelAndView = new ModelAndView("realizarDepositoCambioMoneda");
            modelAndView.addObject("mensajeError", "No se pudo obtener el tipo de cambio actual. Intente más tarde.");
            return modelAndView;
        }

        // Verificar si la cuenta existe
        Cuenta cuenta = cuentaXMLManager.buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null) {
            ModelAndView modelAndView = new ModelAndView("realizarDepositoCambioMoneda");
            modelAndView.addObject("mensajeError", "El número de cuenta ingresado no está registrado.");
            return modelAndView;
        }

        // Convertir el monto si la moneda es en dólares
        double montoEnColones = montoDeposito;
        double tipoCambioCompra = Double.parseDouble(tipoDeCambio.getCompra().replace(",", "."));

        if (moneda.equalsIgnoreCase("dolares")) {
            montoEnColones = montoDeposito * tipoCambioCompra;
        }

        // Realizar el depósito
        boolean exito = cuentaXMLManager.registrarDeposito(numeroCuenta, montoEnColones);
        ModelAndView modelAndView;

        if (exito) {
            modelAndView = new ModelAndView("resultadoDepositoCambioMoneda");
            String nombrePropietario = obtenerNombrePropietario(cuenta.getIdentidadCliente());
            modelAndView.addObject("mensaje", "Depósito realizado exitosamente.");
            modelAndView.addObject("numeroCuenta", numeroCuenta);
            modelAndView.addObject("nombrePropietario", nombrePropietario);
            modelAndView.addObject("montoDeposito", montoDeposito);
            modelAndView.addObject("montoEnColones", montoEnColones);
            modelAndView.addObject("moneda", moneda);
            modelAndView.addObject("tipoCambio", tipoCambioCompra);
        } else {
            modelAndView = new ModelAndView("realizarDepositoCambioMoneda");
            modelAndView.addObject("mensajeError", "Error al realizar el depósito. Verifique el número de cuenta.");
        }

        return modelAndView;
    }

    @GetMapping("/obtenerDatosCuenta")
    @ResponseBody
    public Map<String, String> obtenerDatosCuenta(@RequestParam("numeroCuenta") String numeroCuenta) {
        Map<String, String> datosCuenta = new HashMap<>();

        // Buscar la cuenta en XML
        Cuenta cuenta = cuentaXMLManager.buscarCuentaPorNumero(numeroCuenta);

        if (cuenta != null) {
            String identidadCliente = cuenta.getIdentidadCliente();
            String nombreCliente = "";

            // Buscar el nombre del cliente
            CFisico clienteFisico = clienteFisicoXMLManager.buscarClientePorIdentidad(identidadCliente);
            if (clienteFisico != null) {
                nombreCliente = clienteFisico.getNombre();
            } else {
                CJuridico clienteJuridico = clienteJuridicoXMLManager.buscarClientePorIdentidad(identidadCliente);
                if (clienteJuridico != null) {
                    nombreCliente = clienteJuridico.getNombre();
                }
            }

            // Agregar los datos encontrados al mapa de respuesta
            datosCuenta.put("nombreCliente", nombreCliente);
            datosCuenta.put("saldoActual", String.valueOf(cuenta.getMontoInicial()));
        } else {
            // Si la cuenta no existe
            datosCuenta.put("error", "Cuenta no encontrada");
        }

        return datosCuenta;
    }

    // Mostrar formulario de consulta de estado de cuenta
    @GetMapping("/consultarEstadoCuenta")
    public String mostrarFormularioEstadoCuenta() {
        return "consultarEstadoCuenta"; // Nombre de la vista HTML para el formulario de consulta
    }

    // Procesar consulta de estado de cuenta
    @PostMapping("/consultarEstadoCuenta")
    public ModelAndView consultarEstadoCuenta(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("pinCuenta") String pinCuenta) {

        // Cargar la cuenta junto con sus transacciones
        Cuenta cuenta = cuentaXMLManager.cargarCuentaConTransacciones(numeroCuenta);
        Cuenta cuentaN = cuentaXMLManager.buscarCuentaPorNumero(numeroCuenta);

        ModelAndView modelAndView;

        if (cuenta == null) {
            // Si la cuenta no existe
            modelAndView = new ModelAndView("consultarEstadoCuenta");
            modelAndView.addObject("mensajeError", "El número de cuenta ingresado no está registrado.");
            return modelAndView;
        }

        // Verificar el PIN de la cuenta
        if (!cuenta.getPinCuenta().equals(pinCuenta)) {
            // Si el PIN es incorrecto
            modelAndView = new ModelAndView("consultarEstadoCuenta");
            modelAndView.addObject("mensajeError", "El PIN ingresado es incorrecto.");
            return modelAndView;
        }

        // Obtener el nombre del propietario
        String nombrePropietario = obtenerNombrePropietario(cuentaN.getIdentidadCliente());

        // Preparar la vista con los datos de la cuenta y sus transacciones
        modelAndView = new ModelAndView("resultadoEstadoCuenta");
        modelAndView.addObject("nombrePropietario", nombrePropietario);
        modelAndView.addObject("numeroCuenta", cuenta.getNumeroCuenta());
        modelAndView.addObject("saldo", cuenta.getMontoInicial());
        modelAndView.addObject("estatus", cuenta.getEstadoCuenta());
        modelAndView.addObject("transacciones", cuenta.getTransacciones()); // Pasar las transacciones a la vista

        return modelAndView;
    }

    @GetMapping("/realizarTransferencia")
    public String mostrarFormularioTransferencia() {
        return "realizarTransferencia"; // Nombre de la plantilla HTML para el formulario inicial de transferencia
    }

    @PostMapping("/realizarTransferencia")
    public ModelAndView iniciarTransferencia(
            @RequestParam("cuentaOrigen") String cuentaOrigen,
            @RequestParam("pinOrigen") String pinOrigen,
            HttpSession session) {

        ModelAndView modelAndView;

        // Buscar la cuenta origen y verificar existencia
        Cuenta cuentaOrigenObj = cuentaXMLManager.buscarCuentaPorNumero(cuentaOrigen);
        if (cuentaOrigenObj == null || !cuentaOrigenObj.getPinCuenta().equals(pinOrigen)) {
            modelAndView = new ModelAndView("realizarTransferencia");
            modelAndView.addObject("mensajeError", "Cuenta origen no encontrada o PIN incorrecto.");
            return modelAndView;
        }

        // Generar palabra secreta y enviar por SMS
        String palabraSecreta = generarPalabraAleatoria();
        String nombrePropietario = obtenerNombrePropietario(cuentaOrigenObj.getIdentidadCliente());
        EnviarSMS.enviarMensaje(nombrePropietario, palabraSecreta);

        // Guardar datos en sesión
        session.setAttribute("cuentaOrigen", cuentaOrigen);
        session.setAttribute("palabraSecreta", palabraSecreta);

        // Redirigir a la vista de verificación de transferencia
        modelAndView = new ModelAndView("verificarTransferencia");
        return modelAndView;
    }

    @PostMapping("/verificarTransferencia")
    public ModelAndView verificarTransferencia(
            @RequestParam("palabraIngresada") String palabraIngresada,
            @RequestParam("cuentaDestino") String cuentaDestino,
            @RequestParam("montoTransferencia") double montoTransferencia,
            HttpSession session) {

        ModelAndView modelAndView;

        // Recuperar datos de la sesión
        String cuentaOrigen = (String) session.getAttribute("cuentaOrigen");
        String palabraSecreta = (String) session.getAttribute("palabraSecreta");

        // Validar la palabra secreta
        if (!palabraSecreta.equalsIgnoreCase(palabraIngresada)) {
            modelAndView = new ModelAndView("verificarTransferencia");
            modelAndView.addObject("mensajeError", "Palabra secreta incorrecta. Intente nuevamente.");
            return modelAndView;
        }

        // Validar cuentas y fondos
        Cuenta cuentaOrigenObj = cuentaXMLManager.buscarCuentaPorNumero(cuentaOrigen);
        Cuenta cuentaDestinoObj = cuentaXMLManager.buscarCuentaPorNumero(cuentaDestino);
        if (cuentaOrigenObj == null || cuentaDestinoObj == null) {
            modelAndView = new ModelAndView("verificarTransferencia");
            modelAndView.addObject("mensajeError", "Cuenta origen o destino no válida.");
            return modelAndView;
        }

        if (!cuentaOrigenObj.getIdentidadCliente().equals(cuentaDestinoObj.getIdentidadCliente())) {
            modelAndView = new ModelAndView("verificarTransferencia");
            modelAndView.addObject("mensajeError", "Las cuentas no pertenecen al mismo dueño.");
            return modelAndView;
        }

        if (cuentaOrigenObj.getMontoInicial() < montoTransferencia) {
            modelAndView = new ModelAndView("verificarTransferencia");
            modelAndView.addObject("mensajeError", "Fondos insuficientes en la cuenta origen.");
            return modelAndView;
        }

        // Realizar la transferencia (retiro en cuenta origen y depósito en cuenta destino)
        boolean exitoRetiro = cuentaXMLManager.registrarRetiro(cuentaOrigen, montoTransferencia);
        boolean exitoDeposito = cuentaXMLManager.registrarDeposito(cuentaDestino, montoTransferencia);

        if (exitoRetiro && exitoDeposito) {
            String nombrePropietario = obtenerNombrePropietario(cuentaOrigenObj.getIdentidadCliente());
            modelAndView = new ModelAndView("resultadoTransferencia");
            modelAndView.addObject("mensaje", "Transferencia realizada exitosamente.");
            modelAndView.addObject("nombrePropietario", nombrePropietario);
            modelAndView.addObject("cuentaOrigen", cuentaOrigen);
            modelAndView.addObject("cuentaDestino", cuentaDestino);
            modelAndView.addObject("montoTransferido", montoTransferencia);
        } else {
            modelAndView = new ModelAndView("verificarTransferencia");
            modelAndView.addObject("mensajeError", "Error al realizar la transferencia.");
        }

        // Limpiar atributos de sesión
        session.removeAttribute("cuentaOrigen");
        session.removeAttribute("palabraSecreta");

        return modelAndView;
    }

    
    @GetMapping("/realizarRetiro")
    public String mostrarFormularioRetiro() {
        return "realizarRetiro";  // Nombre de la plantilla HTML para el formulario de retiro
    }

    @PostMapping("/realizarRetiro")
    public ModelAndView realizarRetiro(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("pinCuenta") String pinCuenta,
            Model model,
            HttpSession session) {
        
        // Guardar numeroCuenta en la sesión
        session.setAttribute("numeroCuenta", numeroCuenta);        

        // Verificar si la cuenta existe
        Cuenta cuenta = cuentaXMLManager.buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null || !cuenta.getPinCuenta().equals(pinCuenta)) {
            ModelAndView modelAndView = new ModelAndView("realizarRetiro");
            modelAndView.addObject("mensajeError", "Número de cuenta o PIN incorrecto.");
            return modelAndView;
        }

        // Verificar si la cuenta está activa
        if (cuenta.getEstadoCuenta().equalsIgnoreCase("Inactiva")) {
            ModelAndView modelAndView = new ModelAndView("realizarRetiro");
            modelAndView.addObject("mensajeError", "La cuenta está inactiva.");
            return modelAndView;
        }

        // Generar palabra aleatoria y enviarla al cliente
        String palabraAleatoria = generarPalabraAleatoria();
        String nombrePropietario = obtenerNombrePropietarioDeposito(cuenta.getIdentidadCliente());
        EnviarSMS.enviarMensaje(nombrePropietario, palabraAleatoria);

        // Guardar la palabra en el modelo para la verificación en la siguiente vista
        session.setAttribute("palabraAleatoria", palabraAleatoria);
            
        // Redirigir a la vista de verificación
        return new ModelAndView("redirect:/verificarRetiro");
    }
    
    @GetMapping("/verificarRetiro")
    public ModelAndView mostrarVerificarRetiro(HttpSession session, Model model) {
        String numeroCuenta = (String) session.getAttribute("numeroCuenta");
        String palabraAleatoria = (String) session.getAttribute("palabraAleatoria");

        if (numeroCuenta == null || palabraAleatoria == null) {
            model.addAttribute("mensajeError", "No se pudo obtener la información de verificación.");
            return new ModelAndView("realizarRetiro");
        }

        ModelAndView modelAndView = new ModelAndView("verificarRetiro");
        modelAndView.addObject("numeroCuenta", numeroCuenta);
        modelAndView.addObject("palabraAleatoria", palabraAleatoria);
        return modelAndView;
    }

    @PostMapping("/verificarRetiro")
    public ModelAndView verificarRetiro(           
            @RequestParam("palabraIngresada") String palabraIngresada,
            @RequestParam("montoRetiro") double montoRetiro,            
            HttpSession session,
            Model model) {
        
        // Recuperar `numeroCuenta` y `palabraAleatoria` desde la sesión
        String numeroCuenta = (String) session.getAttribute("numeroCuenta");
        String palabraAleatoria = (String) session.getAttribute("palabraAleatoria");        

        // Verificar que la palabra ingresada coincide
        if (!palabraAleatoria.equalsIgnoreCase(palabraIngresada)) {
            // Incrementar intentos fallidos y enviar una nueva palabra si es necesario
            int intentosFallidos = (int) model.getAttribute("intentosFallidos");
            intentosFallidos++;
            if (intentosFallidos >= 3) {
                cuentaXMLManager.inactivarCuentaRetiro(numeroCuenta);
                model.addAttribute("mensajeError", "Ha fallado tres veces. La cuenta ha sido inactivada.");
                return new ModelAndView("realizarRetiro");
            }

            // Generar y enviar nueva palabra
            palabraAleatoria = generarPalabraAleatoria();
            String nombrePropietario = obtenerNombrePropietarioDeposito(numeroCuenta);
            EnviarSMS.enviarMensaje(nombrePropietario, palabraAleatoria);
            model.addAttribute("intentosFallidos", intentosFallidos);
            model.addAttribute("palabraAleatoria", palabraAleatoria);
            model.addAttribute("mensajeError", "Palabra incorrecta. Se ha enviado una nueva palabra.");
            return new ModelAndView("verificarRetiro");
        }

        // Si la palabra es correcta, realizar el retiro
        boolean exito = cuentaXMLManager.registrarRetiro(numeroCuenta, montoRetiro);
        if (exito) {
            ModelAndView modelAndView = new ModelAndView("resultadoRetiro");
            modelAndView.addObject("mensaje", "Retiro realizado exitosamente.");
            modelAndView.addObject("numeroCuenta", numeroCuenta);
            modelAndView.addObject("montoRetiro", montoRetiro);
            modelAndView.addObject("nombrePropietario", obtenerNombrePropietarioDeposito(numeroCuenta));
            
            // Limpiar atributos de sesión
            session.removeAttribute("intentosFallidos");
            session.removeAttribute("palabraAleatoria");
            session.removeAttribute("numeroCuenta");
            return modelAndView;
        } else {
            model.addAttribute("mensajeError", "Fondos insuficientes o error al procesar el retiro.");
            return new ModelAndView("verificarRetiro");
        }
    }
    
    @GetMapping("/realizarRetiroCambioMoneda")
    public String mostrarFormularioRetiroCambioMoneda() {
        return "realizarRetiroCambioMoneda";  // Plantilla para el formulario de retiro en moneda extranjera
    }

    @PostMapping("/realizarRetiroCambioMoneda")
    public ModelAndView realizarRetiroCambioMoneda(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("pinCuenta") String pinCuenta,
            HttpSession session) {

        // Guardar numeroCuenta en la sesión
        session.setAttribute("numeroCuenta", numeroCuenta);

        // Verificar si la cuenta existe
        Cuenta cuenta = cuentaXMLManager.buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null || !cuenta.getPinCuenta().equals(pinCuenta)) {
            ModelAndView modelAndView = new ModelAndView("realizarRetiroCambioMoneda");
            modelAndView.addObject("mensajeError", "Número de cuenta o PIN incorrecto.");
            return modelAndView;
        }

        // Verificar si la cuenta está activa
        if (cuenta.getEstadoCuenta().equalsIgnoreCase("Inactiva")) {
            ModelAndView modelAndView = new ModelAndView("realizarRetiroCambioMoneda");
            modelAndView.addObject("mensajeError", "La cuenta está inactiva.");
            return modelAndView;
        }

        // Generar palabra aleatoria y enviarla al cliente
        String palabraAleatoria = generarPalabraAleatoria();
        String nombrePropietario = obtenerNombrePropietario(cuenta.getIdentidadCliente());
        EnviarSMS.enviarMensaje(nombrePropietario, palabraAleatoria);

        // Guardar la palabra en la sesión para verificación en la siguiente etapa
        session.setAttribute("palabraAleatoria", palabraAleatoria);

        // Redirigir a la vista de verificación de retiro con cambio de moneda
        return new ModelAndView("redirect:/verificarRetiroCambioMoneda");
    }

    @GetMapping("/verificarRetiroCambioMoneda")
    public ModelAndView mostrarVerificarRetiroCambioMoneda(HttpSession session, Model model) {
        String numeroCuenta = (String) session.getAttribute("numeroCuenta");
        String palabraAleatoria = (String) session.getAttribute("palabraAleatoria");

        if (numeroCuenta == null || palabraAleatoria == null) {
            model.addAttribute("mensajeError", "No se pudo obtener la información de verificación.");
            return new ModelAndView("realizarRetiroCambioMoneda");
        }

        ModelAndView modelAndView = new ModelAndView("verificarRetiroCambioMoneda");
        modelAndView.addObject("numeroCuenta", numeroCuenta);
        modelAndView.addObject("palabraAleatoria", palabraAleatoria);
        return modelAndView;
    }

    @PostMapping("/verificarRetiroCambioMoneda")
    public ModelAndView verificarRetiroCambioMoneda(
            @RequestParam("palabraIngresada") String palabraIngresada,
            @RequestParam("montoRetiro") double montoRetiro,
            HttpSession session,
            Model model) {

        String numeroCuenta = (String) session.getAttribute("numeroCuenta");
        String palabraAleatoria = (String) session.getAttribute("palabraAleatoria");

        // Verificar que la palabra ingresada coincide
        if (!palabraAleatoria.equalsIgnoreCase(palabraIngresada)) {
            int intentosFallidos = (int) model.getAttribute("intentosFallidos");
            intentosFallidos++;
            if (intentosFallidos >= 3) {
                cuentaXMLManager.inactivarCuentaRetiro(numeroCuenta);
                model.addAttribute("mensajeError", "Ha fallado tres veces. La cuenta ha sido inactivada.");
                return new ModelAndView("realizarRetiroCambioMoneda");
            }

            palabraAleatoria = generarPalabraAleatoria();
            String nombrePropietario = obtenerNombrePropietarioDeposito(numeroCuenta);
            EnviarSMS.enviarMensaje(nombrePropietario, palabraAleatoria);
            model.addAttribute("intentosFallidos", intentosFallidos);
            model.addAttribute("palabraAleatoria", palabraAleatoria);
            model.addAttribute("mensajeError", "Palabra incorrecta. Se ha enviado una nueva palabra.");
            return new ModelAndView("verificarRetiroCambioMoneda");
        }

        // Obtener el tipo de cambio actual
        TipoDeCambio.TipoDeCambioValores tipoDeCambio = TipoDeCambio.consultarTipoDeCambio();
        if (tipoDeCambio == null) {
            model.addAttribute("mensajeError", "No se pudo obtener el tipo de cambio actual. Intente más tarde.");
            return new ModelAndView("realizarRetiroCambioMoneda");
        }

        double tipoCambioVenta = Double.parseDouble(tipoDeCambio.getVenta().replace(",", "."));
        double montoEnColones = montoRetiro * tipoCambioVenta;

        if (cuentaXMLManager.buscarCuentaPorNumero(numeroCuenta).getMontoInicial() < montoEnColones) {
            model.addAttribute("mensajeError", "Fondos insuficientes para realizar el retiro.");
            return new ModelAndView("verificarRetiroCambioMoneda");
        }

        boolean exito = cuentaXMLManager.registrarRetiro(numeroCuenta, montoEnColones);
        if (exito) {
            ModelAndView modelAndView = new ModelAndView("resultadoRetiroCambioMoneda");
            modelAndView.addObject("mensaje", "Retiro realizado exitosamente.");
            modelAndView.addObject("numeroCuenta", numeroCuenta);
            modelAndView.addObject("montoRetiro", montoRetiro);
            modelAndView.addObject("montoEnColones", montoEnColones);
            modelAndView.addObject("nombrePropietario", obtenerNombrePropietarioDeposito(numeroCuenta));
            modelAndView.addObject("tipoCambio", tipoCambioVenta);

            // Limpiar atributos de sesión
            session.removeAttribute("intentosFallidos");
            session.removeAttribute("palabraAleatoria");
            session.removeAttribute("numeroCuenta");
            return modelAndView;
        } else {
            model.addAttribute("mensajeError", "Error al procesar el retiro.");
            return new ModelAndView("verificarRetiroCambioMoneda");
        }
    }

    
    @GetMapping("/consultarEstadoCuentaCambioMoneda")
    public String mostrarFormularioEstadoCuentaCambioMoneda() {
        return "consultarEstadoCuentaCambioMoneda"; // Nombre de la vista HTML para el formulario de consulta
    }

    @PostMapping("/consultarEstadoCuentaCambioMoneda")
    public ModelAndView consultarEstadoCuentaCambioMoneda(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("pinCuenta") String pinCuenta) {

        // Verificar si la cuenta existe
        Cuenta cuenta = cuentaXMLManager.cargarCuentaConTransacciones(numeroCuenta);
        Cuenta cuentaN = cuentaXMLManager.buscarCuentaPorNumero(numeroCuenta);
        if (cuenta == null) {
            ModelAndView modelAndView = new ModelAndView("consultarEstadoCuentaCambioMoneda");
            modelAndView.addObject("mensajeError", "El número de cuenta ingresado no está registrado.");
            return modelAndView;
        }

        // Verificar el PIN de la cuenta
        if (!cuenta.getPinCuenta().equals(pinCuenta)) {
            ModelAndView modelAndView = new ModelAndView("consultarEstadoCuentaCambioMoneda");
            modelAndView.addObject("mensajeError", "El PIN ingresado es incorrecto.");
            return modelAndView;
        }

        // Obtener el nombre del propietario
        String nombrePropietario = obtenerNombrePropietario(cuentaN.getIdentidadCliente());

        // Obtener el tipo de cambio actual
        TipoDeCambio.TipoDeCambioValores tipoDeCambio = TipoDeCambio.consultarTipoDeCambio();
        double tipoCambioCompra = Double.parseDouble(tipoDeCambio.getCompra().replace(",", "."));

        // Convertir el saldo de colones a dólares
        double saldoDolares = cuenta.getMontoInicial() / tipoCambioCompra;

        // Preparar la vista de respuesta con el saldo y transacciones en dólares
        ModelAndView modelAndView = new ModelAndView("resultadoConsultaEstadoCuentaCambioMoneda");
        modelAndView.addObject("numeroCuenta", cuenta.getNumeroCuenta());
        modelAndView.addObject("nombrePropietario", nombrePropietario);
        modelAndView.addObject("saldo", cuenta.getMontoInicial());
        modelAndView.addObject("saldoDolares", String.format("%.2f", saldoDolares));
        modelAndView.addObject("estatus", cuenta.getEstadoCuenta());
        modelAndView.addObject("tipoCambio", tipoCambioCompra);

        // Convertir las transacciones a dólares y pasarlas a la vista
        List<Map<String, String>> transaccionesEnDolares = new ArrayList<>();
        for (Transaccion transaccion : cuenta.getTransacciones()) {
            Map<String, String> transaccionData = new HashMap<>();
            transaccionData.put("tipo", transaccion.getTipo());
            double montoEnColones = transaccion.getMonto();
            double montoEnDolares = montoEnColones / tipoCambioCompra;
            transaccionData.put("monto", String.format("%.2f", montoEnColones));
            transaccionData.put("montoDolares", String.format("%.2f", montoEnDolares));
            transaccionData.put("fecha", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(transaccion.getFecha()));
            transaccionesEnDolares.add(transaccionData);
        }
        modelAndView.addObject("transacciones", transaccionesEnDolares);

        return modelAndView;
    }
    
    // Mostrar formulario de inactivar cuenta
    @GetMapping("/inactivarCuenta")
    public String mostrarFormularioInactivarCuenta() {
        return "inactivarCuenta"; // Nombre de la plantilla HTML para el formulario de inactivación
    }

    // Procesar inactivación de cuenta
    @PostMapping("/inactivarCuenta")
    public ModelAndView inactivarCuenta(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("pinCuenta") String pinCuenta) {

        boolean exito = cuentaXMLManager.inactivarCuenta(numeroCuenta, pinCuenta);
        ModelAndView modelAndView = new ModelAndView("resultadoInactivarCuenta");

        if (exito) {
            modelAndView.addObject("mensaje", "Cuenta inactivada exitosamente.");
            modelAndView.addObject("numeroCuenta", numeroCuenta);
        } else {
            modelAndView.addObject("mensajeError", "Error al inactivar la cuenta. Verifique el número de cuenta y el PIN.");
        }

        return modelAndView;
    }
    
    // Mostrar formulario de activar cuenta
    @GetMapping("/activarCuenta")
    public String mostrarFormularioActivarCuenta() {
        return "activarCuenta"; // Nombre de la plantilla HTML para el formulario de activación
    }

    // Procesar activación de cuenta
    @PostMapping("/activarCuenta")
    public ModelAndView activarCuenta(
            @RequestParam("numeroCuenta") String numeroCuenta,
            @RequestParam("pinCuenta") String pinCuenta) {

        boolean exito = cuentaXMLManager.activarCuenta(numeroCuenta, pinCuenta);
        ModelAndView modelAndView = new ModelAndView("resultadoActivarCuenta");

        if (exito) {
            modelAndView.addObject("mensaje", "Cuenta activada exitosamente.");
            modelAndView.addObject("numeroCuenta", numeroCuenta);
        } else {
            modelAndView.addObject("mensajeError", "Error al activar la cuenta. Verifique el número de cuenta y el PIN.");
        }

        return modelAndView;
    }


    private String generarPalabraAleatoria() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder palabra = new StringBuilder();
        Random random = new Random();

        // Genera una palabra de exactamente 7 caracteres
        for (int i = 0; i < 7; i++) {
            int index = random.nextInt(caracteres.length());
            palabra.append(caracteres.charAt(index));
        }

        return palabra.toString();
    }


}


