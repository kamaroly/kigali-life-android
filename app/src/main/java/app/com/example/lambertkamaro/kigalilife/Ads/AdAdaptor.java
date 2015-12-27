package app.com.example.lambertkamaro.kigalilife.Ads;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import app.com.example.lambertkamaro.kigalilife.R;

/**
 * Created by Lambert.Kamaro on 12/27/2015.
 */
public class AdAdaptor extends BaseAdapter {
    Context context;

    List<AdItem> adItems;

    /**
     * Entry point for our class
     *
     * @param context
     * @param adItems
     */
    public AdAdaptor(Context context, List<AdItem> adItems) {
        this.context = context;
        this.adItems = adItems;
    }

    /**
     * Get the number of items in our collection
     *
     * @return
     */
    @Override
    public int getCount() {
        return adItems.size();
    }

    @Override
    public Object getItem(int position) {
        return adItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return adItems.indexOf(getItem(position));
    }

    /**
     * Private view holder class
     */
    private class ViewHolder {
        ImageView profilePic;
        TextView memberName;
        TextView status;
        TextView contactType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        holder = new ViewHolder();

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.ad_item, null);

            holder.memberName = (TextView) convertView.findViewById(R.id.member_name);
            holder.profilePic = (ImageView) convertView.findViewById(R.id.profile_pic);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.contactType = (TextView) convertView.findViewById(R.id.contact_type);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        AdItem rowPosition = adItems.get(position);
        holder.profilePic.setImageResource(rowPosition.getProfilePicId());
        holder.memberName.setText(rowPosition.getMemberName());
        holder.status.setText(rowPosition.getStatus());
        holder.contactType.setText(rowPosition.getContactType());

        return convertView;
    }
}