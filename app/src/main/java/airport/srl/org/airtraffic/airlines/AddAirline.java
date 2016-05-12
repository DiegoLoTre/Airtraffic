package airport.srl.org.airtraffic.airlines;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import airport.srl.org.airtraffic.R;
import airport.srl.org.airtraffic.library.Constants;
import airport.srl.org.airtraffic.library.Httppostaux;

public class AddAirline extends Fragment {

    EditText etAirlineCode, etAirlineName, etAirlineBase;
    Httppostaux post = new Httppostaux();
    String IP_Server = Constants.getAddress(); /* Used for an only ip change */
    String APIKEY = Constants.getApiKey();

    public AddAirline() {}

    public static AddAirline newInstance() {
        return new AddAirline();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_airline, container, false);

        etAirlineCode = (EditText)view.findViewById(R.id.airlineCode);
        etAirlineName = (EditText)view.findViewById(R.id.airlineName);
        etAirlineBase = (EditText)view.findViewById(R.id.airlineBase);

        Button bAdd = (Button)view.findViewById(R.id.addNewAirport);
        bAdd.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                String airlineCode = etAirlineCode.getText().toString();
                String airlineName = etAirlineName.getText().toString();
                String airlineBase =  etAirlineBase.getText().toString();

                if(airlineCode.isEmpty() || airlineName.isEmpty() || airlineBase.isEmpty()) {
                    showToast("Fill the fields");
                } else {

                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonData = new JSONObject();

                    try {
                        jsonObject.put("airlineCode",airlineCode);
                        jsonObject.put("name",airlineName);
                        jsonObject.put("base",airlineBase);

                        jsonData.put("apikey",APIKEY);
                        jsonData.put("data",jsonObject);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    new asyncData().execute(jsonData.toString());
                }
            }
        });

        return view;
    }
    private void showToast(CharSequence text) {
        Context context = getActivity();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void restartFields() {
        etAirlineCode.setText("");
        etAirlineName.setText("");
        etAirlineBase.setText("");

        showToast("Airline Added");
    }

    class asyncData extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Airline");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL_connect = "http://"+IP_Server+"airlines/add";
            try {
                return  post.getInfoString(URL_connect,"airline",params[0]);
                //return  post.getInfoString(URL_connect,"Token",params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        protected void onPostExecute(String localListWeb) {
            if(localListWeb == null)
                showToast("Failed to connect after 15000ms");
            if(localListWeb.equals("true"))
                restartFields();
            else
                showToast("Error Adding Airline");

            pDialog.dismiss();    /* Quit the ProgressDialog */
        }
    }
}