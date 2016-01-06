package app.com.example.lambertkamaro.kigalilife.Adapters;

/**
 * Created by Lambert.Kamaro on 1/5/2016.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    // Keep all Images in array
    public List<String>  thumbnails ;

    /** image bitmap **/
    Bitmap bitmap;

    // Constructor
    public ImageAdapter(Context c,List<String> images){
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
        ImageView imageView = new ImageView(mContext);

        bitmap = BitmapFactory.decodeFile(thumbnails.get(position));
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(120, 120));
        return imageView;
    }

}