package app.com.example.lambertkamaro.kigalilife.Fragments;

/**
 * Created by Lambert.Kamaro on 1/7/2016.
 */
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

import java.util.ArrayList;
import java.util.List;

import app.com.example.lambertkamaro.kigalilife.Models.AdModel;
import app.com.example.lambertkamaro.kigalilife.R;

public class Fragment1 extends Fragment {

    /** an initial list of ads that we search through **/
    private List<AdModel> adsList;

    /** a post-search filtered list of ads **/
    private List<AdModel> adsFiltered;
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

    /** initially offset will be 0, later will be updated while parsing the json **/
    ActionBar actionBar;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment1, null);
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
//            adapter.filter(searchQuery);
        }
    }


    /*** LOADING ADS SECTION **/

}