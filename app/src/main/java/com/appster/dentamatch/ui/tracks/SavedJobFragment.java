package com.appster.dentamatch.ui.tracks;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentSavedJobsBinding;
import com.appster.dentamatch.model.User;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.tracks.TrackJobsRequest;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseFragment;

/**
 * Created by Appster on 02/02/17.
 */

public class SavedJobFragment extends BaseFragment {
    private FragmentSavedJobsBinding mBinding;
    private User mUser;

    public static SavedJobFragment newInstance() {
        return new SavedJobFragment();
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

    private void getAllSavedJobs(){
        TrackJobsRequest request = new TrackJobsRequest();
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
    }
}
