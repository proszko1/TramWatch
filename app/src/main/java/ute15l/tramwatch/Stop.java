package ute15l.tramwatch;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PAWEL on 2015-05-20.
 */
public final class Stop implements Comparable<Stop>{
    private String name;
    private List<Vehicle> vehicleList;
    private boolean checked = true;

    public Stop(JSONObject jsonObject) throws JSONException {
        name = jsonObject.getString("name");
        setVehicleList(new ArrayList<Vehicle>());
        JSONArray jsonArray;
        jsonArray = jsonObject.getJSONArray("vehicles");
        for(int i = 0; i< jsonArray.length(); i++){
            Vehicle vehicle = new Vehicle(jsonArray.getJSONObject(i));
            getVehicleList().add(vehicle);
        }
    }

    public static List<Stop> getCheckedStops(List<Stop> stopList){
        List<Stop> selectedStopsList = new ArrayList<Stop>();
        for(Stop stop : stopList){
            if(stop.isChecked()){
                selectedStopsList.add(stop);
            }
        }
        return selectedStopsList;
    }

    public static void parseJSONToStopsList(JSONObject jsonObject, List<Stop> stopList) {
        JSONArray jsonArray;
        try {
            jsonArray = jsonObject.getJSONArray("stops");
            for(int i = 0; i < jsonArray.length(); i++ ){
                Stop stop = new Stop(jsonArray.getJSONObject(i));
                if(stopList.contains(stop)){
                    stopList.get(stopList.indexOf(stop)).setVehicleList(stop.getVehicleList());
                } else {
                    stopList.add(stop);
                }
            }
        } catch (JSONException e) {
            Log.e("JSONException","Error parsing JSON");
            e.printStackTrace();
        }
    }


    public String getName() {
        return name;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }

    @Override
    public int compareTo(Stop another) {
        if(another.getName().equals(getName())){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Stop){
            Stop stopToCompare = (Stop) object;
            if(stopToCompare.getName().equals(getName())){
                return true;
            }
        }
        return false;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
