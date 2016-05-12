package airport.srl.org.airtraffic.flights;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import airport.srl.org.airtraffic.R;

public class AskStatusFlight extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    EditText etFlightStatus;
    private String mType;

    public AskStatusFlight() {}

    public static AskStatusFlight newInstance(String param1) {
        AskStatusFlight fragment = new AskStatusFlight();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mType = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ask_status_flight, container, false);
        etFlightStatus = (EditText)view.findViewById(R.id.airlineCode);

        final Spinner sStatus = (Spinner)view.findViewById(R.id.status);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.status, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        sStatus.setAdapter(adapter);

        Button bSearch = (Button) view.findViewById(R.id.search);
        bSearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String status = sStatus.getSelectedItem().toString();
                if(status.equals("ON TIME"))
                    status="ON%20TIME";
                Intent intent = new Intent(getActivity(), DeployFlights.class);
                intent.putExtra("type", mType);
                intent.putExtra("code", status);

                startActivity(intent);
            }
        });
        return view;
    }
}