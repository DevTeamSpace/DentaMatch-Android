package com.appster.dentamatch.ui.jobs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.common.BaseFragment;

/**
 * Created by Appster on 23/01/17.
 */

public class JobsFragment extends BaseFragment {

    public static JobsFragment newInstance(){
        return new JobsFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_under_dev, container, false);
    }
}