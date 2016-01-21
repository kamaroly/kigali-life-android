package app.com.example.lambertkamaro.kigalilife.Adapters;

/**
 * Created by Lambert.Kamaro on 1/21/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import app.com.example.lambertkamaro.kigalilife.Activities.ImageViewerActivity;
import app.com.example.lambertkamaro.kigalilife.Controllers.KigaliLifeApplication;
import app.com.example.lambertkamaro.kigalilife.R;

public class GridViewAdapter  extends BaseAdapter {
    private Activity activity;

    // Keep all Images in array
    public List<String> thumbnails ;

    /** image bitmap **/
    Bitmap bitmap;
    private LayoutInflater inflater;
    ImageLoader imageLoader = KigaliLifeApplication.getInstance().getImageLoader();
    // Constructor
    public GridViewAdapter(Activity activity,List<String> images){
        this.activity = activity;
        thumbnails = images;
    }


    @Override
    public int getCount() {
        return thumbnails.size();
    }

    @Override
    public Object getItem(int position) {
        return thumbnails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item_layout, null);
        }
        if (imageLoader == null) {
            imageLoader = KigaliLifeApplication.getInstance().getImageLoader();
        }

            NetworkImageView thumbNail = (com.android.volley.toolbox.NetworkImageView) convertView
                    .findViewById(R.id.grid_item_image);

            // getting movie data for the row
            final String url = thumbnails.get(position);
            thumbNail.setImageUrl(url, imageLoader);

        // Listen for ListView add Click
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(activity, ImageViewerActivity.class);
                // Pass all data url
                intent.putExtra("url", url);
                // Start SingleItemView Class
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_to_left, R.anim.exit);
            }
        });
        return convertView;
    }
}
