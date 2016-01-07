package app.com.example.lambertkamaro.kigalilife.Fragments;

/**
 * Created by Lambert.Kamaro on 1/7/2016.
 */
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBar;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.com.example.lambertkamaro.kigalilife.Adapters.MyAdsListAdapter;
import app.com.example.lambertkamaro.kigalilife.Controllers.AppController;
import app.com.example.lambertkamaro.kigalilife.Helpers.DatabaseHelper;
import app.com.example.lambertkamaro.kigalilife.Models.MyAdsModel;
import app.com.example.lambertkamaro.kigalilife.R;

public class Fragment2 extends Fragment {

    /** Logs Tag **/
    private static final String TAG = Fragment1.class.getSimpleName();

    /**  Movies json url **/
    private static final String url = "http://api.androidhive.info/json/movies.json";

    /** Database helper **/
    DatabaseHelper db;

    /** List view for our ads **/
    private ListView listView;

    /** an initial list of ads that we search through **/
    private List<MyAdsModel> adsList;

    /** a post-search filtered list of ads **/
    private List<MyAdsModel> adsFiltered;

    /** Adaptor for our ads **/
    private MyAdsListAdapter adapter;

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
    // Get global view of our fragment
    View view;

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
        view = inflater.inflate(R.layout.fragment2, null);

        if (savedInstanceState != null) {
            adsList = savedInstanceState.getParcelableArrayList("ADS");
            searchOpened = savedInstanceState.getBoolean("SEARCH_OPENED");
            searchQuery = savedInstanceState.getString("SEARCH_QUERY");
            adsFiltered = savedInstanceState.getParcelableArrayList("ADS_FILTERED");
        }
        else {
            // Get ads we  have in the db
            db = new DatabaseHelper(currentActivity.getApplicationContext());
            adsList =  db.getAllmyAds();
            adsFiltered = adsList;
            searchOpened = false;
            searchQuery = "";
        }

        // Getting the icons.
        iconOpenSearch = getResources().getDrawable(R.drawable.abc_ic_search);
        iconCloseSearch = getResources().getDrawable(R.drawable.abc_ic_clear );

        // Initializing the list view.
        listView = (ListView) view.findViewById(R.id.list);

        // Setting the list adapter. We fill the adapter with filtered list
        adapter  = new MyAdsListAdapter(currentActivity,adsFiltered);

        // Setting the list adapter. We fill the adapter with filtered list
        // because that is the list we want to show. The initial one we
        listView.setAdapter(adapter);

        // If the search bar was opened previously, open it on recreate.
        if (searchOpened){
            openSearchBar(searchQuery);
        }

        // Load the ads in the database.
        return  view;
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
        outState.putParcelableArrayList("ADS", (ArrayList<MyAdsModel>) adsList);
        outState.putParcelableArrayList("ADS_FILTERED", (ArrayList<MyAdsModel>) adsFiltered);
        outState.putBoolean("SEARCH_OPENED", searchOpened);
        outState.putString("SEARCH_QUERY", searchQuery);
    }

    private void openSearchBar(String queryText) {

        // Set custom view on action bar.
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
        actionBar.setDisplayShowCustomEnabled(false);
        // Change search icon accordingly.
        searchAction.setIcon(iconOpenSearch);
        searchOpened = false;
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