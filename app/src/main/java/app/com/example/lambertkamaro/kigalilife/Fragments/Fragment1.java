package app.com.example.lambertkamaro.kigalilife.Fragments;

/**
 * Created by Lambert.Kamaro on 1/7/2016.
 */
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import app.com.example.lambertkamaro.kigalilife.Adapters.AdsListAdapter;
import app.com.example.lambertkamaro.kigalilife.Extra.Keys.EndPointKigaliLife;
import app.com.example.lambertkamaro.kigalilife.Extra.UrlEndPoints;
import app.com.example.lambertkamaro.kigalilife.Helpers.DatabaseHelper;
import app.com.example.lambertkamaro.kigalilife.Models.AdModel;
import app.com.example.lambertkamaro.kigalilife.Network.VolleySingleton;
import app.com.example.lambertkamaro.kigalilife.R;

public class Fragment1 extends Fragment {

    /** Database helper **/
    DatabaseHelper db;

    /** List view for our ads **/
    private ListView listView;

    /** an initial list of ads that we search through **/
    private List<AdModel> adsList;

    /** a post-search filtered list of ads **/
    private List<AdModel> adsFiltered;

    /** Adaptor for our ads **/
    private AdsListAdapter adapter;

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

    /** ACTION BAR **/
    ActionBar actionBar;

    /** Current activity **/
    FragmentActivity currentActivity;

    Context applicationContext;

    ProgressDialog progress;

    private RequestQueue requestQueue;
    // Get global view of our fragment
    View view;
    private String requestUrl;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        currentActivity = getActivity();
        applicationContext = currentActivity.getApplicationContext();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get global view for this fragment
        view = inflater.inflate(R.layout.fragment1, null);
        // Get ads we  have in the db
        db = new DatabaseHelper(currentActivity.getApplicationContext());
        progress = new ProgressDialog(getActivity());

        // If We have nothing in the db show
        if (!db.hasAds()){
            progress.setTitle("Loading");
            progress.setMessage("Wait while loading...");
            progress.show();
        }

        if (savedInstanceState != null) {
            adsList = savedInstanceState.getParcelableArrayList("ADS");
            searchOpened = savedInstanceState.getBoolean("SEARCH_OPENED");
            searchQuery = savedInstanceState.getString("SEARCH_QUERY");
            adsFiltered = savedInstanceState.getParcelableArrayList("ADS_FILTERED");
        }
        else {

            adsList =  db.getAllAds();
            adsFiltered = adsList;
            searchOpened = false;
            searchQuery = "";
        }

        requestQueue = VolleySingleton.getsInstance().getRequestQueue();

        DownloadNewAdsTask task = new DownloadNewAdsTask();
        task.execute(new String[]{});
        // Initiate things here...
        initiator();


        return  view;
    }

    /**
     * Method to initiate things in this framgnet
     */
    private  void initiator(){
        // Getting the icons.
        iconOpenSearch = getResources().getDrawable(R.drawable.abc_ic_search);
        iconCloseSearch = getResources().getDrawable(R.drawable.abc_ic_clear );

        // Initializing the list view.
        listView = (ListView) view.findViewById(R.id.list);
        fillListView();
        // If the search bar was opened previously, open it on recreate.
        if (searchOpened){
            openSearchBar(searchQuery);
        }
    }

    /**
     * Method to diplay information in list view
     */
    public  void fillListView(){
        // Setting the list adapter. We fill the adapter with filtered list
        adapter  = new AdsListAdapter(currentActivity,adsFiltered);
        // Setting the list adapter. We fill the adapter with filtered list
        // because that is the list we want to show. The initial one we
        listView.setAdapter(adapter);
    }

    /**
     * Private class to download ads in the background
     */
    private class DownloadNewAdsTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            Log.e(" PAY ATTENTION "," LOADING ADS HAS Started right here in the background");
            sendJsonRequest();
            return  "Done with updating";
        }
        @Override
        protected void onPostExecute(String result) {

        }
    }

    /**
     * Downloads the ads from the web
     */
   private void sendJsonRequest(){
       JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
               getRequestUrl(),
               null,
               new Response.Listener<JSONObject>() {
                   @Override
                   public void onResponse(JSONObject response) {
                       Log.e(" WE ARE USING URL :",getRequestUrl());
                       parseJsonResponse(response);
                   }
               },
               new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError volleyError) {
                       volleyError.printStackTrace();
                   }
               });
       requestQueue.add(request);
   }



    /**
     * Privatemethod to parse the return ads json string
     * @param response
     */
    private void parseJsonResponse(JSONObject response) {
        if (response == null || response.length() == 0){
            return ;
        }
        try{
            if (response.has(EndPointKigaliLife.KEY_ADS)) {
                JSONArray arrayAds = response.getJSONArray(EndPointKigaliLife.KEY_ADS);
                for (int i=0;i < arrayAds.length(); i++){
                    AdModel ad = new AdModel();
                    JSONObject currentAd = arrayAds.getJSONObject(i);
                    String messageId = currentAd.getString(EndPointKigaliLife.KEY_ID);
                    /*
                      * If this ad is already in the db just skip it
                      */
                    if (db.adExists(messageId)){
                        continue;
                    }

                    ad.setSubject(currentAd.getString(EndPointKigaliLife.KEY_SUBJECT));
                    ad.setBody(currentAd.getString(EndPointKigaliLife.KEY_BODY));
                    ad.setMail_date(currentAd.getString(EndPointKigaliLife.KEY_TIME));
                    ad.setMessage_id(currentAd.getString(EndPointKigaliLife.KEY_ID));
                    ad.setOwner(currentAd.getString(EndPointKigaliLife.KEY_OWNER));
                    ad.setFiles(currentAd.getString(EndPointKigaliLife.KEY_IMAGES));
                    // adding ad to ads array
                    db.createAd(ad);
                    Log.e("We are adding ads to the database", ad.getOwner());

                    // Setting the list adapter. We fill the adapter with filtered list
                    adsFiltered.add(ad);

                    // If we have progress bar on the dismiss it since we have data
                    if (progress.isShowing()) {
                        progress.dismiss();
                    }

                    // if loading is done the update list view
                    fillListView();
                }

            }
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    /** OPTIONS MENU CREATION **/
    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }


    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        searchAction = menu.findItem(R.id.action_search);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
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

    /** SEARCHING SECTION **/
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("ADS", (ArrayList<AdModel>) adsList);
        outState.putParcelableArrayList("ADS", (ArrayList<AdModel>) adsFiltered);
        outState.putBoolean("SEARCH_OPENED", searchOpened);
        outState.putString("SEARCH_QUERY", searchQuery);
    }

    private void openSearchBar(String queryText) {


        try
        {
            // Set custom view on action bar.
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.search_bar);

            // Search edit text field setup.
            searchEditText = (EditText) actionBar.getCustomView().findViewById(R.id.etSearch);
            searchEditText.addTextChangedListener(new SearchWatcher());
            searchEditText.setText(queryText);


            // Change search icon accordingly.
            searchAction.setIcon(iconCloseSearch);
            searchOpened = true;
        }
        catch (Exception e){
            Log.e("Fragment 1 Error:", e.getMessage());
        }

    }

    private void closeSearchBar() {
        // Remove custom view.
        actionBar.setDisplayShowCustomEnabled(false);
        // Change search icon accordingly.
        searchAction.setIcon(iconOpenSearch);
        searchOpened = false;
    }

    /**
     * Get the url for the api
     * @return url for the API
     */
    public String getRequestUrl() {

        requestUrl = UrlEndPoints.URL_KIGALI_LIFE
                    + db.getLatestAdId()
                +UrlEndPoints.URL_CHAR_QUESTION
                    +UrlEndPoints.URL_PARAM_API_KEY
                    +UrlEndPoints.URL_VALUE_API_KEY;


        return requestUrl;
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

        }

        @Override
        public void afterTextChanged(Editable editable) {
            searchQuery = searchEditText.getText().toString();
            adapter.filter(searchQuery);
        }
    }


}