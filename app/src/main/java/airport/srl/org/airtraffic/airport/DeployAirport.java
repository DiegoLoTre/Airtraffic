package airport.srl.org.airtraffic.airport;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import airport.srl.org.airtraffic.R;
import airport.srl.org.airtraffic.library.Constants;
import airport.srl.org.airtraffic.library.Httppostaux;

public class DeployAirport extends AppCompatActivity {
    Httppostaux post = new Httppostaux();
    String IP_Server = Constants.getAddress(); /* Used for an only ip change */
    String APIKEY = Constants.getApiKey();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy_airport);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent i = getIntent();
        String airportCode = i.getStringExtra("code");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Airport: "+airportCode);

        new asyncData().execute(airportCode);
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
            TextView etName    = (TextView) findViewById(R.id.airportName);
            TextView etAddress     = (TextView) findViewById(R.id.airportAddress);

            etName.setText(json_data.getString("name"));
            etAddress.setText(json_data.getString("address"));

            if(json_data.getString("name").equals("DEFAULT"))
                showToast("Airport not founded");

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
            pDialog = new ProgressDialog(DeployAirport.this);
            pDialog.setMessage("Getting Airport");

            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            String URL_connect = "http://" + IP_Server + "airports/"+params[0]+"?apikey="+APIKEY;

            try {
                return post.getInfoJSONObject(URL_connect);
            } catch (IOException e) {
                e.printStackTrace();
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