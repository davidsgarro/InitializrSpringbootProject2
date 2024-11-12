package PersistenciaXML;

import Entidades.CFisico;
import Entidades.CJuridico;
import Entidades.Cuenta;
import Entidades.Transaccion;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Comparator;
import java.util.Date;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.NodeList;

public class CuentaXMLManager {
    private static final String XML_FILE_PATH = "cuentas.xml";
    private static int contadorCuentas = 1;
    // Instancias de los managers
    private ClienteFisicoXMLManager clienteFisicoXMLManager = new ClienteFisicoXMLManager();
    private ClienteJuridicoXMLManager clienteJuridicoXMLManager = new ClienteJuridicoXMLManager();

    @XmlRootElement(name = "cuentas")
    private static class CuentasWrapper {
        private List<Cuenta> cuentas = new ArrayList<>();

        @XmlElement(name = "cuenta")
        public List<Cuenta> getCuentas() {
            return cuentas;
        }

        public void setCuentas(List<Cuenta> cuentas) {
            this.cuentas = cuentas;
        }
    }

    public void guardarCuenta(Cuenta cuenta) {
        List<Cuenta> cuentas = cargarCuentas();
        cuentas.add(cuenta);
        CuentasWrapper wrapper = new CuentasWrapper();
        wrapper.setCuentas(cuentas);

        try {
            JAXBContext context = JAXBContext.newInstance(CuentasWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(wrapper, new File(XML_FILE_PATH));
        } catch (JAXBException e) {
            e.printStackTrace();
            System.out.println("Error al guardar la cuenta en el archivo XML.");
        }
    }

    public List<Cuenta> cargarCuentas() {
        try {
            File file = new File(XML_FILE_PATH);
            if (!file.exists()) {
                return new ArrayList<>();
            }

            JAXBContext context = JAXBContext.newInstance(CuentasWrapper.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            CuentasWrapper wrapper = (CuentasWrapper) unmarshaller.unmarshal(file);
            return wrapper.getCuentas();
        } catch (JAXBException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public String generarNumeroCuenta() {
        return "CTA" + (contadorCuentas++);
    }

    // Método auxiliar para agregar cuenta si el límite no se ha alcanzado
    public String agregarCuentaSiPosible(CFisico clienteFisico, double montoInicial, String pinCuenta) {
        // Cargar todas las cuentas existentes
        List<Cuenta> cuentas = cargarCuentas();

        // Filtrar las cuentas del cliente específico
        List<Cuenta> cuentasCliente = new ArrayList<>();
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getIdentidadCliente().equals(clienteFisico.getIdentificacion())) {
                cuentasCliente.add(cuenta);
            }
        }

        // Verificar si se alcanzó el límite de cuentas
        if (cuentasCliente.size() >= clienteFisico.getMaxCuentas()) {
            System.out.println("El cliente ha alcanzado el límite máximo de cuentas: " + clienteFisico.getMaxCuentas());
            return "El cliente ha alcanzado el límite máximo de cuentas.";
        }

        // Crear y guardar la nueva cuenta
        String numeroCuenta = generarNumeroCuenta();
        Cuenta nuevaCuenta = new Cuenta(clienteFisico.getIdentificacion(), numeroCuenta, "Activa", montoInicial, pinCuenta);
        cuentas.add(nuevaCuenta); // Agregar la cuenta a la lista general
        guardarListaCuentas(cuentas); // Guardar la lista actualizada en XML

        System.out.println("Cuenta creada exitosamente para el cliente: " + clienteFisico.getIdentificacion());
        return "Cuenta creada exitosamente. Número de cuenta: " + numeroCuenta;
    }
    
    private void guardarListaCuentas(List<Cuenta> cuentas) {
        CuentasWrapper wrapper = new CuentasWrapper();
        wrapper.setCuentas(cuentas);

        try {
            JAXBContext context = JAXBContext.newInstance(CuentasWrapper.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(wrapper, new File(XML_FILE_PATH));
            System.out.println("Lista de cuentas guardada exitosamente en el archivo XML.");
        } catch (JAXBException e) {
            e.printStackTrace();
            System.out.println("Error al guardar la lista de cuentas en el archivo XML.");
        }
    }
    
    public boolean cambiarPinCuenta(String numeroCuenta, String pinActual, String nuevoPin) {
        List<Cuenta> cuentas = cargarCuentas(); // Cargar todas las cuentas desde el XML

        for (Cuenta cuenta : cuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta) && cuenta.getPinCuenta().equals(pinActual)) {
                // Si el número de cuenta y el PIN actual coinciden
                cuenta.setPinCuenta(nuevoPin); // Cambiar el PIN
                guardarListaCuentas(cuentas); // Guardar la lista actualizada en el XML
                return true; // Retorna true si el cambio fue exitoso
            }
        }
        return false; // Retorna false si la cuenta no fue encontrada o el PIN actual no coincide
    }
    
    public String eliminarCuenta(String numeroCuenta, String pinCuenta) {
        List<Cuenta> cuentas = cargarCuentas();
        Cuenta cuentaAEliminar = null;

        // Buscar la cuenta por el número y validar el PIN
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta) && cuenta.getPinCuenta().equals(pinCuenta)) {
                cuentaAEliminar = cuenta;
                break;
            }
        }

        if (cuentaAEliminar == null) {
            return "Cuenta no encontrada o PIN incorrecto. No se puede eliminar la cuenta.";
        }

        // Confirmación antes de la eliminación
        // En este paso, normalmente harías que el usuario confirme la eliminación,
        // pero aquí estamos asumiendo que la confirmación ya ha sido obtenida.

        // Verificar si el saldo de la cuenta es cero antes de eliminar
        //if (cuentaAEliminar.getMontoInicial() > 0) {
        //    return "La cuenta tiene saldo. No se puede eliminar hasta que el saldo sea cero.";
        //}

        // Eliminar la cuenta
        cuentas.remove(cuentaAEliminar);
        guardarListaCuentas(cuentas); // Guardar la lista actualizada en el XML

        return "Cuenta eliminada exitosamente. Número de cuenta: " + numeroCuenta;
    }
    
    public List<Cuenta> obtenerCuentasOrdenadasPorSaldo() {
        // Cargar todas las cuentas desde el archivo XML
        List<Cuenta> cuentas = cargarCuentas();

        // Ordenar las cuentas por saldo de forma ascendente
        return cuentas.stream()
                      .sorted(Comparator.comparingDouble(Cuenta::getMontoInicial))
                      .collect(Collectors.toList());
    }
    
    public List<Map<String, String>> obtenerCuentasConDetalles() {
        List<Cuenta> cuentasOrdenadas = obtenerCuentasOrdenadasPorSaldo();
        List<Map<String, String>> detallesCuentas = new ArrayList<>();

        for (Cuenta cuenta : cuentasOrdenadas) {
            Map<String, String> detalles = new HashMap<>();
            detalles.put("numeroCuenta", cuenta.getNumeroCuenta());
            detalles.put("estadoCuenta", cuenta.getEstadoCuenta());
            detalles.put("saldo", String.format("%.2f", cuenta.getMontoInicial()));

            // Buscar nombre del propietario
            String identidadCliente = cuenta.getIdentidadCliente();
            String nombrePropietario = "";  // Valor por defecto

            CFisico clienteFisico = clienteFisicoXMLManager.buscarClientePorIdentidad(identidadCliente);
            if (clienteFisico != null) {
                nombrePropietario = clienteFisico.getNombre();
            } else {
                CJuridico clienteJuridico = clienteJuridicoXMLManager.buscarClientePorIdentidad(identidadCliente);
                if (clienteJuridico != null) {
                    nombrePropietario = clienteJuridico.getNombre();
                }
            }

            detalles.put("nombrePropietario", nombrePropietario);
            detallesCuentas.add(detalles);
        }

        return detallesCuentas;
    }
    
    public List<Cuenta> obtenerCuentasPorIdentificacion(String identidadCliente) {
        List<Cuenta> todasLasCuentas = cargarCuentas();
        List<Cuenta> cuentasDelCliente = new ArrayList<>();

        for (Cuenta cuenta : todasLasCuentas) {
            if (cuenta.getIdentidadCliente().equals(identidadCliente)) {
                cuentasDelCliente.add(cuenta);
            }
        }
        return cuentasDelCliente;
    }
    
    public Cuenta buscarCuentaPorNumero(String numeroCuenta) {
        // Cargar todas las cuentas desde el XML
        List<Cuenta> cuentas = cargarCuentas();

        // Buscar la cuenta con el número especificado
        for (Cuenta cuenta : cuentas) {
            if (cuenta.getNumeroCuenta().equals(numeroCuenta)) {
                return cuenta;
            }
        }
        // Si no se encuentra la cuenta, retornar null
        return null;
    }
    
    public boolean registrarDeposito(String numeroCuenta, double monto) {
        try {
            // Crear un constructor de documentos para cargar el XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(XML_FILE_PATH));

            // Obtener todas las cuentas en el XML
            NodeList listaCuentas = doc.getElementsByTagName("cuenta");

            // Buscar la cuenta en el XML por su número
            for (int i = 0; i < listaCuentas.getLength(); i++) {
                Element cuentaElement = (Element) listaCuentas.item(i);

                // Verificar si el número de cuenta coincide
                String numeroEnXML = cuentaElement.getElementsByTagName("numeroCuenta").item(0).getTextContent();
                if (numeroEnXML.equals(numeroCuenta)) {
                    // Actualizar el saldo de la cuenta
                    Element saldoElement = (Element) cuentaElement.getElementsByTagName("montoInicial").item(0);
                    double saldoActual = Double.parseDouble(saldoElement.getTextContent());
                    saldoElement.setTextContent(String.valueOf(saldoActual + monto));

                    // Crear el elemento transacción y agregarlo directamente bajo el elemento <cuenta>
                    Element transaccionElement = doc.createElement("transaccion");
                    transaccionElement.setAttribute("tipo", "deposito");
                    transaccionElement.setAttribute("monto", String.valueOf(monto));
                    transaccionElement.setAttribute("fecha", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    cuentaElement.appendChild(transaccionElement);

                    // Guardar los cambios en el XML
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(XML_FILE_PATH));
                    transformer.transform(source, result);

                    return true; // Depósito registrado con éxito
                }
            }

            System.out.println("No se encontró la cuenta con el número: " + numeroCuenta);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public Cuenta cargarCuentaConTransacciones(String numeroCuenta) {
        try {
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File(XML_FILE_PATH));
            NodeList listaCuentas = doc.getElementsByTagName("cuenta");

            for (int i = 0; i < listaCuentas.getLength(); i++) {
                Element cuentaElement = (Element) listaCuentas.item(i);
                String numeroEnXML = cuentaElement.getElementsByTagName("numeroCuenta").item(0).getTextContent();

                if (numeroCuenta.equals(numeroEnXML)) {
                    // Crear la cuenta
                    Cuenta cuenta = new Cuenta();
                    cuenta.setNumeroCuenta(numeroCuenta);
                    cuenta.setEstadoCuenta(cuentaElement.getElementsByTagName("estadoCuenta").item(0).getTextContent());
                    cuenta.setMontoInicial(Double.parseDouble(cuentaElement.getElementsByTagName("montoInicial").item(0).getTextContent()));
                    cuenta.setPinCuenta(cuentaElement.getElementsByTagName("pinCuenta").item(0).getTextContent());

                    // Cargar las transacciones
                    NodeList transaccionesList = cuentaElement.getElementsByTagName("transaccion");
                    List<Transaccion> transacciones = new ArrayList<>();

                    for (int j = 0; j < transaccionesList.getLength(); j++) {
                        Element transaccionElement = (Element) transaccionesList.item(j);
                        String tipo = transaccionElement.getAttribute("tipo");
                        double monto = Double.parseDouble(transaccionElement.getAttribute("monto"));
                        String fechaStr = transaccionElement.getAttribute("fecha");
                        Date fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(fechaStr);

                        Transaccion transaccion = new Transaccion(tipo, monto, fecha);
                        transacciones.add(transaccion);
                    }

                    cuenta.setTransacciones(transacciones); // Asignar las transacciones a la cuenta
                    return cuenta;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public boolean registrarRetiro(String numeroCuenta, double monto) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(XML_FILE_PATH));

            NodeList listaCuentas = doc.getElementsByTagName("cuenta");

            for (int i = 0; i < listaCuentas.getLength(); i++) {
                Element cuentaElement = (Element) listaCuentas.item(i);
                String numeroEnXML = cuentaElement.getElementsByTagName("numeroCuenta").item(0).getTextContent();
                if (numeroEnXML.equals(numeroCuenta)) {
                    Element saldoElement = (Element) cuentaElement.getElementsByTagName("montoInicial").item(0);
                    double saldoActual = Double.parseDouble(saldoElement.getTextContent());

                    if (saldoActual < monto) return false;

                    saldoElement.setTextContent(String.valueOf(saldoActual - monto));

                    Element transaccionElement = doc.createElement("transaccion");
                    transaccionElement.setAttribute("tipo", "retiro");
                    transaccionElement.setAttribute("monto", String.valueOf(monto));
                    transaccionElement.setAttribute("fecha", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                    cuentaElement.appendChild(transaccionElement);

                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(XML_FILE_PATH));
                    transformer.transform(source, result);

                    return true;
                }
            }
            
            System.out.println("No se encontró la cuenta con el número: " + numeroCuenta);
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean inactivarCuenta(String numeroCuenta, String pinCuenta) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(XML_FILE_PATH));

            NodeList listaCuentas = doc.getElementsByTagName("cuenta");

            for (int i = 0; i < listaCuentas.getLength(); i++) {
                Element cuentaElement = (Element) listaCuentas.item(i);

                String numeroEnXML = cuentaElement.getElementsByTagName("numeroCuenta").item(0).getTextContent();
                String pinEnXML = cuentaElement.getElementsByTagName("pinCuenta").item(0).getTextContent();

                if (numeroEnXML.equals(numeroCuenta) && pinEnXML.equals(pinCuenta)) {
                    // Cambiar el estado de la cuenta a "Inactiva"
                    Element estadoElement = (Element) cuentaElement.getElementsByTagName("estadoCuenta").item(0);
                    estadoElement.setTextContent("Inactiva");

                    // Guardar los cambios en el XML
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(XML_FILE_PATH));
                    transformer.transform(source, result);

                    return true; // Éxito en la inactivación
                }
            }

            return false; // Cuenta no encontrada o PIN incorrecto
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean activarCuenta(String numeroCuenta, String pinCuenta) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(XML_FILE_PATH));

            NodeList listaCuentas = doc.getElementsByTagName("cuenta");

            for (int i = 0; i < listaCuentas.getLength(); i++) {
                Element cuentaElement = (Element) listaCuentas.item(i);

                String numeroEnXML = cuentaElement.getElementsByTagName("numeroCuenta").item(0).getTextContent();
                String pinEnXML = cuentaElement.getElementsByTagName("pinCuenta").item(0).getTextContent();

                if (numeroEnXML.equals(numeroCuenta) && pinEnXML.equals(pinCuenta)) {
                    // Cambiar el estado de la cuenta a "Activa"
                    Element estadoElement = (Element) cuentaElement.getElementsByTagName("estadoCuenta").item(0);
                    estadoElement.setTextContent("Activa");

                    // Guardar los cambios en el XML
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(XML_FILE_PATH));
                    transformer.transform(source, result);

                    return true; // Éxito en la activación
                }
            }

            return false; // Cuenta no encontrada o PIN incorrecto
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean inactivarCuentaRetiro(String numeroCuenta) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(XML_FILE_PATH));

            NodeList listaCuentas = doc.getElementsByTagName("cuenta");

            for (int i = 0; i < listaCuentas.getLength(); i++) {
                Element cuentaElement = (Element) listaCuentas.item(i);

                String numeroEnXML = cuentaElement.getElementsByTagName("numeroCuenta").item(0).getTextContent();

                if (numeroEnXML.equals(numeroCuenta)) {
                    // Cambiar el estado de la cuenta a "Inactiva"
                    Element estadoElement = (Element) cuentaElement.getElementsByTagName("estadoCuenta").item(0);
                    estadoElement.setTextContent("Inactiva");

                    // Guardar los cambios en el XML
                    Transformer transformer = TransformerFactory.newInstance().newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult result = new StreamResult(new File(XML_FILE_PATH));
                    transformer.transform(source, result);

                    return true; // Éxito en la inactivación
                }
            }

            return false; // Cuenta no encontrada
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



}


