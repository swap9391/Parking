package com.sparken.parking.fragment;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.sparken.parking.ParkingTabActivity;
import com.sparken.parking.R;
import com.sparken.parking.common.CommonUtils;
import com.sparken.parking.common.StringUtils;
import com.sparken.parking.constant.CallWebservice;
import com.sparken.parking.constant.IConstants;
import com.sparken.parking.constant.IJson;
import com.sparken.parking.constant.IUrls;
import com.sparken.parking.constant.ServiceCallbacks;
import com.sparken.parking.constant.VolleyResponseListener;
import com.sparken.parking.model.MasterBean;
import com.sparken.parking.model.VehicleBean;
import com.sparken.parking.service.MyService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 6/2/17.
 */

public class CashCollectionFragment extends Fragment implements View.OnClickListener {


    EditText edtMobile;
    TextView tv_in_2w, tv_out_2w, tv_in_4w, tv_out_4w, tv_avl_2w, tv_avl_4w, tv_total_2w, tv_total_4w, tv_total_price;
    Button btnSave;

    View view;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.cash_collection, container, false);

        edtMobile = (EditText) view.findViewById(R.id.txtMobileNumber);
        tv_in_2w = (TextView) view.findViewById(R.id.tv_in_2w);
        tv_in_4w = (TextView) view.findViewById(R.id.tv_in_4w);

        tv_out_2w = (TextView) view.findViewById(R.id.tv_out_2w);
        tv_out_4w = (TextView) view.findViewById(R.id.tv_out_4w);

        tv_avl_2w = (TextView) view.findViewById(R.id.tv_avl_2w);
        tv_avl_4w = (TextView) view.findViewById(R.id.tv_avl_4w);

        tv_total_2w = (TextView) view.findViewById(R.id.tv_total_2w);
        tv_total_4w = (TextView) view.findViewById(R.id.tv_total_4w);

        tv_total_price = (TextView) view.findViewById(R.id.tv_total_price);
/*
        btnSave = (Button) view.findViewById(R.id.btnIn);


        btnSave.setOnClickListener(this);

*/

        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser) {
            Activity a = getActivity();
            if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }
    private ParkingTabActivity getMyActivity() {
        return (ParkingTabActivity) getActivity();
    }


    @Override
    public void onClick(View v) {

    }
}