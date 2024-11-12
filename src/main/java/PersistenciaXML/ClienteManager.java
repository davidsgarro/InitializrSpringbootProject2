package PersistenciaXML;

import Entidades.CFisico;
import Entidades.CJuridico;
import PersistenciaXML.ClienteFisicoXMLManager;
import PersistenciaXML.ClienteJuridicoXMLManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClienteManager {
    private ClienteFisicoXMLManager clienteFisicoXMLManager = new ClienteFisicoXMLManager();
    private ClienteJuridicoXMLManager clienteJuridicoXMLManager = new ClienteJuridicoXMLManager();

    public List<Map<String, String>> obtenerClientesConDetalles() {
        // Cargar clientes físicos y jurídicos
        List<CFisico> clientesFisicos = clienteFisicoXMLManager.cargarClientesFisicos();
        List<CJuridico> clientesJuridicos = clienteJuridicoXMLManager.cargarClientesJuridicos();

        List<Map<String, String>> detallesClientes = new ArrayList<>();

        // Agregar detalles de clientes físicos
        for (CFisico cliente : clientesFisicos) {
            Map<String, String> detalles = new HashMap<>();
            detalles.put("nombre", cliente.getNombre());
            detalles.put("correo", cliente.getCorreo());
            detalles.put("telefono", cliente.getTelefono());
            detallesClientes.add(detalles);
        }

        // Agregar detalles de clientes jurídicos
        for (CJuridico cliente : clientesJuridicos) {
            Map<String, String> detalles = new HashMap<>();
            detalles.put("nombre", cliente.getNombre());
            detalles.put("correo", cliente.getCorreo());
            detalles.put("telefono", cliente.getTelefono());
            detallesClientes.add(detalles);
        }

        // Ordenar alfabéticamente por el nombre
        return detallesClientes.stream()
                .sorted(Comparator.comparing(cliente -> cliente.get("nombre")))
                .collect(Collectors.toList());
    }
}
