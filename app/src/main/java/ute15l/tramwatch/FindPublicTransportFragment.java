package ute15l.tramwatch;


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
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by PAWEL on 2015-05-18.
 */
public class FindPublicTransportFragment extends Fragment {

    public boolean isRefreshingData;
    private Menu menu;
    private MainActivity mainActivity;
    private TextView opis;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        mainActivity = (MainActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_public_transport, container, false);
        opis = (TextView) view.findViewById(R.id.texts);
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
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.refreshDataAction) {
            refreshData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {
        setRefreshActionButtonState(isRefreshingData = true);
        opis.setText("cycki");
        Toast.makeText(getActivity(), R.string.refresh_data_alert, Toast.LENGTH_SHORT).show();
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
}
