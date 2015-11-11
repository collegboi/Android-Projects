package com.example.timbarnard.lab9;

/**
 * Created by Timbarnard on 30/10/15.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class UserDatabase {

    // Database fields
    private SQLiteDatabase database;
    private MotorBaseHelper dbHelper;

    public UserDatabase(Context context) {
        dbHelper = new MotorBaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insertData(EpsumClass epsumClass) {
        ContentValues values = getContentValue(epsumClass);
        database.insert(MotorDBSchema.MotorTable.NAME, null, values);
    }

    public void removeData(String carID) {

    }

//    public List<EpsumClass> getUsers() {
//
//        List<EpsumClass> userCarClasses = new ArrayList<>();
//        // Select All Query
//        String selectQuery = "SELECT  *  FROM " + MotorDBSchema.MotorTable.NAME;
//
//        Cursor cursor = database.rawQuery(selectQuery, null);
//        // looping through all rows and adding to list
//
//        if (cursor != null) {
//            //more to the first row
//            cursor.moveToFirst();
//            //iterate over rows
//            for (int i = 0; i < cursor.getCount(); i++) {
//
//                UserCarClass userCarClass = new UserCarClass();
//                userCarClass.setCarID(cursor.getString(cursor.getColumnIndex(MotorDBSchema.MotorTable.Cols.CAR_ID)));
//                userCarClass.setCarReg(cursor.getString(cursor.getColumnIndex(MotorDBSchema.MotorTable.Cols.CAR_REG)));
//                //ArrayList<UserCarClass.Location> locations= new ArrayList<>();
//                //UserCarClass.Location location = new UserCarClass.Location();
//                //location.setLocDate(cursor.getString(3));
//                //locations.add(location);
//                //userCarClass.setCarLocation(locations);
//                ArrayList<String> images  = new ArrayList<>();
//                images.add(cursor.getString(cursor.getColumnIndex(MotorDBSchema.MotorTable.Cols.IMAGE_PATH)));
//                userCarClass.setCarPhoto(images);
//
//                userCarClasses.add(userCarClass);
//
//                //move to the next row
//                cursor.moveToNext();
//            }
//
//            cursor.close();
//        }
//
//        return userCarClasses;
//    }



    private static ContentValues getContentValue(EpsumClass epsumClass) {
        ContentValues values = new ContentValues();

        values.put(MotorDBSchema.MotorTable.Cols.ID, epsumClass.getId());
        values.put(MotorDBSchema.MotorTable.Cols.userID, epsumClass.getUserID());
        values.put(MotorDBSchema.MotorTable.Cols.title, epsumClass.getTitle());
        values.put(MotorDBSchema.MotorTable.Cols.complete,epsumClass.isCompleted());

        return values;
    }
}
