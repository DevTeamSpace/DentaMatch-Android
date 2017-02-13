package com.appster.dentamatch.ui.messages;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentMessagesBinding;
import com.appster.dentamatch.model.ChatListModel;
import com.appster.dentamatch.model.ChatUserUnBlockedEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.response.chat.ChatHistoryResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.BaseFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by Appster on 23/01/17.
 */

public class MessagesListFragment extends BaseFragment {
    private FragmentMessagesBinding mMessagesBinding;
    private RecyclerView.LayoutManager mLayoutManager;
    private MessageListAdapter mAdapter;
    private ArrayList<ChatListModel> mData;

    public static MessagesListFragment newInstance() {
        return new MessagesListFragment();
    }

    @Override
    public String getFragmentName() {
        return null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        mMessagesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_under_dev, container, false);
//        initViews();
//        mMessagesBinding.rvMessageList.setLayoutManager(mLayoutManager);
//        mMessagesBinding.rvMessageList.setAdapter(mAdapter);
//        return mMessagesBinding.getRoot();
        return inflater.inflate(R.layout.fragment_under_dev, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        getChatHistory();

    }

    @Override
    public void onResume() {
        super.onResume();
//       SocketManager.getInstance().init(PreferenceUtil.getUserChatId(), PreferenceUtil.getFirstName());
    }

    private void getChatHistory() {
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
                        mData.addAll(response.getResult().getList());
                        mAdapter.notifyDataSetChanged();

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
        mData = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new MessageListAdapter(getActivity(), mData);
        mMessagesBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setText(getString(R.string.nav_message));
        mMessagesBinding.toolbarFragmentJobs.tvToolbarGeneralLeft.setAllCaps(true);
        mMessagesBinding.toolbarFragmentJobs.ivToolBarLeft.setVisibility(View.GONE);

    }

    @Subscribe
    public void onUserUnblocked(ChatUserUnBlockedEvent event){
        if(event != null){

            for (ChatListModel model: mData) {
                if(model.getRecruiterId() == event.getRecruiterID()){
                    model.setSeekerBlock(0);
                    break;
                }
            }
        }
    }
}
