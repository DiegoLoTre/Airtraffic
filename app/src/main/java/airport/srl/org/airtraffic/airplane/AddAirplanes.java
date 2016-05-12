package airport.srl.org.airtraffic.airplane;

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

public class AddAirplanes extends Fragment {

    EditText etPlate, etOwner, etModel, etHoursOnFlight;
    Httppostaux post = new Httppostaux();
    String IP_Server = Constants.getAddress(); /* Used for an only ip change */
    String APIKEY = Constants.getApiKey();

    public AddAirplanes() {
    }

    public static AddAirplanes newInstance() {
        return new AddAirplanes();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_airplanes, container, false);

        etPlate = (EditText) view.findViewById(R.id.plate);
        etOwner = (EditText) view.findViewById(R.id.owner);
        etModel = (EditText) view.findViewById(R.id.model);
        etHoursOnFlight = (EditText) view.findViewById(R.id.hoursOnFlight);

        Button bAdd = (Button) view.findViewById(R.id.addNewAirplane);
        bAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String plate = etPlate.getText().toString();
                String owner = etOwner.getText().toString();
                String model = etModel.getText().toString();
                String hoursOnFlight = etHoursOnFlight.getText().toString();

                if (plate.isEmpty() || owner.isEmpty() || model.isEmpty() || hoursOnFlight.isEmpty()) {
                    showToast("Fill the fields");
                } else {

                    JSONObject jsonObject = new JSONObject();
                    JSONObject jsonData = new JSONObject();
                    try {
                        jsonObject.put("plate", plate);
                        jsonObject.put("owner", owner);
                        jsonObject.put("model", model);
                        jsonObject.put("hoursOnFlight", hoursOnFlight);

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
        etPlate.setText("");
        etOwner.setText("");
        etModel.setText("");
        etHoursOnFlight.setText("");

        showToast("Airplane Added");
    }

    class asyncData extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Airplane");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL_connect = "http://"+IP_Server+"airplanes/add";
            try {
                return post.getInfoString(URL_connect, "airplanes", params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        protected void onPostExecute(String localListWeb) {
            if (localListWeb == null)
                showToast("Failed to connect after 15000ms");
            if (localListWeb.equals("true"))
                restartFields();
            else
                showToast("Error Adding Airplane");

            pDialog.dismiss();    /* Quit the ProgressDialog */
        }
    }
}
