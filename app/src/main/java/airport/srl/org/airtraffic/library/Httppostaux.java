package airport.srl.org.airtraffic.library;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class Httppostaux {

    public String getInfoString(String myURL,String... params) throws IOException {
        HashMap<String, String> postDataParams = new HashMap<>();
        postDataParams.put(params[0],params[1]);
        return performPostCall(myURL,postDataParams);
    }

    public JSONObject getInfoJSONObject(String myURL) throws IOException {
        try {
            return new JSONObject(downloadUrl(myURL));
        } catch (JSONException e) {
            Log.e("Error", e.toString());
            return null;
        }
    }

    public JSONArray getInfoJSON(String myURL) throws IOException {
        try {
            return new JSONArray(downloadUrl(myURL));
        } catch (JSONException e) {
            Log.e("Error", e.toString());
            return null;
        }
    }
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Type","application/json");
            // Starts the query
            conn.connect();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            return getpostresponse(is);

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        }
        finally {
            if (is != null) {
                is.close();
            }
        }
    }
    private String getpostresponse(InputStream is) {
        String result;
        //Convierte respuesta a String
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();

            result = sb.toString();
            Log.e("getpostresponse"," result= "+result);
        }catch(Exception e){
            Log.e("log_tag", "Error converting result "+e.toString());
            result = "Error";
        }
        return result;
    }

    public String  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) throws IOException {
        InputStream is;
        String response = "";
        try {
            URL url = new URL(requestURL);

            Log.d("URL",requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type","application/json");


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            Log.e("ResponseCode",responseCode+"");

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                Log.e("ResponseCode1",responseCode+"");
                is = conn.getInputStream();

                // Convert the InputStream into a string
                response = getpostresponse(is);

                is.close();
            }
            else {
                response="";
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("performPostCallCatch",e.toString());
        }

        Log.d("performPostCall",response);

        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            /*result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");*/
            result.append(entry.getValue());

            Log.d("Val",entry.getValue());
        }

        Log.d("URLEncoder",result.toString());

        return result.toString();
    }
}