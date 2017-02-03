package com.appster.dentamatch.ui.tracks;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentDialogCancelBinding;
import com.appster.dentamatch.model.JobCancelEvent;
import com.appster.dentamatch.util.Constants;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Appster on 03/02/17.
 */

public class CancelReasonDialogFragment extends android.support.v4.app.DialogFragment implements View.OnClickListener {
    private FragmentDialogCancelBinding mBinding;
    private int mJobID;

    public static CancelReasonDialogFragment newInstance(){
        return new CancelReasonDialogFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
            mJobID = getArguments().getInt(Constants.EXTRA_JOB_ID);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_cancel, container, false);
        mBinding.tvFragmentDialogSubmit.setOnClickListener(this);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        return mBinding.getRoot();
    }

    @Override
    public void onClick(View v) {
        if(TextUtils.isEmpty(mBinding.etFragmentDialogCancel.getText())){
            Toast.makeText(getActivity(),"Please enter a reason for cancelling the job", Toast.LENGTH_SHORT).show();
        }else{
            EventBus.getDefault().post(new JobCancelEvent(mJobID, mBinding.etFragmentDialogCancel.getText().toString()));
            getDialog().dismiss();
        }
    }




}
