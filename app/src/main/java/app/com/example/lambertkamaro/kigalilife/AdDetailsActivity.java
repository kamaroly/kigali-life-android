package app.com.example.lambertkamaro.kigalilife;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class AdDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);
        this.addBackButtonInActionBar();
        this.details();

    }


    public void  details()
    {
        TextView title = (TextView)findViewById(R.id.detailTitle);
        TextView body  = (TextView)findViewById(R.id.detailBody);

        Intent item = getIntent();
        String name = item.getStringExtra("name");
        String status = item.getStringExtra("status");
        /** Change this activity title to the current name **/
        setTitle(name);

        /** Update activity layout **/
        title.setText(name);
        body.setText(status);
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
