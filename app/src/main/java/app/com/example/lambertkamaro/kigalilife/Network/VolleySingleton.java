package app.com.example.lambertkamaro.kigalilife.Network;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import app.com.example.lambertkamaro.kigalilife.Controllers.KigaliLifeApplication;

/**
 * Created by Lambert.Kamaro on 1/18/2016.
 */
public class VolleySingleton {
    private static  VolleySingleton sInstance = null;

    private RequestQueue mRequestQueue;

    private ImageLoader imageLoader;

    private  VolleySingleton(){
        mRequestQueue = Volley.newRequestQueue(KigaliLifeApplication.getAppContext());

        imageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {

            // Let's get availbale cache in kilobytes
            private LruCache<String,Bitmap> cache = new LruCache<String, Bitmap>((int) (Runtime.getRuntime().maxMemory()/1024/8));
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url,bitmap);
            }
        });
    }


    public static VolleySingleton getsInstance(){
        if(sInstance == null){
            sInstance = new VolleySingleton();
        }
        return sInstance;
    }

    public RequestQueue getRequestQueue(){
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        return imageLoader;
    }
}
