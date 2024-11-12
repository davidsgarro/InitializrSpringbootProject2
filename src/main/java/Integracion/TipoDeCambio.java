package Integracion;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class TipoDeCambio {
    // Clase auxiliar para retornar los valores de compra y venta
    public static class TipoDeCambioValores {
        private String compra;
        private String venta;

        public TipoDeCambioValores(String compra, String venta) {
            this.compra = compra;
            this.venta = venta;
        }

        public String getCompra() {
            return compra;
        }

        public String getVenta() {
            return venta;
        }
    }
    
    public static TipoDeCambioValores consultarTipoDeCambio() {
        try {
            // Conectar a la página del Banco Central de Costa Rica
            String url = "https://www.bccr.fi.cr/SitePages/Inicio.aspx";
            Document doc = Jsoup.connect(url).get();

            // Extraer la tabla o los elementos que contienen el tipo de cambio
            Elements compraElement = doc.select("label#D317.datos");  // Cambia por el selector adecuado
            Elements ventaElement = doc.select("label#D318.datos");  // Cambia por el selector adecuado

            // Convertir a texto
            String compra = compraElement.text().replaceAll("[^0-9,]", "");
            String venta = ventaElement.text().replaceAll("[^0-9,]", "");

            // Retornar los valores de compra y venta
            return new TipoDeCambioValores(compra, venta);

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Retornar null si ocurre un error
        }
    }
}
