package ute15l.tramwatch;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by PAWEL on 2015-05-20.
 */
public class StopExpandableListAdapter extends BaseExpandableListAdapter {

    private Context parentActivity;
    private Stop[] stops;

    public StopExpandableListAdapter(Context context, Stop[] stops){
        parentActivity = context;
        this.stops = stops;
    }

    @Override
    public int getGroupCount() {
        return stops.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return stops[groupPosition].getVehicleList().size();
    }

    @Override
    public Stop getGroup(int groupPosition) {
        return stops[groupPosition];
    }

    @Override
    public Vehicle getChild(int groupPosition, int childPosition) {
        return stops[groupPosition].getVehicleList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View stopListRow = inflater.inflate(R.layout.stop_row, parent, false);
        TextView stopName = (TextView) stopListRow.findViewById(R.id.stop_name);
        stopName.setText(getGroup(groupPosition).getName());
        return stopListRow;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) parentActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View vehicleListRowView = inflater.inflate(R.layout.vehicle_row, parent, false);

        LinearLayout vehicleRowLayout = (LinearLayout) vehicleListRowView.findViewById(R.id.vehicle_row_layout);
        LinearLayout vehicleRowIconLayout = (LinearLayout) vehicleListRowView.findViewById(R.id.vehicle_row_icon_layout);
        if("bus".equals(getChild(groupPosition,childPosition).getType())){
            vehicleRowIconLayout.setBackground(parentActivity.getResources().getDrawable(R.drawable.bus));
        } else if ("tram".equals(getChild(groupPosition,childPosition).getType())){
            vehicleRowIconLayout.setBackground(parentActivity.getResources().getDrawable(R.drawable.tram));
        }

        TextView time = (TextView) vehicleListRowView.findViewById(R.id.time);
        TextView timeToGo = (TextView) vehicleListRowView.findViewById(R.id.time_to_go);
        TextView number = (TextView) vehicleListRowView.findViewById(R.id.number);
        TextView direction = (TextView) vehicleListRowView.findViewById(R.id.direction);

        time.setText(getChild(groupPosition, childPosition).getTime());
        timeToGo.setText("Za "+getChild(groupPosition, childPosition).getTimeToGo()+" min");
        number.setText(getChild(groupPosition, childPosition).getNumber());
        direction.setText(getChild(groupPosition, childPosition).getDirection());

        LinearLayout vehicleRowAdditionalInfoLayout = (LinearLayout) vehicleListRowView.findViewById(R.id.vehicle_row_additional_info_layout);
        if(getChild(groupPosition,childPosition).getAdditionalVehicleInfo() == null){
            vehicleRowAdditionalInfoLayout.setVisibility(View.GONE);
        } else {
            TextView noAdditionalInfo = (TextView) vehicleListRowView.findViewById(R.id.no_additional_info);
            TextView distance = (TextView) vehicleListRowView.findViewById(R.id.distance);
            TextView realAproximateTimeToGo = (TextView) vehicleListRowView.findViewById(R.id.real_aproximate_time_to_go);

            if(getChild(groupPosition,childPosition).getAdditionalVehicleInfo().getDistance().equals("") && getChild(groupPosition,childPosition).getAdditionalVehicleInfo().getRealAproximateTimeToGo().equals("")){
                distance.setVisibility(View.GONE);
                realAproximateTimeToGo.setVisibility(View.GONE);
                noAdditionalInfo.setText(parentActivity.getResources().getString(R.string.no_additional_info_text));
            } else {
                noAdditionalInfo.setVisibility(View.GONE);
                if(getChild(groupPosition,childPosition).getAdditionalVehicleInfo().getDistance().equals("")){
                    distance.setVisibility(View.GONE);
                }
                if(getChild(groupPosition,childPosition).getAdditionalVehicleInfo().getRealAproximateTimeToGo().equals("")){
                    realAproximateTimeToGo.setVisibility(View.GONE);
                }
                distance.setText(parentActivity.getResources().getString(R.string.distance_text) + getChild(groupPosition, childPosition).getAdditionalVehicleInfo().getDistance() + " km");
                realAproximateTimeToGo.setText(parentActivity.getResources().getString(R.string.approximate_time_text) + getChild(groupPosition,childPosition).getAdditionalVehicleInfo().getRealAproximateTimeToGo() + " min");
            }
        }
        return vehicleListRowView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
