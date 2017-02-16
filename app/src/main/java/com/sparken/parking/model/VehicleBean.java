package com.sparken.parking.model;

import com.sparken.parking.database.ContentHolder;

/**
 * Created by root on 24/1/17.
 */

public class VehicleBean extends BasicBean {
    private String pk_parking_location_id;
    private String vehicle_no;
    private String in_time;
    private String out_time;
    private String vehicle_type;
    private String parking_charges;
    private String status;
    private String mobile_no;
    private int pk_parking_detail_id;
    private String two_wheeler_capacity;
    private String two_wheeler_present;
    private String four_wheeler_capacity;
    private String four_wheeler_present;


    public String getTwo_wheeler_capacity() {
        return two_wheeler_capacity;
    }

    public void setTwo_wheeler_capacity(String two_wheeler_capacity) {
        this.two_wheeler_capacity = two_wheeler_capacity;
    }

    public String getTwo_wheeler_present() {
        return two_wheeler_present;
    }

    public void setTwo_wheeler_present(String two_wheeler_present) {
        this.two_wheeler_present = two_wheeler_present;
    }

    public String getFour_wheeler_capacity() {
        return four_wheeler_capacity;
    }

    public void setFour_wheeler_capacity(String four_wheeler_capacity) {
        this.four_wheeler_capacity = four_wheeler_capacity;
    }

    public String getFour_wheeler_present() {
        return four_wheeler_present;
    }

    public void setFour_wheeler_present(String four_wheeler_present) {
        this.four_wheeler_present = four_wheeler_present;
    }

    public int getPk_parking_detail_id() {
        return pk_parking_detail_id;
    }

    public void setPk_parking_detail_id(int pk_parking_detail_id) {
        this.pk_parking_detail_id = pk_parking_detail_id;
    }

    public String getPk_parking_location_id() {
        return pk_parking_location_id;
    }

    public void setPk_parking_location_id(String pk_parking_location_id) {
        this.pk_parking_location_id = pk_parking_location_id;
    }

    public String getVehicle_no() {
        return vehicle_no;
    }

    public void setVehicle_no(String vehicle_no) {
        this.vehicle_no = vehicle_no;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public String getVehicle_type() {
        return vehicle_type;
    }

    public void setVehicle_type(String vehicle_type) {
        this.vehicle_type = vehicle_type;
    }

    public String getParking_charges() {
        return parking_charges;
    }

    public void setParking_charges(String parking_charges) {
        this.parking_charges = parking_charges;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }


    /*public static final String VEHICLE= "Create table if not exists vehicle"
            + " (_id integer primary key autoincrement," +
            "parking_location_id VARCHAR DEFAULT NULL," +
            "remote_id VARCHAR DEFAULT NULL," +
            "vehicle_number VARCHAR DEFAULT NULL," +
            "vehicle_type VARCHAR DEFAULT NULL," +
            "in_time VARCHAR DEFAULT NULL," +
            "out_time VARCHAR DEFAULT NULL," +
            "mobile_no VARCHAR DEFAULT NULL," +
            "status VARCHAR DEFAULT NULL) ";*/

    @Override
    public void dbBinding(ContentHolder holder) {
        super.dbBinding(holder);
        holder.setTable("vehicle");
        holder.getValues().put("_id", getId());
        holder.getValues().put("parking_location_id", getPk_parking_location_id());
        holder.getValues().put("remote_id", getPk_parking_detail_id());
        holder.getValues().put("vehicle_number", getVehicle_no());
        holder.getValues().put("vehicle_type", getVehicle_type());
        holder.getValues().put("in_time", getIn_time());
        holder.getValues().put("out_time", getOut_time());
        holder.getValues().put("mobile_no", getMobile_no());
        holder.getValues().put("status", getStatus());


    }

}
