package info.androidhive.materialdesign.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.androidhive.materialdesign.AsyncTask.GenericAsyncTask;
import info.androidhive.materialdesign.AsyncTask.HTTPDataHandler;
import info.androidhive.materialdesign.OnTaskCompleted;
import info.androidhive.materialdesign.R;
import info.androidhive.materialdesign.adapter.UserCarAdapter;
import info.androidhive.materialdesign.database.MotorDatabase;
import info.androidhive.materialdesign.model.UserCarClass;

public class Account_page extends Fragment implements OnTaskCompleted {

    private static final String EXTRA_LOG_STATUS = "LOGGEDIN";
    private static final int REQUEST_CODE = 0;
    private static final int ADD_CAR_REQUEST = 1;

    //OnTaskCompleted mOnTaskCompleted;
    MotorDatabase mMotorDatabase;
    Context mContext;

    //UI Widgets
    ImageView userImage;
    TextView userText;
    ListView carList;
    ImageView fabImageView;
    UserCarAdapter userCarAdapter;
    private String username;

    int testCounter = 0;

    public  Account_page() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = getActivity().getApplicationContext();
        mMotorDatabase = new MotorDatabase(mContext);
        mMotorDatabase.open();

        //mOnTaskCompleted = this;

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_account_page, container, false);

        userImage = (ImageView)rootView.findViewById(R.id.profile_image);
        userText = (TextView)rootView.findViewById(R.id.user_text);
        carList = (ListView)rootView.findViewById(R.id.listView);

        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UserCarClass clickedCar = (UserCarClass) userCarAdapter.getItem(position);

                Intent intent = new Intent(getActivity(), CarDetail.class);
                intent.putExtra("key", clickedCar.getCarID());
                intent.putExtra("type", "edit");
                startActivity(intent);
            }
        });

        carList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showSimplePopUp(position);
                return true;
            }
        });

        fabImageView = (ImageView)rootView.findViewById(R.id.fab_image_button);
        fabImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity(),"Add Car", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getActivity(), AddCarView.class);
                startActivityForResult(intent, ADD_CAR_REQUEST);

            }
        });

        userCarAdapter = new UserCarAdapter(getActivity(),R.layout.user_row_layout);
        carList.setAdapter(userCarAdapter);

        SharedPreferences preferences = getActivity().getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        //SharedPreferences.Editor editor = preferences.edit();
        Integer myVar = preferences.getInt("LoggedIn", 0);

        if(myVar != 1) {
            Intent intent = new Intent(getActivity(), LoginPage.class);
            startActivityForResult(intent, REQUEST_CODE);
            Log.w("User", "Not Logged In");

        } else  {
            Log.w("User", "Logged In");
            getVehiclesFromDB();
        }

        return rootView;
    }

    private void showSimplePopUp(final int row) {

        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getActivity());
        helpBuilder.setTitle("");
        helpBuilder.setMessage("Are you sure you want to remove?");
        helpBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        UserCarClass clickedCar = (UserCarClass) userCarAdapter.getItem(row);

                        Map<String ,String> keyValue = new HashMap<>();
                        keyValue.put("carID", clickedCar.getCarID());
                        keyValue.put("action", "delete");

                        String URL = "";
                        GenericAsyncTask genericAsyncTask = new GenericAsyncTask(getActivity(), keyValue );
                        genericAsyncTask.execute(URL);
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

    /*
    * Interface class call back method prePostRequest
    * */
    @Override
    public void finishedTask(String result) {
        Log.w("Return ", result);

        Object carObject = userCarAdapter.getItem(0);
        userCarAdapter.remove(carObject);
        userCarAdapter.notifyDataSetChanged();

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            FragmentManager frag = getFragmentManager();
            frag.beginTransaction().replace(R.id.container_body, new SearchFragment()).commit();
            AppCompatActivity activity = (AppCompatActivity)getActivity();
            activity.getSupportActionBar().setTitle("Search");

        } else if(resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE){
            if(data == null) {
                return;
            }

            username = data.getStringExtra(EXTRA_LOG_STATUS);
            Log.w("User",username);
            new ProcessJSON().execute("http://collegboi.me/vehicleCheck/getCars.php?username=404150666444276&offset=0&limit=40");
        }

        //if(requestCode == REQUEST_CODE) {


        if(requestCode == ADD_CAR_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.w("Car","Added");
            UserCarClass mUserCarClass = data.getParcelableExtra("car");
            userCarAdapter.add(mUserCarClass);
        }

    }

    private void getVehiclesFromDB() {

        List<UserCarClass> userCarClasses = mMotorDatabase.getAllVehicles();

        for(UserCarClass userCarClass: userCarClasses) {
            userCarAdapter.add(userCarClass);
        }
    }


    private class ProcessJSON extends AsyncTask<String, String, String> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onProgressUpdate(String ... values) {
            mProgressDialog.setMessage(values[0]);
        }

        @Override
        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("Downloading.....");
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... strings){
            String stream = null;
            String urlString = strings[0];

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(urlString);

            // Return the data from specified url
            return stream;
        }

        @Override
        protected void onPostExecute(String stream){

            if(stream !=null) try {

                JSONObject reader = new JSONObject(stream);
                JSONArray jsonArray = reader.getJSONArray("Car");

                for(int i = 0; i < jsonArray.length(); i++) {

                    JSONObject coord = jsonArray.getJSONObject(i);
//                    if (coord.getString("carReg") == null) {
//
//                    }
                    final String carReg = coord.getString("carReg");
                    final String carDate = coord.getString("carLocationDate");
                    final String carPhoto = coord.getString("carPhoto");
                    final String id = coord.getString("id");

                    JSONArray UUIDJsonArray = coord.getJSONArray("location_UUID");

                    List<String> reportsUUID = new ArrayList<>();

                    UserCarClass userCarClass = new UserCarClass();
                    userCarClass.setCarID(id);
                    userCarClass.setCarReg(carReg);


                    ArrayList<UserCarClass.Location> locations = new ArrayList<>();
                    UserCarClass.Location location = new UserCarClass.Location();
                    location.setLocDate(carDate);
                    userCarClass.setCarLocation(locations);

                    DownloadImageTask downloadImageTask = new DownloadImageTask(userCarClass);
                    downloadImageTask.execute(carPhoto);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }// if statement end
            mProgressDialog.dismiss();
        } // onPostExecute() end
    } // ProcessJSON class end


    public static String encodeToBase64(Bitmap image)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        return imageEncoded;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        UserCarClass mUserCarClass;

        public DownloadImageTask(UserCarClass userCarClass) {
            mUserCarClass = userCarClass;
        }

        @Override
        protected void onPreExecute() {

        }

        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap mIcon = null;
            try {
                InputStream in = new java.net.URL(url).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon;
        }

        protected void onPostExecute(Bitmap result) {

            String bitmapString = encodeToBase64(result);
            ArrayList<String> images = new ArrayList<>();
            images.add(bitmapString);
            mUserCarClass.setCarPhoto(images);

            userCarAdapter.add(mUserCarClass);

            mMotorDatabase.insertData(mUserCarClass);

            userCarAdapter.notifyDataSetChanged();
        }
    }
}
