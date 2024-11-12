package Controladores; // Asegúrate de que este paquete sea correcto

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

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
    

}

