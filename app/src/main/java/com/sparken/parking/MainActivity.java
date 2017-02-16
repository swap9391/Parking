package com.sparken.parking;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.sparken.parking.common.CommonUtils;
import com.sparken.parking.common.GPSTracker;
import com.sparken.parking.common.StringUtils;
import com.sparken.parking.constant.CallWebservice;
import com.sparken.parking.constant.IConstants;
import com.sparken.parking.constant.IJson;
import com.sparken.parking.constant.IUrls;
import com.sparken.parking.constant.ServiceCallbacks;
import com.sparken.parking.constant.VolleyResponseListener;
import com.sparken.parking.dialog.AddVehicleDialog;
import com.sparken.parking.dialog.OnDataChange;
import com.sparken.parking.model.VehicleBean;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallbacks;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.common.api.ResultCallback;
import com.sparken.parking.service.MyService;


public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult> {


    EditText edtVahicleNumber, edtMobile;
    TextView txtIntime, txtCapacity;
    Button btnSave;
    Toolbar toolbar;
    String lat, longitude;


    private Button btn_two_wheel;
    private Button btn_four_wheel;
    private ImageView chk_two_wheel;
    private ImageView chk_four_wheel;


    //location
    GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    int REQUEST_CHECK_SETTINGS = 100;
    GPSTracker gpsTracker;

    //service
    private MyService myService;
    private boolean bound = false;

    private MainActivity TAG = MainActivity.this;
    VehicleBean vehicleBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_vehicle);
        LocationDialog();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Park Vehicle");
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);

        edtVahicleNumber = (EditText) findViewById(R.id.txtVehicleNumber);
        edtMobile = (EditText) findViewById(R.id.txtMobileNumber);
        txtIntime = (TextView) findViewById(R.id.txtTimestamp);
        txtCapacity = (TextView) findViewById(R.id.txtCapacity);
        btnSave = (Button) findViewById(R.id.btnSave);
        btn_two_wheel = (Button) findViewById(R.id.btn_two_wheel);
        btn_four_wheel = (Button) findViewById(R.id.btn_four_wheel);
        chk_two_wheel = (ImageView) findViewById(R.id.chkTwoWheel);
        chk_four_wheel = (ImageView) findViewById(R.id.chkFourWheel);


        btnSave.setOnClickListener(this);
        btn_two_wheel.setOnClickListener(this);
        btn_four_wheel.setOnClickListener(this);
        chk_two_wheel.setOnClickListener(this);
        chk_four_wheel.setOnClickListener(this);


        txtIntime.setText(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), "dd MM yyyy hh:mm:ss"));
        vehicleBean = new VehicleBean();

    }


    @Override
    public void onClick(View v) {
        Date date = Calendar.getInstance().getTime();
        switch (v.getId()) {

            case R.id.btn_two_wheel:
                chk_two_wheel.setImageResource(android.R.drawable.checkbox_on_background);
                chk_four_wheel.setImageResource(android.R.drawable.checkbox_off_background);
                break;
            case R.id.btn_four_wheel:
                chk_two_wheel.setImageResource(android.R.drawable.checkbox_off_background);
                chk_four_wheel.setImageResource(android.R.drawable.checkbox_on_background);
                break;

            case R.id.btnSave:

                if (CommonUtils.isOnline(this)) {


                } else {
                    CommonUtils.showToast(this, "No Internet!!");
                }
                break;

        }

    }


    public GPSTracker getLocation() {
        gpsTracker = new GPSTracker(this);
        // check if GPS enabled
        if (gpsTracker.getIsGPSTrackingEnabled()) {
            String stringLatitude = String.valueOf(gpsTracker.latitude);
            String stringLongitude = String.valueOf(gpsTracker.longitude);
        }
        return gpsTracker;
    }


    //Location

    private void LocationDialog() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                // NO need to show the dialog;
                break;

            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                // Location settings are not satisfied. Show the user a dialog
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and
                    // check the result
                    // in onActivityResult().

                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);

                } catch (IntentSender.SendIntentException e) {

                    // failed to show
                }
                break;

            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                // Location settings are unavailable so not possible to show any
                // dialog now
                break;
        }
    }


    private void refresh() {
        edtVahicleNumber.setText("");
        edtMobile.setText("");
        txtIntime.setText(CommonUtils.formatDateForDisplay(Calendar.getInstance().getTime(), "dd MM yyyy hh:mm:ss"));
        vehicleBean = new VehicleBean();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // bind to Service
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from service
        if (bound) {
            bound = false;
        }
    }


    /**
     * Callbacks for service binding, passed to bindService()
     */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_parking, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        }
        return true;
    }

}
