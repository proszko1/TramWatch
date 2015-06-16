package ute15l.tramwatch;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by PAWEL on 2015-05-18.
 */
public class FindPublicTransportFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener{

    public boolean isRefreshingData;
    private Menu menu;
    private MainActivity mainActivity;
    private RefreshDataClockAsync refreshDataClockAsync;

    private Location location;
    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;

    private Calendar calendar;
    private long timeStampSinceLastUpdate = 0;

    private List<Stop> stopList;
    private boolean destroyed;

    private TextView waitingForDataTextView;
    private ExpandableListView stopExpandableList;
    private StopExpandableListAdapter stopExpandableListAdapter;

    private boolean stopToSelectDialogOpened;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mainActivity = (MainActivity) getActivity();
        calendar = Calendar.getInstance();
        stopList = new ArrayList<Stop>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_public_transport, container, false);
        waitingForDataTextView = (TextView) view.findViewById(R.id.waiting_for_data_text);
        stopExpandableList = (ExpandableListView) view.findViewById(R.id.stop_expandable_list);
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        refreshDataClockAsync = new RefreshDataClockAsync(this);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(!mainActivity.isDrawerOpen()){
            this.menu = menu;
            inflater.inflate(R.menu.main, menu);
            setRefreshActionButtonState(isRefreshingData);
            getActionBar().setTitle(R.string.find_public_transport_title);
        }
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.select_stops || id == R.id.select_stops_from_settings) {
            if(stopList == null || stopList.isEmpty()){
                Toast.makeText(getActivity(), R.string.select_stops_no_stops, Toast.LENGTH_SHORT).show();
            } else {
                stopToSelectDialogOpened = true;
                new SelectStopsDialogFragment(stopList).show( getFragmentManager(),"SelectStopsDialogFragment");
            }
            return true;
        }
        if (id == R.id.refreshDataAction) {
            if(getLocation() != null){
                startRefreshDataProcedure();
            } else {
                Toast.makeText(getActivity(), R.string.refresh_data_no_GPS, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void startRefreshDataProcedure() {
        setRefreshActionButtonState(isRefreshingData = true);
        Toast.makeText(getActivity(), R.string.refresh_data_alert, Toast.LENGTH_SHORT).show();

        //new ConnectionToServerAsync(this);
        new TestConnectionToServerAsync(this);
    }

    public void refreshGUIWithReceivedDataProcedure(JSONObject jsonObject) {
        setRefreshActionButtonState(isRefreshingData = false);
        timeStampSinceLastUpdate = System.currentTimeMillis();
        if(jsonObject != null){
            Stop.parseJSONToStopsList(jsonObject, stopList);
            if(!stopList.isEmpty()){
                loadList();
            } else {
                hideList();
            }
        }
    }

    private void loadList() {
        waitingForDataTextView.setVisibility(View.GONE);
        stopExpandableList.setVisibility(View.VISIBLE);
        List<Stop> selectedStopList = Stop.getCheckedStops(stopList);
        stopExpandableListAdapter = new StopExpandableListAdapter(getActivity(),selectedStopList.toArray(new Stop[selectedStopList.size()]));
        stopExpandableList.setAdapter(stopExpandableListAdapter);

        stopExpandableList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                    int groupPosition = ExpandableListView.getPackedPositionGroup(id);
                    int childPosition = ExpandableListView.getPackedPositionChild(id);
                    new SendSmsToFriendDialogFragment(stopList.get(groupPosition),childPosition).show(getFragmentManager(), "SendSmsToFriendDialogFragment");
                    Toast.makeText(getActivity(), "group :" + groupPosition + childPosition, Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });

        for(int i=0; i < stopExpandableListAdapter.getGroupCount(); i++){
            stopExpandableList.expandGroup(i);
        }
    }

    private void hideList() {
        waitingForDataTextView.setVisibility(View.VISIBLE);
        stopExpandableList.setVisibility(View.GONE);
    }

    public boolean isTimeForRefreshData(){
        return (System.currentTimeMillis() - timeStampSinceLastUpdate>Constants.DEFAULT_REFRESHING_INTEVALS - Constants.DEFAULT_REFRESHING_INTEVALS/3) && !isStopToSelectDialogOpened();
    }

    public void setRefreshActionButtonState(final boolean refreshing) {
        if (menu != null) {
            final MenuItem refreshItem = menu.findItem(R.id.refreshDataAction);
            if (refreshItem != null) {
                if (refreshing) {
                    refreshItem.setActionView(R.layout.actionbar_loading);
                } else {
                    refreshItem.setActionView(null);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
    }

    @Override
    public void onConnected(Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onStop() {
        super.onStop();
        stopLocationUpdates();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        destroyed = true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
    }

    public Location getLocation() {
        return location;
    }

    public long getTimeStampSinceLastUpdate() {
        return timeStampSinceLastUpdate;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void stopToSelectDialogConfirmed() {
        loadList();
        stopToSelectDialogOpened = false;

    }

    public boolean isStopToSelectDialogOpened() {
        return stopToSelectDialogOpened;
    }

    public void setStopToSelectDialogOpened(boolean stopToSelectDialogOpened) {
        this.stopToSelectDialogOpened = stopToSelectDialogOpened;
    }
}
