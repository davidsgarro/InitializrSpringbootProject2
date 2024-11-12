package Entidades;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Transaccion {
    private String tipo;
    private double monto;
    private Date fecha;
    

    public Transaccion(String tipo, double monto, Date fecha) {
        this.tipo = tipo;
        this.monto = monto;
        this.fecha = fecha;
    }

    // Getters
    public String getTipo() {
        return tipo;
    }

    public double getMonto() {
        return monto;
    }

    public Date getFecha() {
        return fecha;
    }

    // Método toString
    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return "Tipo: " + tipo + ", Monto: " + monto + ", Fecha: " + sdf.format(fecha);
    }
}
