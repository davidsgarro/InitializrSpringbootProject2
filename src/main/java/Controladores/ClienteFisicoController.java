package Controladores;

import Entidades.CFisico;
import PersistenciaXML.ClienteFisicoXMLManager;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/crearClienteFisico")
public class ClienteFisicoController {

    private final ClienteFisicoXMLManager clienteFisicoXMLManager = new ClienteFisicoXMLManager();

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
            // Crea un nuevo cliente físico
            CFisico nuevoCliente = new CFisico(nombre, categoria, telefono, correo, identificacion, maxCuentas, fechaNacimientoStr);
            clienteFisicoXMLManager.guardarClienteFisico(nuevoCliente);

            // Vista de éxito y añade los atributos
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
}





