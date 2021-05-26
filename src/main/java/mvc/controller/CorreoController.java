package mvc.controller;

import mvc.model.Correo;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class CorreoController {
    public static void enviaCorreo(Correo correo){
        try{
            Properties properties = new Properties();
            properties.setProperty("mail.smtp.host","smtp.gmail.com");
            properties.setProperty("mail.smtp.starttls.enable","true");
            properties.setProperty("mail.smtp.port","587");
            properties.setProperty("mail.smtp.user",correo.getEnviaCorreo());
            properties.setProperty("mail.smtp.auth","true");

            Session session = Session.getDefaultInstance(properties);

            MimeMessage mail = new MimeMessage(session);

            mail.setFrom(new InternetAddress(correo.getEnviaCorreo()));
            mail.addRecipient(Message.RecipientType.TO,new InternetAddress(correo.getRecibeCorreo()));
            mail.setSubject(correo.getAsunto());
            mail.setText(correo.getMensaje());

            Transport transport = session.getTransport("smtp");
            transport.connect(correo.getEnviaCorreo(),correo.getContrasena());
            transport.sendMessage(mail,mail.getAllRecipients());
            transport.close();

        }catch (Exception e){
            System.out.println("\nEl correo no se pudo enviar\n");
            e.printStackTrace();
        }
    }
}
