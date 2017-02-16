package com.sparken.parking.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sparken.parking.ParkingTabActivity;
import com.sparken.parking.R;
import com.sparken.parking.adapter.Pager;
import com.sparken.parking.database.Database;
import com.sparken.parking.database.DbInvoker;

/**
 * Created by root on 14/2/17.
 */

public class PagerFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    View view;
    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;
    Database database;
    DbInvoker dbInvoker;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.pager_layout, container, false);


        //Initializing the tablayout
        tabLayout = (TabLayout)view. findViewById(R.id.tabLayout);

        //Adding the tabs using addTab() method
        tabLayout.addTab(tabLayout.newTab().setText("Tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab2"));

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.setSelectedTabIndicatorHeight(5);
        //Initializing viewPager
        viewPager = (ViewPager) view. findViewById(R.id.pager);

        //Creating our pager adapter
        Pager adapter = new Pager(getMyActivity(). getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position, false);
                tabLayout.getTabAt(position).select();
                if (position == 0) {
                    tabLayout.getTabAt(position).setText("In");
                } else {
                    tabLayout.getTabAt(position).setText("Out");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });
        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);


        return view;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    private ParkingTabActivity getMyActivity() {
        return (ParkingTabActivity) getActivity();
    }

}
