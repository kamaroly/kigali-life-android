package app.com.example.lambertkamaro.kigalilife.Adapters;

/**
 * Created by Lambert.Kamaro on 1/3/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.com.example.lambertkamaro.kigalilife.Activities.AdDetailsActivity;
import app.com.example.lambertkamaro.kigalilife.Controllers.KigaliLifeApplication;
import app.com.example.lambertkamaro.kigalilife.Helpers.DatabaseHelper;
import app.com.example.lambertkamaro.kigalilife.Models.AdModel;
import app.com.example.lambertkamaro.kigalilife.R;


/**
 * This is a custom list adapter class which provides data to list view. In other words it renders the layout_row.xml in list by pre-filling appropriate information.
 * Created by Lambert.Kamaro on 1/3/2016.
 */

public class AdsListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<AdModel> adItems;
    private ArrayList<AdModel> arraylist;
    ImageLoader imageLoader = KigaliLifeApplication.getInstance().getImageLoader();

    public AdsListAdapter(Activity activity, List<AdModel> adItems) {
        this.activity = activity;
        this.adItems = adItems;

        this.arraylist = new ArrayList<AdModel>();

        if (!this.adItems.isEmpty()) {
            this.arraylist.addAll(adItems);
        }
    }

    @Override
    public int getCount() {
        return adItems.size();
    }

    @Override
    public Object getItem(int location) {
        return adItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null) {
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_row, null);
        }

        if (imageLoader == null) {
            imageLoader = KigaliLifeApplication.getInstance().getImageLoader();
        }

        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView owner = (TextView) convertView.findViewById(R.id.owner);
        TextView summary = (TextView) convertView.findViewById(R.id.summary);
        TextView releaseDate = (TextView) convertView.findViewById(R.id.releaseDate);

        // getting movie data for the row
        AdModel ad = adItems.get(position);

        // thumbnail
        String images = ad.getFiles();
        String image = null;
        try {
            JSONArray arrayImages = new JSONArray(images);
            if (arrayImages!= null && arrayImages.length() != 0){
                image = arrayImages.get(0).toString();
            }
        }catch (JSONException e){
            Log.e("ERROR OCCURED",e.getMessage());
        }

        thumbNail.setBackgroundColor(Color.LTGRAY);
        if (image !=null && !image.isEmpty()) {
            thumbNail.setImageUrl(image, imageLoader);
        }
        // owner
        owner.setText(ad.getOwner());

        // rating
        String body = ad.getBody();
        summary.setText(String.valueOf(body));
        summary.setLines(1);
        // release year
        releaseDate.setText(String.valueOf(ad.getMail_date()));

        final String  messageId = ad.getMessage_id();
        // Listen for ListView add Click
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(activity, AdDetailsActivity.class);
                // Pass all data message id
                intent.putExtra("message_id",messageId);

                // Pass all data flag
                // Start SingleItemView Class
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.right_to_left, R.anim.exit);
            }
        });

        return convertView;
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        adItems.clear();
        if (charText.length() == 0) {
            adItems.addAll(arraylist);
        }
        else
        {
            for (AdModel ad : arraylist)
            {
                if (ad.getSubject().toLowerCase(Locale.getDefault()).contains(charText) || ad.getBody().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    adItems.add(ad);
                }
            }
        }
        notifyDataSetChanged();
    }
}
