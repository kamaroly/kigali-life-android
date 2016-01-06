package app.com.example.lambertkamaro.kigalilife.Activities;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.com.example.lambertkamaro.kigalilife.R;

public class TabFragment extends Fragment {

    private int index;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        index = data.getInt("idx");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.activity_main, null);
        TextView tv = (TextView) v.findViewById(R.id.editTextSubject);
        tv.setText("Fragment " + (index + 1));

        return v;

    }



}
