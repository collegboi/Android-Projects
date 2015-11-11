package com.example.timbarnard.lab9;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private ListView mListView;
    private ArrayAdapter mArrayAdapter;

    UserDatabase mUserDatabase;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplicationContext();
        mUserDatabase = new UserDatabase(mContext);
        mUserDatabase.open();

        mArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1);

        mListView = (ListView)findViewById(R.id.listView);
        mListView.setAdapter(mArrayAdapter);
        new Download().execute("http://jsonplaceholder.typicode.com/todos");
    }

    private class Download extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {

            String stream = null;
            String urlString = params[0];

            HTTPConnection hh = new HTTPConnection();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        @Override
        protected void onPostExecute(String stream) {

            try {

                JSONArray jsonArray = new JSONArray(stream);

                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject coord = jsonArray.getJSONObject(i);

                    int id = coord.getInt("id");
                    int userID = coord.getInt("userId");
                    String title = coord.getString("title");
                    boolean completed = coord.getBoolean("completed");

                    EpsumClass epsumClass = new EpsumClass();
                    epsumClass.setId(id);
                    epsumClass.setUserID(userID);
                    epsumClass.setTitle(title);
                    epsumClass.setCompleted(completed);

                    mUserDatabase.insertData(epsumClass);

                    mArrayAdapter.add(title);

                }


            }catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
