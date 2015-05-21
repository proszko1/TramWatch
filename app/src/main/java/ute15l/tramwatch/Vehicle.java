package ute15l.tramwatch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PAWEL on 2015-05-20.
 */
public final class Vehicle {
    private String number;
    private String type;
    private String time;
    private String timeToGo;
    private String direction;
    private AdditionalVehicleInfo additionalVehicleInfo;

    public Vehicle(JSONObject jsonObject) throws JSONException {
        number = jsonObject.getString("number");
        type = jsonObject.getString("type");
        time = jsonObject.getString("time");
        timeToGo = jsonObject.getString("timeToGo");
        direction = jsonObject.getString("direction");
        if(jsonObject.has("additionalInfo")){
            additionalVehicleInfo = new AdditionalVehicleInfo(jsonObject.getJSONObject("additionalInfo"));
        }
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public String getTimeToGo() {
        return timeToGo;
    }

    public String getDirection() {
        return direction;
    }

    public AdditionalVehicleInfo getAdditionalVehicleInfo() {
        return additionalVehicleInfo;
    }
}
