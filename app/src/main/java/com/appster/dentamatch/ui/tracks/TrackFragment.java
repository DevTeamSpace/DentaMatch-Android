package com.appster.dentamatch.ui.tracks;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentTracksBinding;
import com.appster.dentamatch.ui.common.BaseFragment;

/**
 * Created by Appster on 23/01/17.
 */

public class TrackFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {
    private FragmentTracksBinding mBinding;

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
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.lay_container_fragment_tracks, SavedJobFragment.newInstance())
                .commit();

        return mBinding.getRoot();
    }

    private void initViews() {
        mBinding.layTabFragmentTracks.addTab(mBinding.layTabFragmentTracks.newTab().setText("SAVED"));
        mBinding.layTabFragmentTracks.addTab(mBinding.layTabFragmentTracks.newTab().setText("APPLIED"));
        mBinding.layTabFragmentTracks.addTab(mBinding.layTabFragmentTracks.newTab().setText("SHORTLISTED"));

        mBinding.layTabFragmentTracks.addOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()){

            case 0:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.lay_container_fragment_tracks, SavedJobFragment.newInstance())
                        .commit();

                break;

            case 1:
                getActivity().getSupportFragmentManager()
                    .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.lay_container_fragment_tracks, AppliedJobsFragment.newInstance())
                    .commit();

                break;

            case 2:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                        .replace(R.id.lay_container_fragment_tracks, ShortlistedJobsFragment.newInstance())
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
