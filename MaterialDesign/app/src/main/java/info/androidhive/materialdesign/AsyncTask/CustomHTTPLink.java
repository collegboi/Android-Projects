package info.androidhive.materialdesign.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by Timbarnard on 05/11/15.
 */
public class CustomHTTPLink {

    Map<String ,String> mDataToSend;
    String mURL;

    public CustomHTTPLink(Map<String, String> data, String URL) {
        mDataToSend = data;
        mURL = URL;
    }

    public String createConnection() {

        String encodedStr = getEncodedData(mDataToSend);

        //Will be used if we want to read some data from server
        BufferedReader reader = null;

        //Connection HandlinG
        try {
            //Converting address String to URL
            //
            URL url = new URL(mURL);
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
