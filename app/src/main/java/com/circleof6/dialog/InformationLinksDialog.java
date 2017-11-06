package com.circleof6.dialog;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.Model.ParentObject;
import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.data.DBHelper;
import com.circleof6.model.CollegeCountry;
import com.circleof6.model.InfoLink;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.ui.MainActivity;
import com.circleof6.util.Constants;
import com.circleof6.util.ConstantsAnalytics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Edgar Salvador Maurilio on 17/11/2015.
 */
public class InformationLinksDialog extends DialogFragment
{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        final List<InfoLink> infolinks = new DBHelper(getContext()).getInformationLinks();
        Collections.reverse(infolinks);
        final String[] titles = new String[infolinks.size()];

        for(int i = 0; i<infolinks.size(); i++){
            titles[i] = infolinks.get(i).getName();
        }

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext())
                .setItems(titles, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int position)
                    {
                        CircleOf6Application
                                .trackingEvent(ConstantsAnalytics.ACTION_INFORMATION_BUTTON,
                                               AppPreferences.getInstance(getContext())
                                                             .getCollegeLocation() + "/: /" + infolinks.get(position).getName());
                        startActivityCall(infolinks.get(position).getLink());
                    }
                });

        return builder.create();
    }

    private void startActivityCall(String link)
    {
        try {
            Intent loadURLIntent = new Intent(Intent.ACTION_VIEW);
            loadURLIntent.setData(Uri.parse(link));
            startActivity(loadURLIntent);
        }catch (ActivityNotFoundException e){
            Toast.makeText(getActivity().getApplicationContext(), "Wrongly formatted URL", Toast.LENGTH_LONG).show();
        }

    }

}
