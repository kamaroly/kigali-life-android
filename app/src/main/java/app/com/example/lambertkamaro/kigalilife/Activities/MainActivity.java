package app.com.example.lambertkamaro.kigalilife.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import app.com.example.lambertkamaro.kigalilife.Adapters.FragmentTabPagerAdapter;
import app.com.example.lambertkamaro.kigalilife.R;

public class MainActivity extends ActionBarActivity {
    private ViewPager mPager;

    ActionBar mActionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Getting a reference to action bar of this activity */
        mActionbar = getSupportActionBar();

        /** Set tab navigation mode */
        mActionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        /** Getting a reference to ViewPager from the layout */
        mPager = (ViewPager) findViewById(R.id.pager);

        /** Getting a reference to FragmentManager */
        FragmentManager fm = getSupportFragmentManager();

        /** Defining a listener for pageChange */
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mActionbar.setSelectedNavigationItem(position);
            }
        };

        /** Setting the pageChange listener to the viewPager */
        mPager.setOnPageChangeListener(pageChangeListener);

        /** Creating an instance of  */
        FragmentTabPagerAdapter fragmentPagerAdapter = new FragmentTabPagerAdapter(fm);

        /** Setting the  object to the viewPager object */
        mPager.setAdapter(fragmentPagerAdapter);

        mActionbar.setDisplayShowTitleEnabled(true);

        /** Defining tab listener */
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {

            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabSelected(Tab tab, FragmentTransaction ft) {
                mPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabReselected(Tab tab, FragmentTransaction ft) {
            }
        };

        /** Creating fragment1 Tab */
        Tab tab = mActionbar.newTab()
                .setText("Tab1")
                .setTabListener(tabListener);

        mActionbar.addTab(tab);

        /** Creating fragment2 Tab */
        tab = mActionbar.newTab()
                .setText("Tab2")
                .setTabListener(tabListener);

        mActionbar.addTab(tab);
    }

    /** OPTIONS MENU SECTION **/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_new_add:
                Intent newAd = new Intent(MainActivity.this,NewAdActivity.class);
                startActivity(newAd);
                return true;
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}