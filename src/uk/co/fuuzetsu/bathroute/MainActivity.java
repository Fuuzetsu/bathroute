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
import java.io.IOException;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.xmlpull.v1.XmlPullParserException;
import uk.co.fuuzetsu.bathroute.Engine.Node;
import uk.co.fuuzetsu.bathroute.Engine.NodeDeserialiser;

public class MainActivity
    extends FragmentActivity
    implements ActionBar.TabListener {

    private ViewPager viewPager;
    private TabsPagerAdapter mAdapter;
    private ActionBar actionBar;
    // Tab titles
    private String[] tabs = { "Misc", "Places", "Settings" };


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
        Log.v("Main", "deserialising");
        NodeDeserialiser nd = new NodeDeserialiser();
        Resources res = getResources();

        try {
            String nodeText = IOUtils.toString(res.openRawResource(R.raw.nodes));
            List<Node> nodes = nd.deserialise(nodeText);
            Log.v("Main", "Done deseralising");
            Log.v("Main", nodes.toString());
        } catch (IOException e) {
            Log.v("Main", "Deserialising failed with IOException");
            Log.v("Main", ExceptionUtils.getStackTrace(e));
        } catch (XmlPullParserException e) {
            Log.v("Main", "Deserialising failed with XmlPullParserException");
            Log.v("Main", ExceptionUtils.getStackTrace(e));
        }
    }

    private class TabsPagerAdapter extends FragmentPagerAdapter {

        public TabsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int index) {

            switch (index) {
            case 0:
                return new MiscActivity();
            case 1:
                return new PlacesActivity();
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
