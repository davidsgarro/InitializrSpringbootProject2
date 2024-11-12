package Integracion;

import com.twilio.Twilio;
import com.twilio.converter.Promoter;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.net.URI;
import java.math.BigDecimal;

public class EnviarSMS {
  // Find your Account Sid and Token at twilio.com/console
  public static final String ACCOUNT_SID = "AC53bf2506116928bd7c35616190b920b7";
  public static final String AUTH_TOKEN = "fee4801a38e1917129cb8f398a57c1fc";

  public static void enviarMensaje(String nombrePropietario, String palabraAleatoria) {
    // Inicializar Twilio
    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    
    // Crear el mensaje personalizado
    String mensaje = "Estimado/a " + nombrePropietario + ", su palabra de verificación es: " + palabraAleatoria;    
    // Crear y enviar el mensaje
    Message message = Message.creator(
      new com.twilio.type.PhoneNumber("+50686820880"),  // Número destino (teléfono del cliente)
      new com.twilio.type.PhoneNumber("+18647147848"),   // Número Twilio (origen)
      mensaje                                           // Mensaje personalizado
    ).create();
    
    // Imprimir el SID del mensaje para verificar
    System.out.println("Mensaje enviado. SID: " + message.getSid());
  }
}
