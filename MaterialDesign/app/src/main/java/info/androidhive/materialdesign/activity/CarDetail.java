package info.androidhive.materialdesign.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
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
import info.androidhive.materialdesign.adapter.LocationAdapter;
import info.androidhive.materialdesign.model.UserCarClass;

public class CarDetail extends AppCompatActivity {

    private UserCarClass cardata;

    private String FEED_URL;
    LinearLayout myGallery;

    TextView carRegLabel, carVinLabel, carMakeLabel;
    TextView carModelLabel, carYearLabel, carColorLabel;
    TextView carDescBox;
    ListView mListView;
    LocationAdapter mLocationAdapter;

    int screenWidth;
    int screenHeight;

    enum EditType {
        Edit,
        Add
    }

    private EditType mEditType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;

        Toolbar mToolbar;
        mToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        String id = intent.getStringExtra("key");
        String type = intent.getStringExtra("type");

        if (type.equals("edit")) {
            mEditType = EditType.Edit;
        } else {
            mEditType = EditType.Add;
        }

        // Font path /Users/Timbarnard/Desktop/Android Test Folder/MaterialDesign/app/src/main/assets
        String fontPath = "font/avenir-next-regular.ttf";
        Typeface tf = Typeface.createFromAsset(getAssets(), fontPath);

        myGallery = (LinearLayout) findViewById(R.id.mygallery);

        carRegLabel = (TextView) findViewById(R.id.carRegLabel);
        carRegLabel.setTypeface(tf);
        carMakeLabel = (TextView) findViewById(R.id.carMakeLabel);
        carMakeLabel.setTypeface(tf);
        carModelLabel = (TextView) findViewById(R.id.carModelLabel);
        carModelLabel.setTypeface(tf);
        carYearLabel = (TextView) findViewById(R.id.carYearLabel);
        carYearLabel.setTypeface(tf);
        carDescBox = (TextView) findViewById(R.id.car_desc_box);
        carDescBox.setTypeface(tf);
        carVinLabel = (TextView) findViewById(R.id.carVinLabel);
        carVinLabel.setTypeface(tf);
        carColorLabel = (TextView) findViewById(R.id.carColorLabel);
        carColorLabel.setTypeface(tf);

        mListView = (ListView)findViewById(R.id.listView);
        mLocationAdapter = new LocationAdapter(CarDetail.this,R.layout.location_row_layout);
        mListView.setAdapter(mLocationAdapter);

        FEED_URL = "http://collegboi.me/vehicleCheck/returnCar.php?carID=" + id;

        new AsyncGetCarData().execute(FEED_URL);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_car_view, menu);

        if (mEditType.equals(EditType.Edit)) {
            MenuItem menuItem = menu.findItem(R.id.action_report);
            menuItem.setVisible(false);
        } else {
            MenuItem menuItem = menu.findItem(R.id.action_edit);
            menuItem.setVisible(false);
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_report) {
            Intent intent = new Intent(CarDetail.this, ReportActivity.class);
            intent.putExtra("type","send");
            intent.putExtra("carID", cardata.getCarID());
            startActivity(intent);
            return true;

        } else if (id == R.id.action_edit) {
            Intent intent = new Intent(CarDetail.this, AddCarView.class);
            intent.putExtra("carObject", cardata);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                //Log.w("Test", "" + statusCode);
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
            //Log.w("Result", "" + result);
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            /* Download complete. Lets update UI */

            //Log.w("Result", "" + result);

            if (result == 1) {
                setTitle(cardata.getCarReg());
                carRegLabel.setText(cardata.getCarReg());
                carVinLabel.setText(cardata.getCarVin());
                carMakeLabel.setText(cardata.getCarMake());
                carModelLabel.setText(cardata.getCarModel());
                carYearLabel.setText(cardata.getCarYear());
                carColorLabel.setText(cardata.getCarColor());
                carDescBox.setText(cardata.getCarDesc());

                for (int i = 0; i < cardata.getCarPhoto().size(); i++) {
                    myGallery.addView(insertPhoto(cardata.getCarPhoto().get(i)));
                }


                for(int j = 0; j < cardata.getCarLocation().size(); j++) {

                    mLocationAdapter.add(cardata.getCarLocation().get(j));
                }

            } else {
               Toast.makeText(CarDetail.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }

            //Hide progressbar
            //mProgressBar.setVisibility(View.GONE);
        }
    }

    View insertPhoto(String url) {

        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, 500));
        layout.setGravity(Gravity.CENTER_VERTICAL);

        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(screenWidth, 500));
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

        Picasso.with(CarDetail.this).load(url).into(imageView);

        layout.addView(imageView);
        return layout;
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
            String carID = post.optString("id");
            String reg = post.optString("carReg");
            String vin = post.optString("carVin");
            String make = post.optString("carMake");
            String model = post.optString("carModel");
            String color = post.optString("carColor");
            String year = post.optString("carYear");
            String desc = post.optString("description");

            JSONArray jsonImageArray = post.getJSONArray("carPhoto");
            JSONArray jsonLocationArray = post.getJSONArray("Location");

            ArrayList<String> imageList = new ArrayList<>();
            ArrayList<UserCarClass.Location> locationList = new ArrayList<>();


            cardata = new UserCarClass();
            cardata.setCarID(carID);
            cardata.setCarReg(reg);
            cardata.setCarMake(make);
            cardata.setCarModel(model);
            cardata.setCarColor(color);
            cardata.setCarYear(year);
            cardata.setCarDesc(desc);
            cardata.setCarVin(vin);

            for (int i = 0; i < jsonImageArray.length(); i++) {
                String image = jsonImageArray.getString(i);
                imageList.add(image);
            }

            for (int j = 0; j < jsonLocationArray.length(); j++) {
                JSONObject locObject = jsonLocationArray.getJSONObject(j);

                String id = locObject.getString("id");
                String country = locObject.getString("carLocationCountry");
                String county = locObject.getString("carLocationCounty");
                String town = locObject.getString("carLocationTown");
                String date = locObject.getString("carLocationDate");
                String loc_Country = locObject.getString("locationCode");
                String uuid = locObject.getString("uuid");

                UserCarClass.Location newLocation = new UserCarClass.Location();
                newLocation.setLocID(id);
                newLocation.setLocCountry(country);
                newLocation.setLocCounty(county);
                newLocation.setLocTown(town);
                newLocation.setLocDate(date);
                newLocation.setLoc_country_code(loc_Country);
                newLocation.setReportsUUID(uuid);
                locationList.add(newLocation);

            }
            cardata.setCarLocation(locationList);
            cardata.setCarPhoto(imageList);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
