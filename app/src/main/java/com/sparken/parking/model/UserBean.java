package com.sparken.parking.model;

import com.sparken.parking.database.ContentHolder;

/**
 * Created by root on 7/2/17.
 */

public class UserBean extends BasicBean {
    private int pk_user_id;
    private int pk_parking_location_id;
    private String user_name;
    private String password;
    private String newPassword;
    private String parking_location_name;
    private String two_wheeler_parking_capacity;
    private String four_wheeler_parking_capacity;
    private String latitude;
    private String longitude;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public int getPk_user_id() {
        return pk_user_id;
    }

    public void setPk_user_id(int pk_user_id) {
        this.pk_user_id = pk_user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



    public String getTwo_wheeler_parking_capacity() {
        return two_wheeler_parking_capacity;
    }

    public void setTwo_wheeler_parking_capacity(String two_wheeler_parking_capacity) {
        this.two_wheeler_parking_capacity = two_wheeler_parking_capacity;
    }

    public String getFour_wheeler_parking_capacity() {
        return four_wheeler_parking_capacity;
    }

    public void setFour_wheeler_parking_capacity(String four_wheeler_parking_capacity) {
        this.four_wheeler_parking_capacity = four_wheeler_parking_capacity;
    }

    public int getPk_parking_location_id() {
        return pk_parking_location_id;
    }

    public void setPk_parking_location_id(int pk_parking_location_id) {
        this.pk_parking_location_id = pk_parking_location_id;
    }

    public String getParking_location_name() {
        return parking_location_name;
    }

    public void setParking_location_name(String parking_location_name) {
        this.parking_location_name = parking_location_name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    /*
    public static final String USER= "Create table if not exists user_master"
            + " (_id integer primary key autoincrement," +
            "user_name VARCHAR DEFAULT NULL," +
            "password VARCHAR DEFAULT NULL," +
            "user_type VARCHAR DEFAULT NULL," +
            "parking_location VARCHAR DEFAULT NULL," +
            "two_w_parking_capacity VARCHAR DEFAULT NULL," +
            "four_w_parking_capacity VARCHAR DEFAULT NULL, " +
            "two_w_parking_rate VARCHAR DEFAULT NULL" +
            "four_w_parking_rate VARCHAR DEFAULT NULL" +
            ")";
*/



}
