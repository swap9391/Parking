package com.sparken.parking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.Toast;

import com.android.volley.Request;
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

public class ChangePasswordActivity extends AppCompatActivity {

    Toolbar toolbar;
    Database database;
    EditText txtUserName, txtPassword, txtNewPass;
    UserBean userBean;
    private ChangePasswordActivity TAG = ChangePasswordActivity.this;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_SMS
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        database = new Database(this);


        userBean = new UserBean();
        txtUserName = (EditText) findViewById(R.id.edt_user_name);
        txtPassword = (EditText) findViewById(R.id.edt_password);
        txtNewPass = (EditText) findViewById(R.id.edt_new_password);
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

                bindModel();
                if(check()){
                    Changepassword();
                }

            }
        });

        // toolbar =(Toolbar) findViewById(R.id.toolbar);
    }


    private boolean check() {
        if (txtUserName.getText().toString().isEmpty()) {
            txtUserName.setError("Enter User Name");
            return false;
        }

        if (txtPassword.getText().toString().isEmpty()) {
            txtPassword.setError("Enter Password");
            return false;
        }

        if (txtNewPass.getText().toString().isEmpty()) {
            txtNewPass.setError("Enter New Password");
            return false;
        }

        if (!txtNewPass.getText().toString().isEmpty() && txtNewPass.equals(txtPassword)) {
            txtNewPass.setError("New password is similar to old password");
            return false;
        }

        return true;

    }


    private void bindModel() {
        userBean.setUser_name(txtUserName.getText().toString());
        userBean.setPassword(txtPassword.getText().toString());
        userBean.setNewPassword(txtNewPass.getText().toString());
    }


    public void Changepassword() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IJson.user_name, "" + userBean.getUser_name());
        hashMap.put(IJson.password, "" + userBean.getPassword());
        hashMap.put(IJson.new_password, "" + userBean.getNewPassword());


        CallWebservice.getWebservice(true, this, Request.Method.POST, IUrls.URL_CHANGE_PASS, hashMap, new VolleyResponseListener<UserBean>() {
            @Override
            public void onResponse(UserBean[] object, String message) {

                if (object[0] instanceof UserBean) {
                    for (UserBean bean : object) {
                        Intent intent1 = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                        startActivity(intent1);
                        CommonUtils.showToast(ChangePasswordActivity.this,message);
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



}
