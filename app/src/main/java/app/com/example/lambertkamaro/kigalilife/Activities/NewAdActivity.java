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
import android.util.Log;
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
import app.com.example.lambertkamaro.kigalilife.Helpers.DatabaseHelper;
import app.com.example.lambertkamaro.kigalilife.Models.MyAdsModel;
import app.com.example.lambertkamaro.kigalilife.R;
import app.com.example.lambertkamaro.kigalilife.Tasks.SendMailTask;


public class NewAdActivity extends ActionBarActivity{

    private static final int REQUEST_CAMERA = 100;
    private  static  final int SELECT_FILE = 101;
    /** Declare edit Texts inputs for our interface**/
    EditText editTextSubText, ediTextMessage;
    /** Strings **/
    String email,subject,messageBody;
    ArrayList<String> attachmentFile = new ArrayList<String>();

    /** SETTING UP OUR IMAGE ADAPTER **/
    ImageAdapter imageAdaptor;

    /** Getting our DATABASE **/
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_ad);

        // Initiating our DATABASE HELPER
        db = new DatabaseHelper(getApplicationContext());

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
          MyAdsModel myAd = new MyAdsModel();

          myAd.setSubject(subject);
          myAd.setBody(messageBody);
          myAd.setOwner("gerageza@gmail.com");

          String filePaths;
          // Convert files to string
          JSONArray files = new JSONArray(Arrays.asList(attachmentFile));

           // Make sure no escape characters are here
           filePaths = files.toString().replaceAll("\\\\", "");
           myAd.setFiles(filePaths);
           Log.e("JSON FILES",filePaths);
          // Now insert into database
          db.createMyAd(myAd);

        try {
            SendMailTask sendMailTask = new SendMailTask(myAd);
            sendMailTask.sendMail();
            this.clearForm();
            // Go back to the mail activity
            onBackPressed();
        }
        catch (Throwable t){
            Toast.makeText(this, "Request Failed, Try again..."+t.getMessage(), Toast.LENGTH_LONG).show();
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
    /** Clear data from from **/
    private void clearForm(){
        // Remove all previous used content
        ediTextMessage.setText(null);
        editTextSubText.setText(null);
        attachmentFile.clear();
        imageAdaptor.notifyDataSetChanged();
    }
    /**
     * Show add button in the actionBar
     */
    public  void addBackButtonInActionBar(){
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
