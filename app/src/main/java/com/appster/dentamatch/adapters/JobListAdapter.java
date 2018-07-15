/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.jobs.SaveUnSaveRequest;
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.searchjob.JobDetailActivity;
import com.appster.dentamatch.ui.searchjob.SearchJobDataHelper;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;

/**
 * Created by Appster on 24/01/17.
 * Adapter for job listing.
 */

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.MyHolder> implements View.OnClickListener {
    private static final int JOB_SAVED = 1;
    private static final int STATUS_UNSAVED = 0;
    private static final int STATUS_SAVED = 1;
    private static final int ADDED_PART_TIME = 1;
    private static final int LINE_COUNT_ONE = 1;
    private static final int VIEW_DELAY_TIME = 100;
    private static final int DURATION_TIME_0 = 0;
    private static final int DURATION_TIME_1 = 1;

    private ItemJobListBinding mBinding;
    private final Context mContext;
    private final ArrayList<SearchJobModel> mJobListData;


    public JobListAdapter(Context context, ArrayList<SearchJobModel> jobListData) {
        mContext = context;
        mJobListData = jobListData;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_job_list, parent, false);
        return new MyHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        SearchJobModel data = mJobListData.get(position);

        if (data != null) {
            holder.itemView.setTag(data.getId());
            holder.itemView.setOnClickListener(this);
            holder.tvName.setText(data.getJobTitleName());
            holder.cbSelect.setTag(position);
            holder.cbSelect.setOnClickListener(this);
            holder.cbSelect.setChecked(data.getIsSaved() == JOB_SAVED);

            if (data.getJobType() == Constants.JOBTYPE.PART_TIME.getValue()) {
                holder.tvJobType.setText(mContext.getString(R.string.txt_part_time));
                holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_part_time);

                ArrayList<String> partTimeDaysArray = new ArrayList<>();
                if (data.getIsMonday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(mContext.getString(R.string.mon));
                }

                if (data.getIsTuesday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(mContext.getString(R.string.tue));
                }

                if (data.getIsWednesday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(mContext.getString(R.string.wed));
                }

                if (data.getIsThursday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(mContext.getString(R.string.thu));
                }

                if (data.getIsFriday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(mContext.getString(R.string.fri));
                }

                if (data.getIsSaturday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(mContext.getString(R.string.sat));
                }

                if (data.getIsSunday() == ADDED_PART_TIME) {
                    partTimeDaysArray.add(mContext.getString(R.string.sun));
                }

                String partTimeDays = TextUtils.join(", ", partTimeDaysArray);
                holder.tvDate.setVisibility(View.VISIBLE);
                holder.tvDate.setText(partTimeDays);

                final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.END_OF, holder.tvJobType.getId());
                params.addRule(RelativeLayout.START_OF, holder.tvDistance.getId());
                params.setMargins(Utils.dpToPx(mContext,
                        mContext.getResources().getInteger(R.integer.margin_12)),
                        mContext.getResources().getInteger(R.integer.margin_0),
                        Utils.dpToPx(mContext, mContext.getResources().getInteger(R.integer.margin_10)),
                        mContext.getResources().getInteger(R.integer.margin_12));

                holder.tvDate.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (holder.tvDate.getLineCount() == LINE_COUNT_ONE) {
                            params.addRule(RelativeLayout.ALIGN_BOTTOM, holder.tvJobType.getId());
                            holder.tvDate.setLayoutParams(params);
                        } else {
                            params.addRule(RelativeLayout.ALIGN_TOP, holder.tvJobType.getId());
                            holder.tvDate.setLayoutParams(params);
                            holder.tvDate.setPadding(mContext.getResources().getInteger(R.integer.padding_0),
                                    mContext.getResources().getInteger(R.integer.padding_0),
                                    mContext.getResources().getInteger(R.integer.padding_0),
                                    mContext.getResources().getInteger(R.integer.padding_0));
                        }
                    }
                }, VIEW_DELAY_TIME);

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
            if (data.getDays() == DURATION_TIME_0) {
                holder.tvDuration.setText(mContext.getString(R.string.text_todays));

            } else {
                String endMessage = data.getDays() > DURATION_TIME_1 ? mContext.getString(R.string.txt_days_ago) : mContext.getString(R.string.txt_day_ago);
                holder.tvDuration.setText(String.valueOf(data.getDays()).concat(" ").concat(endMessage));
            }

            //holder.tvDistance.setText(String.format(Locale.getDefault(), "%.1f", data.getDistance()).concat(mContext.getString(R.string.txt_miles)));
            holder.tvDistance.setText(String.format(Locale.getDefault(), "%.2f", data.getPercentaSkillsMatch()).concat("%"));

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

    private void saveUnSaveJob(int JobID, final int status, final int position) {
        SaveUnSaveRequest request = new SaveUnSaveRequest();
        request.setJobId(JobID);
        request.setStatus(status);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        ((BaseActivity) mContext).processToShowDialog();
        webServices.saveUnSaveJob(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) mContext) {
            @Override
            public void onSuccess(BaseResponse response) {
                ((BaseActivity) mContext).showToast(response.getMessage());

                if (response.getStatus() == 1) {
                    mJobListData.get(position).setIsSaved(status);
                    notifyItemChanged(position);
                    SearchJobDataHelper.getInstance().notifyItemsChanged(mJobListData.get(position));
                } else {
                    if (TextUtils.isEmpty(response.getMessage())) {
                        notifyItemChanged(position);
                        ((BaseActivity) mContext).showToast(response.getMessage());
                    }
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_job_selection:
                final int position = (int) v.getTag();
                final int status = mJobListData.get(position).getIsSaved() == JOB_SAVED ? STATUS_UNSAVED : STATUS_SAVED;
                if (status == 0) {
                    Alert.createYesNoAlert(mContext,
                            mContext.getString(R.string.txt_ok),
                            mContext.getString(R.string.txt_cancel),
                            mContext.getString(R.string.txt_alert_title),
                            mContext.getString(R.string.msg_unsave_warning),
                            new Alert.OnAlertClickListener() {
                                @Override
                                public void onPositive(DialogInterface dialog) {
                                    saveUnSaveJob(mJobListData.get(position).getId(), status, position);
                                }

                                @Override
                                public void onNegative(DialogInterface dialog) {
                                    dialog.dismiss();
                                    notifyItemChanged(position);
                                }
                            });
                } else {
                    saveUnSaveJob(mJobListData.get(position).getId(), status, position);
                }

                break;

            case R.id.lay_item_job_list:
                int jobID = (int) v.getTag();
                double matchPercentage = 0;
                for (SearchJobModel searchJobModel : mJobListData) {
                    if (searchJobModel.getId() == jobID) {
                        matchPercentage = searchJobModel.getPercentaSkillsMatch();
                        break;
                    }
                }

                mContext.startActivity(new Intent(mContext, JobDetailActivity.class)
                        .putExtra(Constants.EXTRA_JOB_DETAIL_ID, jobID)
                        .putExtra(Constants.EXTRA_MATCHES_PERCENT, matchPercentage)
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));

                break;

            default:
                break;
        }

    }

    public void updateData(SearchJobModel model) {
        for (int i = 0; i < mJobListData.size(); i++) {
            if (mJobListData.get(i).getId() == model.getId()) {
                mJobListData.set(i, model);
                notifyItemChanged(i);
                break;
            }
        }
    }


    class MyHolder extends RecyclerView.ViewHolder {
        final TextView tvName;
        final TextView tvJobType;
        final TextView tvDate;
        final TextView tvDocName;
        final TextView tvDocAddress;
        final TextView tvDuration;
        final TextView tvDistance;
        final CheckBox cbSelect;

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
}
