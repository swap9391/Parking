package com.sparken.parking.model;

import com.sparken.parking.database.ContentHolder;

/**
 * Created by root on 9/2/17.
 */

public class VehicleMobileMaster extends BasicBean {

    private long remote_id;
    private String vehicle_no;
    private String mobile_no;

    public long getRemote_id() {
        return remote_id;
    }

    public void setRemote_id(long remote_id) {
        this.remote_id = remote_id;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }


/*
    public static final String VEHICLE_MOBILE= "Create table if not exists vehicle_mobile_mapping"
            + " (_id integer primary key autoincrement," +
            "remote_id VARCHAR DEFAULT NULL," +
            "vehicle_number VARCHAR DEFAULT NULL," +
            "mobile_number VARCHAR DEFAULT NULL) ";
*/


    @Override
    public void dbBinding(ContentHolder holder) {
        super.dbBinding(holder);
        holder.setTable("vehicle_mobile_mapping");
        holder.getValues().put("_id", getId());
        holder.getValues().put("remote_id", getRemote_id());
        holder.getValues().put("vehicle_number", getVehicle_no());
        holder.getValues().put("mobile_number", getMobile_no());

    }
}
