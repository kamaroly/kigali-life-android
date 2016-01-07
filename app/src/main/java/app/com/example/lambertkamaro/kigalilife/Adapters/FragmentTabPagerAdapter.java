package app.com.example.lambertkamaro.kigalilife.Adapters;

/**
 * Created by Lambert.Kamaro on 1/6/2016.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app.com.example.lambertkamaro.kigalilife.Fragments.AdsFragment;
import app.com.example.lambertkamaro.kigalilife.Fragments.MyAdsFragment;

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
                AdsFragment adsFragment = new AdsFragment();
                return adsFragment;

            /** tab2 is selected */
            case 1:
                MyAdsFragment myAdsFragment = new MyAdsFragment();
                return myAdsFragment;
        }
        return null;
    }

    /** Returns the number of pages */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}