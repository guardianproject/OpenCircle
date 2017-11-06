package com.circleof6.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.data.DBHelper;
import com.circleof6.model.CampusLang;
import com.circleof6.model.CollegeCountry;
import com.circleof6.preferences.AppPreferences;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * { @_FIXME_link WelcomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
*/
public class WelcomeFragment extends TutorialFragment
{
    private TextView welcomeMessage;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tutorial_welcome, container, false);
        dbHelper = new DBHelper(getContext());

        return view;
    }


    @Override
    public void setupInfoComponents(View view) {
        setupWelcomeMessage(view);
    }

    private void setupWelcomeMessage(View root) {

        welcomeMessage = (TextView) root.findViewById(R.id.welcome_main_text);
      //  welcomeMessage.setText(getWelcomeMessage());

    }

    @Nullable
    private String getWelcomeMessage(){
        /**
        String college_id = AppPreferences.getInstance(getContext()).getCollegeLocation();
        if(college_id.startsWith("c_")){
            CampusLang campusLang = dbHelper.getCampus(college_id.replace("c_", ""));
            return campusLang.getWelcomeMessage();
        }else{
            CollegeCountry collegeCountry = dbHelper.getCollege(college_id);
            return collegeCountry.getWelcomeMessage();
        }**/
        return getString(R.string.welcome_message);
    }

    @Override
    public void updateInfoCollegeLocation() {
        welcomeMessage.setText(getWelcomeMessage());
    }

}
