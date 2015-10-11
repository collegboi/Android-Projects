package com.example.timbarnard.jsonapp;

import android.os.Bundle;
import android.os. AsyncTask;

import android.app.Activity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class MainActivity extends Activity{

    private static String urlString;
    ListView listView;
    CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.listView);

        carAdapter = new CarAdapter(getApplicationContext(), R.layout.row_layout);
        listView.setAdapter(carAdapter);

        Button btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urlString = "http://collegboi.me/vehicleCheck/getCars.php?limit=10&offset=0";
                new ProcessJSON().execute(urlString);
            }
        });
    }

    private class ProcessJSON extends AsyncTask<String, Void, String>{
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        protected void onPostExecute(String stream){

            if(stream !=null) try {

                JSONObject reader = new JSONObject(stream);
                JSONArray jsonArray = reader.getJSONArray("Car");

                for(int i = 0; i < jsonArray.length(); i++) {

                    JSONObject coord = jsonArray.getJSONObject(i);
                    String carReg = coord.getString("carReg");
                    String carDate = coord.getString("carLocationDate");
                    String carPhoto = coord.getString("carPhoto");
                    String uuid = coord.getString("uuid");
                    String id = coord.getString("id");

                    CarClass carClass = new CarClass(carReg,carDate,id,carPhoto);
                    carAdapter.add(carClass);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            // if statement end
        } // onPostExecute() end
    } // ProcessJSON class end
} // Activity end