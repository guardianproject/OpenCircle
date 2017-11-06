package com.circleof6.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.widget.TextView;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.data.DBHelper;
import com.circleof6.model.CustomNumber;
import com.circleof6.model.InfoLink;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.util.Constants;
import com.circleof6.util.ConstantsAnalytics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Edgar Salvador Maurilio on 17/11/2015.
 */
public class EmergencyPhonesDialog extends DialogFragment {

    public static final String LOG_TAG = EmergencyPhonesDialog.class.getSimpleName();

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final List<CustomNumber> phones = new DBHelper(getContext()).getCustomNumbers();
        final String[] titles = new String[phones.size()];

        for(int i = 0; i<phones.size(); i++){
            titles[i] = "" + phones.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                .setCustomTitle(createTitle(null))
                .setItems(titles, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position) {
                        CircleOf6Application
                                .trackingEvent(ConstantsAnalytics.ACTION_EMERGENCY_BUTTON,
                                               AppPreferences.getInstance(getContext())
                                                             .getCollegeLocation() + "/: /" + phones.get(position).getName());
                        startActivityCall(phones.get(position).getnumber());
                    }
                });

        return builder.create();
    }

    private void startActivityCall(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }

    @NonNull
    private TextView createTitle(Constants.UniversityLocalization localization) {
        TextView textViewTitle = (TextView) getActivity().getLayoutInflater().inflate(R.layout.dialog_default_tittle, null);
        textViewTitle.setText(getString(R.string.emergency_button_action_title));
        return textViewTitle;
    }

}
