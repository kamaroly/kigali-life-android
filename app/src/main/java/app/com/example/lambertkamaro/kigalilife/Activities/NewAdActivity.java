package app.com.example.lambertkamaro.kigalilife.Activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import app.com.example.lambertkamaro.kigalilife.R;
import app.com.example.lambertkamaro.kigalilife.Services.MailService;

import static javax.mail.internet.InternetAddress.parse;

public class NewAdActivity extends ActionBarActivity implements OnClickListener{

    private static final int REQUEST_CAMERA = 100;
    private  static  final int SELECT_FILE = 101;
    Session session = null;
    ProgressDialog progressDialog = null;
    /** Declare edit Texts inputs for our interface**/
    EditText editTextSubText, ediTextMessage;
    ImageView ivImage;

    /** Declaring buttons **/
    Button   buttonSend,buttonAttachment;
    /** Strings **/
    String email,subject,message;
    String attachmentFile = null;


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

        ivImage = (ImageView) findViewById(R.id.ivImage);

        buttonSend.setOnClickListener(this);
        buttonAttachment.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {

                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                this.getImageFromCamera(thumbnail);
            }else if (requestCode == SELECT_FILE) {

                this.getImageFromGallery(data);
            }
         }
        }

    /**
     * This method handles image picked from the camera on activity result
     * @param data
     */
    public  void getImageFromCamera(Bitmap data)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        data.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        String filename = System.currentTimeMillis() + ".jpg";
        File destination = new File(Environment.getExternalStorageDirectory(),filename);

        FileOutputStream fo;

        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(data);
        // Set global attachment path
        attachmentFile = filename;
    }

    /**
     * This method handles image picked from the mediaStore
     * @param data
     */
    public  void getImageFromGallery(Intent data){
        Uri selectedImageUri = data.getData();
        String[] projection = {MediaStore.MediaColumns.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                null);
        Cursor cursor = cursorLoader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        String selectedImagePath = cursor.getString(column_index);

        Bitmap bm;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(selectedImagePath, options);

        // Set global attachment path
        attachmentFile = selectedImagePath;

        final int REQUIRED_SIZE = 200;

        int scale = 1;

        while (options.outWidth / scale / 2 >= REQUIRED_SIZE
                && options.outHeight / scale / 2 >= REQUIRED_SIZE)
            scale *= 2;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bm = BitmapFactory.decodeFile(selectedImagePath, options);

        ivImage.setImageBitmap(bm);
    }

    /**
     * Method to open gallery
     */
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(NewAdActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item)
            {
                // Does use want to take a picture ?
                if (items[item].equals("Take Photo"))
                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
                // User want to take image from the library
                else if (items[item].equals("Choose from Library"))
                {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                    // Use want to cancel this
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }



    /**
     * Sending Email method
     */
    public  void  sendMail(){
          email = "kamaroly@gmail.com";
          subject = "Sending new ADs";
          message = "Testing message for android kigali life";

        try {

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
     * Class to process images
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
                // Create the message part
                messageBodyPart.setText("Body of Java android app");
                // Create a multipart message
                Multipart multipart = new MimeMultipart();
                // Set text message part
                multipart.addBodyPart(messageBodyPart);

                // if we have attachment then add it here
                if (attachmentFile != null) {
                    // Part two is attachment
                    DataSource source = new FileDataSource(attachmentFile);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(attachmentFile);
                    multipart.addBodyPart(messageBodyPart);
                }
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
            selectImage();
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
