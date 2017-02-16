package com.sparken.parking.mapper;


import android.database.Cursor;

import com.sparken.parking.database.DbMapper;
import com.sparken.parking.model.VehicleBean;
import com.sparken.parking.model.VehicleMobileMaster;

import java.util.ArrayList;
import java.util.List;


public class VehicleMobileMapper extends DbMapper<VehicleMobileMaster> {



    @Override
    protected List<VehicleMobileMaster> doMap(Cursor rs) {
        List<VehicleMobileMaster> lst = new ArrayList<VehicleMobileMaster>();

        while (rs.moveToNext()) {
            int index = 0;
            VehicleMobileMaster bean = new VehicleMobileMaster();

            bean.setId(getInt(rs, 0));
            bean.setRemote_id(getInt(rs, 1));
            bean.setVehicle_no(getString(rs, 2));
            bean.setMobile_no(getString(rs, 3));

            lst.add(bean);

        }
        return lst;
    }
}