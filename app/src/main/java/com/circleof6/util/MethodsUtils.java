package com.circleof6.util;

import android.content.Context;
import android.content.res.Resources;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;

import com.circleof6.R;

import java.util.Date;

/**
 * Created by Edgar Salvador Maurilio on 11/11/2015.
 */
public class MethodsUtils {


    public static String getPhotoFileByContact(int reqCode) {
        return "friend" + reqCode + "photoname.png";
    }

    public static int dpToPx(int dp, Context ctx)
    {
        Resources r = ctx.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    public static String dateDiffDisplayString(Date date, Context context, int idStringNever, int idStringRecently, int idStringMinutes, int idStringMinute,
                                               int idStringHours, int idStringHour, int idStringDays, int idStringDay) {
        if (date == null)
            return context.getString(idStringNever);

        Date todayDate = new Date();
        double ti = todayDate.getTime() - date.getTime();
        if (ti < 0)
            ti = -ti;
        ti = ti / 1000; // Convert to seconds
        if (ti < 60) {
            return context.getString(idStringRecently);
        } else if (ti < 3600 && (int) Math.round(ti / 60) < 60) {
            int diff = (int) Math.round(ti / 60);
            if (diff == 1)
                return context.getString(idStringMinute, diff);
            return context.getString(idStringMinutes, diff);
        } else if (ti < 86400 && (int) Math.round(ti / 60 / 60) < 24) {
            int diff = (int) Math.round(ti / 60 / 60);
            if (diff == 1)
                return context.getString(idStringHour, diff);
            return context.getString(idStringHours, diff);
        } else {
            int diff = (int) Math.round(ti / 60 / 60 / 24);
            if (diff == 1)
                return context.getString(idStringDay, diff);
            return context.getString(idStringDays, diff);
        }
    }

    public static void connectTabLayoutAndViewPager(final ViewPager viewPager, final TabLayout tabLayout) {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (viewPager.getCurrentItem() != tab.getPosition()) {
                    viewPager.setCurrentItem(tab.getPosition(), true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position != tabLayout.getSelectedTabPosition()) {
                    tabLayout.getTabAt(position).select();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
