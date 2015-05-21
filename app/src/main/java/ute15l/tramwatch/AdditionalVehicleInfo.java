package ute15l.tramwatch;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PAWEL on 2015-05-20.
 */
public class AdditionalVehicleInfo {
    private String distance = "";
    private String realAproximateTimeToGo = "";

    public AdditionalVehicleInfo(JSONObject jsonObject) throws JSONException {
        if(jsonObject.has("distance")){
            distance = jsonObject.getString("distance");
        }
        if(jsonObject.has("realAproximateTimeToGo")){
            realAproximateTimeToGo = jsonObject.getString("realAproximateTimeToGo");
        }

    }

    public String getDistance() {
        return distance;
    }

    public String getRealAproximateTimeToGo() {
        return realAproximateTimeToGo;
    }
}
