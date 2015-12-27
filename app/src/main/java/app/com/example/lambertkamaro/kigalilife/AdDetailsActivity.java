package app.com.example.lambertkamaro.kigalilife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class AdDetailsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_details);
        this.details();
    }


    public void  details()
    {
        TextView title = (TextView)findViewById(R.id.detailTitle);
        Intent item = getIntent();

        title.setText(item.getStringExtra("name"));

    }
}
