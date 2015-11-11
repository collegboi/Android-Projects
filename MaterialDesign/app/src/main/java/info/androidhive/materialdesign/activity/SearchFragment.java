package info.androidhive.materialdesign.activity;

/**
 * Created by Ravi on 29/07/15.
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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


public class SearchFragment extends Fragment {

    EditText carEditText;
    Button sendCarButton;

    String carID;

    String FEED_URL;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);

        carEditText = (EditText) rootView.findViewById(R.id.editText);
        sendCarButton = (Button) rootView.findViewById(R.id.button);

        sendCarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = carEditText.getText().toString().trim();

                if(text.equals("")) {
                    Toast.makeText(getActivity(), "Enter Car Number Plate", Toast.LENGTH_SHORT).show();
                } else {
                    String upperCaseString = text.toUpperCase();

                    FEED_URL =  "http://collegboi.me/vehicleCheck/check.php?numberPlate="+upperCaseString;

                    new AsyncGetCarData().execute(FEED_URL);
                }


            }
        });

        // Inflate the layout for this fragment
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

    //Downloading data asynchronously
    public class AsyncGetCarData extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {

                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                Log.w("Test", "" + statusCode);
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
                // Log.d(TAG, e.getLocalizedMessage());
            }
            //result = 1;
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if(carID == null) {
                Toast.makeText(getActivity(), "Motor not Found", Toast.LENGTH_SHORT).show();
            } else {
               // Toast.makeText(getActivity(), "Motor Found", Toast.LENGTH_SHORT).show();
                showSimplePopUp(carID);
            }
            //reset value
            carID = null;
        }
    }


    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }

//         Close stream
//        if (null != stream) {
//            stream.close();
//        }
        return result;
    }

    /**
     * Parsing the feed results and get the list
     *
     * @param result
     */
    private void parseResult(String result) {

        try {

            JSONObject reader = new JSONObject(result);
            JSONArray jsonArray = reader.getJSONArray("Car");

            JSONObject post = jsonArray.optJSONObject(0);
            String id = post.optString("id");
            Log.w("CarID",id);
            carID = id;
            // mGridData.add(item);
            // }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showSimplePopUp(final String value) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());
        helpBuilder.setTitle("Motor Found");
        helpBuilder.setMessage("");
        helpBuilder.setPositiveButton("View",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getActivity(),"Car ID:" + value, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(), CarDetail.class);
                        intent.putExtra("key", value);
                        startActivity(intent);
                    }
                });

        helpBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                    }
                });
        // Remember, create doesn't show the dialog
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }
}
