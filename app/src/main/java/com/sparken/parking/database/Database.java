package com.sparken.parking.database;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Swapnil on 30/01/2016.
 */


public class Database extends SQLiteOpenHelper  {

    public static final String TAG = "DatabaseHelper";
    public static final String DATABASE_NAME = "ParkingDB";
    public static final int DATABASE_VERSION = 1;


    public static final String VEHICLE= "Create table if not exists vehicle"
            + " (_id integer primary key autoincrement," +
            "parking_location_id VARCHAR DEFAULT NULL," +
            "remote_id VARCHAR DEFAULT NULL," +
            "vehicle_number VARCHAR DEFAULT NULL," +
            "vehicle_type VARCHAR DEFAULT NULL," +
            "in_time VARCHAR DEFAULT NULL," +
            "out_time VARCHAR DEFAULT NULL," +
            "mobile_no VARCHAR DEFAULT NULL," +
            "status VARCHAR DEFAULT NULL) ";


    public static final String USER= "Create table if not exists user_master"
            + " (_id integer primary key autoincrement," +
            "user_name VARCHAR DEFAULT NULL," +
            "password VARCHAR DEFAULT NULL," +
            "user_type VARCHAR DEFAULT NULL," +
            "parking_location VARCHAR DEFAULT NULL," +
            "two_w_parking_capacity VARCHAR DEFAULT NULL," +
            "four_w_parking_capacity VARCHAR DEFAULT NULL, " +
            "two_w_parking_rate VARCHAR DEFAULT NULL," +
            "four_w_parking_rate VARCHAR DEFAULT NULL" +
            ")";


    public static final String STATE_CODE= "Create table if not exists state_code"
            + " (_id integer primary key autoincrement," +
            "code VARCHAR DEFAULT NULL," +
            "type VARCHAR DEFAULT NULL) ";

    public static final String VEHICLE_MOBILE= "Create table if not exists vehicle_mobile_mapping"
            + " (_id integer primary key autoincrement," +
            "remote_id VARCHAR DEFAULT NULL," +
            "vehicle_number VARCHAR DEFAULT NULL," +
            "mobile_number VARCHAR DEFAULT NULL) ";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
       SQLiteDatabase db=  context.openOrCreateDatabase(
                DATABASE_NAME, context.MODE_PRIVATE, null);
        db.execSQL(VEHICLE);
        db.execSQL(STATE_CODE);
        db.execSQL(USER);
        db.execSQL(VEHICLE_MOBILE);


    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(VEHICLE);
        db.execSQL(STATE_CODE);
        db.execSQL(USER);
        db.execSQL(VEHICLE_MOBILE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");

        db.execSQL("DROP TABLE IF EXISTS vehicle" );
        db.execSQL("DROP TABLE IF EXISTS user_master" );
        db.execSQL("DROP TABLE IF EXISTS state_code" );
        db.execSQL("DROP TABLE IF EXISTS vehicle_mobile_mapping" );
        onCreate(db);
    }



    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }

}

