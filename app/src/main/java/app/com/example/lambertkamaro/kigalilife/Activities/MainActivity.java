package app.com.example.lambertkamaro.kigalilife.Activities;
import app.com.example.lambertkamaro.kigalilife.Adapters.CustomListAdapter;
import app.com.example.lambertkamaro.kigalilife.Controllers.AppController;
import app.com.example.lambertkamaro.kigalilife.Helpers.DatabaseHelper;
import app.com.example.lambertkamaro.kigalilife.Models.AdModel;
import app.com.example.lambertkamaro.kigalilife.Models.Movie;
import app.com.example.lambertkamaro.kigalilife.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.support.v7.app.ActionBarActivity;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

public class MainActivity extends ActionBarActivity {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

    // Movies json url
    private static final String url = "http://api.androidhive.info/json/movies.json";
    private ProgressDialog pDialog;
    private List<AdModel> adsList = new ArrayList<AdModel>();
    DatabaseHelper db;
    private ListView listView;
    private CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(getApplicationContext());

        listView = (ListView) findViewById(R.id.list);
        adapter  = new CustomListAdapter(this,db.getAllAds());
        listView.setAdapter(adapter);

//        pDialog = new ProgressDialog(this);
//        // Showing progress dialog before making http request
//        pDialog.setMessage("Loading...");
//        pDialog.show();

        // changing action bar color
        getActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
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
                        if(response.length() > 0) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            case R.id.action_new_add:
                Intent newAd = new Intent(MainActivity.this,NewAdActivity.class);
                startActivity(newAd);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}