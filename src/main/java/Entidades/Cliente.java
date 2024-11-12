
package Entidades;

public abstract class Cliente {

    private String nombre;
    private String categoria;
    private String telefono;
    private String correo;
    private String codigoCliente;
    private static int contadorClientes = 0;

    // Constructor, getters y setters
    public Cliente(String nombre, String categoria, String telefono, String correo) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.telefono = telefono;
        this.correo = correo;
        this.codigoCliente = generarCodigoUnico();
    }
    
    // Constructor vacío
    public Cliente() {}
    
    private String generarCodigoUnico() {
        contadorClientes++;
        return "Cliente" + contadorClientes;
    }

    // Métodos getters y setters
    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public String getCodigoCliente() {
        return codigoCliente;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCategoria(String categoriaCliente) {
        this.categoria = categoriaCliente;
    }

    public void setTelefono(String numeroTelefono) {
        this.telefono = numeroTelefono;
    }

    public void setCorreo(String direccionCorreo) {
        this.correo = direccionCorreo;
    }

    public void setCodigoCliente(String codigoCliente) {
        this.codigoCliente = codigoCliente;
    }
    
}

