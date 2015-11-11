package info.androidhive.materialdesign.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.materialdesign.AsyncTask.HTTPDataHandler;
import info.androidhive.materialdesign.R;

public class RegisterPage extends AppCompatActivity {

    Toolbar mActionBarToolbar;
    ImageView mImageView;
    EditText mUsernameText, mPasswordText1, mPasswordText2;
    EditText mEmailText, mFirstName, mSurname;
    Button mRegisterButton;

    private static final int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("Register");


        mImageView = (ImageView)findViewById(R.id.profile_photo);
        mUsernameText = (EditText)findViewById(R.id.text_username);
        mPasswordText1 = (EditText)findViewById(R.id.text_password_1);
        mPasswordText2 = (EditText)findViewById(R.id.text_password_2);
        mEmailText = (EditText)findViewById(R.id.text_email);
        mFirstName= (EditText)findViewById(R.id.text_first_name);
        mSurname = (EditText)findViewById(R.id.text_last_name);
        mRegisterButton = (Button)findViewById(R.id.button_register);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent, RESULT_LOAD_IMAGE);
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkEditTextValues() == true) {

                    Map<String, String> data = new HashMap<>();
                    data.put("user",mUsernameText.getText().toString());
                    data.put("pass", mPasswordText1.getText().toString());
                    data.put("email", mEmailText.getText().toString());
                    data.put("fistname", mFirstName.getText().toString());
                    data.put("lastname", mSurname.getText().toString());

                    RegisterUserConnect registerUserConnect = new RegisterUserConnect(data);
                    registerUserConnect.execute();
                }

            }
        });

    }

    //check all input from user to authenticate
    private boolean checkEditTextValues() {

        if (mUsernameText.getText().length() == 0) {
            mUsernameText.requestFocus();
            mUsernameText.setError("Enter username");
        } else if (mUsernameText.getText().toString().contains(" ")) {
            mUsernameText.requestFocus();
            mUsernameText.setError("No spaces");
        } else if (mPasswordText1.getText().length() == 0) {
            mPasswordText1.requestFocus();
            mPasswordText1.setError("Enter password");
        } else if (!mPasswordText1.getText().toString().equals(mPasswordText2.getText().toString())) {
            mPasswordText2.requestFocus();
            mPasswordText2.setError("Passwords do not match");
        } else if (mEmailText.getText().length() == 0) {
            mEmailText.requestFocus();
            mEmailText.setError("Enter Email");
        } else if (!mEmailText.getText().toString().matches("^\\S+@\\S+\\.\\S+$")) {
            mEmailText.requestFocus();
            mEmailText.setError("Email incorrect format");
        } else if (mFirstName.getText().toString().length() == 0) {
            mFirstName.requestFocus();
            mFirstName.setError("Enter first name");
        } else if (mSurname.getText().toString().length() == 0) {
            mSurname.requestFocus();
            mSurname.setError("Enter surname");
        }else {
            return  true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {

            Uri selectedImage = data.getData();
            mImageView.setImageURI(selectedImage);
        }

    }

    private class RegisterUserConnect extends AsyncTask<Void, Void, String> {

        Map<String ,String> dataToSend;
        String userID;

        private RegisterUserConnect(Map<String, String> params) {
            dataToSend = params;
        }

        @Override
        protected String doInBackground(Void... params) {

            String URL = "";

            String encodedString = getEncodedData(dataToSend);

            String URLString = URL+"?"+encodedString;

            String stream;

            HTTPDataHandler hh = new HTTPDataHandler();
            stream = hh.GetHTTPData(URLString);

            return stream;

        }

        @Override
        protected void onPostExecute(String stream) {

            if (stream != null) try {

                JSONObject reader = new JSONObject(stream);
                JSONArray jsonArray = reader.getJSONArray("User");

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject coord = jsonArray.getJSONObject(i);
                    String id = coord.getString("id");
                    userID = id;
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected void onPreExecute() {

            if(userID != null) {
                //return back to account with id;
            }else {
                Toast.makeText(RegisterPage.this, "Error in registering", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getEncodedData(Map<String,String> data) {
        StringBuilder sb = new StringBuilder();
        for(String key : data.keySet()) {
            String value = null;
            try {
                value = URLEncoder.encode(data.get(key), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            if(sb.length()>0) sb.append("&");

            sb.append(key + "=" + value);
        }
        return sb.toString();
    }
}
