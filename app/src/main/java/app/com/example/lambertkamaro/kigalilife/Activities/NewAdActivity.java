package app.com.example.lambertkamaro.kigalilife.Activities;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import app.com.example.lambertkamaro.kigalilife.Adapters.ImageAdapter;
import app.com.example.lambertkamaro.kigalilife.Models.MyAdsModel;
import app.com.example.lambertkamaro.kigalilife.R;


public class NewAdActivity extends ActionBarActivity{

    private static final int REQUEST_CAMERA = 100;
    private  static  final int SELECT_FILE = 101;
    Session session = null;
    ProgressDialog progressDialog = null;
    /** Declare edit Texts inputs for our interface**/
    EditText editTextSubText, ediTextMessage;
    /** Strings **/
    String email,subject,messageBody;
    ArrayList<String> attachmentFile = new ArrayList<String>();

    ImageAdapter imageAdaptor;


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

        GridView gridView = (GridView) findViewById(R.id.grid_view);

        // Instance of ImageAdapter Class
        imageAdaptor = new ImageAdapter(this,attachmentFile);
        gridView.setAdapter(imageAdaptor);
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

            // Set global attachment path
            attachmentFile.add(destination.getAbsolutePath());
            imageAdaptor.notifyDataSetChanged();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

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
        attachmentFile.add(selectedImagePath);

        imageAdaptor.notifyDataSetChanged();
    }

    /**
     * Method to open gallery
     */
    private void selectImage() {
        // Define what will be shown on our popup
        final String add_photo = getResources().getString(R.string.add_photo);
        final String takePhoto =  getResources().getString(R.string.take_photo);
        final String fromLibrary = getResources().getString(R.string.from_library);
        final String cancel  = getResources().getString(R.string.cancel);
        final String selectFile = getResources().getString(R.string.select_file);

        final CharSequence[] items = {takePhoto,fromLibrary,cancel};

        AlertDialog.Builder builder = new AlertDialog.Builder(NewAdActivity.this);
        builder.setTitle(add_photo);
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                // Does use want to take a picture ?
                if (items[item].equals(takePhoto)) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    startActivityForResult(intent, REQUEST_CAMERA);
                }
                // User want to take image from the library
                else if (items[item].equals(fromLibrary)) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, selectFile),
                            SELECT_FILE);
                    // Use want to cancel this
                } else if (items[item].equals(cancel)) {
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
          subject = editTextSubText.getText().toString();
          messageBody = ediTextMessage.getText().toString();
        MyAdsModel ad = new MyAdsModel();

        ad.setSubject(subject);
        ad.setBody(messageBody);
        ad.setOwner("kamaroly");
        JSONArray files = new JSONArray(Arrays.asList(attachmentFile));
        ad.setFiles(files.toString());

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

//            progressDialog = ProgressDialog.show(this, "", "Sending email...", true);

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
                message.setSubject(subject);

                // If we have attachment then process it
                if (attachmentFile.size() > 0) {
                    // Create the message part
                    BodyPart messageBodyPart = new MimeBodyPart();

                    // Fill the message
                    messageBodyPart.setText(messageBody);

                    // Create a multipar message
                    Multipart multipart = new MimeMultipart("mixed");

                    // Set text message part
                    multipart.addBodyPart(messageBodyPart);

                    // Part two is attachments to be added to the mail
                    String filePath = null;

                    for (int i=0; i < attachmentFile.size(); i++){
                        filePath = attachmentFile.get(i);
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
                    message.setContent(messageBody, "text/html" );
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

            // Remove all previous used content
            ediTextMessage.setText(null);
            editTextSubText.setText(null);
            attachmentFile.clear();
            imageAdaptor.notifyDataSetChanged();
//            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Message sent",Toast.LENGTH_SHORT).show();

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_ad_menu, menu);
        super.onCreateOptionsMenu(menu);
        return true;
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
            case R.id.action_attachment:
                selectImage();
                return true;
            case R.id.action_send:
                sendMail();
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
