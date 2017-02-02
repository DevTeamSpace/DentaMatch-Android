package com.appster.dentamatch.ui.tracks;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentAppliedJobsBinding;
import com.appster.dentamatch.ui.common.BaseFragment;

/**
 * Created by Appster on 02/02/17.
 */

public class AppliedJobsFragment extends BaseFragment {
    private FragmentAppliedJobsBinding mBinding;

    public static AppliedJobsFragment newInstance() {
        return new AppliedJobsFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_applied_jobs, container, false);
        return mBinding.getRoot();

    }
}
