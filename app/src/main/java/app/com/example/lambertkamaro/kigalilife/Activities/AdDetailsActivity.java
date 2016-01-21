package app.com.example.lambertkamaro.kigalilife.Activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import app.com.example.lambertkamaro.kigalilife.Adapters.GridViewAdapter;
import app.com.example.lambertkamaro.kigalilife.Helpers.DatabaseHelper;
import app.com.example.lambertkamaro.kigalilife.Models.AdModel;
import app.com.example.lambertkamaro.kigalilife.R;

public class AdDetailsActivity extends ActionBarActivity {

    DatabaseHelper db;
    ArrayList<String> imagesList;
    GridViewAdapter imageAdaptor;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);
        // Get ads we  have in the db
        db = new DatabaseHelper(getApplicationContext());

        gridView = (GridView) findViewById(R.id.gridView);

        this.addBackButtonInActionBar();
        this.details();
    }

    /**
     * Get details for the selected Ad
     */
    public void  details()
    {
        TextView memberName = (TextView)findViewById(R.id.member_name);
        TextView body  = (TextView)findViewById(R.id.body);

        Intent item = getIntent();
        String message_id = item.getStringExtra("message_id");

        AdModel ad = db.getAdByMessageId(message_id);
        /** Change this activity title to the current name **/
        String dateString = ad.getCreated_at().toString();
        String title = ad.getOwner();
        title += " at "+ dateString;
        setTitle( title );
        /** Update activity layout **/
        memberName.setText(ad.getSubject());
        body.setText(ad.getBody());

        // Set global attachment path
        // thumbnail
        String images = ad.getFiles();
        String image = null;
        Log.e("FOUND IMAGES ",images);
        imagesList = new ArrayList<String>();
        try {
            JSONArray arrayImages = new JSONArray(images);
            for (int i= 0 ; i <= arrayImages.length();i++){
                image = arrayImages.get(i).toString();
                Log.e(" IMAGES FOUND ",image);
                imagesList.add(image);
            }
        }catch (JSONException e){
            Log.e("ERROR OCCURED", e.getMessage());
        }

        if (!imagesList.isEmpty()) {
            // Instance of ImageAdapter Class
            imageAdaptor = new GridViewAdapter(this, imagesList);
            gridView.setAdapter(imageAdaptor);
        }
    }


    public static String convertDate(String dateInMilliseconds,String dateFormat) {
        return DateFormat.format(dateFormat, Long.parseLong(dateInMilliseconds)).toString();
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
