package Entidades;

import java.util.ArrayList;
import java.util.List;

public class Cuenta {
    private String identidadCliente;
    private String numeroCuenta;
    private String estadoCuenta;
    private double montoInicial;
    private String pinCuenta;
    private List<Transaccion> transacciones;
    
    // Constructor sin parámetros
    public Cuenta() {
        // Constructor vacío para JAXB
    }

    // Constructor
    public Cuenta(String identidadCliente, String numeroCuenta, String estadoCuenta, double montoInicial, String pinCuenta) {
        this.identidadCliente = identidadCliente;
        this.numeroCuenta = numeroCuenta;
        this.estadoCuenta = estadoCuenta;
        this.montoInicial = montoInicial;
        this.pinCuenta = pinCuenta;
        this.transacciones = new ArrayList<>();
    }
    
    // Método para obtener las transacciones
    public List<Transaccion> getTransacciones() {
        return transacciones;
    }
    
    public void setTransacciones(List<Transaccion> transacciones) {
        this.transacciones = transacciones;
    }


    // Método para agregar una transacción
    public void agregarTransaccion(Transaccion transaccion) {
        transacciones.add(transaccion);
    }

    // Getters y Setters
    public String getIdentidadCliente() {
        return identidadCliente;
    }

    public void setIdentidadCliente(String identidadCliente) {
        this.identidadCliente = identidadCliente;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getEstadoCuenta() {
        return estadoCuenta;
    }

    public void setEstadoCuenta(String estadoCuenta) {
        this.estadoCuenta = estadoCuenta;
    }

    public double getMontoInicial() {
        return montoInicial;
    }

    public void setMontoInicial(double montoInicial) {
        this.montoInicial = montoInicial;
    }

    public String getPinCuenta() {
        return pinCuenta;
    }

    public void setPinCuenta(String pinCuenta) {
        this.pinCuenta = pinCuenta;
    }
}

