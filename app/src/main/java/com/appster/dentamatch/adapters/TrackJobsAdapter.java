package com.appster.dentamatch.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemTrackJobListBinding;
import com.appster.dentamatch.model.SaveUnSaveEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.jobs.SaveUnSaveRequest;
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.tracks.CancelReasonDialogFragment;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by Appster on 03/02/17.
 */

public class TrackJobsAdapter extends RecyclerView.Adapter<TrackJobsAdapter.MyHolder> implements View.OnClickListener, View.OnLongClickListener {
    private ItemTrackJobListBinding mBinding;
    private Context mContext;
    private ArrayList<SearchJobModel> mJobListData;
    private boolean mIsSaved, mIsApplied, mIsShortListed;

    public TrackJobsAdapter(Context context, ArrayList<SearchJobModel> jobListData, boolean isSaved, boolean isApplied, boolean isShortListed) {
        mContext = context;
        mJobListData = jobListData;
        mIsSaved = isSaved;
        mIsApplied = isApplied;
        mIsShortListed = isShortListed;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_track_job_list, parent, false);
        return new MyHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        SearchJobModel data = mJobListData.get(position);

        if (data != null) {
            holder.itemView.setTag(data.getId());
            holder.itemView.setOnClickListener(this);
            holder.tvName.setText(data.getJobTitleName());

            if (mIsSaved) {
                holder.cbSelect.setVisibility(View.VISIBLE);
                holder.ivChat.setVisibility(View.GONE);
                holder.cbSelect.setTag(position);
                holder.cbSelect.setOnClickListener(this);
                holder.itemView.setOnLongClickListener(null);

            } else if (mIsApplied) {
                holder.cbSelect.setVisibility(View.GONE);
                holder.ivChat.setVisibility(View.GONE);
                holder.cbSelect.setTag(null);
                holder.cbSelect.setOnClickListener(null);
                holder.itemView.setTag(position);
                holder.itemView.setOnLongClickListener(this);

            } else {
                holder.cbSelect.setVisibility(View.GONE);
                holder.ivChat.setVisibility(View.VISIBLE);
                holder.cbSelect.setTag(null);
                holder.cbSelect.setOnClickListener(null);
                holder.itemView.setTag(position);
                holder.itemView.setOnLongClickListener(this);

            }

            if (data.getJobType() == Constants.JOBTYPE.PART_TIME.getValue()) {
                holder.tvJobType.setText(mContext.getString(R.string.txt_part_time));
                holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_part_time);

                ArrayList<String> partTimeDaysArray = new ArrayList<>();
                if (data.getIsMonday() == 1) {
                    partTimeDaysArray.add(mContext.getString(R.string.txt_monday));
                }

                if (data.getIsTuesday() == 1) {
                    partTimeDaysArray.add(mContext.getString(R.string.txt_tuesday));
                }

                if (data.getIsWednesday() == 1) {
                    partTimeDaysArray.add(mContext.getString(R.string.txt_wednesday));
                }

                if (data.getIsThursday() == 1) {
                    partTimeDaysArray.add(mContext.getString(R.string.txt_thursday));
                }

                if (data.getIsFriday() == 1) {
                    partTimeDaysArray.add(mContext.getString(R.string.txt_friday));
                }

                if (data.getIsSaturday() == 1) {
                    partTimeDaysArray.add(mContext.getString(R.string.txt_saturday));
                }

                if (data.getIsSunday() == 1) {
                    partTimeDaysArray.add(mContext.getString(R.string.txt_sunday));
                }

                String partTimeDays = TextUtils.join(", ", partTimeDaysArray);
                holder.tvDate.setVisibility(View.VISIBLE);
                holder.tvDate.setText(partTimeDays);

                final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.BELOW, holder.tvName.getId());
                params.addRule(RelativeLayout.END_OF, holder.tvJobType.getId());
                params.addRule(RelativeLayout.START_OF, holder.tvDistance.getId());
                params.setMargins(Utils.dpToPx(mContext, 12), 0, Utils.dpToPx(mContext, 10), 0);

                holder.tvDate.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (holder.tvDate.getLineCount() == 1) {
                            params.addRule(RelativeLayout.ALIGN_BASELINE, holder.tvJobType.getId());
                            holder.tvDate.setLayoutParams(params);
                        } else {
                            holder.tvDate.setLayoutParams(params);
                        }
                    }
                }, 200);


            } else if (data.getJobType() == Constants.JOBTYPE.FULL_TIME.getValue()) {
                holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_full_time);
                holder.tvDate.setVisibility(View.GONE);
                holder.tvJobType.setText(mContext.getString(R.string.txt_full_time));

            } else if (data.getJobType() == Constants.JOBTYPE.TEMPORARY.getValue()) {
                holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_temporary);
                holder.tvDate.setVisibility(View.GONE);
                holder.tvJobType.setText(mContext.getString(R.string.txt_temporary));
            }

            holder.tvDocAddress.setText(data.getAddress());
            String endMessage = data.getDays() > 1 ? mContext.getString(R.string.txt_days_ago) : mContext.getString(R.string.txt_day_ago);
            holder.tvDuration.setText(String.valueOf(data.getDays()).concat(" ").concat(endMessage));
            holder.tvDistance.setText(String.format(Locale.getDefault(), "%.1f", data.getDistance()).concat(mContext.getString(R.string.txt_miles)));
            holder.tvDocName.setText(data.getOfficeName());
        }
    }

    @Override
    public int getItemCount() {
        if (mJobListData != null) {
            return mJobListData.size();
        }

        return 0;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.cb_job_selection:
                final int position = (int) v.getTag();
                Alert.createYesNoAlert(mContext, "OK", "CANCEL", mContext.getString(R.string.app_name), "Are you sure you want to unsave the job?", new Alert.OnAlertClickListener() {
                    @Override
                    public void onPositive(DialogInterface dialog) {
                        unSaveJob(mJobListData.get(position).getId(), position);
                    }

                    @Override
                    public void onNegative(DialogInterface dialog) {
                        /**
                         * change the star uncheck by notifying the item.
                         */
                        notifyItemChanged(position);
                        dialog.dismiss();
                    }
                });
            break;

            default: break;
        }

    }

    private void unSaveJob(final int JobID, final int position) {
        SaveUnSaveRequest request = new SaveUnSaveRequest();
        request.setJobId(JobID);
        request.setStatus(0);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);

        ((BaseActivity) mContext).processToShowDialog("", mContext.getString(R.string.please_wait), null);

        webServices.saveUnSaveJob(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) mContext) {
            @Override
            public void onSuccess(BaseResponse response) {
                ((BaseActivity) mContext).showToast(response.getMessage());

                if(response.getStatus() == 1){
                    mJobListData.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, mJobListData.size());

                    /**
                     * Update the search job screens for un-save events.
                     */
                    EventBus.getDefault().post(new SaveUnSaveEvent(JobID,0));
                }

            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        final int position = (int) v.getTag();
        Alert.createYesNoAlert(mContext, "OK", "CANCEL", mContext.getString(R.string.app_name), "Are you sure you want to cancel the job?", new Alert.OnAlertClickListener() {
            @Override
            public void onPositive(DialogInterface dialog) {
                CancelReasonDialogFragment dialogFragment = CancelReasonDialogFragment.newInstance();
                Bundle bundle = new Bundle();
                bundle.putInt(Constants.EXTRA_JOB_ID, mJobListData.get(position).getId());
                dialogFragment.setArguments(bundle);
                dialogFragment.show(((BaseActivity)mContext).getSupportFragmentManager(),null);
            }

            @Override
            public void onNegative(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        return false;
    }

    public void cancelJob(int JobID){
       for(int i = 0; i < mJobListData.size(); i++){

           if(mJobListData.get(i).getId() == JobID){
               mJobListData.remove(i);
               notifyItemRemoved(i);
               notifyItemRangeChanged(i,mJobListData.size());
               break;
           }
       }
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvJobType, tvDate, tvDocName, tvDocAddress, tvDuration, tvDistance;
        ImageView ivChat;
        CheckBox cbSelect;

        MyHolder(View itemView) {
            super(itemView);
            tvName = mBinding.tvJobName;
            tvJobType = mBinding.tvJobType;
            tvDate = mBinding.tvJobDate;
            tvDocName = mBinding.tvJobDocName;
            tvDocAddress = mBinding.tvJobDocAddress;
            tvDuration = mBinding.tvJobDocTime;
            tvDistance = mBinding.tvJobDocDistance;
            cbSelect = mBinding.cbJobSelection;
            ivChat = mBinding.ivJobMessage;
        }
    }
}