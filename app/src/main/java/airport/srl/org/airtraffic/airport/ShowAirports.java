package airport.srl.org.airtraffic.airport;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class ShowAirports extends Fragment {
    Httppostaux post = new Httppostaux();
    String IP_Server = Constants.getAddress(); /* Used for an only ip change */
    String APIKEY = Constants.getApiKey();
    ListView listView;
    ListHandler listHandler = new ListHandler();

    public ShowAirports() {}

    public static ShowAirports newInstance() {
        return new ShowAirports();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_show_airports, container, false);

        listView = (ListView) view.findViewById(R.id.ShowAirportsListView);

        new asyncData().execute();

        return view;
    }

    private void setData(List<HashMap<String, Object>> localList) {
        String[] from = new String[] {"airportCode","name","address"};
        int[] to = new int[]{ R.id.airportCode,R.id.airportName,R.id.airportAddress};

        // fill in the grid_item layout
        SimpleAdapter localSimpleAdapter = new SimpleAdapter(getActivity().getBaseContext(), localList, R.layout.list_airports, from, to);
        listView.setAdapter(localSimpleAdapter);

        listView.setFastScrollEnabled(true);
        registerForContextMenu(listView);
    }
    private void showToast() {
        Context context = getActivity();
        CharSequence text = "Failed to connect after 15000ms";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }
    class asyncData extends AsyncTask<String, String, List<HashMap<String, Object>>> {
        ProgressDialog pDialog;

        protected void onPreExecute() {
            //para el progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Airports");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected List<HashMap<String, Object>> doInBackground(String... params) {
            String URL_connect = "http://" + IP_Server + "airports?apikey="+APIKEY;
            try {
                return listHandler.createList(post.getInfoJSON(URL_connect),"Airports");
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