package com.sparken.parking.database;

/**
 * Created by Swapnil on 31/01/2016.
 */
public class SQLs {
    private SQLs() {

    }

    public static final String sel_state_code = "Select _id,code,type From state_code Where 1=1";
    public static final String del_room = "DELETE from room_name";

    public static final String sel_vehicle_park = "Select _id," +
            "parking_location_id," +
            "remote_id," +
            "vehicle_number," +
            "vehicle_type, " +
            "in_time," +
            "out_time," +
            "mobile_no," +
            "status " +
            "From vehicle Where 1=1";
    public static final String sel_vehicle_mobile_master = "Select _id," +
            "remote_id," +
            "vehicle_number," +
            "mobile_number, " +
            "From vehicle_mobile_mapping Where 1=1";


}
