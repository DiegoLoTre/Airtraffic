package airport.srl.org.airtraffic.airlines;

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
import airport.srl.org.airtraffic.flights.DeployFlights;

public class AskAirline extends Fragment {
    private static final String ARG_PARAM1 = "param1";

    EditText etAirlineCode;
    private String mType;

    public AskAirline() {}

    public static AskAirline newInstance(String param1) {
        AskAirline fragment = new AskAirline();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask_airline, container, false);
        etAirlineCode = (EditText)view.findViewById(R.id.airlineCode);
        Button bSearch = (Button) view.findViewById(R.id.search);
        bSearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String airlineCode = etAirlineCode.getText().toString();
                if(!airlineCode.isEmpty()) {
                    Intent intent;
                    if(mType.equals("Airlines"))
                        intent = new Intent(getActivity(), DeployAirline.class);
                    else {
                        intent = new Intent(getActivity(), DeployFlights.class);
                        intent.putExtra("type", mType);
                    }
                    intent.putExtra("code", airlineCode);

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