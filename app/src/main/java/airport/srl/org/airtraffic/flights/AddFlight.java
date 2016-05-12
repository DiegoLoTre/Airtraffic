package airport.srl.org.airtraffic.flights;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.doomonafireball.betterpickers.calendardatepicker.CalendarDatePickerDialog;
import com.doomonafireball.betterpickers.radialtimepicker.RadialTimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;

import airport.srl.org.airtraffic.R;
import airport.srl.org.airtraffic.library.Constants;
import airport.srl.org.airtraffic.library.Httppostaux;

public class AddFlight extends Fragment
        implements View.OnClickListener, RadialTimePickerDialog.OnTimeSetListener, CalendarDatePickerDialog.OnDateSetListener {

    EditText etFlightNumber, etPlane, etOrigin, etDestination;
    TextView etDepartureDate, etArrivalDate,etDepartureHMS,etArrivalHMS;
    Spinner sStatus;
    Httppostaux post = new Httppostaux();
    String IP_Server = Constants.getAddress(); /* Used for an only ip change */
    String APIKEY = Constants.getApiKey();

    Calendar cDeparture = null, cArrival = null; // This will also use to the choose date


    public AddFlight() {}

    public static AddFlight newInstance() {
        return new AddFlight();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_flight, container, false);

        etFlightNumber = (EditText) view.findViewById(R.id.flightNumber);
        etPlane = (EditText) view.findViewById(R.id.plane);
        etOrigin = (EditText) view.findViewById(R.id.origin);
        etDestination = (EditText) view.findViewById(R.id.destination);
        etDepartureDate = (TextView)view.findViewById(R.id.departureDate);
        etDepartureHMS = (TextView)view.findViewById(R.id.departureHMS);
        etArrivalDate = (TextView)view.findViewById(R.id.arrivalDate);
        etArrivalHMS = (TextView)view.findViewById(R.id.arrivalHMS);

        etDepartureDate.setOnClickListener(this);
        etDepartureHMS.setOnClickListener(this);
        etArrivalDate.setOnClickListener(this);
        etArrivalHMS.setOnClickListener(this);

        getActualTime();

        sStatus = (Spinner)view.findViewById(R.id.status);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.status, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sStatus.setAdapter(adapter);
        Button bAdd = (Button) view.findViewById(R.id.addNewFlight);
        bAdd.setOnClickListener(this);

        return view;
    }

    private void showToast(CharSequence text) {
        Context context = getActivity();
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void restartFields() {
        etFlightNumber.setText("");
        etPlane.setText("");
        etOrigin.setText("");
        etDestination.setText("");
        getActualTime();

        showToast("Flight Added");
    }

    private void getActualTime(){
        cDeparture = Calendar.getInstance();
        cArrival = Calendar.getInstance();

        String departureDate = cDeparture.get(Calendar.DAY_OF_MONTH) +"-"+ (cDeparture.get(Calendar.MONTH)+1) +"-"+ cDeparture.get(Calendar.YEAR);
        String arrivalDate = cArrival.get(Calendar.DAY_OF_MONTH) +"-"+ (cArrival.get(Calendar.MONTH)+1) +"-"+ cArrival.get(Calendar.YEAR);
        String departureHMS = cDeparture.get(Calendar.HOUR) +":"+ cDeparture.get(Calendar.MINUTE);
        String arrivalHMS = cArrival.get(Calendar.HOUR)+"-"+ cArrival.get(Calendar.MINUTE);

        etDepartureDate.setText(departureDate);
        etDepartureHMS.setText(departureHMS);
        etArrivalDate.setText(arrivalDate);
        etArrivalHMS.setText(arrivalHMS);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.departureDate) {
            setDate("Departure","Date");
        }
        else if(v.getId() == R.id.departureHMS){
            setDate("Departure","Hour");
        }
        else if(v.getId() == R.id.arrivalDate) {
            setDate("Arrival","Date");
        }
        else if(v.getId() == R.id.arrivalHMS){
            setDate("Arrival","Hour");
        }
        else if(v.getId() == R.id.addNewFlight) {
            String flightNum = etFlightNumber.getText().toString();
            String status = sStatus.getSelectedItem().toString();
            String plane = etPlane.getText().toString();
            String origin = etOrigin.getText().toString();
            String destination = etDestination.getText().toString();
            long departure = cDeparture.getTimeInMillis();
            long arrival = cArrival.getTimeInMillis();

            if (flightNum.isEmpty() || status.isEmpty() || plane.isEmpty() || origin.isEmpty() || destination.isEmpty()) {
                showToast("Fill the fields");
            } else {

                JSONObject jsonObject = new JSONObject();
                JSONObject jsonData = new JSONObject();
                try {
                    jsonObject.put("flightNumber", flightNum);
                    jsonObject.put("status", status);
                    jsonObject.put("plane", plane);
                    jsonObject.put("origin", origin);
                    jsonObject.put("destination", destination);
                    jsonObject.put("departure", departure);
                    jsonObject.put("arrival", arrival);

                    jsonData.put("apikey",APIKEY);
                    jsonData.put("data",jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new asyncData().execute(jsonData.toString());
            }
        }
    }

    private String picker="";
    private void setDate(String option, String type) {
        picker = option;

        if(type.equals("Date")) {
            int year,month,day;
            if (option.equals("Departure")) {
                year = cDeparture.get(Calendar.YEAR);
                month = cDeparture.get(Calendar.MONTH);
                day = cDeparture.get(Calendar.DAY_OF_MONTH);
            } else {
                year = cArrival.get(Calendar.YEAR);
                month = cArrival.get(Calendar.MONTH);
                day = cArrival.get(Calendar.DAY_OF_MONTH);
            }

            CalendarDatePickerDialog calendarDatePickerDialog = CalendarDatePickerDialog
                    .newInstance(AddFlight.this, year, month, day);
            calendarDatePickerDialog.show(getFragmentManager(), "fragment_date_picker_name");
        } else {
            int hour,minute;
            if (option.equals("Departure")) {
                hour = cDeparture.get(Calendar.HOUR_OF_DAY);
                minute = cDeparture.get(Calendar.MINUTE);
            } else {
                hour = cArrival.get(Calendar.HOUR_OF_DAY);
                minute = cArrival.get(Calendar.MINUTE);
            }
            RadialTimePickerDialog timePickerDialog = RadialTimePickerDialog
                    .newInstance(AddFlight.this, hour, minute,false);

            timePickerDialog.show(getFragmentManager(), "timePickerDialogFragment");
        }
    }

    @Override
    public void onDateSet(CalendarDatePickerDialog dialog, int year, int month, int day) {
        String newDate = day + "-" + (month+1) + "-" + year;
        if(picker.equals("Departure")) {
            etDepartureDate.setText(newDate);

            cDeparture.set(Calendar.DAY_OF_MONTH, day);
            cDeparture.set(Calendar.MONTH, month);
            cDeparture.set(Calendar.YEAR, year);
        } else {
            etArrivalDate.setText(newDate);

            cArrival.set(Calendar.DAY_OF_MONTH, day);
            cArrival.set(Calendar.MONTH, month);
            cArrival.set(Calendar.YEAR, year);
        }
    }

    @Override
    public void onTimeSet(RadialTimePickerDialog dialog, int hourOfDay, int minuteOfDay) {
        String newHour = hourOfDay + ":" + minuteOfDay;
        if(picker.equals("Departure")) {
            cDeparture.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cDeparture.set(Calendar.MINUTE, minuteOfDay);

            etDepartureHMS.setText(newHour);
        }else{
            cArrival.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cArrival.set(Calendar.MINUTE, minuteOfDay);

            etArrivalHMS.setText(newHour);
        }
    }

    class asyncData extends AsyncTask<String, String, String> {
        ProgressDialog pDialog;

        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Adding Flight");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String URL_connect = "http://"+IP_Server+"flights/add";
            try {
                return post.getInfoString(URL_connect, "flights", params[0]);
            } catch (IOException e) {
                return null;
            }
        }

        protected void onPostExecute(String localListWeb) {
            if (localListWeb == null) {
                showToast("Failed to connect after 15000ms");
            }else {
                if (localListWeb.equals("true"))
                    restartFields();
                else
                    showToast("Error Adding Flight");
            }

            pDialog.dismiss();    /* Quit the ProgressDialog */
        }
    }
}