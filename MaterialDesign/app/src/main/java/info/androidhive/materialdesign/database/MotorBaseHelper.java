package info.androidhive.materialdesign.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Timbarnard on 28/10/15.
 */


public class MotorBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "motorBase";

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
                        MotorDBSchema.MotorTable.Cols.CAR_ID + "," +
                        MotorDBSchema.MotorTable.Cols.CAR_REG + "," +
                        MotorDBSchema.MotorTable.Cols.DATE + "," +
                        MotorDBSchema.MotorTable.Cols.IMAGE_PATH + ")"
        );
    }
}
