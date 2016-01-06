package app.com.example.lambertkamaro.kigalilife.Activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import app.com.example.lambertkamaro.kigalilife.R;

/**
 * Created by Lambert.Kamaro on 1/6/2016.
 */
public class Tab1Fragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return (LinearLayout) inflater.inflate(R.layout.activity_main, container, false);
    }

}