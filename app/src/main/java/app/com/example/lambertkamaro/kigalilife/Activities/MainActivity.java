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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
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
    DatabaseHelper db;
    private ListView listView;
    private CustomListAdapter adapter;

    private List<AdModel> adsList;


    /** keeps track if the search bar is opened **/
    private boolean searchOpened;

    /** holds current text in the search bar **/
    private String searchQuery;

    /**  icon that shows when the search bar is closed (magnifier) **/
    private Drawable iconOpenSearch;

    /**  icon that shows when the search bar is opened (x sign) **/
    private Drawable iconCloseSearch;

    /** search bar text field **/
    private EditText searchEditText;

    /** search bar action button **/
    private MenuItem searchAction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Getting the list of movies to fill the list view.
        if (savedInstanceState != null) {
            adsList = savedInstanceState.getParcelableArrayList("ADS");
            searchOpened = savedInstanceState.getBoolean("SEARCH_OPENED");
            searchQuery = savedInstanceState.getString("SEARCH_QUERY");
        }
        else {
            // Get ads we  have in the db
            db = new DatabaseHelper(getApplicationContext());
            adsList =  db.getAllAds();
            searchOpened = false;
            searchQuery = "";
        }
        // Getting the icons.
        iconOpenSearch = getResources().getDrawable(R.drawable.abc_ic_search);
        iconCloseSearch = getResources().getDrawable(R.drawable.abc_ic_clear );

        // Initializing the list view.
        listView = (ListView) findViewById(R.id.list);



        // Setting the list adapter. We fill the adapter with filtered list
        adapter  = new CustomListAdapter(this,adsList);

        // Setting the list adapter. We fill the adapter with filtered list
        // because that is the list we want to show. The initial one we
        listView.setAdapter(adapter);

        // If the search bar was opened previously, open it on recreate.
        if (searchOpened){
            openSearchBar(searchQuery);
        }
        // Load the ads in the database.
        this.loadAds();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<AdModel> adsFiltered = (ArrayList<AdModel>) adsList;
        outState.putParcelableArrayList("ADS", adsFiltered);
        outState.putBoolean("SEARCH_OPENED", searchOpened);
        outState.putString("SEARCH_QUERY", searchQuery);
    }


    private void openSearchBar(String queryText) {

        // Set custom view on action bar.
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.search_bar);

        // Search edit text field setup.
        searchEditText = (EditText) actionBar.getCustomView().findViewById(R.id.etSearch);
        searchEditText.addTextChangedListener(new SearchWatcher());
        searchEditText.setText(queryText);
        searchEditText.requestFocus();

        // Change search icon accordingly.
        searchAction.setIcon(iconCloseSearch);
        searchOpened = true;

    }

    private void closeSearchBar() {
        // Remove custom view.
        getSupportActionBar().setDisplayShowCustomEnabled(false);
        // Change search icon accordingly.
        searchAction.setIcon(iconOpenSearch);
        adsList = db.getAllAds();
        searchOpened = false;


    }

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
        AppController.getInstance().addToRequestQueue(adsRequest);
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
    public boolean onPrepareOptionsMenu(Menu menu) {
        searchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
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
            case  R.id.action_search:
                if (searchOpened) {
                    closeSearchBar();
                } else {
                    openSearchBar(searchQuery);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Responsible for handling changes in search edit text.
     */
    private class SearchWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence c, int i, int i2, int i3) {

        }

        @Override
        public void onTextChanged(CharSequence c, int i, int i2, int i3) {
            searchQuery = searchEditText.getText().toString();
            adsList = db.searchAds(searchQuery);
        }

        @Override
        public void afterTextChanged(Editable editable) {
            searchQuery = searchEditText.getText().toString();
            adsList = db.searchAds(searchQuery);
        }

    }

}