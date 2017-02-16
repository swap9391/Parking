package com.sparken.parking.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.sparken.parking.mapper.IdValueMapper;
import com.sparken.parking.mapper.VehicleMapper;
import com.sparken.parking.mapper.VehicleMobileMapper;
import com.sparken.parking.model.BasicBean;
import com.sparken.parking.model.MasterBean;
import com.sparken.parking.model.UserBean;
import com.sparken.parking.model.VehicleBean;
import com.sparken.parking.model.VehicleMobileMaster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class DbInvoker {
    // Database fields
    private SQLiteDatabase database;
    private DBService dbHelper;

    public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd";
    public static final String DEFAULT_TIME_FORMAT = "HHmm";

    private final Context context;

    public DbInvoker(Context context) {
        this.context = context;
        dbHelper = new DBService(context);
        open();
    }

    public boolean isOpen() throws SQLException {
        return dbHelper.getWritableDatabase().isOpen()
                || dbHelper.getReadableDatabase().isOpen();
    }

    public void open() throws SQLException {

        database = dbHelper.getWritableDatabase();

    }


    public void executeSQL(String sql) {
        database.execSQL(sql);
    }

    public void executeSQL(String sql, String[] params) {
        database.execSQL(sql, params);
    }

    public Cursor executeSelect(String sql, String[] params) {
        ensure();
        Cursor rs = null;
        Log.d("aapi", "Executing " + sql);
        rs = database.rawQuery(sql, params);
        // Log.i("Db Service", "Sql:" + sql + " Result " + rs.getCount());
        return rs;
    }

    public <T> Collection<T> executeSelect(String sql, String[] params,
                                           DbMapper<T> mapper) {
        open();

        Cursor rs = null;
        try {
            Log.d("aapi", "Executing " + sql);
            rs = database.rawQuery(sql, null);
            return mapper.map(rs);
        } finally {
            if (rs != null) {
                rs.close();
            }
        }

    }

    public <T> T executeSelectOne(String sql, String[] params,
                                  DbMapper<T> mapper) {
        Collection<T> result = executeSelect(sql, params, mapper);
        if (!result.isEmpty()) {
            return result.iterator().next();
        }
        return null;
    }

    private void ensure() {
        if (!database.isOpen()) {
            this.dbHelper = new DBService(context);
            open();
        }
    }

    public void close() {
        dbHelper.close();
    }

    public long executeCreate(ContentHolder holder) {
        ensure();
        long id;
        id = database.insert(holder.getTable(), null, holder.getValues());
        return id;
    }

    public int executeUpdate(ContentHolder holder) {
        return database.update(holder.getTable(), holder.getValues(),
                holder.getWhereClause(), holder.getWhereArgs());

    }

    public void executeDelete(ContentHolder holder) {
        database.delete(holder.getTable(), holder.getWhereClause(),
                holder.getWhereArgs());
    }

    private void insertUpdateBean(BasicBean bean) {
        ContentHolder holder = new ContentHolder();
        bean.dbBinding(holder);
        if (bean.getId() == null || bean.getId() <= 0) {
            long id = executeCreate(holder);
            bean.setId((int) id);
        } else {
            String[] params = {"" + bean.getId()};
            holder.setWhereClause("_id=?");
            holder.setWhereArgs(params);
            executeUpdate(holder);
            // update
        }

    }

    public void insertUpdateMaster(MasterBean bean) {
        insertUpdateBean(bean);
    }

    public void insertUpdateVehicle(VehicleBean bean) {
        insertUpdateBean(bean);
    }

    public void insertUpdateVehicleMobile(VehicleMobileMaster bean) {
        insertUpdateBean(bean);
    }


    public List<MasterBean> getMasterByTpe(String type) {
        String sql = SQLs.sel_state_code + " AND type='" + type + "'";
        String[] params = {""};
        return (List<MasterBean>) executeSelect(sql, params,
                new IdValueMapper());
    }

    public List<VehicleBean> getVehicleByStatus(String status) {
        String sql = SQLs.sel_vehicle_park + " AND status='" + status + "'";
        String[] params = {""};
        return (List<VehicleBean>) executeSelect(sql, params,
                new VehicleMapper());
    }

    public VehicleBean getVehicle(String number) {
        String sql = SQLs.sel_vehicle_park + " AND vehicle_number='" + number + "'";
        String[] params = {""};
        return (VehicleBean) executeSelectOne(sql, params,
                new VehicleMapper());
    }


  /*  public void insertUpdateRoomType(RoomTypeBean bean) {
        insertUpdateBean(bean);
    }

    public List<RoomTypeBean> getRoomType() {
        String sql = SQLs.sel_room_type ;
        String[] params = {""};
        return (List<RoomTypeBean>) executeSelect(sql, params,
                new RoomTypeMapper());
    }

    public RoomTypeBean getRoomType(int id) {
        String sql = SQLs.sel_room_type +" AND room_type_id="+id;
        String[] params = {""};
        return (RoomTypeBean) executeSelectOne(sql, params,
                new RoomTypeMapper());
    }

    public void deleteRoom() {
        String[] params = new String[0];
        String sql = SQLs.del_room;
        executeSQL(sql, params);

    }
*/

	/*public void insertUpdateClass(ClassName bean) {
        insertUpdateBean(bean);
	}*/


   /*
    public List<ClassName> getClassName() {
        String sql = SQLs.sel_class_name+" ORDER BY class_name ASC";
        String[] params = { "" };
        return (List<ClassName>) executeSelect(sql, params,
                new RoomTypeMapper());
    }

    public ClassName getClassNameById(int id) {
        String[] params = { "" };
        String sql = SQLs.sel_class_name+" and _id="+id;
        return  executeSelectOne(sql, params,
                new RoomTypeMapper());
    }*/

    /*public List<IdValue> getCodeValue(int codeTypeId) {
        String sql = SQLs.sel_class_name+" and _id="+codeTypeId;
        String[] params = new String[] { ""  };
        return (List<IdValue>) executeSelect(sql, params, new IdValueMapper());
    }*/


    /*public \void deleteClassById(int Id) {
        String[] params = new String[0];
        String sql = SQLs.del_class_name+" WHERE _id="+Id;
        executeSQL(sql, params);

    }
*/

   /* public void insertUpdateUser(UserBean bean,Context context,Bitmap bmp) {
        insertUpdateBean(bean);
        bean.setLocalPath("/sdcard/CoolSchool/"+ bean.getImageFolder().trim()+"/school_image"+ bean.getId() +".jpg");
        insertUpdateBean(bean);
        ImageUtills imageUtills = new ImageUtills();
        String filename=bean.getLocalPath().substring(bean.getLocalPath().lastIndexOf("/")+1);
        imageUtills.saveToInternalStorage(bmp,context,"Profile",filename);

    }*/


  /*  public AttendanceBean getAttendanceByUserId(int userId,String date) {
        String[] params = { "" };
        String sql = SQLs.sel_attendance+" and user_id="+userId+" and attendance_date = "+date;
        return  executeSelectOne(sql, params,
                new AttendanceMapper());
    }*/

}







