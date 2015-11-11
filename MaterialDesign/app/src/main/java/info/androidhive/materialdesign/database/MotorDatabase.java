package info.androidhive.materialdesign.database;

/**
 * Created by Timbarnard on 30/10/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.androidhive.materialdesign.model.UserCarClass;

public class MotorDatabase {

    // Database fields
    private SQLiteDatabase database;
    private MotorBaseHelper dbHelper;

    public MotorDatabase(Context context) {
        dbHelper = new MotorBaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertData(UserCarClass userCarClass) {
        ContentValues values = getContentValue(userCarClass);
        database.insert(MotorDBSchema.MotorTable.NAME, null, values);
    }

    public void removeData(String  carID) {

    }

    public List<UserCarClass> getAllVehicles() {

        List<UserCarClass> userCarClasses = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  *  FROM " + MotorDBSchema.MotorTable.NAME;

        Cursor cursor = database.rawQuery(selectQuery, null);
        // looping through all rows and adding to list

        if (cursor != null) {
            //more to the first row
            cursor.moveToFirst();
            //iterate over rows
            for (int i = 0; i < cursor.getCount(); i++) {

                UserCarClass userCarClass = new UserCarClass();
                userCarClass.setCarID(cursor.getString(cursor.getColumnIndex(MotorDBSchema.MotorTable.Cols.CAR_ID)));
                userCarClass.setCarReg(cursor.getString(cursor.getColumnIndex(MotorDBSchema.MotorTable.Cols.CAR_REG)));
                //ArrayList<UserCarClass.Location> locations= new ArrayList<>();
                //UserCarClass.Location location = new UserCarClass.Location();
                //location.setLocDate(cursor.getString(3));
                //locations.add(location);
                //userCarClass.setCarLocation(locations);
                ArrayList<String> images  = new ArrayList<>();
                images.add(cursor.getString(cursor.getColumnIndex(MotorDBSchema.MotorTable.Cols.IMAGE_PATH)));
                userCarClass.setCarPhoto(images);

                userCarClasses.add(userCarClass);

                //move to the next row
                cursor.moveToNext();
            }

            cursor.close();
        }

        return userCarClasses;
    }

    public int getVehiclesCount() {
        String countQuery = "SELECT  *  FROM " + MotorDBSchema.MotorTable.NAME;
        Cursor cursor = database.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }

    /*Removing all Vehicles*/
    public void removeVehicles() {

        database.delete(MotorDBSchema.MotorTable.NAME, null, null);
    }

    /*Remove single vehicle*/
    public void removeVehicle(String carID) {
        database.delete(MotorDBSchema.MotorTable.NAME, MotorDBSchema.MotorTable.Cols.CAR_ID + " = ?",
                new String[] { String.valueOf(carID) });
        database.close();
    }

    private static ContentValues getContentValue(UserCarClass userCarClass) {
        ContentValues values = new ContentValues();

        Log.w("id",userCarClass.getCarID());
        Log.w("car", userCarClass.getCarReg());
       // Log.w("date", userCarClass.getCarLocation().get(0).getLocDate());
        Log.w("image", userCarClass.getCarPhoto().get(0));

        values.put(MotorDBSchema.MotorTable.Cols.CAR_ID, userCarClass.getCarID());
        values.put(MotorDBSchema.MotorTable.Cols.CAR_REG, userCarClass.getCarReg());
        //values.put(MotorDBSchema.MotorTable.Cols.DATE, userCarClass.getCarLocation().get(0).getLocDate());
        values.put(MotorDBSchema.MotorTable.Cols.IMAGE_PATH, userCarClass.getCarPhoto().get(0));

        return values;
    }
}
