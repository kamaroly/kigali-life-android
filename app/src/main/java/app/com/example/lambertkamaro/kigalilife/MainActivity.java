package app.com.example.lambertkamaro.kigalilife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    public  static Button newAdButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.onClickNewAdListener();
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
