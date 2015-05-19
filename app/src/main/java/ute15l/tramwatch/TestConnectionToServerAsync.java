package ute15l.tramwatch;

/**
 * Created by PAWEL on 2015-05-19.
 */
public class TestConnectionToServerAsync extends ConnectionToServerAsync {

    public TestConnectionToServerAsync(FindPublicTransportFragment findPublicTransportFragment) {
        super(findPublicTransportFragment);
    }

    @Override
    protected Void doInBackground(Void... params) {
        data = "{\"stops\":[{\"name\":\"Metro Politechnika 01\",\"vehicles\":[{\"number\":\"136\",\"type\":\"bus\",\"time\":\"19:40\",\"timeToGo\":\"3\",\"direction\":\"Wilanów\"},{\"number\":\"501\",\"type\":\"bus\",\"time\":\"19: 42\",\"timeToGo\":\"5\",\"direction\":\"Sadyba\"}]},{\"name\":\"Metro Politechnika 05\",\"vehicles\":[{\"number\":\"13\",\"type\":\"tram\",\"time\":\"19:45\",\"timeToGo\":\"8\",\"direction\":\"Kabaty\",\"additionalInfo\":{\"distance\":\"2.3\",\"realAproximateTimeToGo\":\"6\"}},{\"number\":\"15\",\"type\":\"tram\",\"time\":\"19:47\",\"timeToGo\":\"10\",\"direction\":\"Wyœcigi\",\"additionalInfo\":{\"distance\":\"2.6\",\"realAproximateTimeToGo\":\"\"}},{\"number\":\"33\",\"type\":\"tram\",\"time\":\"19:48\",\"timeToGo\":\"11\",\"direction\":\"FSO\",\"additionalInfo\":{}},{\"number\":\"503\",\"type\":\"bus\",\"time\":\"19: 50\",\"timeToGo\":\"13\",\"direction\":\"Imielin\"}]}]}";
        return null;
    }
}