package airport.srl.org.airtraffic.flights;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import airport.srl.org.airtraffic.R;

public class AskFlight extends Fragment {
    EditText etFlightNumber;
    public AskFlight() {}

    public static AskFlight newInstance() {
        return new AskFlight();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ask_flight, container, false);
        etFlightNumber = (EditText)view.findViewById(R.id.flightNumber);
        Button bSearch = (Button) view.findViewById(R.id.search);
        bSearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String flightNumber = etFlightNumber.getText().toString();
                if(!flightNumber.isEmpty()) {
                    Intent intent = new Intent(getActivity(), DeployFlight.class);
                    intent.putExtra("flightNumber", flightNumber);

                    startActivity(intent);
                } else {
                    Context context = getActivity();
                    CharSequence text = "Fill the fields";
                    int duration = Toast.LENGTH_LONG;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            }
        });
        return view;
    }
}