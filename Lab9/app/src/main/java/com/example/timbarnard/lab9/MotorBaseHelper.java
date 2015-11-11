package com.example.timbarnard.lab9;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Timbarnard on 28/10/15.
 */


public class MotorBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "epsumDB";

    public MotorBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

//        // Drop older table if existed
//        db.execSQL("DROP TABLE IF EXISTS " + MotorDBSchema.MotorTable.NAME);
//        Log.i("Upgrade","Complete");
//        // Create tables again
//        onCreate(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ MotorDBSchema.MotorTable.NAME + "(" +
                "_id integer primary key autoincrement," +
                        MotorDBSchema.MotorTable.Cols.ID + "," +
                        MotorDBSchema.MotorTable.Cols.userID + "," +
                        MotorDBSchema.MotorTable.Cols.title + "," +
                        MotorDBSchema.MotorTable.Cols.complete + ")"
        );
    }
}
