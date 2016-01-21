package app.com.example.lambertkamaro.kigalilife.Adapters;

/**
 * Created by Lambert.Kamaro on 1/21/2016.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import app.com.example.lambertkamaro.kigalilife.Controllers.KigaliLifeApplication;
import app.com.example.lambertkamaro.kigalilife.Models.AdModel;
import app.com.example.lambertkamaro.kigalilife.R;

public class GridViewAdapter  extends BaseAdapter {
    private Context mContext;

    // Keep all Images in array
    public List<String> thumbnails ;

    /** image bitmap **/
    Bitmap bitmap;
    private LayoutInflater inflater;
    ImageLoader imageLoader = KigaliLifeApplication.getInstance().getImageLoader();
    // Constructor
    public GridViewAdapter(Context c,List<String> images){
        mContext = c;
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
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.grid_item_layout, null);
        }
        if (imageLoader == null) {
            imageLoader = KigaliLifeApplication.getInstance().getImageLoader();
        }

        if (!thumbnails.isEmpty()) {
            NetworkImageView thumbNail = (com.android.volley.toolbox.NetworkImageView) convertView
                    .findViewById(R.id.grid_item_image);

            // getting movie data for the row
            thumbNail.setBackgroundColor(Color.LTGRAY);
            String image = thumbnails.get(position);
            thumbNail.setImageUrl(image, imageLoader);
        }
        return convertView;
    }
}
