package app.com.example.lambertkamaro.kigalilife.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

import app.com.example.lambertkamaro.kigalilife.Controllers.AppController;
import app.com.example.lambertkamaro.kigalilife.Fragments.Fragment1;
import app.com.example.lambertkamaro.kigalilife.Helpers.DatabaseHelper;
import app.com.example.lambertkamaro.kigalilife.Models.AdModel;
import app.com.example.lambertkamaro.kigalilife.Models.MyAdsModel;
import app.com.example.lambertkamaro.kigalilife.Tasks.SendMailTask;

public class SyncWithServerService extends Service {
    /** Database helper **/
    DatabaseHelper db;
    SendMailTask sendMailTask;
    /** Logs Tag **/
    private static final String TAG = Fragment1.class.getSimpleName();

    /**  Movies json url **/
    private static final String url = "http://api.androidhive.info/json/movies.json";
    public SyncWithServerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       return  null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        this.db = new DatabaseHelper(getApplicationContext());

        this.loadAds();

        Toast.makeText(this, " SERVICE STARTED", Toast.LENGTH_LONG);
        Log.e(" SERVICE STARTED"," WE ARE SYNCYING PAPA");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, " SERVICE STOPPED", Toast.LENGTH_LONG);
        Log.e(" SERVICE STOPPED", " WE ARE STOPPING PAPA");
    }

    /*** LOADING ADS SECTION **/
    public  void loadAds(){
        // Creating volley request obj
        JsonArrayRequest adsRequest = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                AdModel ad  = new AdModel();
                                ad.setSubject(obj.getString("title"));
                                ad.setBody(obj.getString("title") + obj.getString("title") + obj.getString("title"));
                                ad.setMail_date(new Date().toString());
                                ad.setMessage_id("23423423");
                                ad.setOwner("kamaroly");
                                ad.setFiles(obj.getString("image"));
                                // adding ad to ads array
                                db.createAd(ad);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(adsRequest);
    }

}
