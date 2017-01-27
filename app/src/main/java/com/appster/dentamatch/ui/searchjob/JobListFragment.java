package com.appster.dentamatch.ui.searchjob;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.JobListAdapter;
import com.appster.dentamatch.databinding.FragmentJobListBinding;
import com.appster.dentamatch.model.PaginationDataUpdatedEvent;
import com.appster.dentamatch.network.response.jobs.SearchJobResponseData;
import com.appster.dentamatch.ui.common.BaseFragment;
import com.appster.dentamatch.util.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by Appster on 24/01/17.
 */

public class JobListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    private FragmentJobListBinding mJobListBinding;
    private SearchJobResponseData mJobListData;

    public static JobListFragment newInstance() {
        return new JobListFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       if( !EventBus.getDefault().isRegistered(this)){
           EventBus.getDefault().register(this);
       }
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mJobListData = getArguments().getParcelable(Constants.EXTRA_JOB_LIST);
        }
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
        mJobListBinding.rvJobs.setAdapter(new JobListAdapter(getActivity(),mJobListData));
        mJobListBinding.swipeRefreshJobList.setColorSchemeResources(R.color.colorAccent);
        mJobListBinding.swipeRefreshJobList.setOnRefreshListener(this);
        return mJobListBinding.getRoot();
    }


    @Subscribe
    public void onDataUpdated(PaginationDataUpdatedEvent event){
        if(event != null) {
            mJobListData.getList().addAll(event.getUpdatedData().getList());
        }
    }



    @Override
    public void onRefresh() {

    }
}
