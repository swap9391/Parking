package com.sparken.parking.mapper;


import android.database.Cursor;

import com.sparken.parking.database.DbMapper;
import com.sparken.parking.model.VehicleBean;

import java.util.ArrayList;
import java.util.List;


public class VehicleMapper extends DbMapper<VehicleBean> {

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
    protected List<VehicleBean> doMap(Cursor rs) {
        List<VehicleBean> lst = new ArrayList<VehicleBean>();

        while (rs.moveToNext()) {
            int index = 0;
            VehicleBean bean = new VehicleBean();

            bean.setId(getInt(rs, 0));
            bean.setPk_parking_location_id(getString(rs, 1));
            bean.setPk_parking_detail_id(getInt(rs, 2));
            bean.setVehicle_no(getString(rs, 3));
            bean.setVehicle_type(getString(rs, 4));
            bean.setIn_time(getString(rs, 5));
            bean.setOut_time(getString(rs, 6));
            bean.setMobile_no(getString(rs, 7));
            bean.setStatus(getString(rs, 8));


            lst.add(bean);

        }
        return lst;
    }
}