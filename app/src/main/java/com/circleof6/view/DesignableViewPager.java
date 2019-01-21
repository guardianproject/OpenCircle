package com.circleof6.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by N-Pex on 2018-10-30.
 */
public class DesignableViewPager extends ViewPager {

    private boolean swipingEnabled = true;

    public DesignableViewPager(@NonNull Context context) {
        super(context);
    }

    public DesignableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    // Take from here: https://stackoverflow.com/questions/12295066/android-how-to-implement-viewpager-and-views-in-single-xml-file
    @Override
    protected void onFinishInflate() {
        int childCount = getChildCount();
        final List<View> pages = new ArrayList<>(childCount);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            Class<?> clazz = child.getClass();
            if (clazz.getAnnotation(DecorView.class) == null) {
                pages.add(child);
            }
        }
        for (View page : pages) {
            page.setVisibility(View.INVISIBLE);
            //removeView(page);
        }
        setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return pages.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                pages.get(position).setVisibility(View.VISIBLE);
                pages.get(position).bringToFront();
                return position;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //((container.removeView(pages.get((Integer) object));
                pages.get((Integer) object).setVisibility(View.INVISIBLE);
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return pages.get((Integer) object) == view;
            }
        });
        super.onFinishInflate();
    }

    public boolean isSwipingEnabled() {
        return swipingEnabled;
    }

    /**
     * Enable or disable swiping between pages. The default is "enabled".
     * @param swipingEnabled true to enable page swipes, false to disable.
     */
    public void setSwipingEnabled(boolean swipingEnabled) {
        this.swipingEnabled = swipingEnabled;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (swipingEnabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (swipingEnabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }
}
