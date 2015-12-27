package app.com.example.lambertkamaro.kigalilife;

import android.content.Intent;
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


public class MainActivity extends ActionBarActivity {

    /** Instance for our data **/
    public  static Button newAdButton;

    /** Contains our list view **/
    public  static ListView adsListView;

    /** Initial data for our list View of ads*/
    public static   String[] ADSLIST = new String[]{
            "Selling Car","House fore Rent","MacBook pro 13"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.adsListViewMethod();
        this.onClickNewAdListener();
    }

    /**
     * Show list view
     */
    public  void adsListViewMethod(){
        // Populating our list view
        adsListView = (ListView) findViewById(R.id.adsList);
        ArrayAdapter<String> adsAdaptor = new ArrayAdapter<String>(this,R.layout.ad_item,ADSLIST);
        adsListView.setAdapter(adsAdaptor);

        // Add action when someone clicks on the item
        adsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    String value = (String) adsListView.getItemAtPosition(position);
                Toast.makeText(MainActivity.this,"Position : "+position+" value :"+value,Toast.LENGTH_LONG).show();
            }
        });
    }
    /**
     * method to listen when someone clicks on the new ad
     */
    public void onClickNewAdListener(){
        // Locate button in activity main
        newAdButton = (Button) findViewById(R.id.new_ad);

        // Capture button click
        newAdButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Start newAddActivity class
                Intent newAdIntent = new Intent(MainActivity.this,NewAdActivity.class);
                startActivity(newAdIntent);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
