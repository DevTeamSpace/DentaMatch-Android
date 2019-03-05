/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.calendar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemCalendarBinding;
import com.appster.dentamatch.databinding.ItemJobListBinding;
import com.appster.dentamatch.interfaces.OnDateSelected;
import com.appster.dentamatch.network.response.jobs.HiredJobs;
import com.appster.dentamatch.base.BaseActivity;
import com.appster.dentamatch.presentation.searchjob.JobDetailActivity;
import com.appster.dentamatch.presentation.tracks.CancelReasonDialogFragment;
import com.appster.dentamatch.util.Alert;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.StringUtils;
import com.appster.dentamatch.util.Utils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * Created by virender on 10/02/17.
 * To inject activity reference.
 */
public class HiredJobAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final static int CALENDAR_ITEM = 1;
    private final static int TYPE_CALENDAR = 1001;
    private final static int TYPE_JOB = 1002;
    private final static int TYPE_NO_JOBS = 1003;
    private static final int NO_JOBS_ITEM = 1;

    private boolean mIsFullTime = false;

    @NonNull
    private ArrayList<HiredJobs> mJobListData;

    @NonNull
    private ArrayList<HiredJobs> mCalendarJobListData = new ArrayList<>();

    @NonNull
    private final OnDateSelected mListener;

    HiredJobAdapter(@NonNull ArrayList<HiredJobs> jobListData,
                    @NonNull OnDateSelected listener) {
        mJobListData = new ArrayList<>(jobListData);
        mListener = listener;
    }

    @NonNull
    public ArrayList<HiredJobs> getList() {
        return mJobListData;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_CALENDAR;
        } else if (position == 1 && isEmptyList()) {
            return TYPE_NO_JOBS;
        }
        return TYPE_JOB;
    }

    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_CALENDAR) {
            return new CalendarViewHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(parent.getContext()),
                            R.layout.item_calendar,
                            parent,
                            false
                    )
            );
        } else if (viewType == TYPE_NO_JOBS) {
            return new NoJobsViewHolder(
                    LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.item_no_jobs, parent, false)
            );
        } else {
            return new MyHolder(
                    DataBindingUtil.inflate(
                            LayoutInflater.from(parent.getContext()),
                            R.layout.item_job_list,
                            parent,
                            false
                    )
            );
        }
    }

    @Override
    public void onBindViewHolder(@NotNull final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_CALENDAR) {
            ((CalendarViewHolder) holder).bind();
        } else if (!isEmptyList()) {
            ((MyHolder) holder).bind(mJobListData.get(position - CALENDAR_ITEM));
        }
    }

    private boolean isEmptyList() {
        return mJobListData.size() == 0;
    }

    @Override
    public int getItemCount() {
        return mJobListData.size() + CALENDAR_ITEM + (isEmptyList() ? NO_JOBS_ITEM : 0);
    }

    void setCalendarJobList(@NonNull ArrayList<HiredJobs> list) {
        mCalendarJobListData = new ArrayList<>(list);
        notifyDataSetChanged();
    }

    void isFullTimeJob(boolean isFullTimeJob) {
        mIsFullTime = isFullTimeJob;
        notifyDataSetChanged();
    }

    void setJobList(@NonNull ArrayList<HiredJobs> jobList) {
        mJobListData = new ArrayList<>(jobList);
        notifyDataSetChanged();
    }

    public void cancelJob(int JobID) {
        for (int i = 0; i < mJobListData.size(); i++) {
            if (mJobListData.get(i).getId() == JobID) {
                mJobListData.remove(i);
                notifyItemRemoved(i);
                notifyItemRangeChanged(i, mJobListData.size());
                break;
            }
        }
    }

    class NoJobsViewHolder extends RecyclerView.ViewHolder {
        NoJobsViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    class CalendarViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemCalendarBinding mBinding;

        CalendarViewHolder(@NonNull ItemCalendarBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        void bind() {
            Context context = itemView.getContext();
            mBinding.tvUpdateAvl.setOnClickListener(v ->
                    context.startActivity(new Intent(context, SetAvailabilityActivity.class)));
            mBinding.customCalendar.setDateSelectedInterface(mListener);
            mBinding.customCalendar.setHiredListData(mCalendarJobListData);
            mBinding.customCalendar.isFullTimeJob(mIsFullTime);
        }
    }

    class MyHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {

        private ItemJobListBinding mBinding;

        MyHolder(@NonNull ItemJobListBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bind(@NonNull HiredJobs data) {
            Context context = itemView.getContext();

            itemView.setTag(getAdapterPosition());
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            mBinding.tvJobName.setText(data.getJobtitleName());
            mBinding.cbJobSelection.setVisibility(View.GONE);
            mBinding.tvJobDocDistance.setVisibility(View.GONE);
            mBinding.tvHwaName.setVisibility(View.GONE);
            mBinding.tvHwaVal.setVisibility(View.GONE);
            if (data.getJobType() == Constants.JOBTYPE.PART_TIME.getValue()) {
                mBinding.tvJobType.setText(context.getString(R.string.txt_part_time));
                mBinding.tvJobType.setBackgroundResource(R.drawable.job_type_background_part_time);

                ArrayList<String> partTimeDaysArray = new ArrayList<>();
                if (data.getIsMonday() == 1) {
                    partTimeDaysArray.add(context.getString(R.string.txt_monday));
                }
                if (data.getIsTuesday() == 1) {
                    partTimeDaysArray.add(context.getString(R.string.txt_tuesday));
                }
                if (data.getIsWednesday() == 1) {
                    partTimeDaysArray.add(context.getString(R.string.txt_wednesday));
                }
                if (data.getIsThursday() == 1) {
                    partTimeDaysArray.add(context.getString(R.string.txt_thursday));
                }
                if (data.getIsFriday() == 1) {
                    partTimeDaysArray.add(context.getString(R.string.txt_friday));
                }
                if (data.getIsSaturday() == 1) {
                    partTimeDaysArray.add(context.getString(R.string.txt_saturday));
                }
                if (data.getIsSunday() == 1) {
                    partTimeDaysArray.add(context.getString(R.string.txt_sunday));
                }
                String partTimeDays = TextUtils.join(", ", partTimeDaysArray);
                mBinding.tvJobDate.setVisibility(View.VISIBLE);
                mBinding.tvJobDate.setText(partTimeDays);

                final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.END_OF, mBinding.tvJobType.getId());
                params.addRule(RelativeLayout.START_OF, mBinding.tvJobDocDistance.getId());
                params.setMargins(Utils.dpToPx(context, 12), 0, Utils.dpToPx(context, 10), 0);

                mBinding.tvJobDate.postDelayed(() -> {
                    if (mBinding.tvJobDate.getLineCount() == 1) {
                        params.addRule(RelativeLayout.ALIGN_BOTTOM, mBinding.tvJobType.getId());
                        mBinding.tvJobDate.setLayoutParams(params);
                    } else {
                        params.addRule(RelativeLayout.ALIGN_BASELINE, mBinding.tvJobType.getId());
                        mBinding.tvJobDate.setLayoutParams(params);
                        mBinding.tvJobDate.setPadding(0, 4, 0, 0);
                    }
                }, 100);
                mBinding.tvJobDate.setEllipsize(TextUtils.TruncateAt.END);
            } else if (data.getJobType() == Constants.JOBTYPE.FULL_TIME.getValue()) {
                mBinding.tvJobType.setBackgroundResource(R.drawable.job_type_background_full_time);
                mBinding.tvJobDate.setVisibility(View.GONE);
                mBinding.tvJobType.setText(context.getString(R.string.txt_full_time));
            } else if (data.getJobType() == Constants.JOBTYPE.TEMPORARY.getValue()) {
                mBinding.tvJobType.setBackgroundResource(R.drawable.job_type_background_temporary);
                mBinding.tvJobDate.setVisibility(View.GONE);
                mBinding.tvJobType.setText(context.getString(R.string.txt_temporary));
                mBinding.tvHwaName.setVisibility(View.VISIBLE);
                mBinding.tvHwaVal.setVisibility(View.VISIBLE);
                mBinding.tvHwaVal.setText(StringUtils.getPayRate(data.getPayRate()));
            }
            mBinding.tvJobDocAddress.setText(data.getAddress());
            if (data.getDays() == 0) {
                mBinding.tvJobDocTime.setText(context.getString(R.string.text_todays));
            } else {
                String endMessage = data.getDays() > 1 ? context.getString(R.string.txt_days_ago) : context.getString(R.string.txt_day_ago);
                mBinding.tvJobDocTime.setText(String.valueOf(data.getDays()).concat(" ").concat(endMessage));
            }
            mBinding.tvJobDocName.setText(data.getOfficeName());
        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            switch (v.getId()) {
                case R.id.lay_item_job_list:
                    int jobID = mJobListData.get((int) v.getTag() - CALENDAR_ITEM).getId();
                    context.startActivity(new Intent(context, JobDetailActivity.class)
                            .putExtra(Constants.EXTRA_JOB_DETAIL_ID, jobID)
                            .putExtra(Constants.INTENT_KEY.FROM_WHERE, true));
                    break;
                default:
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            Context context = itemView.getContext();
            final int position = (int) v.getTag() - CALENDAR_ITEM;
            String jobDate = mJobListData.get(position).getTempDates();
            String getCurrentDate = mJobListData.get(position).getCurrentDate();
            if (jobDate.compareTo(getCurrentDate) > 0) {
                Alert.createYesNoAlert(context,
                        context.getString(R.string.ok),
                        context.getString(R.string.cancel),
                        context.getString(R.string.txt_alert_title),
                        context.getString(R.string.alert_cancel_job),
                        new Alert.OnAlertClickListener() {

                            @Override
                            public void onPositive(DialogInterface dialog) {
                                CancelReasonDialogFragment dialogFragment = CancelReasonDialogFragment.newInstance();
                                Bundle bundle = new Bundle();
                                bundle.putInt(Constants.EXTRA_JOB_ID, mJobListData.get(position).getId());
                                dialogFragment.setArguments(bundle);
                                dialogFragment.show(((BaseActivity) context).getSupportFragmentManager(), null);
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {
                                dialog.dismiss();
                            }
                        });
            }
            return false;
        }

    }
}
