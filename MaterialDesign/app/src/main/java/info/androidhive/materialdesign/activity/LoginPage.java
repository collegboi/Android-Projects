package info.androidhive.materialdesign.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import info.androidhive.materialdesign.R;

/**
 * Created by Ravi on 29/07/15.
 */
public class LoginPage extends AppCompatActivity {


    EditText usernameText;
    EditText passwordText;
    Button loginButton;
    Button registerButton;

    private static final String EXTRA_LOG_STATUS = "LOGGEDIN";

    String login_URL = "http://collegboi.me/vehicleCheck/android_account.php";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        usernameText = (EditText) findViewById(R.id.username_text);
        passwordText = (EditText) findViewById(R.id.password_text);

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                passwordText.setInputType(InputType.TYPE_NULL);
                usernameText.setInputType(InputType.TYPE_NULL);

                String userText = usernameText.getText().toString().trim();
                String passText = passwordText.getText().toString().trim();

                if (userText.equals("") || passText.equals("")) {
                    Toast.makeText(LoginPage.this, "Enter Fields", Toast.LENGTH_SHORT).show();
                } else {

                    Map<String, String> params = new HashMap<>();
                    params.put("user", userText);
                    params.put("pass", passText);
                    params.put("action", "read");

                    LoginAsyncTask loginAsyncTask = new LoginAsyncTask(params);
                    loginAsyncTask.execute(login_URL);

                }

            }
        });

        registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(LoginPage.this, MainActivity.class);
//                intent.putExtra(EXTRA_LOG_STATUS, true);
//                finish();
                Intent regIntent = new Intent(LoginPage.this, RegisterPage.class);
                startActivity(regIntent);
            }
        });
    }
    public class LoginAsyncTask extends AsyncTask<String, Void, Integer> {

        Map<String, String> dataToSend;

        String userID;


        public LoginAsyncTask(Map<String, String> data) {
            dataToSend = data;
        }


        @Override
        protected Integer doInBackground(String... params) {

            Integer result = 0;

            String url = params[0];

            String encodedStr = getEncodedData(dataToSend);

            String URL_FEED = url+"?"+encodedStr;

            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(URL_FEED));
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

            } catch (Exception e) {
                // Log.d(TAG, e.getLocalizedMessage());
            }
            //result = 1;
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {

            if(userID != null) {
                //--SAVE Data
                SharedPreferences preferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("LoggedIn", 1);
                editor.putString("user", dataToSend.get("user"));
                editor.putString("id", userID);
                editor.apply();

                Toast.makeText(LoginPage.this,"Success",Toast.LENGTH_SHORT).show();

                Intent intent = new Intent();
                intent.putExtra(EXTRA_LOG_STATUS, userID);
                setResult(RESULT_OK, intent);
                finish();

            } else {
                Toast.makeText(LoginPage.this,"Error",Toast.LENGTH_SHORT).show();
            }

        }



        @Override
        protected void onPreExecute() {

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
         */
        private void parseResult(String result) {

            try {

                JSONObject reader = new JSONObject(result);
                JSONArray jsonArray = reader.getJSONArray("User");

                JSONObject post = jsonArray.optJSONObject(0);
                String id = post.optString("id");
                userID = id;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


}



