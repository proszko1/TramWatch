package ute15l.tramwatch;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by PAWEL on 2015-05-20.
 */
public class StopToSelectListAdapter extends ArrayAdapter<Stop> {

    private final Context context;
    private final Stop[] stops;


    public StopToSelectListAdapter(Context context, Stop[] stops) {
        super(context, R.layout.stop_to_select_row,stops);
        this.context = context;
        this.stops = stops;
    }

    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View stopToSelectListRow = inflater.inflate(R.layout.stop_to_select_row, parent, false);
        TextView stopToSelectNameText = (TextView) stopToSelectListRow.findViewById(R.id.stop_to_select_name);

        stopToSelectNameText.setText(stops[pos].getName());
        CheckBox checkBox = (CheckBox) stopToSelectListRow.findViewById(R.id.stop_to_select_name_checkbox);
        checkBox.setChecked(stops[pos].isChecked());

        return stopToSelectListRow;
    }

    public void setCheckedStop(int select){
        stops[select].setChecked(!stops[select].isChecked());
    }
}