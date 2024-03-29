package com.appster.dentamatch.ui.auth;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

public class IntroSlidePagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragments;

    public IntroSlidePagerAdapter(FragmentManager fm, @NonNull List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return this.mFragments.get(position);
    }

    @Override
    public int getCount() {
        return this.mFragments.size();
    }

    @NonNull
    public List<Fragment> getFragments() {
        return mFragments;
    }
}
