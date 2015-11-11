package info.androidhive.materialdesign.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.database.MotorDatabase;
import info.androidhive.materialdesign.model.UserCarClass;

public class AddCarView extends AppCompatActivity {

    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int REQUEST_CODE = 0;
    private Context mContext;
    private MotorDatabase mMotorDatabase;

    ArrayList<Bitmap> imageBitmapArray;
    ArrayList<ImageView> mImageViews;

    //Location Class
    UserCarClass.Location mLocationClass;

    Button locationButton, uploadButton;
    EditText regTextView, vinTextView, makeTextView, modelTextView, colorTextView, yearTextView, carDescTextView;
    Toolbar mActionBarToolbar;
    TextInputLayout mInputLayoutReg, mInputLayoutVin, mInputLayoutMake, mInputLayoutModel, mInputLayoutColor, mInputLayoutYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car_view);

        mContext = this.getApplicationContext();
        mMotorDatabase = new MotorDatabase(mContext);
        mMotorDatabase.open();

        imageBitmapArray = new ArrayList<>();

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Add Motor");

        mImageViews = new ArrayList<>();
        mImageViews.add((ImageView) findViewById(R.id.car_photo1));
        mImageViews.add((ImageView) findViewById(R.id.car_photo2));
        mImageViews.add((ImageView) findViewById(R.id.car_photo3));
        mImageViews.add((ImageView) findViewById(R.id.car_photo4));
        mImageViews.add((ImageView) findViewById(R.id.car_photo5));

        mInputLayoutReg = (TextInputLayout) findViewById(R.id.input_layout_reg);
        regTextView = (EditText) findViewById(R.id.text_car_reg);
        mInputLayoutVin = (TextInputLayout) findViewById(R.id.input_layout_vin);
        vinTextView = (EditText) findViewById(R.id.text_car_vin);
        mInputLayoutMake = (TextInputLayout) findViewById(R.id.input_layout_make);
        makeTextView = (EditText) findViewById(R.id.text_car_make);
        mInputLayoutModel = (TextInputLayout) findViewById(R.id.input_layout_model);
        modelTextView = (EditText) findViewById(R.id.text_car_model);
        mInputLayoutColor = (TextInputLayout) findViewById(R.id.input_layout_color);
        colorTextView = (EditText) findViewById(R.id.text_car_color);
        mInputLayoutYear = (TextInputLayout) findViewById(R.id.input_layout_year);
        yearTextView = (EditText) findViewById(R.id.text_car_year);
        locationButton = (Button) findViewById(R.id.location_button);
        carDescTextView = (EditText) findViewById(R.id.car_description);
        uploadButton = (Button) findViewById(R.id.button_upload);

        regTextView.addTextChangedListener(new MyTextWatcher(regTextView));
        yearTextView.addTextChangedListener(new MyTextWatcher(yearTextView));

        //only if data is send to class
        if (getIntent().getExtras() != null) {
            Bundle b = getIntent().getExtras();
            UserCarClass obj = b.getParcelable("carObject");
            setDataTextField(obj);
        }

        for (int i = 0; i < mImageViews.size(); i++) {

            mImageViews.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
                }
            });
            final int finalI = i;
            mImageViews.get(i).setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    final int next = finalI + 1;
                    mImageViews.get(finalI).setImageBitmap(imageBitmapArray.get(next));
                    mImageViews.get(next).setImageResource(R.drawable.take_a_pic);
                    imageBitmapArray.remove(imageBitmapArray.size());
                    return true;
                }
            });

        }

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent locationIntent = new Intent(AddCarView.this, ReportActivity.class);
                locationIntent.putExtra("type", "add");
                startActivityForResult(locationIntent, REQUEST_CODE);

            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (checkAllTextFields()) {

                    SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                    String userName = preferences.getString("user", "");

                    //get all text values into hash map
                    Map<String, String> carData = new HashMap<>();
                    carData.put("carReg", regTextView.getText().toString());
                    carData.put("carVin", vinTextView.getText().toString());
                    carData.put("carMake", makeTextView.getText().toString());
                    carData.put("carModel", modelTextView.getText().toString());
                    carData.put("carColor", colorTextView.getText().toString());
                    carData.put("carYear", yearTextView.getText().toString());
                    carData.put("carDesc", carDescTextView.getText().toString());
                    carData.put("locCountry", mLocationClass.getLocCountry());
                    carData.put("locCounty", mLocationClass.getLocCounty());
                    carData.put("locTown", mLocationClass.getLocTown());
                    carData.put("locDate", mLocationClass.getLocDate());
                    carData.put("locCode", mLocationClass.getLoc_country_code());
                    carData.put("carUSERNAME", userName);

                    new UploadImage(imageBitmapArray, carData).execute();
                }
            }
        });
    }
    //email reg exp ^\S+@\S+\.\S+$

    //setting up text-fields with data if retrieved from car detail
    private void setDataTextField(UserCarClass mUserCarClass) {
        regTextView.setText(mUserCarClass.getCarReg());
        regTextView.setFocusable(false);
        makeTextView.setText(mUserCarClass.getCarMake());
        modelTextView.setText(mUserCarClass.getCarModel());
        colorTextView.setText(mUserCarClass.getCarColor());
        yearTextView.setText(mUserCarClass.getCarYear());
        carDescTextView.setText(mUserCarClass.getCarDesc());

        int count = mUserCarClass.getCarPhoto().size();
        for (int i = 0; i < count; i++) {
            //Picasso.with(this).load().into();

            final int finalI1 = i;
            Picasso.with(this)
                    .load(mUserCarClass.getCarPhoto().get(i))
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            mImageViews.get(finalI1).setImageBitmap(bitmap);
                            imageBitmapArray.add(bitmap);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            mImageViews.get(finalI1).setImageResource(R.drawable.ic_error);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }
    }

    //checking user input values
    private boolean checkAllTextFields() {

        if (!validateReg()) {
            return false;
        } else if (makeTextView.getText().length() == 0) {
            makeTextView.requestFocus();
            makeTextView.setError("Make cannot be empty");
        } else if (!makeTextView.getText().toString().matches("[a-zA-Z ]+")) {
            makeTextView.requestFocus();
            makeTextView.setError("Make only alpha characters");
        } else if (modelTextView.getText().length() == 0) {
            modelTextView.requestFocus();
            modelTextView.setError("Model cannot be empty");
        } else if (colorTextView.getText().length() == 0) {
            colorTextView.requestFocus();
            colorTextView.setError("Color cannot be empty");
        } else if (!colorTextView.getText().toString().matches("[a-zA-Z ]+")) {
            colorTextView.requestFocus();
            colorTextView.setError("Color only alpha characters");
        } else if (!validateYear()) {
            return false;
        } else {
            return true;
        }
        return false;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Back from gallery picker
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            if (imageBitmapArray.size() <= 5) {

                ImageView posImage = mImageViews.get(imageBitmapArray.size());

                Uri selectedImage = data.getData();
                posImage.setImageURI(selectedImage);

                try {
                    Bitmap bm = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                    imageBitmapArray.add(bm);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }

            //back from picking a location
        } else if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            Bundle b = data.getExtras();
            mLocationClass = b.getParcelable("location");
        }

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.text_car_reg:
                    validateReg();
                    break;
                case R.id.text_car_year:
                    validateYear();
                    break;
            }
        }
    }

    private Boolean validateReg() {
        if (regTextView.getText().toString().trim().isEmpty()) {
            mInputLayoutReg.setError("Enter Reg");
            mInputLayoutReg.requestFocus();
            return false;
        } else if (regTextView.getText().toString().matches(".*[^A-Za-z0-9].*")) {
            mInputLayoutReg.setError("No symbols or space");
            mInputLayoutReg.requestFocus();
            return false;
        } else {
            mInputLayoutReg.setErrorEnabled(false);
        }

        return true;


    }

    public Boolean validateYear() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        year += 1;
        if (yearTextView.getText().toString().trim().isEmpty()) {
            mInputLayoutYear.setError("Enter year");
            mInputLayoutYear.requestFocus();
            return false;
        } else if (!yearTextView.getText().toString().matches("[0-9 ]+")) {
            mInputLayoutYear.setError("Year only numbers");
            mInputLayoutYear.requestFocus();
            return false;
        } else if (Integer.valueOf(yearTextView.getText().toString()) > year) {
            mInputLayoutYear.requestFocus();
            mInputLayoutYear.setError("Year less than " + year);
        } else {
            mInputLayoutYear.setErrorEnabled(false);
        }
        return true;
    }


    private class UploadImage extends AsyncTask<Void, Void, String> {

        ArrayList<Bitmap> mBitmaps;
        Map<String, String> carInfo;

        public UploadImage(ArrayList<Bitmap> bitmaps, Map<String, String> data) {
            this.mBitmaps = bitmaps;
            this.carInfo = data;
        }

        @Override
        protected String doInBackground(Void... params) {

            Integer count = 1;

            Log.w("Upload", "Starting...");

            //Use HashMap, it works similar to NameValuePair
            Map<String, String> dataToSend = new HashMap<>();

            for (Bitmap bitmap : mBitmaps) {

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

                String encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);

                dataToSend.put("image" + count, encodedImage);

                count++;
            }


            dataToSend.putAll(carInfo);

            //Server Communication part - it's relatively long but uses standard methods

            //Encoded String - we will have to encode string by our custom method (Very easy)
            String encodedStr = getEncodedData(dataToSend);

            //Will be used if we want to read some data from server
            BufferedReader reader = null;

            //Connection HandlinG
            try {
                //Converting address String to URL
                //http://timothybarnard.org/motorSpy/savePicture.php
                //
                URL url = new URL("http://collegboi.me/vehicleCheck/android_Upload.php");
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
            //Just check to the values received in Logcat
            Log.i("custom_check", "The values received in the store part are as follows:");
            Log.i("JSON", data);

            /* once it is successful then then it is sent back to the profile page
            checking that data return is not null */
            if (data != null)

                try {
                    JSONObject jsonObject = new JSONObject(data);


                    String carID = jsonObject.getString("carID");
                    String image_URL = jsonObject.getString("photo");

                    //create a UserCarClass with few variables to send to SQLITEDB
                    UserCarClass userCarClass = new UserCarClass();
                    userCarClass.setCarID(carID);
                    ArrayList<String> images = new ArrayList<>();
                    images.add(image_URL);
                    userCarClass.setCarPhoto(images);
                    userCarClass.setCarReg(regTextView.getText().toString());
                    ArrayList<UserCarClass.Location> locations = new ArrayList<>();
                    UserCarClass.Location location = new UserCarClass.Location();
                    location.setLocDate(mLocationClass.getLocDate());
                    locations.add(location);
                    userCarClass.setCarLocation(locations);

                    mMotorDatabase.insertData(userCarClass);
                    mMotorDatabase.close();

                    returnCarBack(userCarClass);

                    Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error in data", Toast.LENGTH_SHORT).show();
                }
            else {

                Toast.makeText(getApplicationContext(), "Error in uploading", Toast.LENGTH_SHORT).show();

            }

        }

    }

    private void returnCarBack(UserCarClass userCarClass) {
        Intent intent = new Intent();
        intent.putExtra("car", userCarClass);
        setResult(RESULT_OK, intent);
        finish();
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
