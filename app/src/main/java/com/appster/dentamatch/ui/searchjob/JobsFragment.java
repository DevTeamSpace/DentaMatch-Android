package com.appster.dentamatch.ui.searchjob;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentJobsBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.jobs.SearchJobRequest;
import com.appster.dentamatch.network.response.jobs.SearchJobResponse;
import com.appster.dentamatch.network.response.jobs.SearchJobResponseData;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.BaseFragment;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;

import retrofit2.Call;

/**
 * Created by Appster on 23/01/17.
 */

public class JobsFragment extends BaseFragment implements View.OnClickListener {
    private FragmentJobsBinding mJobsBinding;
    private boolean mIsList;
    private SearchJobResponseData jobData;
    private int mPageNumber = 1;
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
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mJobsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_jobs, container, false);
        initViews();

        /**
         * Search for the selected job using saved job filters.
         */
        if (PreferenceUtil.getJobFilter() != null) {
            searchJob(mPageNumber, false);
        }

        return mJobsBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_tool_bar_right:

                if (mIsList) {
                    mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setImageResource(R.drawable.img_map);
                    mJobsBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getActivity().getString(R.string.header_map_view));

                    mJobMapFragment =  JobMapFragment.newInstance();
                    Bundle arguments = new Bundle();
                    arguments.putParcelable(Constants.EXTRA_JOB_LIST,jobData);
                    mJobMapFragment.setArguments(arguments);

                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                            .replace(R.id.lay_container, mJobMapFragment).commit();
                    mIsList = false;

                } else {
                    mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setImageResource(R.drawable.img_list);
                    mJobsBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getActivity().getString(R.string.header_list_view));
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .setCustomAnimations(R.anim.enter_from_left, R.anim.exit_to_right)
                            .replace(R.id.lay_container, mJobListFragment).commit();
                    mIsList = true;
                }

                break;

            default:
                break;
        }
    }

    private void initViews() {
        mJobListFragment = JobListFragment.newInstance();
        mJobMapFragment = JobMapFragment.newInstance();
        mJobsBinding.toolbarFragmentJobs.ivToolBarLeft.setImageResource(R.drawable.img_notifications);
        mJobsBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getActivity().getString(R.string.header_list_view));
        mJobsBinding.toolbarFragmentJobs.txvToolbarGeneralRight
                .setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ContextCompat.getDrawable(getActivity(), R.drawable.img_search), null);
        mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setVisibility(View.VISIBLE);
        mJobsBinding.toolbarFragmentJobs.txvToolbarGeneralRight.setVisibility(View.VISIBLE);
        mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setImageResource(R.drawable.img_list);

        mJobsBinding.toolbarFragmentJobs.ivToolBarRight.setOnClickListener(this);

    }

    private void searchJob(int pageNumber, final boolean isPaginationLoading) {
        SearchJobRequest request = (SearchJobRequest) PreferenceUtil.getJobFilter();
        request.setPage(pageNumber);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.searchJob(request).enqueue(new BaseCallback<SearchJobResponse>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(SearchJobResponse response) {
                if(!isPaginationLoading) {
                    if (response.getStatus() == 1) {
                        jobData = response.getSearchJobResponseData();
                        /**
                         * Load list job fragment as the default fragment.
                         */
                        mJobListFragment = JobListFragment.newInstance();
                        Bundle arguments = new Bundle();
                        arguments.putParcelable(Constants.EXTRA_JOB_LIST,jobData);
                        mJobListFragment.setArguments(arguments);

                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.lay_container, mJobListFragment)
                                .commit();

                        mIsList = true;
                    }
                }
            }

            @Override
            public void onFail(Call<SearchJobResponse> call, BaseResponse baseResponse) {

            }
        });
    }
}
