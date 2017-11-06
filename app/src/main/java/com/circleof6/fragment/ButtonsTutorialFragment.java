package com.circleof6.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.NetworkImageView;
import com.circleof6.CircleOf6Application;
import com.circleof6.R;
import com.circleof6.data.DBHelper;
import com.circleof6.preferences.AppPreferences;

import java.util.EnumMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@_FIXME_link ButtonsTutorialFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ButtonsTutorialFragment extends TutorialFragment
{

    enum ButtonKey {
        MOCK_CONTACT, INFO, CHECK, CAMPUS_RESOURCES, CAR,
        PHONE, CHAT, DANGER, NINEONEONE
    }

    private EnumMap<ButtonKey, FrameLayout> mBlurredFrameLayouts;

    private EnumMap<ButtonKey, ImageView>   mSharpImageViews;
    private EnumMap<ButtonKey, String>      mButtonExplanationHtmls;
    private WebView mButtonExplanationWebView;
    private DBHelper dbHelper;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_buttons_tutorial, container, false);
        dbHelper = new DBHelper(getContext());
        return view;
    }

    @Override
    public void setupInfoComponents(View view) {
        setupTappableImages(view);
        setupTextViewExplantion(view);
    }


    private void setupTappableImages(View view) {

        mBlurredFrameLayouts = new EnumMap<>(ButtonKey.class);
        mSharpImageViews = new EnumMap<>(ButtonKey.class);
        mButtonExplanationHtmls = new EnumMap<>(ButtonKey.class);

        addTappableImage(ButtonKey.MOCK_CONTACT, R.id.friend2,
                R.drawable.tutorial_contact_icon_blurred,
                getString(R.string.button_tutorial_sample_friend_text), view,
                getContext());
        addTappableImage(ButtonKey.INFO, R.id.infobutton,
                R.drawable.tutorial_information_icon_blurred,
                getString(R.string.button_tutorial_info_text), view,
                getContext());
        addTappableImage(ButtonKey.CHECK, R.id.checkbutton,
                R.drawable.tutorial_checkmark_icon_blurred,
                getString(R.string.button_tutorial_check_text), view,
                getContext());
        addTappableImage(ButtonKey.CAMPUS_RESOURCES, R.id.campus_resources_button,
                R.drawable.tutorial_resources_icon_blurred,
                getString(R.string.button_tutorial_resources_text), view,
                getContext());
        addTappableImage(ButtonKey.CAR, R.id.leftbutton, R.drawable.tutorial_car_icon_blurred,
                getString(R.string.button_tutorial_car_text), view,
                getContext());
        addTappableImage(ButtonKey.PHONE, R.id.middlebutton, R.drawable.tutorial_phone_icon_blurred,
                getString(R.string.button_tutorial_call_text), view,
                getContext());
        addTappableImage(ButtonKey.CHAT, R.id.rightbutton, R.drawable.tutorial_bubble_icon_blurred,
                getString(R.string.button_tutorial_message_text), view,
                getContext());
        addTappableImage(ButtonKey.DANGER, R.id.emergency_button,
                R.drawable.tutorial_danger_icon_blurred,
                getString(R.string.button_tutorial_emergency_text), view,
                getContext());

        //        if(isUCLALocalization())
        //        {
        //            addTappableImage(ButtonKey.NINEONEONE, R.id.nineoneonebutton,
        //                             R.drawable.tutorial_nineoneone_button_blurred, "Dial 911",
        //                             fragmentView, container.getContext());
        //        }
    }

    public boolean isUCLALocalization() {
        return AppPreferences.getInstance(getContext()).getCollegeLocation().equals(AppPreferences.ID_UCLA);

    }

    private void addTappableImage(final ButtonKey buttonKey, int sharpImageViewId, int blurredImageId, final String explanationHtml, final View fragmentView, Context context)
    {
        final ImageView sharpImageView = (ImageView) fragmentView.findViewById(sharpImageViewId);

        final ImageView blurredImageView = new ImageView(context);
        blurredImageView.setImageResource(blurredImageId);

        FrameLayout blurredImageFrame = new FrameLayout(context);
        blurredImageFrame.addView(blurredImageView);

        ((RelativeLayout) fragmentView.findViewById(R.id.main_layout)).addView(blurredImageFrame);


        blurredImageView.setVisibility(ImageView.INVISIBLE);

        View.OnClickListener onClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                for(Map.Entry<ButtonKey, FrameLayout> entry : mBlurredFrameLayouts.entrySet())
                {
                    ImageView sharpImageView = mSharpImageViews.get(entry.getKey());
                    FrameLayout blurredImageFrame = entry.getValue();
                    ImageView blurredImageView = (ImageView) blurredImageFrame.getChildAt(0);

                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) blurredImageFrame
                            .getLayoutParams();
                    lp.setMargins((int) (getLeftRelativeToView(sharpImageView,
                                                               fragmentView) - 0.5 * (blurredImageView
                                          .getWidth() - sharpImageView.getWidth())),
                                  (int) (getTopRelativeToView(sharpImageView,
                                                              fragmentView) - 0.5 * (blurredImageView
                                          .getHeight() - sharpImageView.getHeight())), 0, 0);

                    blurredImageFrame.setLayoutParams(lp);

                    sharpImageView.setVisibility(ImageView.INVISIBLE);
                    blurredImageView.setVisibility(ImageView.VISIBLE);
                }

                blurredImageView.setVisibility(ImageView.INVISIBLE);
                sharpImageView.setVisibility(ImageView.VISIBLE);

                setExplanationText(explanationHtml);
            }
        };

        sharpImageView.setOnClickListener(onClickListener);
        blurredImageView.setOnClickListener(onClickListener);

        mSharpImageViews.put(buttonKey, sharpImageView);
        mBlurredFrameLayouts.put(buttonKey, blurredImageFrame);
        mButtonExplanationHtmls.put(buttonKey, explanationHtml);
    }

    private static int getLeftRelativeToView(View child, View parent) {
        View v = child;
        int left = 0;

        while (v != parent) {
            left += v.getLeft();
            v = (View) v.getParent();   // FIXME: is this cast always safe?
        }

        return left;
    }

    private static int getTopRelativeToView(View child, View parent) {
        View v = child;
        int top = 0;

        while (v != parent) {
            top += v.getTop();
            v = (View) v.getParent();
        }

        return top;
    }

    private void setupTextViewExplantion(View view) {
        mButtonExplanationWebView = (WebView) view.findViewById(R.id.button_explanation_web_view);

        mButtonExplanationWebView.reload();

        mButtonExplanationWebView.setBackgroundColor(0x00000000);
        if (Build.VERSION.SDK_INT >= 11) {
            mButtonExplanationWebView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
        }

        setExplanationText(getString(R.string.button_tutorial_initial_text));
    }

    private void setExplanationText(String textExplanation) {
        mButtonExplanationWebView.loadData("<html>" +
                        "<body style=\"background-color: transparent; font-family: light; font-size: 11.5pt; color: #77899f;\">" +
                        "<div style=\"text-align: center;\"" +
                        "<div style=\"display: block;\">" +
                        textExplanation +
                        "</div>" +
                        "</div>" +
                        "</body></html>",
                "text/html; charset=UTF-8", null);
        mButtonExplanationWebView.reload();
    }

    @Override
    public void updateInfoCollegeLocation() {

    }

}
