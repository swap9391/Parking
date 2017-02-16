package com.sparken.parking.mapper;



import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.database.Cursor;

import com.sparken.parking.database.DbMapper;
import com.sparken.parking.model.MasterBean;


public class IdValueMapper extends DbMapper<MasterBean> {

	 /*public static final String sel_room_type = "Select _id,room_type_id,hotel_code,
    room_type_name,room_type_total_rooms,room_type_charges From room_type Where 1=1";*/

	@Override
	protected List<MasterBean> doMap(Cursor rs) {
		List<MasterBean> lst = new ArrayList<MasterBean>();

		while (rs.moveToNext()) {
			int index = 0;
			MasterBean bean = new MasterBean();

			bean.setId(getInt(rs, 0));
			bean.setValue(getString(rs, 1));
			bean.setType(getString(rs, 2));

			lst.add(bean);

		}
		return lst;
	}
}