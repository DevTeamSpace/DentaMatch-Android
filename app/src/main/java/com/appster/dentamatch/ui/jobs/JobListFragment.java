package com.appster.dentamatch.ui.jobs;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.JobListAdapter;
import com.appster.dentamatch.databinding.FragmentJobListBinding;
import com.appster.dentamatch.ui.common.BaseFragment;

/**
 * Created by Appster on 24/01/17.
 */

public class JobListFragment extends BaseFragment {
    private FragmentJobListBinding mJobListBinding;

    public static JobListFragment newInstance() {
        return new JobListFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mJobListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_job_list, container, false);
        mJobListBinding.rvJobs.setLayoutManager(new LinearLayoutManager(getActivity()));
        mJobListBinding.rvJobs.setAdapter(new JobListAdapter(getActivity()));
        return mJobListBinding.getRoot();
    }
}
