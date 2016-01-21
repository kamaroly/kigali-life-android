package app.com.example.lambertkamaro.kigalilife.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import app.com.example.lambertkamaro.kigalilife.Controllers.KigaliLifeApplication;
import app.com.example.lambertkamaro.kigalilife.R;

/**
 * Created by Lambert.Kamaro on 1/21/2016.
 */
public class ImageViewerActivity  extends ActionBarActivity {
    private ImageLoader imageLoader;
    private NetworkImageView image;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_viewer);

        /**Show add button in the actionBar */
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent item = getIntent();
        if (imageLoader == null) {
            imageLoader = KigaliLifeApplication.getInstance().getImageLoader();
        }
        image = (NetworkImageView) findViewById(R.id.image_viewer);
        url = item.getStringExtra("url");

        /** Guessing the file name **/
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        setTitle(fileName);

        image.setImageUrl(url, imageLoader);
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
                finish();
                overridePendingTransition(R.anim.left_to_right, R.anim.exit);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
