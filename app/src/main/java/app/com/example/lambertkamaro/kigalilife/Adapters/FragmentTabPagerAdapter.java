package app.com.example.lambertkamaro.kigalilife.Adapters;

/**
 * Created by Lambert.Kamaro on 1/6/2016.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app.com.example.lambertkamaro.kigalilife.Fragments.Fragment1;
import app.com.example.lambertkamaro.kigalilife.Fragments.Fragment2;

public class FragmentTabPagerAdapter extends FragmentPagerAdapter{

    final int PAGE_COUNT = 2;

    /** Constructor of the class */
    public FragmentTabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    /** This method will be invoked when a page is requested to create */
    @Override
    public Fragment getItem(int arg0) {
        Bundle data = new Bundle();
        switch(arg0){

            /** tab1 is selected */
            case 0:
                Fragment1 fragment1 = new Fragment1();
                return fragment1;

            /** tab2 is selected */
            case 1:
                Fragment2 fragment2 = new Fragment2();
                return fragment2;
        }
        return null;
    }

    /** Returns the number of pages */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}