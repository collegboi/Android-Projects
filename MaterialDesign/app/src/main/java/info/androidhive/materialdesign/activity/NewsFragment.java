package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.GridItem;
import info.androidhive.materialdesign.adapter.GridViewAdapter;

/**
 * Created by Ravi on 29/07/15.
 */
public class NewsFragment extends Fragment {

    private String totalNumber = "";
    private GridView mGridView;
    //private ProgressBar mProgressBar;

    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;

    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // setContentView(R.layout.fragment_news);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_news, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.gridView);

        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(getActivity(), R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                Intent intent = new Intent(getActivity(), CarDetail.class);
                intent.putExtra("key", mGridData.get(position).getId());
                intent.putExtra("type","add");
                startActivity(intent);
            }
        });


        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int totalInt = 89;

                Log.w("First",""+firstVisibleItem);
                Log.w("Visible",""+visibleItemCount);
                Log.w("Total",""+totalItemCount);

                if (((firstVisibleItem + visibleItemCount) == totalItemCount) && (totalItemCount < totalInt) && totalItemCount > 0) {
                    Log.w("End", "The end.....");
                    executeURLFeed(10, totalItemCount);
                }
            }
        });


         /* Start download */
        this.executeURLFeed(20, 0);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void executeURLFeed(Integer limit, Integer offset) {

        String urlString = "http://collegboi.me/vehicleCheck/getCars.php?limit="+limit + "&offset="+offset;

        new AsyncHttpTask().execute(urlString);
        //mProgressBar.setVisibility(View.VISIBLE);
    }


    //Downloading data asynchronously
    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        Boolean initRun = true;

        @Override
        protected Integer doInBackground(String... params) {
            Log.w("Value",params[0]);

//            String[] tokens = params[0].split("=", -1);
//
//            String value = tokens[2];
//
//            if(Objects.equals(value, new String("0"))) {
//                Log.w("Test",tokens[2]);
//                initRun = true;
//            }

            Integer result = 0;
            try {

                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                //Log.w("Test",""+statusCode);
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
                //Log.d(TAG, e.getLocalizedMessage());
            }
            //result = 1;
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            /* Download complete. Lets update UI */
            //Log.w("Result",""+result);
            if ((result == 1) && (initRun == true)) {

                mGridAdapter.setGridData(mGridData);

            }   else if (result == 0) {
                Toast.makeText(getActivity(), "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }

            //Hide progressbar
            //mProgressBar.setVisibility(View.GONE);
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

    private void parseResult(String result) {

        //mGridData.clear();

        try {

            JSONObject reader = new JSONObject(result);

            String total = reader.optString("total");
            totalNumber = total;

            JSONArray jsonArray = reader.getJSONArray("Car");

           // Log.w("JSON","Reading..."+jsonArray.length());
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

                //Log.w("Count",""+mGridData.size());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

