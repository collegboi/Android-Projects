package info.androidhive.materialdesign.AsyncTask;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Map;

import info.androidhive.materialdesign.OnTaskCompleted;
import info.androidhive.materialdesign.database.MotorDatabase;

/**
 * Created by Timbarnard on 05/11/15.
 */
public class GenericAsyncTask extends AsyncTask {

    private Context mContext;
    MotorDatabase mMotorDatabase;
    Map<String, String> mStringStringMap;

    private OnTaskCompleted mOnComplete;

    public GenericAsyncTask(Context context, Map<String, String> map) {
        mContext = context;
        mStringStringMap = map;
        mOnComplete = (OnTaskCompleted)context;
    }



    @Override
    protected String doInBackground(Object[] params) {

        CustomHTTPLink customHTTPLink = new CustomHTTPLink(mStringStringMap, params[0].toString());

        return  customHTTPLink.createConnection();
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Object c) {

        mOnComplete.finishedTask("ta daaa");

//        if(s == null) {
//            Toast.makeText(mContext, "Error in removing", Toast.LENGTH_SHORT).show();
//        } else {
//
//            mMotorDatabase = new MotorDatabase(mContext);
//            mMotorDatabase.open();
//
//            Toast.makeText(mContext, "Removed", Toast.LENGTH_SHORT).show();
//        }
    }

}