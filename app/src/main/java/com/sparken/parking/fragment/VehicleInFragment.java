package com.sparken.parking.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
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

public class VehicleInFragment extends Fragment implements View.OnClickListener, ServiceCallbacks, TextWatcher, AdapterView.OnItemClickListener {


    EditText edtMobile;
    TextView txtTwoCapacity, txtFourCapacity, txtTwoPresent, txtFourPresent, txt_parking_location;
    Button btnSave;
    private Button btn_two_wheel;
    private Button btn_four_wheel;
    private ImageView chk_two_wheel;
    private ImageView chk_four_wheel;

    private AutoCompleteTextView stateCodeCodeEditText;
    private EditText districtCodeEditText;
    private EditText seriesEditText;
    private EditText restDigitsEditText;
    RadioButton rdnew, rdold;
    private EditText txtOldVehi;
    LinearLayout layNewVehicle;
    private MyService myService;
    private boolean bound = false;
    VehicleBean vehicleBean;
    View view;

    //Overriden method onCreateView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.tab_parking_in, container, false);

        edtMobile = (EditText) view.findViewById(R.id.txtMobileNumber);
        txtTwoCapacity = (TextView) view.findViewById(R.id.txtTwoCapacity);
        txtFourCapacity = (TextView) view.findViewById(R.id.txtFourCapacity);
        txtTwoPresent = (TextView) view.findViewById(R.id.txtTwoPresent);
        txtFourPresent = (TextView) view.findViewById(R.id.txtFourPresent);
        txt_parking_location = (TextView) view.findViewById(R.id.txt_parking_location);
        btnSave = (Button) view.findViewById(R.id.btnIn);
        btn_two_wheel = (Button) view.findViewById(R.id.btn_two_wheel);
        btn_four_wheel = (Button) view.findViewById(R.id.btn_four_wheel);
        chk_two_wheel = (ImageView) view.findViewById(R.id.chkTwoWheel);
        chk_four_wheel = (ImageView) view.findViewById(R.id.chkFourWheel);

        rdnew = (RadioButton) view.findViewById(R.id.rdNew);
        rdold = (RadioButton) view.findViewById(R.id.rdOld);
        txtOldVehi = (EditText) view.findViewById(R.id.txtOldVehinumber);
        stateCodeCodeEditText = (AutoCompleteTextView) view.findViewById(R.id.ETSTATE_CODE);
        districtCodeEditText = (EditText) view.findViewById(R.id.ETDISTRICT_CODE);
        seriesEditText = (EditText) view.findViewById(R.id.ETSERIES_CODE);
        restDigitsEditText = (EditText) view.findViewById(R.id.ETREST_DIGITS);
        layNewVehicle = (LinearLayout) view.findViewById(R.id.layNewVehicle);


        btnSave.setOnClickListener(this);
        btn_two_wheel.setOnClickListener(this);
        btn_four_wheel.setOnClickListener(this);
        chk_two_wheel.setOnClickListener(this);
        chk_four_wheel.setOnClickListener(this);
        rdnew.setOnClickListener(this);
        rdold.setOnClickListener(this);

        vehicleBean = new VehicleBean();

        stateCodeCodeEditText.setOnItemClickListener(this);
        stateCodeCodeEditText.addTextChangedListener(this);
        districtCodeEditText.addTextChangedListener(this);
        seriesEditText.addTextChangedListener(this);
        restDigitsEditText.addTextChangedListener(this);
        edtMobile.addTextChangedListener(this);
        txtOldVehi.addTextChangedListener(this);
        setAdaptor(getStateCode(), stateCodeCodeEditText);

        txt_parking_location.setText(CommonUtils.getSharedPref(IConstants.SH_PARKING_LOC, getMyActivity()));

        Intent intent = new Intent(getMyActivity(), MyService.class);
        getMyActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);

        return view;
    }

    private ParkingTabActivity getMyActivity() {
        return (ParkingTabActivity) getActivity();
    }

    private void bindModel() {
        vehicleBean.setMobile_no(edtMobile.getText().toString());
        vehicleBean.setPk_parking_location_id("" + CommonUtils.getSharedPref(getMyActivity(), IConstants.SH_PARKING_ID));

        if (stateCodeCodeEditText.getText().toString() != null &&
                districtCodeEditText.getText().toString() != null &&
                seriesEditText.getText().toString() != null &&
                restDigitsEditText.getText().toString() != null &&
                !StringUtils.isEmpty(stateCodeCodeEditText.getText().toString()) &&
                !StringUtils.isEmpty(districtCodeEditText.getText().toString()) &&
                !StringUtils.isEmpty(seriesEditText.getText().toString()) &&
                !StringUtils.isEmpty(restDigitsEditText.getText().toString())
                )

        {

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(stateCodeCodeEditText.getText().toString());
            stringBuilder.append(districtCodeEditText.getText().toString());
            stringBuilder.append(seriesEditText.getText().toString());
            stringBuilder.append(restDigitsEditText.getText().toString());

            vehicleBean.setVehicle_no(stringBuilder.toString());

        }

        if (txtOldVehi.getText().toString() != null && !StringUtils.isEmpty(txtOldVehi.getText().toString())) {
            vehicleBean.setVehicle_no(txtOldVehi.getText().toString());
        }


    }


    private void setAdaptor(List<String> listOfElements, AutoCompleteTextView autoCompleteTextView) {
        ArrayAdapter<String> arrayAdptor = new ArrayAdapter<String>(getMyActivity(), android.R.layout.simple_list_item_1,
                listOfElements);
        autoCompleteTextView.setAdapter(arrayAdptor);

        autoCompleteTextView.setThreshold(1);

    }

    @Override
    public void onClick(View v) {
        Date date = Calendar.getInstance().getTime();
        switch (v.getId()) {

            case R.id.btn_two_wheel:
                chk_two_wheel.setImageResource(android.R.drawable.checkbox_on_background);
                chk_four_wheel.setImageResource(android.R.drawable.checkbox_off_background);
                vehicleBean.setVehicle_type("two-wheeler");
                break;
            case R.id.btn_four_wheel:
                chk_two_wheel.setImageResource(android.R.drawable.checkbox_off_background);
                chk_four_wheel.setImageResource(android.R.drawable.checkbox_on_background);
                vehicleBean.setVehicle_type("four-wheeler");
                break;

            case R.id.btnIn:

                //  getMyActivity().printRecipt();
                bindModel();

                if (CommonUtils.isOnline(getMyActivity())) {

                    if (check()) {
                        ParkRegistration();
                    }
                } else {
                    CommonUtils.showToast(getMyActivity(), "No Internet!!");
                }


                break;

            case R.id.rdNew:
                layNewVehicle.setVisibility(View.VISIBLE);
                txtOldVehi.setVisibility(View.GONE);
                break;
            case R.id.rdOld:
                layNewVehicle.setVisibility(View.GONE);
                txtOldVehi.setVisibility(View.VISIBLE);
                txtOldVehi.requestFocus();
                break;


        }

    }


    private boolean check() {

        if (vehicleBean.getVehicle_no() == null || StringUtils.isEmpty(vehicleBean.getVehicle_no())) {
            CommonUtils.showToast(getMyActivity(), "Please Enter Vehicle Number");
            return false;
        }

        if (vehicleBean.getVehicle_no() != null) {

            String pattern = "123456789 ";
            String str1 = vehicleBean.getVehicle_no().substring(0, 2);
            if (pattern.contains(str1)) {
                CommonUtils.showToast(getMyActivity(), "Please Enter Valid Vehicle Number");
                return false;
            }
        }

        if (vehicleBean.getVehicle_type() == null || StringUtils.isEmpty(vehicleBean.getVehicle_type())) {
            CommonUtils.showToast(getMyActivity(), "Please Select Vehicle Type");
            return false;
        }


        return true;
    }

    public void ParkRegistration() {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IJson.vehi_no, "" + vehicleBean.getVehicle_no());
        hashMap.put(IJson.mobile_no, "" + vehicleBean.getMobile_no());
        hashMap.put(IJson.vehicle_type, "" + vehicleBean.getVehicle_type());
        hashMap.put(IJson.parking_location_id, "" + vehicleBean.getPk_parking_location_id());
        hashMap.put(IJson.user_id, "" + CommonUtils.getSharedPref(getMyActivity(), IConstants.SH_USER_ID));


        CallWebservice.getWebservice(true, getMyActivity(), Request.Method.POST, IUrls.URL_ADD_PARKING, hashMap, new VolleyResponseListener<VehicleBean>() {
            @Override
            public void onResponse(VehicleBean[] object, String message) {

                if (object[0] instanceof VehicleBean) {
                    for (VehicleBean bean : object) {
                        // txtCapacity.setText(bean.getFilled() + "/" + bean.getCapacity());
                        Toast.makeText(getMyActivity(), "Vehicle Parked Successfully", Toast.LENGTH_SHORT).show();
                        bean.setPk_parking_location_id(vehicleBean.getPk_parking_location_id());
                        bean.setVehicle_no(vehicleBean.getVehicle_no());
                        bean.setVehicle_type(vehicleBean.getVehicle_type());
                        bean.setMobile_no(vehicleBean.getMobile_no());
                        bean.setStatus(IConstants.STATUS_IN);

                        getMyActivity().getDbInvoker().insertUpdateVehicle(bean);
                        refresh();
                    }
                }
            }


            @Override
            public void onError(String message) {

            }
        }, VehicleBean[].class);

    }

    private void refresh() {
        getMyActivity().startService(new Intent(getMyActivity(), MyService.class));
        vehicleBean = new VehicleBean();
        txtOldVehi.setText("");
        stateCodeCodeEditText.setText("");
        districtCodeEditText.setText("");
        seriesEditText.setText("");
        restDigitsEditText.setText("");
        edtMobile.setText("");
        chk_two_wheel.setImageResource(android.R.drawable.checkbox_off_background);
        chk_four_wheel.setImageResource(android.R.drawable.checkbox_off_background);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getMyActivity().startService(new Intent(getMyActivity(), MyService.class));
    }


    @Override
    public void onStart() {
        super.onStart();
        // bind to Service
        Intent intent = new Intent(getMyActivity(), MyService.class);
        getMyActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }


    @Override
    public void onStop() {
        super.onStop();
        // Unbind from service
        if (bound) {
            myService.setCallbacks(null); // unregister
            getMyActivity().unbindService(serviceConnection);
            bound = false;
        }
    }


    /**
     * Callbacks for service binding, passed to bindService()
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // cast the IBinder and get MyService instance
            MyService.LocalBinder binder = (MyService.LocalBinder) service;
            myService = binder.getService();
            bound = true;
            myService.setCallbacks(VehicleInFragment.this); // register
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            bound = false;
        }
    };

    /* Defined by ServiceCallbacks interface */
    @Override
    public void doSomething(String twoPresent, String twoCapacity, String fourPresent, String fourCapacity) {


        try {
            int twoavl = CommonUtils.asInt(twoCapacity, 0) - CommonUtils.asInt(twoPresent, 0);
            int fouravl = CommonUtils.asInt(fourCapacity, 0) - CommonUtils.asInt(fourPresent, 0);


            if (twoavl <= 10) {
                txtTwoPresent.setTextColor(Color.RED);
            }
            if (fouravl <= 10) {
                txtFourPresent.setTextColor(Color.RED);
            }


            txtTwoPresent.setText("" + twoavl);
            txtTwoCapacity.setText("/" + twoCapacity);
            txtFourPresent.setText("" + fouravl);
            txtFourCapacity.setText("/" + fourCapacity);
        } catch (Exception e) {

        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.e("TEXTWATCHER", "texhChanged");
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();

        if (s == stateCodeCodeEditText.getEditableText()) {
            // DO STH
            if (text != null && text.length() == 2) {
                districtCodeEditText.requestFocus();
            }

        } else if (s == districtCodeEditText.getEditableText()) {
            // DO STH
            if (text != null && text.length() == 2) {
                seriesEditText.requestFocus();
            }
        } else if (s == seriesEditText.getEditableText()) {
            // DO STH
            if (text != null && text.length() == 2) {
                restDigitsEditText.requestFocus();
            }
        } else if (s == restDigitsEditText.getEditableText()) {
            // DO STH
            if (text != null && text.length() == 4) {
                CommonUtils.hideSoftKeyboard(getMyActivity());
                bindModel();
                if (vehicleBean.getVehicle_no().length() == 10) {
                    getMobile();
                }
            }

        } else if (s == txtOldVehi.getEditableText()) {
            // DO STH
            if (text != null && text.length() == 10) {
                CommonUtils.hideSoftKeyboard(getMyActivity());
                bindModel();
                if (vehicleBean.getVehicle_no().length() == 10) {
                    getMobile();
                }
            }
        } else if (s == edtMobile.getEditableText()) {
            // DO STH
            if (text != null && text.length() == 10) {
                CommonUtils.hideSoftKeyboard(getMyActivity());
            }
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        districtCodeEditText.requestFocus();
    }

    private List<String> getStateCode() {
        List<MasterBean> lstmaster = new ArrayList<>();
        lstmaster = getMyActivity().getDbInvoker().getMasterByTpe("STCODE");
        List<String> lststr = new ArrayList<>();

        for (MasterBean masterBean : lstmaster) {
            lststr.add(masterBean.getValue());
        }

        CommonUtils.InsertSharedPref(getMyActivity(), IConstants.SH_ST_COUNT, lststr.size());

        return lststr;
    }


    public void getMobile() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IJson.vehi_no, "" + vehicleBean.getVehicle_no());
        CallWebservice.getWebservice(false, getMyActivity(), Request.Method.POST, IUrls.URL_SEARCH_MOBILE, hashMap, new VolleyResponseListener<VehicleBean>() {
            @Override
            public void onResponse(VehicleBean[] object, String message) {

                if (object[0] instanceof VehicleBean) {


                    List<VehicleBean> lst = new ArrayList<VehicleBean>();
                    for (VehicleBean bean : object) {

                        lst.add(bean);

                        if (lst.size() > 1) {
                            selectMobile(lst);
                        } else {
                            edtMobile.setText(bean.getMobile_no());
                            return;
                        }


                    }
                }
            }


            @Override
            public void onError(String message) {

            }
        }, VehicleBean[].class);
    }


    public void selectMobile(final List<VehicleBean> type1) {

        final String[] labels = new String[type1.size()];

        int checked = 0;

        for (int i = 0; i < type1.size(); i++) {
            labels[i] = type1.get(i).getVehicle_no();
            if (type1.get(i).equals(vehicleBean.getVehicle_no())) {
                checked = i;
            }
        }
        AlertDialog dlg = new AlertDialog.Builder(getActivity())
                .setTitle("Select Mobile Number")

                .setItems(labels, new DialogInterface.OnClickListener() {

                    // input = new
                    // EditText(getActivity()).setView(input);

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();

                        edtMobile.setText(type1.get(which).getMobile_no());
                               /* ddUserType.setText(type1.get(which));
                                mobileLoginRequest.setUserType(type1.get(which));

                                UserTypeChange(type1.get(which));
*/
                    }
                }).create();
        dlg.show();
    }


    public void checkVehicleType() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IJson.vehi_no, "" + vehicleBean.getVehicle_no());
        hashMap.put(IJson.vehicle_type, "" + vehicleBean.getVehicle_no());
        CallWebservice.getWebservice(false, getMyActivity(), Request.Method.POST, IUrls.URL_SEARCH_MOBILE, hashMap, new VolleyResponseListener<VehicleBean>() {
            @Override
            public void onResponse(VehicleBean[] object, String message) {

                if (object[0] instanceof VehicleBean) {
                    for (VehicleBean bean : object) {
                        TypeDialog(message);
                    }
                }
            }


            @Override
            public void onError(String message) {

            }
        }, VehicleBean[].class);
    }

    public static AlertDialog.Builder showAlertDialog(Context context, String string) {
        AlertDialog.Builder alertDialog = null;
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(string);
        return alertDialog;
    }

    private void TypeDialog(String string) {

        try {

            AlertDialog.Builder builder = showAlertDialog(getMyActivity(), string);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ParkRegistration();

                }
            }).show();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }

}