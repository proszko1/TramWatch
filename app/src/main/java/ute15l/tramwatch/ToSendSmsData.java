package ute15l.tramwatch;

/**
 * Created by PAWEL on 2015-06-16.
 */
public class ToSendSmsData {
    private String yourName;
    private String friendName;
    private String friendNumber;
    private String stopName;
    private String minutesToGo;
    private String vehicleType;
    private String vehicleNumber;
    private String destination;

    public ToSendSmsData(String yourName,String friendName,String friendNumber,Stop selectedStop, int selectedVehicle){
        this.yourName = yourName;
        this.friendName = friendName;
        this.friendNumber = friendNumber;
        stopName = selectedStop.getName();
        Vehicle vehicle = selectedStop.getVehicleList().get(selectedVehicle);
        minutesToGo = vehicle.getTimeToGo();
        vehicleType = "bus".equals(vehicle.getType()) ? "autobus" : "tramwaj";
        vehicleNumber = vehicle.getNumber();
        destination = vehicle.getDirection();
    }

    public String getYourName() {
        return yourName;
    }

    public String getFriendName() {
        return friendName;
    }

    public String getFriendNumber() {
        return friendNumber;
    }

    public String getMinutesToGo() {
        return minutesToGo;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getDestination() {
        return destination;
    }

    public String getStopName() {
        return stopName;
    }
}
