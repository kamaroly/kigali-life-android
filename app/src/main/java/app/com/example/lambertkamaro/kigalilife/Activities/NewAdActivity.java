package app.com.example.lambertkamaro.kigalilife.Activities;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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

import java.util.Date;
import java.util.Properties;

import app.com.example.lambertkamaro.kigalilife.R;

import static javax.mail.internet.InternetAddress.parse;

public class NewAdActivity extends ActionBarActivity implements OnClickListener{

    Session session = null;
    ProgressDialog progressDialog = null;
    /** Declare edit Texts inputs for our interface**/
    EditText editTextSubText, ediTextMessage;

    /** Declaring buttons **/
    Button   buttonSend,buttonAttachment;
    /** Strings **/
    String email,subject,message,attachmentFile;

    Uri URI = null;

    private  static  final int PICK_FROM_GALLERY = 101;

    int columnIndex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);
        this.addBackButtonInActionBar();

        this.newAdManager();
    }

    public void newAdManager(){
        editTextSubText = (EditText) findViewById(R.id.editTextSubject);
        ediTextMessage  = (EditText) findViewById(R.id.editTextMessage);

        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonAttachment = (Button) findViewById(R.id.buttonAttachment);

        buttonSend.setOnClickListener(this);
        buttonAttachment.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //Check if we need to pick something from the gallery
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK){

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,filePathColumn,null,null,null);
            cursor.moveToFirst();

            columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            attachmentFile = cursor.getString(columnIndex);

            Log.e("Attachment path",attachmentFile);

            URI = Uri.parse("file://"+attachmentFile);
            cursor.close();
        }
    }

    /**
     * Method to open gallery
     */
    public  void openGallery(){
     Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.putExtra("return-data", true);

        startActivityForResult(Intent.createChooser(galleryIntent,"Complete action using"),PICK_FROM_GALLERY);
    }

    /**
     * Sending Email method
     */
    public  void  sendMail(){
        try {
            email = "kamaroly@gmail.com";
            subject = editTextSubText.getText().toString();
            message = ediTextMessage.getText().toString();

            // Make an intent to send email
            final Intent emailIntent = new Intent(Intent.ACTION_SEND);
            emailIntent.setType("plain/text");
            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
            emailIntent.putExtra(Intent.EXTRA_SUBJECT,subject);

            // If we have an attachment let's add it
            if (URI != null){
                emailIntent.putExtra(Intent.EXTRA_STREAM,URI);
            }

            // Add message
            emailIntent.putExtra(Intent.EXTRA_TEXT, message);

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

            progressDialog = ProgressDialog.show(this, "", "Sending email...", true);

            RetrieveFeedTask sendMailTask = new RetrieveFeedTask();
            sendMailTask.execute();
        }
        catch (Throwable t){
            Toast.makeText(this, "Request Failed, Try again..."+t.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     *
     */
    public class RetrieveFeedTask extends AsyncTask<String,Void, String> {
        private String[] to = {"kamaroly@gmail.com"};
        private String from="gerageza@gmail.com";

        @Override
        protected String doInBackground(String... strings) {
            try {

                Message message =  new MimeMessage(session);
                message.setFrom(new InternetAddress(from));

                InternetAddress[] addressTo = new InternetAddress[to.length];

                for (int i = 0; i < to.length; i++) {
                    addressTo[i] = new InternetAddress(to[i]);
                }
                message.setRecipients(MimeMessage.RecipientType.TO, addressTo);

                message.setSubject("Testing kigalilife android app");
                message.setSentDate(new Date());

                // setup message body
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setText("Body of Java android app");

                // Put parts in message
                Log.i("From ",from);
                Log.i("To ",to[0]);
                Log.i("Message ","Body of Java android app");


                // send email
                Transport.send(message);
            }
            catch (MessagingException exception)
            {
                exception.printStackTrace();

            }
            catch (Exception exception){
                exception.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onPostExecute(String results) {
            progressDialog.dismiss();

            Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onClick(View view) {
        // Determine the clicked on view
        if (view == buttonAttachment){
            openGallery();
        }

        if (view == buttonSend){
            sendMail();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * Show add button in the actionBar
     */
    public  void addBackButtonInActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }


}
