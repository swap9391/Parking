package com.sparken.parking;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.sparken.parking.MainActivity;
import com.sparken.parking.common.CommonUtils;
import com.sparken.parking.common.FloatingActionButton;
import com.sparken.parking.constant.CallWebservice;
import com.sparken.parking.constant.IConstants;
import com.sparken.parking.constant.IJson;
import com.sparken.parking.constant.IUrls;
import com.sparken.parking.constant.VolleyResponseListener;
import com.sparken.parking.database.Database;
import com.sparken.parking.model.UserBean;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Database database;
    EditText txtUserName, txtPassword;
    TextView txtChangepassword;
    private LoginActivity TAG = LoginActivity.this;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        database = new Database(this);

        if (CommonUtils.getUserId(this) > 0) {
            Intent intent = new Intent(this, ParkingTabActivity.class);
            startActivity(intent);
            finish();
        }

        txtUserName = (EditText) findViewById(R.id.edt_user_name);
        txtPassword = (EditText) findViewById(R.id.edt_password);
        txtChangepassword=(TextView) findViewById(R.id.txtChangePass);

        txtChangepassword.setOnClickListener(this);

        LinearLayout yourframelayout = (LinearLayout) findViewById(R.id.floating_login);
        FloatingActionButton fabButton = new FloatingActionButton.Builder(this, yourframelayout)
                .withDrawable(getResources().getDrawable(R.mipmap.ic_check_white))
                .withButtonColor(Color.parseColor("#283593"))
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(2, 2, 2, 2)
                .create();


        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*Intent intent1 = new Intent(LoginActivity.this,MainActivity.class);
               startActivity(intent1);*/
                if (!txtUserName.getText().toString().isEmpty() && !txtPassword.getText().toString().isEmpty()) {
                    Login(txtUserName.getText().toString(), txtPassword.getText().toString());
                } else {
                    Toast.makeText(LoginActivity.this, "Enter Username and Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        verifyStoragePermissions(LoginActivity.this);
        // toolbar =(Toolbar) findViewById(R.id.toolbar);
    }

    public void Login(final String username, final String password) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IJson.user_name, "" + username);
        hashMap.put(IJson.password, "" + password);

        CallWebservice.getWebservice(true, this, Request.Method.POST, IUrls.URL_LOGIN, hashMap, new VolleyResponseListener<UserBean>() {
            @Override
            public void onResponse(UserBean[] object,String message) {

                if (object[0] instanceof UserBean) {
                    for (UserBean bean : object) {

                        CommonUtils.InsertSharedPref(LoginActivity.this, IConstants.SH_USER_ID, bean.getPk_user_id());
                        CommonUtils.InsertSharedPref(LoginActivity.this, IConstants.SH_PARKING_ID, bean.getPk_parking_location_id());
                        CommonUtils.insertSharedPref(LoginActivity.this, IConstants.SH_ST_USER_NAME, bean.getUser_name());
                        CommonUtils.insertSharedPref(LoginActivity.this, IConstants.SH_PARKING_LOC, bean.getParking_location_name());
                        CommonUtils.insertSharedPref(LoginActivity.this, IConstants.SH_two_capacity, bean.getTwo_wheeler_parking_capacity());
                        CommonUtils.insertSharedPref(LoginActivity.this, IConstants.SH_four_capacity, bean.getFour_wheeler_parking_capacity());
                        CommonUtils.insertSharedPref(LoginActivity.this, IConstants.SH_LAT, bean.getLatitude());
                        CommonUtils.insertSharedPref(LoginActivity.this, IConstants.SH_LONG, bean.getLongitude());

                        //  Toast.makeText(getApplicationContext(), "Welcome " + bean.getFirst_name() + " " + bean.getLast_name(), Toast.LENGTH_LONG).show();
                        //USER_ID = bean.getJs_id();
                        Intent intent1 = new Intent(LoginActivity.this, ParkingTabActivity.class);
                        startActivity(intent1);
                        // startActivity(new Intent(LoginActivity.this, UserDashboardActivity.class));
                        finish();
                    }
                }
            }

            @Override
            public void onError(String message) {

            }
        }, UserBean[].class);


    }


    public static void verifyStoragePermissions(Activity activity) {

        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permissionSMS = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS);

        if (permission != PackageManager.PERMISSION_GRANTED && permissionSMS != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtChangePass:
                Intent i= new Intent(this,ChangePasswordActivity.class);
                startActivity(i);
                break;
        }


    }
}
