package PersistenciaXML;

import Entidades.CFisico;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class ClienteFisicoXMLManager {
    private static final String XML_FILE_PATH = "clientesFisicos.xml";

    @XmlRootElement(name = "clientesFisicos")
    private static class ClientesFisicosWrapper {
        private List<CFisico> clientesFisicos;

        public ClientesFisicosWrapper() {
            clientesFisicos = new ArrayList<>();
        }

        @XmlElement(name = "clienteFisico")
        public List<CFisico> getClientesFisicos() {
            return clientesFisicos;
        }

        public void setClientesFisicos(List<CFisico> clientesFisicos) {
            this.clientesFisicos = clientesFisicos;
        }
    }

    // Método para guardar la lista completa de clientes físicos en el archivo XML
    private void guardarListaClientesFisicos(List<CFisico> clientesFisicos) {
        ClientesFisicosWrapper wrapper = new ClientesFisicosWrapper();
        wrapper.setClientesFisicos(clientesFisicos);

        try {
            JAXBContext context = JAXBContext.newInstance(ClientesFisicosWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(wrapper, new File(XML_FILE_PATH));
            System.out.println("Lista de clientes físicos guardada exitosamente en el archivo XML.");
        } catch (JAXBException e) {
            e.printStackTrace();
            System.out.println("Error al guardar la lista de clientes físicos en el archivo XML.");
        }
    }

    // Método para guardar un solo cliente físico en el archivo XML (añadiéndolo a la lista)
    public void guardarClienteFisico(CFisico clienteFisico) {
        List<CFisico> clientesFisicos = cargarClientesFisicos();
        clientesFisicos.add(clienteFisico);
        guardarListaClientesFisicos(clientesFisicos);
    }

    // Método para cargar la lista de clientes físicos desde el archivo XML
    public List<CFisico> cargarClientesFisicos() {
        try {
            File file = new File(XML_FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }

            JAXBContext context = JAXBContext.newInstance(ClientesFisicosWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            ClientesFisicosWrapper wrapper = (ClientesFisicosWrapper) unmarshaller.unmarshal(file);
            return wrapper.getClientesFisicos();
        } catch (JAXBException e) {
            e.printStackTrace();
            System.out.println("Error al cargar los clientes físicos desde el archivo XML.");
            return new ArrayList<>();
        }
    }

    // Método para actualizar el teléfono de un cliente físico específico
    public boolean actualizarTelefonoFisico(String identidadCliente, String nuevoTelefono) {
        List<CFisico> clientesFisicos = cargarClientesFisicos(); // Cargar la lista desde XML
        boolean clienteEncontrado = false;

        for (CFisico cliente : clientesFisicos) {
            if (cliente.getIdentificacion().equals(identidadCliente)) {
                cliente.setTelefono(nuevoTelefono); // Actualizar el teléfono
                clienteEncontrado = true;
                break;
            }
        }

        if (clienteEncontrado) {
            guardarListaClientesFisicos(clientesFisicos); // Guardar la lista actualizada en XML
            return true; // Actualización exitosa
        } else {
            return false; // Cliente no encontrado
        }
    }

    // Método para actualizar el correo de un cliente físico específico
    public boolean actualizarCorreoFisico(String identidadCliente, String nuevoCorreo) {
        List<CFisico> clientesFisicos = cargarClientesFisicos(); // Carga la lista de clientes físicos
        boolean clienteEncontrado = false;

        for (CFisico cliente : clientesFisicos) {
            if (cliente.getIdentificacion().equals(identidadCliente)) {
                cliente.setCorreo(nuevoCorreo); // Actualizar el correo
                clienteEncontrado = true;
                break;
            }
        }

        if (clienteEncontrado) {
            guardarListaClientesFisicos(clientesFisicos); // Guardar la lista actualizada en XML
            return true; // Actualización exitosa
        } else {
            return false; // Cliente no encontrado
        }
    }

    // Método para buscar un cliente físico por identidad
    public CFisico buscarClientePorIdentidad(String identidadCliente) {
        List<CFisico> clientesFisicos = cargarClientesFisicos();
        for (CFisico cliente : clientesFisicos) {
            if (cliente.getIdentificacion().equals(identidadCliente)) {
                return cliente; // Cliente encontrado
            }
        }
        return null; // Cliente no encontrado
    }
}



