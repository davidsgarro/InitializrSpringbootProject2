package Entidades;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CFisico extends Cliente {
    
    private String identificacion;
    private int maxCuentas;
    private String fechaNacimiento; // Cambiado a String para simplificar el almacenamiento

    public CFisico() {
        // Constructor vacío para JAXB
    }

    public CFisico(String nombre, String categoria, String telefono, String correo, String identificacion, int maxCuentas, String fechaNacimiento) {
        super(nombre, categoria, telefono, correo);
        this.identificacion = identificacion;
        this.maxCuentas = maxCuentas;
        this.fechaNacimiento = fechaNacimiento;
    }

    @XmlElement
    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    @XmlElement
    public int getMaxCuentas() {
        return maxCuentas;
    }

    public void setMaxCuentas(int maxCuentas) {
        this.maxCuentas = maxCuentas;
    }

    @XmlElement
    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }
}





