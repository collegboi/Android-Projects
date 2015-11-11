package info.androidhive.materialdesign.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import info.androidhive.materialdesign.AsyncTask.HTTPDataHandler;
import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.FilterAdapter;
import info.androidhive.materialdesign.model.FilterClass;

public class ReturnListPage extends AppCompatActivity {

    Toolbar mActionBarToolbar;
    private String URL;
    private String searchType;
    private ArrayList<FilterClass> listValues;
    private ListView monthsListView;
    private FilterAdapter mFilterAdapter;

    //JSON Values;
    String JSON1;
    String JSON2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_return_list_page);

        listValues = new ArrayList<>();

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        Intent intent = getIntent();
        searchType = intent.getStringExtra("type");
        URL = intent.getStringExtra("URL");

        JSON1 = intent.getStringExtra("JSON1");
        JSON2 = intent.getStringExtra("JSON2");

        new LocationsTask().execute(URL);

        getSupportActionBar().setTitle(searchType);


        monthsListView = (ListView) findViewById(R.id.months_list);
        mFilterAdapter = new FilterAdapter(this, R.layout.text_row_layout, listValues);
        monthsListView.setAdapter(mFilterAdapter);

        monthsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent();
                intent.putExtra("id", mFilterAdapter.getItem(position).getID());
                intent.putExtra("name", mFilterAdapter.getItem(position).getName());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private class LocationsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String stream = null;
            String urlString = params[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        @Override
        protected void onPostExecute(String stream) {

            if (stream != null) try {

                JSONObject reader = new JSONObject(stream);
                JSONArray jsonArray = reader.getJSONArray("Values");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject coord = jsonArray.getJSONObject(i);
                    String id = coord.getString(JSON1);
                    String name = coord.getString(JSON2);

                    FilterClass newClass = new FilterClass(id, capitalize(name));
                    mFilterAdapter.add(newClass);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

    private String capitalize(final String line) {
        return Character.toUpperCase(line.charAt(0)) + line.substring(1);
    }
}
