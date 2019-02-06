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
import com.appster.dentamatch.chat.DBHelper;
import com.appster.dentamatch.chat.DBModel;
import com.appster.dentamatch.databinding.FragmentMessagesBinding;
import com.appster.dentamatch.model.ChatListModel;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.base.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.chat.Recruiter;
import com.appster.dentamatch.network.response.chat.ChatHistoryResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.base.BaseActivity;
import com.appster.dentamatch.base.BaseFragment;
import com.appster.dentamatch.util.Utils;

import io.realm.RealmResults;
import retrofit2.Call;

/**
 * Created by Appster on 23/01/17.
 * To inject activity reference.
 */

public class MessagesListFragment extends BaseFragment implements MessageListAdapter.IDeleteMessage {
    private FragmentMessagesBinding mMessagesBinding;
    private RecyclerView.LayoutManager mLayoutManager;
    private MessageListAdapter mAdapter;
    private RealmResults<DBModel> data;

    public static MessagesListFragment newInstance() {
        return new MessagesListFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
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

    }


    private void getAllUserChats() {
        showProgressBar(getString(R.string.please_wait));
        AuthWebServices client = RequestController.createService(AuthWebServices.class, true);
        client.getChatHistory().enqueue(new BaseCallback<ChatHistoryResponse>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(ChatHistoryResponse response) {
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

            @Override
            public void onFail(Call<ChatHistoryResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    private void initViews() {
        mLayoutManager = new LinearLayoutManager(getActivity());
        mMessagesBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getString(R.string.nav_message));
        mMessagesBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setAllCaps(true);
        mMessagesBinding.toolbarFragmentJobs.ivToolBarLeft.setVisibility(View.GONE);
    }


    @Override
    public void onDelete(final String id, final int position) {
        showProgressBar(getString(R.string.please_wait));
        AuthWebServices client = RequestController.createService(AuthWebServices.class, true);
        client.deleteUserChat(new Recruiter(id)).enqueue(new BaseCallback<BaseResponse>((BaseActivity) getActivity()) {
            @Override
            public void onSuccess(BaseResponse response) {
                if (response != null && response.getStatus() == 1) {
                    mAdapter.notifyItemRemoved(position);
                    showToastLengthLong(getString(R.string.del_chat_his));
                    DBHelper.getInstance().clearRecruiterChats(id);
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }

}
