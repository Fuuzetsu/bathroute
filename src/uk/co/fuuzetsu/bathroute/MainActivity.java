package uk.co.fuuzetsu.bathroute;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import fj.Unit;
import java.io.IOException;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.json.JSONException;
import uk.co.fuuzetsu.bathroute.Engine.CommunicationManager;
import uk.co.fuuzetsu.bathroute.Engine.DataStore;
import uk.co.fuuzetsu.bathroute.Engine.Friend;
import uk.co.fuuzetsu.bathroute.Engine.JSONWriter;

public class MainActivity
    extends FragmentActivity
    implements ActionBar.TabListener {

    private static Boolean ranOnce = false;
    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Places", "Events", "Settings" };


    /* We can start things we want to happen early in this thread. This involves things like
       starting to grab user location. */
    private static Thread runOnceT() {
        return new Thread() {
            @Override
            public void run() {
                if (!MainActivity.ranOnce) {
                    MainActivity.ranOnce = true;

                    DataStore ds = DataStore.getInstance();

                    /* Start talking to the server */
                    try {
                        CommunicationManager cm = new CommunicationManager();
                        cm.listener().start();
                        ds.setCommunicationManager(cm);
                    } catch (IOException e) {
                        Log.e("MainActivity", ExceptionUtils.getStackTrace(e));
                    }
                }
            }
        };
    }

    public static Unit runOnce() {
        MainActivity.runOnceT().start();
        return Unit.unit();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        return;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.runOnce();
        setContentView(R.layout.main);

        // Initilization
        viewPager = (ViewPager) findViewById(R.id.pager);
        actionBar = getActionBar();
        mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

        viewPager.setAdapter(mAdapter);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Adding Tabs
        for (String tab_name : tabs) {
            actionBar.addTab(actionBar.newTab().setText(tab_name)
                             .setTabListener(this));
        }


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    actionBar.setSelectedNavigationItem(position);
                }

                @Override
                public void onPageScrolled(int arg0, float arg1, int arg2) {
                }

                @Override
                public void onPageScrollStateChanged(int arg0) {
                }
            });
    }

    private class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
            case 0:
                return new PlacesActivity();
            case 1:
                return new EventsActivity();
            case 2:
                return new SettingsActivity();
            }

            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
