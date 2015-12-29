package app.com.example.lambertkamaro.kigalilife;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import app.com.example.lambertkamaro.kigalilife.Ads.AdAdaptor;
import app.com.example.lambertkamaro.kigalilife.Ads.AdItem;


public class MainActivity extends ActionBarActivity {

    /** Instance for our data **/
    public  static Button newAdButton;

    /** Array to hold member names **/
    String[] memberNames;

    /** Array to hold profile pictures **/
    TypedArray profilePics;

    /** Array to hold status **/
    String[] statuses;

    /** Array to hold contact type **/
    String[] contactTypes;

    /** Data transfer object for our ad item **/
    List<AdItem> adItems;
    ListView   adsListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.adsListViewMethod();
    }

    /**
     * Show list view
     */
    public  void adsListViewMethod(){
        adItems = new ArrayList<AdItem>();
        memberNames = getResources().getStringArray(R.array.Member_names);
        statuses    = getResources().getStringArray(R.array.statuses);
        contactTypes = getResources().getStringArray(R.array.contactTypes);
        profilePics  = getResources().obtainTypedArray(R.array.profile_pics);

        // Loop through all this members
        for (int i = 0 ; i< memberNames.length; i++)
        {
            AdItem item = new AdItem(
                                    memberNames[i],
                                    profilePics.getResourceId(i,-1),
                                    statuses[i],
                                    contactTypes[i]
                                    );

            // Add this Item to list view collection
            adItems.add(item);
        }

        adsListView = (ListView) findViewById(R.id.adsList);
        AdAdaptor adsAdaptor = new AdAdaptor(this,adItems);

        // Connect list view to adaption
        adsListView.setAdapter(adsAdaptor);

        // Recycle profile picture
        profilePics.recycle();

        adsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public  void onItemClick(AdapterView<?> adapterView,View view,int position, long id){
                Intent detailIntent = new Intent(MainActivity.this,AdDetailsActivity.class);

                String member_name = adItems.get(position).getMemberName();
                String status = adItems.get(position).getStatus();
                String contactType = adItems.get(position).getContactType();

                // Let's add data to our Intent so that we may know which one to show
                detailIntent.putExtra("position",position);
                detailIntent.putExtra("name",member_name);
                detailIntent.putExtra("status",status);
                detailIntent.putExtra("contactType", contactType);
                startActivity(detailIntent);

            }
        });

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
            case R.id.action_profile:
                Intent profile = new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(profile);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
