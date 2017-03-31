package com.appster.dentamatch.ui.tracks;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.common.BaseFragment;

/**
 * Created by Appster on 23/01/17.
 */

public class TrackFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    private com.appster.dentamatch.databinding.FragmentTracksBinding mBinding;
    private SavedJobFragment savedJobsFragment ;
    private AppliedJobsFragment appliedJobsFragment ;
    private ShortlistedJobsFragment shortListedJobsFragment;

    public static TrackFragment newInstance(){
        return new TrackFragment();
    }
    @Override
    public String getFragmentName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding=  DataBindingUtil.inflate(inflater, R.layout.fragment_tracks, container, false);
        initViews();

        /**
         * Load list job fragment as the default fragment.
         */
       getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_container_fragment_tracks, savedJobsFragment)
                .commit();

        return mBinding.getRoot();
    }

    private void initViews() {
        mBinding.layTabFragmentTracks.addTab(mBinding.layTabFragmentTracks.newTab().setText(getString(R.string.txt_saved).toUpperCase()));
        mBinding.layTabFragmentTracks.addTab(mBinding.layTabFragmentTracks.newTab().setText(getString(R.string.txt_applied).toUpperCase()));
        mBinding.layTabFragmentTracks.addTab(mBinding.layTabFragmentTracks.newTab().setText(getString(R.string.txt_shortlisted).toUpperCase()));
        savedJobsFragment = SavedJobFragment.newInstance();
        appliedJobsFragment = AppliedJobsFragment.newInstance();
        shortListedJobsFragment = ShortlistedJobsFragment.newInstance();

        mBinding.layTabFragmentTracks.addOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()){

            case 0:
               getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.lay_container_fragment_tracks, savedJobsFragment)
                        .commit();

                break;

            case 1:
               getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.lay_container_fragment_tracks, appliedJobsFragment)
                    .commit();

                break;

            case 2:
               getChildFragmentManager()
                        .beginTransaction()
                        .replace(R.id.lay_container_fragment_tracks, shortListedJobsFragment)
                        .commit();
                break;

            default: break;
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
