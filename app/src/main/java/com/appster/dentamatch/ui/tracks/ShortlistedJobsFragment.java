package com.appster.dentamatch.ui.tracks;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentShortlistedJobsBinding;
import com.appster.dentamatch.ui.common.BaseFragment;

/**
 * Created by Appster on 02/02/17.
 */

public class ShortlistedJobsFragment extends BaseFragment {
    private FragmentShortlistedJobsBinding mBinding;

    public static ShortlistedJobsFragment newInstance() {
        return new ShortlistedJobsFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_saved_jobs, container, false);
        return mBinding.getRoot();

    }
}
