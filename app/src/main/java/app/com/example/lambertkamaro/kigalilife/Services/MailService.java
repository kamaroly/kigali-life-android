package app.com.example.lambertkamaro.kigalilife.Services;

/**
 * Created by Lambert.Kamaro on 1/1/2016.
 */
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailService {
    private static String to ;
    private String messageTExt;
    private static  String filePath = null;

    public  MailService(String to,String messageText,String filePath){
        // Recipient's email ID needs to be mentioned.
        this.to = to;
        this.messageTExt = messageText;
        this.filePath = filePath;
    }

    public static void send() {

        // Sender's email ID needs to be mentioned
        String from = "gerageza@gmail.com";

        final String username = "gerageza";//change accordingly
        final String password = "Hard2g3t1n!";//change accordingly

        // Assuming you are sending email through smtp.gmail.com
        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.host",host);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");

        // Get the Session object.
           Session session = Session.getDefaultInstance(props, new Authenticator() {
               @Override
               protected PasswordAuthentication getPasswordAuthentication() {
                   return new PasswordAuthentication(username,password);
               }
           });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Testing Subject");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("This is message body");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filePath);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(filePath);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);

            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}