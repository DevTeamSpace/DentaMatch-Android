/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.searchjob;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentJobsBinding;
import com.appster.dentamatch.base.BaseFragment;
import com.appster.dentamatch.presentation.notification.NotificationActivity;

/**
 * Created by Appster on 23/01/17.
 * Job detail fragment user interface.
 */

public class JobsFragment extends BaseFragment implements View.OnClickListener {
    private FragmentJobsBinding mJobsBinding;
    private boolean mIsList;
    private JobListFragment mJobListFragment;
    private JobMapFragment mJobMapFragment;

    public static JobsFragment newInstance() {
        return new JobsFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mJobsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_jobs, container, false);
        initViews();
        /*
          Load list job fragment as the default fragment.
         */
        getChildFragmentManager().beginTransaction()
                .replace(R.id.lay_container, mJobListFragment)
                .commit();
        mIsList = true;

        return mJobsBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_tool_bar_right:

                if (mIsList && getActivity() != null) {
                    mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setImageResource(R.drawable.img_map);
                    mJobsBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getActivity().getString(R.string.header_map_view));

                    getChildFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.lay_container, mJobMapFragment).commit();
                    mIsList = false;

                } else {
                    mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setImageResource(R.drawable.img_list);
                    if (getActivity() != null)
                        mJobsBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getActivity().getString(R.string.header_list_view));
                    getChildFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.lay_container, mJobListFragment).commit();
                    mIsList = true;
                }

                break;

            case R.id.txv_toolbar_general_right:
                startActivity(new Intent(getActivity(), SearchJobActivity.class));
                break;
            case R.id.iv_tool_bar_left:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;

            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initViews() {
        mJobListFragment = JobListFragment.newInstance();
        mJobMapFragment = JobMapFragment.newInstance();
        mJobsBinding.toolbarFragmentJobs.ivToolBarLeft.setImageResource(R.drawable.img_notifications);
        if (getActivity() != null)
            mJobsBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getActivity().getString(R.string.header_list_view));
        mJobsBinding.toolbarFragmentJobs.txvToolbarGeneralRight
                .setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.img_filter), null);
        mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setVisibility(View.VISIBLE);
        mJobsBinding.toolbarFragmentJobs.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
        mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setImageResource(R.drawable.img_list);

        mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setOnClickListener(this);
        mJobsBinding.toolbarFragmentJobs.txvToolbarGeneralRight.setOnClickListener(this);
        mJobsBinding.toolbarFragmentJobs.ivToolBarLeft.setOnClickListener(this);

    }

}