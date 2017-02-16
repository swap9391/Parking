package com.sparken.parking.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.android.volley.Request;
import com.sparken.parking.MainActivity;
import com.sparken.parking.common.CommonUtils;
import com.sparken.parking.constant.CallWebservice;
import com.sparken.parking.constant.IConstants;
import com.sparken.parking.constant.IJson;
import com.sparken.parking.constant.IUrls;
import com.sparken.parking.constant.ServiceCallbacks;
import com.sparken.parking.constant.VolleyResponseListener;
import com.sparken.parking.model.VehicleBean;

import java.util.HashMap;

/**
 * Created by root on 27/1/17.
 */

public class MyService extends Service {
    // Binder given to clients
    private final IBinder binder = new LocalBinder();
    // Registered callbacks
    private ServiceCallbacks serviceCallbacks;
    private MyService TAG = MyService.this;
    private String vehicle_no;

    @Override
    public void onCreate() {
        if (CommonUtils.isOnline(this)) {
            getCapacity();
        }


        startHandler();
    }


    public MyService() {


    }


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        if (CommonUtils.isOnline(this)) {
            getCapacity();
        }

    }

    private void startHandler() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                handler.postDelayed(this, 120000);
                // sendDataToServer();
                // sendAttendaceToServer();
                if (CommonUtils.isOnline(MyService.this)) {
                    getCapacity();
                }
            }
        }, 2000);
    }

    // Class used for the client Binder.
    public class LocalBinder extends Binder {
        public MyService getService() {
            // Return this instance of MyService so clients can call public methods
            return MyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }

    public void getCapacity() {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put(IJson.parking_location_id, "" + CommonUtils.getSharedPref(getBaseContext(), IConstants.SH_PARKING_ID));

        CallWebservice.getWebservice(false, TAG, Request.Method.POST, IUrls.URL_GET_CAPACITY, hashMap, new VolleyResponseListener<VehicleBean>() {
            @Override
            public void onResponse(VehicleBean[] object,String message) {

                if (object[0] instanceof VehicleBean) {
                    for (VehicleBean bean : object) {

                        if (serviceCallbacks != null) {
                            serviceCallbacks.doSomething(bean.getTwo_wheeler_present().toString() , bean.getTwo_wheeler_capacity().toString(), bean.getFour_wheeler_present().toString() ,bean.getFour_wheeler_capacity().toString());
                        }

                    }
                }
            }


            @Override
            public void onError(String message) {

            }
        }, VehicleBean[].class);
    }


}
