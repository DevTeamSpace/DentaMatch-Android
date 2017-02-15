package com.appster.dentamatch.ui.calendar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemJobListBinding;
import com.appster.dentamatch.network.response.jobs.HiredJobs;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.tracks.CancelReasonDialogFragment;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import java.util.ArrayList;

/**
 * Created by virender on 10/02/17.
 */
public class HiredJobAdapter extends RecyclerView.Adapter<HiredJobAdapter.MyHolder> implements View.OnClickListener, View.OnLongClickListener {
    private ItemJobListBinding mBinding;
    private Context mContext;
    private ArrayList<HiredJobs> mJobListData;


    public HiredJobAdapter(Context context, ArrayList<HiredJobs> jobListData) {
        mContext = context;
        mJobListData = jobListData;
    }

    public ArrayList<HiredJobs> getList() {
        return mJobListData;
    }

    public void setJobList(ArrayList<HiredJobs> jobList) {
        mJobListData.clear();
        mJobListData = jobList;
        notifyDataSetChanged();
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_job_list, parent, false);
        return new MyHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        HiredJobs data = mJobListData.get(position);

        if (data != null) {
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);

            holder.tvName.setText(data.getJobtitleName());
            holder.cbSelect.setVisibility(View.GONE);
            holder.tvDistance.setVisibility(View.GONE);

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
                params.addRule(RelativeLayout.END_OF, holder.tvJobType.getId());
                params.addRule(RelativeLayout.START_OF, holder.tvDistance.getId());
                params.setMargins(Utils.dpToPx(mContext, 12), 0, Utils.dpToPx(mContext, 10), 0);

                holder.tvDate.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (holder.tvDate.getLineCount() == 1) {
                            params.addRule(RelativeLayout.ALIGN_BOTTOM, holder.tvJobType.getId());
                            holder.tvDate.setLayoutParams(params);
                        } else {
                            params.addRule(RelativeLayout.ALIGN_BASELINE, holder.tvJobType.getId());
                            holder.tvDate.setLayoutParams(params);
                            holder.tvDate.setPadding(0, 4, 0, 0);
                        }
                    }
                }, 100);

                holder.tvDate.setEllipsize(TextUtils.TruncateAt.END);


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
            String endMessage="";
            if(data.getDays()==0){
              endMessage=mContext.getString(R.string.text_todays);
            }else{
                 endMessage = data.getDays() > 1 ? mContext.getString(R.string.txt_days_ago) : mContext.getString(R.string.txt_day_ago);

            }
            holder.tvDuration.setText(String.valueOf(data.getDays()).concat(" ").concat(endMessage));
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
        switch (v.getId()) {

//
//            case R.id.lay_item_job_list:
//                int jobID = (int) v.getTag();
//                mContext.startActivity(new Intent(mContext, JobDetailActivity.class)
//                        .putExtra(Constants.EXTRA_JOB_DETAIL_ID, jobID).putExtra(Constants.INTENT_KEY.FROM_WHERE,true));
//                break;

            default:
                break;
        }

    }


    class MyHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvJobType, tvDate, tvDocName, tvDocAddress, tvDuration, tvDistance;
        private CheckBox cbSelect;

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
        }
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
                dialogFragment.show(((BaseActivity) mContext).getSupportFragmentManager(), null);
            }

            @Override
            public void onNegative(DialogInterface dialog) {
                dialog.dismiss();
            }
        });
        return false;
    }

    public void cancelJob(int positon) {
//        for(int i = 0; i < mJobListData.size(); i++){
//
//            if(mJobListData.get(i).getId() == JobID){
//                mJobListData.remove(i);
//                notifyItemRemoved(i);
//                notifyItemRangeChanged(i,mJobListData.size());
//                break;
//            }
//        }

        mJobListData.remove(positon);
        notifyItemRemoved(positon);
        notifyItemRangeChanged(positon, mJobListData.size());
    }
}
