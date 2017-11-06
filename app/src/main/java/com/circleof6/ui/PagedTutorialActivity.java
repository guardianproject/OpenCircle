package com.circleof6.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.toolbox.NetworkImageView;
import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.adapter.TutorialFragmentPagerAdapter;
import com.circleof6.data.DBHelper;
import com.circleof6.fragment.ButtonsTutorialFragment;
import com.circleof6.fragment.CustomNumberEntryFragment;
import com.circleof6.fragment.TermsOfServiceFragment;
import com.circleof6.fragment.TutorialFragment;
import com.circleof6.fragment.WelcomeFragment;
import com.circleof6.preferences.AppPreferences;
import com.circleof6.util.Constants;
import com.circleof6.util.ConstantsAnalytics;

import java.util.Arrays;
import java.util.List;

public class PagedTutorialActivity extends AppCompatActivity
{

    private ImageView[] mNavDotImageViews;


    public static Intent getInstance(Context context)
    {
        return new Intent(context, PagedTutorialActivity.class);
    }

    private static final List<TutorialFragment> TUTORIAL_FRAMENTS_PAGER = Arrays.asList(
            new WelcomeFragment(),
            new ButtonsTutorialFragment(),
            new CustomNumberEntryFragment(),
            new TermsOfServiceFragment()
    );
    private TutorialFragmentPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paged_tutorial);
        CircleOf6Application.defaultLabelTrackingEvent(this, ConstantsAnalytics.ACTION_TUTORIAL_RAN);

        DBHelper dbHelper = new DBHelper(PagedTutorialActivity.this);
        NetworkImageView logo = (NetworkImageView) findViewById(R.id.logo);
        CircleOf6Application.getInstance().setUpLogo(logo, dbHelper);

        setupViewPagerTutorial();
        setupCloseButtonTutorial();
        setupImageIndicator();

    }

    public void setupViewPagerTutorial() {
        ViewPager mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new TutorialFragmentPagerAdapter(getSupportFragmentManager(), TUTORIAL_FRAMENTS_PAGER);
        mPager.setAdapter(pagerAdapter);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelectedNavDotImageView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupCloseButtonTutorial() {
        ImageView closeTutorialButton = (ImageView) findViewById(R.id.closeTutorialButton);
        closeTutorialButton.setVisibility(AppPreferences.getInstance(this).hasCompletedTutorial()
                ? View.VISIBLE
                : View.INVISIBLE);
    }


    private void setupImageIndicator() {

        mNavDotImageViews = new ImageView[4];

        mNavDotImageViews[0] = (ImageView) findViewById(R.id.nav_dot_1);
        mNavDotImageViews[1] = (ImageView) findViewById(R.id.nav_dot_2);
        mNavDotImageViews[2] = (ImageView) findViewById(R.id.nav_dot_3);
        mNavDotImageViews[3] = (ImageView) findViewById(R.id.nav_dot_4);

    }

    private void setSelectedNavDotImageView(int index)
    {
        for(int i = 0; i < mNavDotImageViews.length; ++ i)
        {
            if(i == index)
            {
                mNavDotImageViews[i].setImageResource(R.drawable.tutorial_nav_dot_full);
            }
            else
            {
                mNavDotImageViews[i].setImageResource(R.drawable.tutorial_nav_dot_not_full);
            }
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.PICK_COLLEGE_INTENT && resultCode == RESULT_OK) {
            finish();
            pagerAdapter.updateInfoPages();
        }
    }

    public void closeTutorialButton(View view)
    {
        finish();
    }

}
