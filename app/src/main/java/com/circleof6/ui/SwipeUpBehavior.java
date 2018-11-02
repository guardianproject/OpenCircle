package com.circleof6.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.circleof6.util.MethodsUtils;

/**
 * Created by N-Pex on 2018-11-01.
 */
public class SwipeUpBehavior extends AppBarLayout.ScrollingViewBehavior {

    public interface OnSwipeUpListener {
        void onSwipeUp();
    }

    private OnSwipeUpListener swipeUpListener;
    private AppBarLayout appBarLayout;
    private int totalSwipeUp = 0;
    private int maxSwipeUp = Integer.MAX_VALUE;

    public enum NestedScrollType {
        None,
        Normal,
        SwipeUp;
    }
    private NestedScrollType nestedScrollType = NestedScrollType.None;

    public OnSwipeUpListener getSwipeUpListener() {
        return swipeUpListener;
    }

    public void setSwipeUpListener(OnSwipeUpListener swipeUpListener) {
        this.swipeUpListener = swipeUpListener;
    }

    public SwipeUpBehavior() {
        super();
    }

    public SwipeUpBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if (dependency instanceof AppBarLayout) {
            appBarLayout = (AppBarLayout)dependency;
            maxSwipeUp = MethodsUtils.dpToPx(40, parent.getContext());
            return true;
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        if (ev.getActionMasked() == MotionEvent.ACTION_CANCEL || ev.getActionMasked() == MotionEvent.ACTION_UP) {
            stopSwipe(child);
        } else if (ev.getActionMasked() == MotionEvent.ACTION_DOWN) {
            if (appBarLayout.getBottom() == 0) {
                nestedScrollType = NestedScrollType.SwipeUp;
            } else {
                nestedScrollType = NestedScrollType.None;
            }
        }
        return super.onInterceptTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        boolean ret = super.onTouchEvent(parent, child, ev);
        if (ev.getActionMasked() == MotionEvent.ACTION_CANCEL || ev.getActionMasked() == MotionEvent.ACTION_UP) {
            stopSwipe(child);
        }
        return ret;
    }

    private void stopSwipe(View child) {
        if (totalSwipeUp > maxSwipeUp) {
            if (getSwipeUpListener() != null) {
                getSwipeUpListener().onSwipeUp();
            }
        } else {
            totalSwipeUp = 0;
            setSwipeUpValue(child, totalSwipeUp);
        }
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        boolean ret = super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, axes, type);
        return ret || nestedScrollType == NestedScrollType.SwipeUp;
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
        stopSwipe(child);
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type);
        if (nestedScrollType == NestedScrollType.SwipeUp && dyUnconsumed > 0) {
            totalSwipeUp += dyUnconsumed;
            setSwipeUpValue(child, totalSwipeUp);
        }
    }

    private void setSwipeUpValue(View child, int value) {
        child.setTranslationY(-value);
        child.setAlpha((float)Math.abs(1.0 - 0.5f * Math.min(1, value / maxSwipeUp)));
    }
}