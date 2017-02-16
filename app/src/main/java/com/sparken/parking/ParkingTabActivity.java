package com.sparken.parking;

/**
 * Created by root on 6/2/17.
 */

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sparken.parking.adapter.Pager;
import com.sparken.parking.common.CommonUtils;
import com.sparken.parking.constant.IConstants;
import com.sparken.parking.database.Database;
import com.sparken.parking.database.DbInvoker;
import com.sparken.parking.fragment.CashCollectionFragment;
import com.sparken.parking.fragment.PagerFragment;
import com.sparken.parking.model.MasterBean;
import com.sparken.parking.model.VehicleBean;
import com.sparken.parking.printer.PrinterFragmentActivity;
import com.sparken.parking.printer.PrintingService;

import java.util.ArrayList;
import java.util.List;

public class ParkingTabActivity extends PrinterFragmentActivity {

    //This is our tablayout
    private TabLayout tabLayout;

    //This is our viewPager
    private ViewPager viewPager;
    Database database;
    DbInvoker dbInvoker;

    //drawer
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    ActionBarDrawerToggle actionBarDrawerToggle;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "Parking";
    private static final String TAG_CASH = "Cash Collection";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tab);

        database = new Database(this);
        dbInvoker = new DbInvoker(this);

        insertMaster();
        //Adding toolbar to the activity
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_drawer);


        //drawer
        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);


        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }


    private void loadNavHeader() {
        // name, website
        txtName.setText(CommonUtils.getSharedPref(IConstants.SH_ST_USER_NAME,this));
        txtWebsite.setText(CommonUtils.getSharedPref(IConstants.SH_PARKING_LOC,this));

    }


    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();


            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }


        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                PagerFragment homeFragment = new PagerFragment();
                return homeFragment;
            case 1:
                // photos
                CashCollectionFragment cashCollectionFragment = new CashCollectionFragment();
                return cashCollectionFragment;
            case 2:
                // movies fragment
                PagerFragment moviesFragment = new PagerFragment();
                return moviesFragment;
            case 3:
                // notifications fragment
                PagerFragment notificationsFragment = new PagerFragment();
                return notificationsFragment;

            case 4:
                // settings fragment
                PagerFragment settingsFragment = new PagerFragment();
                return settingsFragment;
            default:
                return new PagerFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_photos:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_CASH;
                        break;
                    case R.id.nav_movies:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIES;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
                    case R.id.nav_settings:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_SETTINGS;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                     /*   startActivity(new Intent(ParkingTabActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();*/
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                       /* startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();*/
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();


    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }


    private void insertMaster() {

        List<MasterBean> lstState = new ArrayList<>();
        lstState.add(new MasterBean("AP", "STCODE"));
        lstState.add(new MasterBean("AR", "STCODE"));
        lstState.add(new MasterBean("AS", "STCODE"));
        lstState.add(new MasterBean("BR", "STCODE"));
        lstState.add(new MasterBean("GA", "STCODE"));
        lstState.add(new MasterBean("HR", "STCODE"));
        lstState.add(new MasterBean("HP", "STCODE"));
        lstState.add(new MasterBean("JK", "STCODE"));
        lstState.add(new MasterBean("JH", "STCODE"));
        lstState.add(new MasterBean("KA", "STCODE"));
        lstState.add(new MasterBean("KL", "STCODE"));
        lstState.add(new MasterBean("MP", "STCODE"));
        lstState.add(new MasterBean("MH", "STCODE"));
        lstState.add(new MasterBean("MN", "STCODE"));
        lstState.add(new MasterBean("ML", "STCODE"));
        lstState.add(new MasterBean("MZ", "STCODE"));
        lstState.add(new MasterBean("NL", "STCODE"));
        lstState.add(new MasterBean("OR", "STCODE"));
        lstState.add(new MasterBean("PB", "STCODE"));
        lstState.add(new MasterBean("RJ", "STCODE"));
        lstState.add(new MasterBean("SK", "STCODE"));
        lstState.add(new MasterBean("TR", "STCODE"));
        lstState.add(new MasterBean("TN", "STCODE"));
        lstState.add(new MasterBean("UK", "STCODE"));
        lstState.add(new MasterBean("UP", "STCODE"));
        lstState.add(new MasterBean("WB", "STCODE"));
        lstState.add(new MasterBean("AN", "STCODE"));
        lstState.add(new MasterBean("DH", "STCODE"));
        lstState.add(new MasterBean("DL", "STCODE"));
        lstState.add(new MasterBean("DD", "STCODE"));
        lstState.add(new MasterBean("LD", "STCODE"));
        lstState.add(new MasterBean("PY", "STCODE"));


        if (CommonUtils.getSharedPref(this, IConstants.SH_ST_COUNT) < lstState.size()) {
            for (MasterBean bean : lstState) {
                dbInvoker.insertUpdateMaster(bean);
            }
        }


    }

    public DbInvoker getDbInvoker() {
        return dbInvoker;
    }

    public void setDbInvoker(DbInvoker dbInvoker) {
        this.dbInvoker = dbInvoker;
    }


    //printer


    @Override
    protected void onStart() {
        super.onStart();
        startPrinter();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startPrinter();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopPrinter();
    }


    public void printRecipt(VehicleBean bean) {

        printReceipt(PrintingService.printingService.getTotalCollectionReport(bean, this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_parking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_logout:

                LogoutDialog();

                break;
        }
        return true;
    }

    public static AlertDialog.Builder showAlertDialog(Context context) {
        AlertDialog.Builder alertDialog = null;
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Do you want to logout?");
        return alertDialog;
    }

    private void LogoutDialog() {

        try {
            AlertDialog.Builder builder = showAlertDialog(this);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    CommonUtils.removeSharePref(IConstants.SH_PARKING_ID, ParkingTabActivity.this);
                    CommonUtils.removeSharePref(IConstants.SH_USER_ID, ParkingTabActivity.this);
                    CommonUtils.removeSharePref(IConstants.SH_ST_USER_NAME, ParkingTabActivity.this);
                    CommonUtils.removeSharePref(IConstants.SH_PARKING_LOC, ParkingTabActivity.this);
                    CommonUtils.removeSharePref(IConstants.SH_two_capacity, ParkingTabActivity.this);
                    CommonUtils.removeSharePref(IConstants.SH_four_capacity, ParkingTabActivity.this);


                    Intent i = new Intent(ParkingTabActivity.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            }).show();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

}