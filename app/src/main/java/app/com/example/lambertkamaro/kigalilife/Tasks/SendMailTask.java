package app.com.example.lambertkamaro.kigalilife.Tasks;

import android.os.AsyncTask;
import android.util.Log;
import java.util.ArrayList;
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

import app.com.example.lambertkamaro.kigalilife.Helpers.DatabaseHelper;
import app.com.example.lambertkamaro.kigalilife.Helpers.StringHelpers;
import app.com.example.lambertkamaro.kigalilife.Models.MyAdsModel;

/**
 * Created by Lambert.Kamaro on 1/7/2016.
 * Class to process emails ...
 */
public class SendMailTask extends AsyncTask<String,Void, String> {
    private String[] to = {"kamaroly@gmail.com"};
    private String from="gerageza@gmail.com";
    ArrayList<String> attachmentFile = new ArrayList<String>();
    Session session = null;
    /** Getting our DATABASE **/
    DatabaseHelper db;
    // Ad ID
    MyAdsModel myAd;
    public  SendMailTask(MyAdsModel myAd){
        super();
        this.myAd = myAd;
        this.initiation();
    }

    /** Method to deal with setting up connection properties **/
    public  void initiation(){
        // Set up authentication
        Properties props = new Properties();
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtp.auth", "true");

        session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("gerageza@gmail.com", "Hard2g3t1n!");
            }
        });
    }

    /** Send email passed here **/
    public  String sendMail(){
        String response = "";
        try {

            // Create a default MimeMessage object.
            Message message =  new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            InternetAddress[] addressTo = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                addressTo[i] = new InternetAddress(to[i]);
            }
            message.setRecipients(MimeMessage.RecipientType.TO, addressTo);

            // Set Subject: header field
            message.setSubject(myAd.getSubject());

            StringHelpers stringHelper = new StringHelpers();

            attachmentFile = stringHelper.jsonStringToArray(myAd.getFiles());

            // If we have attachment then process it
            if (attachmentFile.size() > 0) {
                // Create the message part
                BodyPart messageBodyPart = new MimeBodyPart();

                // Fill the message
                messageBodyPart.setText(myAd.getBody());

                // Create a multipar message
                Multipart multipart = new MimeMultipart("mixed");

                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // Part two is attachments to be added to the mail
                String filePath = null;

                for (int i=0; i < attachmentFile.size(); i++){
                    filePath = attachmentFile.get(i);

                    Log.e(" filePath",filePath);
                    DataSource source = new FileDataSource(filePath);
                    messageBodyPart = new MimeBodyPart();
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(source.getName());
                    multipart.addBodyPart(messageBodyPart);
                }

                // Send the complete message parts
                message.setContent(multipart);
            }
            else{ // We don't have attachment then send just a plain text email

                // Send the actual HTML message, as big as you like
                message.setContent(myAd.getBody(), "text/html" );
            }
            // send email
            Transport.send(message);

            // By reaching here we assume all went well
            // Let's go ahead and update the My Ad and give it sent status
            myAd.setIs_sent(1);
            db.updateMyAds(myAd);
            response = "message sent";
        }
        catch (MessagingException exception)
        {
            response = exception.getMessage();
        }
        catch (Exception exception){
            response = exception.getMessage();
        }

        return response;
    }

    @Override
    protected String doInBackground(String... strings) {

        return this.sendMail();
    }

}
