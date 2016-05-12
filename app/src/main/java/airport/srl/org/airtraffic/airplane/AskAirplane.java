package airport.srl.org.airtraffic.airplane;

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

public class AskAirplane extends Fragment {
    EditText etPlate;
    public AskAirplane() {}

    public static AskAirplane newInstance() {
        return new AskAirplane();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ask_airplane, container, false);
        etPlate = (EditText)view.findViewById(R.id.plate);
        Button bSearch = (Button) view.findViewById(R.id.search);
        bSearch.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                String plate = etPlate.getText().toString();
                if(!plate.isEmpty()) {
                    Intent intent = new Intent(getActivity(), DeployAirplane.class);
                    intent.putExtra("code", plate);

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