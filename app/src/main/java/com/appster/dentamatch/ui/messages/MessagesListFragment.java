package com.appster.dentamatch.ui.messages;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentMessagesBinding;
import com.appster.dentamatch.ui.common.BaseFragment;

/**
 * Created by Appster on 23/01/17.
 */

public class MessagesListFragment extends BaseFragment {
    private FragmentMessagesBinding mMessagesBinding;
    private RecyclerView.LayoutManager mLayoutManager;
    private MessageListAdapter mAdapter;

    public static MessagesListFragment newInstance(){
        return new MessagesListFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMessagesBinding =  DataBindingUtil.inflate( inflater, R.layout.fragment_messages, container, false);
        initViews();
        mMessagesBinding.rvMessageList.setLayoutManager(mLayoutManager);
        mMessagesBinding.rvMessageList.setAdapter(mAdapter);
        return mMessagesBinding.getRoot();
    }

    private void initViews() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MessageListAdapter();
        mMessagesBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getString(R.string.nav_message));
        mMessagesBinding.toolbarFragmentJobs.ivToolBarLeft.setVisibility(View.GONE);

    }
}
