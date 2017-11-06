package com.circleof6.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.preferences.AppPreferences;

/**
 * Created by corbett on 11/22/14.
 */
abstract public class TutorialFragment extends Fragment
{

    private static final String LOG_TAG = TutorialFragment.class.getSimpleName();
    private ImageView logo;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        setupInfoComponents(view);
    }


    public void updateInfo()
    {
        if(isAdded())
        {
            updateInfoCollegeLocation();

        }
    }

    abstract public void updateInfoCollegeLocation();

    abstract public void setupInfoComponents(View view);
}
