package ute15l.tramwatch;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by PAWEL on 2015-05-19.
 */
public class RefreshDataClockAsync extends AsyncTask<Void, Void, Void> {

    private long refreshingInterval = 1000;

    FindPublicTransportFragment findPublicTransportFragment;

    public RefreshDataClockAsync(FindPublicTransportFragment findPublicTransportFragment){
        this.findPublicTransportFragment = findPublicTransportFragment;
        this.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    protected Void doInBackground(Void... params) {
        while(!findPublicTransportFragment.isDestroyed()){
            try {
                if(findPublicTransportFragment.getLocation() != null && findPublicTransportFragment.isTimeForRefreshData()){
                    refreshingInterval = Constants.DEFAULT_REFRESHING_INTEVALS;
                    publishProgress();
                }
                Thread.sleep(refreshingInterval);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... params){
        findPublicTransportFragment.startRefreshDataProcedure();
    }
}
