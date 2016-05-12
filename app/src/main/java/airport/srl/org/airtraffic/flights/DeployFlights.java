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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import airport.srl.org.airtraffic.R;
import airport.srl.org.airtraffic.library.Constants;
import airport.srl.org.airtraffic.library.Httppostaux;
import airport.srl.org.airtraffic.library.ListHandler;

public class DeployFlights extends AppCompatActivity {

    Httppostaux post = new Httppostaux();
    String IP_Server = Constants.getAddress(); /* Used for an only ip change */
    String APIKEY = Constants.getApiKey();
    ListView listView;
    ListHandler listHandler = new ListHandler();
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deploy_departure_arrivals);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent i = getIntent();
        String code = i.getStringExtra("code");
        type = i.getStringExtra("type");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        switch (type){
            case "AirlinesFlights":
                getSupportActionBar().setTitle("Flights of Airlines: "+code);
                break;
            case "Status":
                getSupportActionBar().setTitle("Flights:"+code);
            default:
                getSupportActionBar().setTitle(type+" of Airport: "+code);
                break;
        }

        listView = (ListView)findViewById(R.id.listView);

        new asyncData().execute(code);
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

    private void setData(List<HashMap<String, Object>> localList) {
        String[] from = new String[] {"flightNum","status","plane","origin","destiny","departure","arrival"};
        int[] to = new int[]{ R.id.flightNumber,R.id.status,R.id.airplane,R.id.origin,R.id.destination,R.id.departure,R.id.arrival};

        // fill in the grid_item layout
        SimpleAdapter localSimpleAdapter = new SimpleAdapter(getBaseContext(), localList, R.layout.list_flights, from, to);
        listView.setAdapter(localSimpleAdapter);

        listView.setFastScrollEnabled(true);
        registerForContextMenu(listView);
    }

    private void showToast() {
        Context context = this;
        CharSequence text = "Failed to connect after 15000ms";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    class asyncData extends AsyncTask<String, String, List<HashMap<String, Object>>> {
        ProgressDialog pDialog;

        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(DeployFlights.this);
            pDialog.setMessage("Getting Flights");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected List<HashMap<String, Object>> doInBackground(String... params) {
            String URL_connect;
            switch (type) {
                case "Departures":
                    URL_connect = "http://" + IP_Server + "airports/"+params[0]+"/departures?apikey="+APIKEY;
                    break;
                case "AirlinesFlights":
                    URL_connect = "http://" + IP_Server + "airlines/"+params[0]+"/flights?apikey="+APIKEY;
                    break;
                case "Status":
                    URL_connect = "http://"+ IP_Server + "flights/status/"+params[0]+"?apikey="+APIKEY;
                    Log.d("URL",URL_connect);
                    break;
                default:
                    URL_connect = "http://" + IP_Server + "airports/"+params[0]+"/arrivals?apikey="+APIKEY;
                    break;
            }
            Log.d("URL",URL_connect);
            try {
                return listHandler.createList(post.getInfoJSON(URL_connect),"Flights");
            } catch (IOException e) {
                Log.e("IOException",e.toString());
                return null;
            }
        }

        protected void onPostExecute(List<HashMap<String, Object>> localListWeb) {
            if(localListWeb != null)
                setData(localListWeb);
            else
                showToast();

            pDialog.dismiss();    /* Quit the ProgressDialog */
        }
    }

}