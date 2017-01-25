package com.appster.dentamatch.ui.jobs;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentJobsBinding;
import com.appster.dentamatch.ui.common.BaseFragment;

/**
 * Created by Appster on 23/01/17.
 */

public class JobsFragment extends BaseFragment implements View.OnClickListener {
    private FragmentJobsBinding mJobsBinding;
    private boolean mIsList;

    public static JobsFragment newInstance() {
        return new JobsFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mJobsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_jobs, container, false);
        initViews();

        /**
         * Load list job fragment as the default fragment.
         */
        getActivity().getSupportFragmentManager()
                .beginTransaction().replace(R.id.lay_container, JobListFragment.newInstance()).commit();
        mIsList = true;
        return mJobsBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.iv_tool_bar_right:

                if(mIsList) {
                    mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setImageResource(R.drawable.img_map);
                    mJobsBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getActivity().getString(R.string.header_map_view));
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.lay_container, JobMapFragment.newInstance()).commit();
                    mIsList = false;

                }else{
                    mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setImageResource(R.drawable.img_list);
                    mJobsBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getActivity().getString(R.string.header_list_view));
                    getActivity().getSupportFragmentManager()
                            .beginTransaction().replace(R.id.lay_container, JobListFragment.newInstance()).commit();
                    mIsList = true;
                }

                break;

            default: break;
        }
    }

    private void initViews() {
        mJobsBinding.toolbarFragmentJobs.ivToolBarLeft.setImageResource(R.drawable.img_notifications);
        mJobsBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getActivity().getString(R.string.header_list_view));
        mJobsBinding.toolbarFragmentJobs.txvToolbarGeneralRight
                .setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.img_search), null);
        mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setVisibility(View.VISIBLE);
        mJobsBinding.toolbarFragmentJobs.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
        mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setImageResource(R.drawable.img_list);

        mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setOnClickListener(this);

    }
}
