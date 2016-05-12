package airport.srl.org.airtraffic.flights;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import airport.srl.org.airtraffic.R;
import airport.srl.org.airtraffic.library.Constants;
import airport.srl.org.airtraffic.library.Httppostaux;

public class DeployFlight extends AppCompatActivity {
    Httppostaux post = new Httppostaux();
    String IP_Server = Constants.getAddress(); /* Used for an only ip change */
    String APIKEY = Constants.getApiKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy_flight);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        String plate = i.getStringExtra("flightNumber");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Flight: "+plate);

        new asyncData().execute(plate);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setData(JSONObject json_data) {
        try {
            TextView etFlightNum = (TextView) findViewById(R.id.flightNum);
            TextView etStatus    = (TextView) findViewById(R.id.status);
            TextView etPlane     = (TextView) findViewById(R.id.plane);
            TextView etOrigin    = (TextView) findViewById(R.id.origin);
            TextView etDestiny   = (TextView) findViewById(R.id.destination);
            TextView etDeparture = (TextView) findViewById(R.id.departure);
            TextView etArrival   = (TextView) findViewById(R.id.arrival);

            etFlightNum.setText(json_data.getString("flightNumber"));
            etStatus.setText(json_data.getString("status"));
            etPlane.setText(json_data.getString("plane"));
            etOrigin.setText(json_data.getString("origin"));
            etDestiny.setText(json_data.getString("destination"));
            etDeparture.setText(json_data.getString("departure"));
            etArrival.setText(json_data.getString("arrival"));

            if(json_data.getString("status").equals("DEFAULT"))
                showToast("Fligjt not founded");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showToast(CharSequence text) {
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    class asyncData extends AsyncTask<String, String, JSONObject> {
        ProgressDialog pDialog;

        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(DeployFlight.this);
            pDialog.setMessage("Getting Flight");

            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String URL_connect = "http://" + IP_Server + "flights/"+params[0]+"?apikey="+APIKEY;

            try {
                return post.getInfoJSONObject(URL_connect);
            } catch (IOException e) {
                Log.e("DoInBackground",e.toString());
                return null;
            }
        }

        protected void onPostExecute(JSONObject localListWeb) {

            if(localListWeb != null)
                setData(localListWeb);
            else
                showToast("Failed to connect after 15000ms");

            pDialog.dismiss();    /* Quit the ProgressDialog */
        }
    }
}