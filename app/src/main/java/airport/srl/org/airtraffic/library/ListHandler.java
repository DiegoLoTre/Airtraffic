package airport.srl.org.airtraffic.library;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListHandler {
    public List<HashMap<String,Object>> createList(JSONArray jdata, String type) {
        Log.w("Type:", type);
        List<HashMap<String, Object>> localListWeb = new ArrayList<>();
        try
        {
            for (int i = 0; i < jdata.length(); i++) {
                JSONObject json_data = jdata.getJSONObject(i);

                switch (type){
                    case "Airports":
                        localListWeb.add(hashMapAirports(json_data));
                        break;
                    case "Flights":
                        localListWeb.add(hashMapFlights(json_data));
                        break;
                    case "Airlines":
                        localListWeb.add(hashMapAirlines(json_data));
                        break;
                    case "Airplanes":
                        localListWeb.add(hashMapAirplanes(json_data));
                        break;
                }
            }
        }
        catch (Exception localException)
        {
            localException.printStackTrace();
        }

        return localListWeb;
    }

    private HashMap<String,Object> hashMapAirplanes(JSONObject json_data) {
        HashMap<String, Object> localHashMap = new HashMap<>(2);

        try {
            localHashMap.put("plate",        json_data.getString("plate"));
            localHashMap.put("owner",        json_data.getString("owner"));
            localHashMap.put("model",        json_data.getString("model"));
            localHashMap.put("hoursOnFlight",json_data.getString("hoursOnFlight"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return localHashMap;
    }

    private HashMap<String,Object> hashMapAirlines(JSONObject json_data) {
        HashMap<String, Object> localHashMap = new HashMap<>(2);

        try {
            localHashMap.put("airlineCode",json_data.getString("airlineCode"));
            localHashMap.put("name",       json_data.getString("name"));
            localHashMap.put("base",       json_data.getString("base"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return localHashMap;
    }

    private HashMap<String,Object> hashMapAirports(JSONObject json_data) {
        HashMap<String, Object> localHashMap = new HashMap<>(2);

        try {
            localHashMap.put("airportCode",json_data.getString("airportCode"));
            localHashMap.put("name",       json_data.getString("name"));
            localHashMap.put("address",    json_data.getString("address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return localHashMap;
    }
    private HashMap<String,Object> hashMapFlights(JSONObject json_data) {
        HashMap<String, Object> localHashMap = new HashMap<>(2);

        try {
            localHashMap.put("flightNum",json_data.getInt("flightNumber"));
            localHashMap.put("plane",    json_data.getString("plane"));
            localHashMap.put("origin",   json_data.getString("origin"));
            localHashMap.put("departure",json_data.getString("departure"));
            localHashMap.put("destiny",  json_data.getString("destination"));
            localHashMap.put("arrival",  json_data.getString("arrival"));
            localHashMap.put("status",   json_data.getString("status"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return localHashMap;
    }
}