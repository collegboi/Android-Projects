package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.model.UserCarClass;

public class ReportActivity extends AppCompatActivity {

    private String countryID;
    Button countryButton, countyButton;
    EditText countryTextView, countyTextView;
    EditText townText;
    Button dateButton;
    Toolbar mActionBarToolbar;
    private TextInputLayout townInputLayout, countryInputLayout, countyInputLayout;
    private String carID;

    UserCarClass.Location mLocationClass;

    private static final int REQUEST_CODE = 0;

    enum LocType {
        Send,
        Add
    }
    private LocType mLocType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Report");

        countryInputLayout = (TextInputLayout)findViewById(R.id.input_layout_country);
        countryButton = (Button)findViewById(R.id.country_button);
        countryTextView = (EditText)findViewById(R.id.country_textView);
        countryTextView.setEnabled(false);

        countyInputLayout = (TextInputLayout)findViewById(R.id.input_layout_county);
        countyButton = (Button)findViewById(R.id.county_button);
        countyTextView = (EditText)findViewById(R.id.county_textView);
        countyTextView.setEnabled(false);

        townInputLayout = (TextInputLayout)findViewById(R.id.input_layout_town);
        townText = (EditText)findViewById(R.id.town_text);
        dateButton = (Button)findViewById(R.id.date_button);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        //if view is to send, then add the id from car;
        if (type.equals("send")) {
            carID = intent.getStringExtra("carID");
            Log.w("Send",carID);
            mLocType = LocType.Send;
        } else {
            mLocType = mLocType.Add;
        }

        countryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //previously clicked
                if (countryID != null) {
                    countryID = null;
                    countyTextView.setText("...");
                }

                Intent newIntent = new Intent(ReportActivity.this, ReturnListPage.class);
                newIntent.putExtra("type", "Country");
                newIntent.putExtra("JSON1", "country_id");
                newIntent.putExtra("JSON2", "country_name");
                newIntent.putExtra("URL", "http://collegboi.me/vehicleCheck/androidFilter.php?action=country");
                startActivityForResult(newIntent, REQUEST_CODE);
            }
        });

        countyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(countryID != null) {
                    Intent newIntent = new Intent(ReportActivity.this, ReturnListPage.class);
                    newIntent.putExtra("type","Country");
                    newIntent.putExtra("JSON1","county_country_id");
                    newIntent.putExtra("JSON2","county_name");
                    newIntent.putExtra("URL", "http://collegboi.me/vehicleCheck/androidFilter.php?action=country&where="+countryID);
                    startActivityForResult(newIntent, REQUEST_CODE);;
                } else {
                    Toast.makeText(ReportActivity.this, "Select country first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_report, menu);
        if (mLocType.equals(LocType.Add)) {
            MenuItem menuItem = menu.findItem(R.id.send_report);
            menuItem.setVisible(false);
        } else {
            MenuItem menuItem = menu.findItem(R.id.add_report);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(validateAllFields()) {

            if (id == R.id.send_report) {

                //get all text values into hash map
                Map<String, String> locationData = new HashMap<>();
                locationData.put("carLocationCountry", countryTextView.getText().toString());
                locationData.put("carLocationCounty", countyTextView.getText().toString());
                locationData.put("carLocationTown", townText.getText().toString());
                locationData.put("carLocationDate", dateButton.getText().toString());
                locationData.put("locCode", countryID);
                locationData.put("carID", carID);
                locationData.put("action", "add");

                LocationTask locationTask = new LocationTask(locationData);
                locationTask.execute();


            } else if (id == R.id.add_report) {

                mLocationClass = new UserCarClass.Location();
                mLocationClass.setLocCountry(countryTextView.getText().toString());
                mLocationClass.setLocCounty(countyTextView.getText().toString());
                mLocationClass.setLocTown(townText.getText().toString());
                mLocationClass.setLocDate(dateButton.getText().toString());
                mLocationClass.setLoc_country_code(countryID);

                Intent intent = new Intent();
                intent.putExtra("location", mLocationClass);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public Boolean validateAllFields() {
        if(countryTextView.getText().toString().trim().isEmpty()) {
            countryInputLayout.requestFocus();
            countryInputLayout.setError("Pick Country");
            return false;
        } else  {
            countryInputLayout.setErrorEnabled(false);
        }
        if(countyTextView.getText().toString().trim().isEmpty()) {
            countyInputLayout.requestFocus();
            countyInputLayout.setError("Pick County");
            return false;
        } else {
            countyInputLayout.setErrorEnabled(false);
        }
        if(townText.getText().toString().trim().isEmpty()) {
            townInputLayout.requestFocus();
            townInputLayout.setError("Pick Town");
            return false;
        } else {
            townInputLayout.setErrorEnabled(false);
        }
        if(dateButton.getText().toString().equals("Add Date")) {
            dateButton.requestFocus();
            dateButton.setError("Pick Date");
            return false;
        }

        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        } else {

            if(countryID == null) {
                countryTextView.setTextColor(Color.RED);
                countryTextView.setText(intent.getStringExtra("name"));
                countryID = intent.getStringExtra("id");
            } else {
                countyTextView.setTextColor(Color.RED);
                countyTextView.setText(intent.getStringExtra("name"));
            }
        }
    }

    public class LocationTask extends AsyncTask<Void, Void, String> {

        Map<String,String> mDataToSend;

        public LocationTask(Map<String, String> passedData) {
            mDataToSend = passedData;
        }

        @Override
        protected String doInBackground(Void... params) {

            //Server Communication part - it's relatively long but uses standard methods
            //Encoded String - we will have to encode string by our custom method (Very easy)
            String encodedStr = getEncodedData(mDataToSend);

            //Will be used if we want to read some data from server
            BufferedReader reader = null;

            //Connection HandlinG
            try {
                //Converting address String to URL
                URL url = new URL("http://www.collegboi.me/vehicleCheck/android_Report.php");
                //Opening the connection (Not setting or using CONNECTION_TIMEOUT)
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                //Post Method
                con.setRequestMethod("POST");
                //To enable inputting values using POST method
                //(Basically, after this we can write the dataToSend to the body of POST method)
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                //Writing dataToSend to outputstreamwriter
                writer.write(encodedStr);
                //Sending the data to the server - This much is enough to send data to server
                //But to read the response of the server, you will have to implement the procedure below
                writer.flush();

                //Data Read Procedure - Basically reading the data comming line by line
                StringBuilder sb = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) { //Read till there is something available
                    sb.append(line + "\n");     //Reading and saving line by line - not all at once
                }
                line = sb.toString();
                //Saving complete data received in string, you can do it differently

                return line;
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();     //Closing the

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String data) {

            //Log.i("JSON", data);

            if (data == null) {
                Toast.makeText(getApplicationContext(), "Report not send", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Report sent", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


    private String getEncodedData(Map<String, String> data) {
        StringBuilder sb = new StringBuilder();
        for (String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if (sb.length() > 0) sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
    }
}

