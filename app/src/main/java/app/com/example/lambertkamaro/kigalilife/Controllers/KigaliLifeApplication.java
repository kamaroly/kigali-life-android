package app.com.example.lambertkamaro.kigalilife.Controllers;

/**
 * This class is a singleton class which initializes core objects of volley library.
 * Created by Lambert.Kamaro on 1/3/2016.
 */
import app.com.example.lambertkamaro.kigalilife.util.LruBitmapCache;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

    public class KigaliLifeApplication extends Application {

    public static final String TAG = KigaliLifeApplication.class.getSimpleName();

    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    private static KigaliLifeApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized KigaliLifeApplication getInstance() {
        return mInstance;
    }
        public static Context getAppContext(){
            return mInstance.getApplicationContext();
        }
        public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            }

            return mRequestQueue;
        }

        public ImageLoader getImageLoader() {

            getRequestQueue();
            if (mImageLoader == null) {
                mImageLoader = new ImageLoader(this.mRequestQueue,
                        new LruBitmapCache());
            }
            return this.mImageLoader;
        }

        public <T> void addToRequestQueue(Request<T> req) {
            req.setTag(TAG);
            getRequestQueue().add(req);
        }

}
