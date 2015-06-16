package ute15l.tramwatch;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by PAWEL on 2015-06-16.
 */
public class SendSmsToFriendDialogFragment extends DialogFragment {

    private View view;
    private Resources resources;
    private Stop selectedStop;
    private int selectedVehicle;

    private EditText friendNameEditText;
    private EditText yourNameEditText;
    private EditText friendNumberEditText;


    private ToSendSmsData toSendSmsData;

    public SendSmsToFriendDialogFragment(Stop selectedStop, int selectedVehicle) {
        this.selectedStop = selectedStop;
        this.selectedVehicle = selectedVehicle;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        resources = getActivity().getResources();
        view = inflater.inflate(R.layout.send_sms_to_friend_dialog_fragment, null);
        friendNameEditText = (EditText) view.findViewById(R.id.send_sms_to_friend_name);
        yourNameEditText = (EditText) view.findViewById(R.id.send_sms_to_friend_your_name);
        friendNumberEditText = (EditText) view.findViewById(R.id.send_sms_to_friend_nr);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view)
                .setPositiveButton(resources.getString(R.string.send_sms_to_friend_dialog_fragment_confirm_button_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        toSendSmsData = new ToSendSmsData(yourNameEditText.getText().toString(),friendNameEditText.getText().toString(),friendNumberEditText.getText().toString(),selectedStop,selectedVehicle);
                        if("".equals(toSendSmsData.getYourName()) || "".equals(toSendSmsData.getFriendName()) || "".equals(toSendSmsData.getFriendNumber())){
                            Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.send_sms_to_friend_not_filled),Toast.LENGTH_SHORT).show();
                        } else {
                            new SendSmsToFriendAsync(getActivity(),toSendSmsData);
                        }
                    }
                })
                .setNegativeButton(resources.getString(R.string.send_sms_to_friend_dialog_fragment_cancel_button_text), null);
        return builder.create();
    }
}
