package PersistenciaXML;

import Entidades.CJuridico;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class ClienteJuridicoXMLManager {
    private static final String XML_FILE_PATH = "clientesJuridicos.xml";

    // Clase contenedora para la lista de clientes jurídicos (necesaria para JAXB)
    @XmlRootElement(name = "clientesJuridicos")
    private static class ClientesJuridicosWrapper {
        private List<CJuridico> clientesJuridicos;

        public ClientesJuridicosWrapper() {
            clientesJuridicos = new ArrayList<>();
        }

        @XmlElement(name = "clienteJuridico")
        public List<CJuridico> getClientesJuridicos() {
            return clientesJuridicos;
        }

        public void setClientesJuridicos(List<CJuridico> clientesJuridicos) {
            this.clientesJuridicos = clientesJuridicos;
        }
    }

    // Método para guardar la lista completa de clientes jurídicos en el archivo XML
    private void guardarListaClientesJuridicos(List<CJuridico> clientesJuridicos) {
        ClientesJuridicosWrapper wrapper = new ClientesJuridicosWrapper();
        wrapper.setClientesJuridicos(clientesJuridicos);

        try {
            JAXBContext context = JAXBContext.newInstance(ClientesJuridicosWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(wrapper, new File(XML_FILE_PATH));
            System.out.println("Lista de clientes jurídicos guardada exitosamente en el archivo XML.");
        } catch (JAXBException e) {
            e.printStackTrace();
            System.out.println("Error al guardar la lista de clientes jurídicos en el archivo XML.");
        }
    }

    // Método para guardar un solo cliente jurídico en el archivo XML (añadiéndolo a la lista)
    public void guardarClienteJuridico(CJuridico clienteJuridico) {
        List<CJuridico> clientesJuridicos = cargarClientesJuridicos();
        clientesJuridicos.add(clienteJuridico);
        guardarListaClientesJuridicos(clientesJuridicos);
    }

    // Método para cargar la lista de clientes jurídicos desde el archivo XML
    public List<CJuridico> cargarClientesJuridicos() {
        try {
            File file = new File(XML_FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }

            JAXBContext context = JAXBContext.newInstance(ClientesJuridicosWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ClientesJuridicosWrapper wrapper = (ClientesJuridicosWrapper) unmarshaller.unmarshal(file);
            return wrapper.getClientesJuridicos();
        } catch (JAXBException e) {
            e.printStackTrace();
            System.out.println("Error al cargar los clientes jurídicos desde el archivo XML.");
            return new ArrayList<>();
        }
    }
    
    // Método para actualizar el teléfono de un cliente jurídico específico
    public boolean actualizarTelefonoJuridico(String identidadCliente, String nuevoTelefono) {
        List<CJuridico> clientesJuridicos = cargarClientesJuridicos(); // Cargar la lista desde XML
        boolean clienteEncontrado = false;

        for (CJuridico cliente : clientesJuridicos) {
            if (cliente.getCedulaJuridica().equals(identidadCliente)) {
                cliente.setTelefono(nuevoTelefono); // Actualizar el teléfono
                clienteEncontrado = true;
                break;
            }
        }

        if (clienteEncontrado) {
            guardarListaClientesJuridicos(clientesJuridicos); // Guardar la lista actualizada en XML
            return true; // Actualización exitosa
        } else {
            return false; // Cliente no encontrado
        }
    }
    
    public boolean actualizarCorreoJuridico(String identidadCliente, String nuevoCorreo) {
        List<CJuridico> clientesJuridicos = cargarClientesJuridicos(); // Carga la lista de clientes jurídicos

        for (CJuridico cliente : clientesJuridicos) {
            if (cliente.getCedulaJuridica().equals(identidadCliente)) {
                cliente.setCorreo(nuevoCorreo); // Actualiza el correo del cliente
                guardarListaClientesJuridicos(clientesJuridicos); // Guarda la lista actualizada en XML
                return true; // Indica que el correo fue actualizado exitosamente
            }
        }
        return false; // Cliente no encontrado
    }
    
    public CJuridico buscarClientePorIdentidad(String identidadCliente) {
        List<CJuridico> clientesJuridicos = cargarClientesJuridicos();
        for (CJuridico cliente : clientesJuridicos) {
            if (cliente.getCedulaJuridica().equals(identidadCliente)) {
                return cliente; // Cliente encontrado
            }
        }
        return null; // Cliente no encontrado
    }

}

