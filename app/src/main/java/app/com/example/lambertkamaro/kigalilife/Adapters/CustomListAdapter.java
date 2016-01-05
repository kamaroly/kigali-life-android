package app.com.example.lambertkamaro.kigalilife.Adapters;

/**
 * Created by Lambert.Kamaro on 1/3/2016.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.com.example.lambertkamaro.kigalilife.Activities.AdDetailsActivity;
import app.com.example.lambertkamaro.kigalilife.Controllers.AppController;
import app.com.example.lambertkamaro.kigalilife.Models.AdModel;
import app.com.example.lambertkamaro.kigalilife.Models.Movie;
import app.com.example.lambertkamaro.kigalilife.R;


/**
 * This is a custom list adapter class which provides data to list view. In other words it renders the layout_row.xml in list by pre-filling appropriate information.
 * Created by Lambert.Kamaro on 1/3/2016.
 */

public class CustomListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<AdModel> adItems;
    private ArrayList<AdModel> arraylist;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapter(Activity activity, List<AdModel> adItems) {
        this.activity = activity;
        this.adItems = adItems;

        this.arraylist = new ArrayList<AdModel>();
        this.arraylist.addAll(adItems);
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

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView thumbNail = (NetworkImageView) convertView
                .findViewById(R.id.thumbnail);
        TextView title = (TextView) convertView.findViewById(R.id.title);
        TextView rating = (TextView) convertView.findViewById(R.id.rating);
        TextView genre = (TextView) convertView.findViewById(R.id.genre);
        TextView year = (TextView) convertView.findViewById(R.id.releaseYear);

        // getting movie data for the row
        AdModel ad = adItems.get(position);

        // thumbnail image
        thumbNail.setImageUrl(ad.getFiles(), imageLoader);

        // title
        title.setText(ad.getSubject());

        // rating
        rating.setText("Description: " + String.valueOf(ad.getBody()));

        // genre
        genre.setText(ad.getSubject());

        // release year
        year.setText(String.valueOf(ad.getMail_date()));

        // Listen for ListView Item Click
        // Listen for ListView Item Click
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // Send single item click data to SingleItemView Class
                Intent intent = new Intent(activity, AdDetailsActivity.class);
                // Pass all data status
                intent.putExtra("status","Test status");
                // Pass all data name
                intent.putExtra("name","Test description");

                // Pass all data flag
                // Start SingleItemView Class
                activity.startActivity(intent);
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
