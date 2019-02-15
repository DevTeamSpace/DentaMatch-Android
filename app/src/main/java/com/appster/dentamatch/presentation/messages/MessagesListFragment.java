/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.messages;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingFragment;
import com.appster.dentamatch.chat.DBHelper;
import com.appster.dentamatch.chat.DBModel;
import com.appster.dentamatch.databinding.FragmentMessagesBinding;
import com.appster.dentamatch.model.ChatListModel;
import com.appster.dentamatch.network.response.chat.ChatHistoryResponse;
import com.appster.dentamatch.util.Utils;

import io.realm.RealmResults;
import kotlin.Pair;

/**
 * Created by Appster on 23/01/17.
 * To inject activity reference.
 */

public class MessagesListFragment extends BaseLoadingFragment<MessagesListViewModel>
        implements MessageListAdapter.IDeleteMessage {

    private FragmentMessagesBinding mMessagesBinding;
    private RecyclerView.LayoutManager mLayoutManager;
    private MessageListAdapter mAdapter;
    private RealmResults<DBModel> data;

    public static MessagesListFragment newInstance() {
        return new MessagesListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mMessagesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false);
        initViews();
        mMessagesBinding.rvMessageList.setLayoutManager(mLayoutManager);

        return mMessagesBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Utils.isConnected(getActivity())) {
            getAllUserChats();
        } else {
            data = DBHelper.getInstance().getAllUserChats();
            if (data != null && data.size() > 0) {
                mMessagesBinding.tvNoJobs.setVisibility(View.GONE);
                if (getActivity() != null) {
                    mAdapter = new MessageListAdapter(getActivity(), data, true);
                    mAdapter.setListener(this);
                }
                mMessagesBinding.rvMessageList.setAdapter(mAdapter);
            } else {
                mMessagesBinding.tvNoJobs.setVisibility(View.VISIBLE);
            }
        }

        viewModel.getChatHistory().observe(this, this::onSuccessChatHistoryRequest);
        viewModel.getChatDelete().observe(this, this::onSuccessChatDelete);
    }

    private void onSuccessChatDelete(@Nullable Pair<String, Integer> result) {
        if (result != null && result.getFirst() != null && result.getSecond() != null) {
            mAdapter.notifyItemRemoved(result.getSecond());
            showToastLengthLong(getString(R.string.del_chat_his));
            DBHelper.getInstance().clearRecruiterChats(result.getFirst());
        }
    }

    private void onSuccessChatHistoryRequest(@Nullable ChatHistoryResponse response) {
        if (response != null && response.getStatus() == 1) {
            if (response.getResult() != null &&
                    response.getResult().getList() != null &&
                    response.getResult().getList().size() > 0) {
                mMessagesBinding.tvNoJobs.setVisibility(View.GONE);
                for (ChatListModel model : response.getResult().getList()) {
                    DBHelper.getInstance().updateRecruiterDetails(String.valueOf(model.getRecruiterId()),
                            model.getName(),
                            Integer.parseInt(model.getUnreadCount()),
                            String.valueOf(model.getMessageListId()),
                            model.getMessage(),
                            model.getTimestamp(),
                            true);
                }
                data = DBHelper.getInstance().getAllUserChats();
                if (getActivity() != null) {
                    mAdapter = new MessageListAdapter(getActivity(), data, true);
                    mAdapter.setListener(MessagesListFragment.this);
                }
                mMessagesBinding.rvMessageList.setAdapter(mAdapter);
            } else {
                mMessagesBinding.tvNoJobs.setVisibility(View.VISIBLE);
            }
        }
    }

    private void getAllUserChats() {
        viewModel.requestChatHistory();
    }

    private void initViews() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mMessagesBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getString(R.string.nav_message));
        mMessagesBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setAllCaps(true);
        mMessagesBinding.toolbarFragmentJobs.ivToolBarLeft.setVisibility(View.GONE);
    }

    @Override
    public void onDelete(final String id, final int position) {
        viewModel.deleteUserChat(id, position);
    }
}
