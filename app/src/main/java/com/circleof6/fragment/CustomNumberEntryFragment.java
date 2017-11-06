package com.circleof6.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.NetworkImageView;
import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.data.DBHelper;
import com.circleof6.model.CampusLang;
import com.circleof6.model.CollegeCountry;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.util.ConstantsAnalytics;

//import android.app.FragmentTransaction;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@_FIXME_link CustomNumberEntryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class CustomNumberEntryFragment extends TutorialFragment implements View.OnClickListener {

    private static final String LOG_TAG = CustomNumberEntryFragment.class.getSimpleName();
    private EditText mPhoneNumberEntryEditText;
    private TextView numberText;
    private DBHelper dbHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_custom_number_entry, container, false);
        dbHelper = new DBHelper(getContext());
        return v;
    }


    @Override
    public void setupInfoComponents(View view) {
        setupButtons(view);
        setupEditTextPhoneNumber(view);
        setupCustomNumberText(view);
    }


    private void setupButtons (View view)
    {
        view.findViewById(R.id.ok_button).setOnClickListener(this);
        view.findViewById(R.id.do_it_later_button).setOnClickListener(this);
    }

    private  void setupEditTextPhoneNumber(View view){
        mPhoneNumberEntryEditText = (EditText) view.findViewById(R.id.phone_number_entry);
        //mPhoneNumberEntryEditText.addTextChangedListener(new PhoneNumberFormattingTextWatcher("US"));
        mPhoneNumberEntryEditText.setText(AppPreferences.getInstance(getContext()).getCustomNumberHotline());

    }

    private void setupCustomNumberText(View view) {

        numberText = (TextView) view.findViewById(R.id.custom_number_info_text);
        //setTextNumberinfo();
    }

    /**
    private void setTextNumberinfo() {
        String college_id = AppPreferences.getInstance(getContext()).getCollegeLocation();
        String text;
        if(college_id.startsWith("c_")){
            CampusLang campusLang = dbHelper.getCampus(college_id.replace("c_", ""));
            text = campusLang.getCustomNumberMessage();
        }else{
            CollegeCountry collegeCountry = dbHelper.getCollege(college_id);
            text = collegeCountry.getCustomNumberMessage();
        }
        numberText.setText(text);
    }**/



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ok_button:
                onOkButtonClicked();
                break;
            case R.id.do_it_later_button:
                onDoItLaterButtonClicked();
                break;
        }
    }


    private void onOkButtonClicked()
    {
        if (isValidPhoneNumber()) {
            AppPreferences.getInstance(getContext()).saveCustomHotline(mPhoneNumberEntryEditText.getText().toString());
            showConfirmDIalog();
            trackEventCustomNumber();
        } else {
            Toast.makeText(getContext(), R.string.numberi_invalid, Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isValidPhoneNumber()
    {
        String phoneNumber = mPhoneNumberEntryEditText.getText().toString();
        return Patterns.PHONE.matcher(phoneNumber).matches();

    }

    private void showConfirmDIalog() {
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
        dlgAlert.setMessage(R.string.custom_hotline_set);
        dlgAlert.setTitle(R.string.custom_hotline_title);
        dlgAlert.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                chageConcurrentPage();
            }
        });

        dlgAlert.create().show();
    }



    private void trackEventCustomNumber() {
        if (AppPreferences.getInstance(getContext()).isEmptyCustomNumberHorline()) {
            CircleOf6Application.defaultLabelTrackingEvent(getContext(),
                                                           ConstantsAnalytics.ACTION_CUSTOM_NUMBER_ENTERED);
        } else {
            CircleOf6Application.defaultLabelTrackingEvent(getContext(),
                                                           ConstantsAnalytics.ACTION_CUSTOM_NUMBER_CHANGED);
        }
    }


    private void chageConcurrentPage()
    {
        ((ViewPager) getActivity().findViewById(R.id.pager)).setCurrentItem(3, true);

    }

    private void onDoItLaterButtonClicked(){
        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(getContext());
        dlgAlert.setMessage(R.string.custom_hotline_not_added);
        dlgAlert.setTitle(R.string.custom_hotline_not_added_title);
        dlgAlert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                chageConcurrentPage();
            }
        });
        dlgAlert.create().show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser && isResumed()) {
            setFocusEditTextNumber();
        }
    }

    private void setFocusEditTextNumber() {

        if (TextUtils.isEmpty(mPhoneNumberEntryEditText.getText())) {
            mPhoneNumberEntryEditText.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(mPhoneNumberEntryEditText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void updateInfoCollegeLocation() {

        //setTextNumberinfo();
    }


}
