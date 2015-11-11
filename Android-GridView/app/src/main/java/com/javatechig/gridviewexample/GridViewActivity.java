package com.javatechig.gridviewexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.util.LogWriter;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GridViewActivity extends ActionBarActivity {
    private static final String TAG = GridViewActivity.class.getSimpleName();

    private String totalNumber = "";

    private GridView mGridView;
    private ProgressBar mProgressBar;

    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private String FEED_URL;
//http://javatechig.com/?json=get_recent_posts&count=45
    //http://collegboi.me/vehicleCheck/getCars.php?limit=20&offset=0
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gridview);

        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);


        /* Start download */
        this.executeURLFeed(20, 0);

        //set scroll listener to gridView
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /* TODO Auto-generated method stub */

                Log.w("Count1", "" + firstVisibleItem);
                Log.w("Count2", "" + visibleItemCount);
                Log.w("Count3", "" + totalItemCount);

                int totalInt = 89;

                Log.w("Total",totalNumber);

                if (((firstVisibleItem + visibleItemCount) == totalItemCount) && (totalItemCount < totalInt)) {
                    Log.w("End", "The end.....");
                    executeURLFeed(10, totalItemCount);
                }

            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                /*
                TODO Auto-generated method stub
                final ListView lw = getListView();
                */

                if (scrollState == 0) {
                    //Log.i("a", "scrolling stopped...");
                }
            }
        });

        //Grid view click event
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {


                Intent intent = new Intent(GridViewActivity.this, DetailView.class);
                intent.putExtra("key", mGridData.get(position).getId());
                startActivity(intent);
            }
        });

    }

    private void executeURLFeed(Integer limit, Integer offset) {

        String urlString = "http://collegboi.me/vehicleCheck/getCars.php?limit="+limit+"&offset="+offset;

        new AsyncHttpTask().execute(urlString);
        mProgressBar.setVisibility(View.VISIBLE);
    }


    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {

                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                Log.w("Test",""+statusCode);
                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
//                String response = streamToString(httpResponse.getEntity().getContent());
//                parseResult(response);
//                result = 1;

            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            //result = 1;
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            /* Download complete. Lets update UI */
            Log.w("Result",""+result);
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(GridViewActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }

            //Hide progressbar
            mProgressBar.setVisibility(View.GONE);
        }
    }


    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

/*
Close stream
if (null != stream) {
stream.close();
}
*/
        return result;
    }

    /**
     * Parsing the feed results and get the list
     *
     *
     */
    private void parseResult(String result) {
        try {
            Log.w("JSON","Reading...");
            JSONObject reader = new JSONObject(result);

            String total = reader.optString("total");
            totalNumber = total;

            JSONArray jsonArray = reader.getJSONArray("Car");
            GridItem item;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject post = jsonArray.optJSONObject(i);
                String title = post.optString("carReg");
                String imageURL = post.optString("carPhoto");
                String carID = post.optString("id");
                item = new GridItem();
                item.setTitle(title);
                item.setImage(imageURL);
                item.setId(carID);

                mGridData.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}