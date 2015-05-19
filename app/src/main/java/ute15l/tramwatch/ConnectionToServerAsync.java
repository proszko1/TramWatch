package ute15l.tramwatch;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PAWEL on 2015-05-19.
 */
public class ConnectionToServerAsync extends AsyncTask<Void, Void, Void> {

    private FindPublicTransportFragment findPublicTransportFragment;
    protected String data;
    private boolean errorFlag;
    private JSONObject jsonResponse;

    public ConnectionToServerAsync(FindPublicTransportFragment findPublicTransportFragment){
        this.findPublicTransportFragment = findPublicTransportFragment;
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected Void doInBackground(Void... params) {
        BufferedReader reader=null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(Constants.SERVER_ADDRESS+Constants.REFRESH_DATA_REST_SERVICE_NAME);
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("long", String.valueOf(findPublicTransportFragment.getLocation().getLongitude())));
            nameValuePairs.add(new BasicNameValuePair("lat", String.valueOf(findPublicTransportFragment.getLocation().getLatitude())));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse response = httpclient.execute(httppost);
            reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while((line = reader.readLine()) != null){
                sb.append(line + "");
            }
            data = sb.toString();
        } catch(Exception ex) {
            errorFlag = true;
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        if(!errorFlag){
            try {
                String status;
                jsonResponse = new JSONObject(data);
            } catch (JSONException e) {
                Log.e("JSON Exception","Error parsing to JSON");
                e.printStackTrace();
            }
        } else{
            Toast.makeText(findPublicTransportFragment.getActivity(), R.string.connection_with_server_error, Toast.LENGTH_SHORT).show();
        }
        findPublicTransportFragment.refreshGUIWithReceivedDataProcedure(jsonResponse);
    }
}
