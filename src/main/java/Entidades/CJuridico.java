
package Entidades;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class CJuridico extends Cliente {

    private String tipoNegocio;
    private String cedulaJuridica;
    private String razonSocial;
    
    // Constructor sin argumentos (recomendado para JAXB)
    public CJuridico() {}

    // Constructor completo
    public CJuridico(String nombre, String categoria, String telefono, String correo, String nombreApoderado, String tipoNegocio, String cedulaJuridica, String razonSocial) {
        super(nombre, categoria, telefono, correo);
        this.tipoNegocio = tipoNegocio;
        this.cedulaJuridica = cedulaJuridica;
        this.razonSocial = razonSocial;
    }

    // Getters y Setters
    @XmlElement
    public String getTipoNegocio() {
        return tipoNegocio;
    }

    public void setTipoNegocio(String tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
    }

    @XmlElement
    public String getCedulaJuridica() {
        return cedulaJuridica;
    }

    public void setCedulaJuridica(String cedulaJuridica) {
        this.cedulaJuridica = cedulaJuridica;
    }

    @XmlElement
    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    // Métodos de validación para el CAC
    public boolean validarTelefono(String telefono) {
        return telefono.matches("\\d{8}"); // Valida que tenga 8 dígitos
    }

    public boolean validarCorreo(String correo) {
        return correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$"); // Validación básica de correo
    }

    public boolean validarCedulaJuridica(String cedula) {
        return cedula.matches("\\d{10}"); // Ejemplo de formato de cédula jurídica en CR
    }

    public boolean validarTexto(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    
}

