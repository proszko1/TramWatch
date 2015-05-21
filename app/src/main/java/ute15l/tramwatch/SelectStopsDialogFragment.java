package ute15l.tramwatch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.support.v4.app.DialogFragment;


import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

/**
 * Created by PAWEL on 2015-05-20.
 */
public class SelectStopsDialogFragment extends DialogFragment {

    private View view;
    private Resources resources;
    DialogListener dialogListener;
    private ListView stopToSelectList;
    private StopToSelectListAdapter stopToSelectListAdapter;
    private List<Stop> stopList;

    public interface DialogListener {
        public void onDialogSendClick();
    }

    public SelectStopsDialogFragment(List<Stop> stopList){
        this.stopList = stopList;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        resources = getActivity().getResources();
        view = inflater.inflate(R.layout.stop_to_select_dialog_fragment_layout, null);
        stopToSelectList = (ListView) view.findViewById(R.id.stop_to_select_list);
        stopToSelectListAdapter = new StopToSelectListAdapter(getActivity(), stopList.toArray(new Stop[stopList.size()]));
        stopToSelectList.setAdapter(stopToSelectListAdapter);
        AdapterView.OnItemClickListener stopToSelectListClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                stopToSelectListAdapter.setCheckedStop(position);
                stopToSelectListAdapter.notifyDataSetChanged();
            }
        };
        stopToSelectList.setOnItemClickListener(stopToSelectListClickListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton(resources.getString(R.string.select_stops_dialog_fragment_confirm_button_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialogListener.onDialogSendClick();
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            dialogListener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DialogListener from DefineOpponentNameDialogFragment class ");
        }
    }
}
