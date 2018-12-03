package com.circleof6.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;

import com.android.volley.toolbox.NetworkImageView;
import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.data.DBHelper;
import com.circleof6.model.CampusLang;
import com.circleof6.model.CollegeCountry;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.ui.MainActivity;
import com.circleof6.util.Constants;
import com.circleof6.util.ConstantsAnalytics;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@_FIXME_link TermsOfServiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class TermsOfServiceFragment extends TutorialFragment
{
    DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_terms_of_service, container, false);
        dbHelper = new DBHelper(getContext());

        return view;
    }

    @Override
    public void setupInfoComponents(View view) {
        setupTextTerms(view);
        setupAgreeButton(view);
    }

    private void setupTextTerms(View view) {

      //  Constants.UniversityLocalization universityLocalization = AppPreferences
        //        .getInstance(getContext()).getUniversityLocalization();
        WebView webView = (WebView) view.findViewById(R.id.terms_of_service_webview);
        webView.loadData(
                "<html><body style=\"background-color: rgb(255,221,0); color: #9f1f72;\">" +
                        getTerms() + //getString(showPrinceSpecial ? R.string.terms_of_service_text_pwcs : R.string.terms_of_service_text) +
                        "</body></html>", "text/html", null);
    }
    private String getTerms(){
        /**
        String college_id = AppPreferences.getInstance(getContext()).getCollegeLocation();
        if(college_id.startsWith("c_")){
            CampusLang campusLang = dbHelper.getCampus(college_id.replace("c_", ""));
            return campusLang.getTermsAndConditions();
        }else{
            CollegeCountry collegeCountry = dbHelper.getCollege(college_id);
            return collegeCountry.getTermsAndConditions();
        }**/
        StringBuffer result = new StringBuffer();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(getActivity().getAssets().open("license.html")));
            String line = null;
            while ((line = reader.readLine())!=null)
                result.append(line);
        }
        catch (Exception e){}

        return result.toString();
    }
    private void setupAgreeButton(View view) {
        ImageButton agreeButton = (ImageButton) view.findViewById(R.id.i_agree_button);
        agreeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAgreeButtonClicked();
            }
        });
    }

    private void onAgreeButtonClicked() {

        AppPreferences.getInstance(getContext()).saveHasCompletedTutorial(true);

        getActivity().finish();

    }

    @Override
    public void updateInfoCollegeLocation() {

    }

}
