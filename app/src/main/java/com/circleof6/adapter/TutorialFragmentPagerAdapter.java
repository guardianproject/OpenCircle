package com.circleof6.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.circleof6.fragment.TutorialFragment;

import java.util.List;

/**
 * Created by Edgar Salvador Maurilio on 15/11/2015.
 */
public class TutorialFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<TutorialFragment> tutorialFragmentPagers;

    public TutorialFragmentPagerAdapter(FragmentManager fm, List<TutorialFragment> tutorialFragmentPagers) {
        super(fm);
        this.tutorialFragmentPagers = tutorialFragmentPagers;

    }

    @Override
    public Fragment getItem(int position) {
        return tutorialFragmentPagers.get(position);
    }

    @Override
    public int getCount() {
        return tutorialFragmentPagers.size();
    }

    public void updateInfoPages() {
        for (TutorialFragment tutorialFragment : tutorialFragmentPagers) {
            tutorialFragment.updateInfo();
        }
    }

}
