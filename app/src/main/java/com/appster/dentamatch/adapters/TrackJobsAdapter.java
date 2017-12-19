package com.appster.dentamatch.adapters;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemTrackJobListBinding;
import com.appster.dentamatch.eventbus.SaveUnSaveEvent;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.jobs.SaveUnSaveRequest;
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.searchjob.JobDetailActivity;
import com.appster.dentamatch.ui.tracks.CancelReasonDialogFragment;
import com.appster.dentamatch.ui.tracks.TrackJobsDataHelper;
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
    private final int JOB_SAVED = 1;
    private static final int ADDED_PART_TIME = 1;
    private static final int LINE_COUNT_ONE = 1;
    private static final int VIEW_DELAY_TIME = 100;
    private static final int DURATION_TIME_0 = 0;
    private static final int DURATION_TIME_1 = 1;


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
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(this);
            holder.tvName.setText(data.getJobTitleName());

            if (mIsSaved) {
                holder.cbSelect.setVisibility(View.VISIBLE);
                holder.cbSelect.setChecked(data.getIsSaved() == JOB_SAVED);
                holder.ivChat.setVisibility(View.GONE);
                holder.cbSelect.setTag(position);
                holder.cbSelect.setOnClickListener(this);
                holder.itemView.setOnLongClickListener(null);

            } else if (mIsApplied) {
                holder.cbSelect.setVisibility(View.INVISIBLE);
                holder.ivChat.setVisibility(View.GONE);
                holder.cbSelect.setTag(null);
                holder.cbSelect.setOnClickListener(null);
                holder.itemView.setTag(position);
                holder.itemView.setOnLongClickListener(this);

            } else {
                holder.cbSelect.setVisibility(View.INVISIBLE);
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
                        mContext.getResources().getInteger(R.integer.margin_0));

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
                                    mContext.getResources().getInteger(R.integer.padding_4),
                                    mContext.getResources().getInteger(R.integer.padding_0),
                                    mContext.getResources().getInteger(R.integer.padding_0));
                        }
                    }
                }, VIEW_DELAY_TIME);


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

           // holder.tvDistance.setText(String.format(Locale.getDefault(), "%.1f", data.getDistance()).concat(mContext.getString(R.string.txt_miles)));
            holder.tvDistance.setText(String.format(Locale.getDefault(), "%.2f", data.getPercentaSkillsMatch()).concat("%"));


            holder.tvDocName.setText(data.getOfficeName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int jobID = mJobListData.get((int) view.getTag()).getId();

                    mContext.startActivity(new Intent(mContext, JobDetailActivity.class)
                            .putExtra(Constants.EXTRA_JOB_DETAIL_ID, jobID)
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
                }
            });
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
            case R.id.cb_job_selection:
                final int position = (int) v.getTag();
                Alert.createYesNoAlert(mContext, mContext.getString(R.string.txt_ok),
                        mContext.getString(R.string.txt_cancel),
                        mContext.getString(R.string.txt_alert_title),
                        mContext.getString(R.string.msg_unsave_warning),
                        new Alert.OnAlertClickListener() {
                            @Override
                            public void onPositive(DialogInterface dialog) {
                                unSaveJob(mJobListData.get(position).getId(), position);
                            }

                            @Override
                            public void onNegative(DialogInterface dialog) {
                        /*
                          change the star unCheck by notifying the item.
                         */
                                notifyItemChanged(position);
                                dialog.dismiss();
                            }
                        });

            default:
                break;
        }

    }

    public void unSaveJob(final int JobID, final int position) {
        SaveUnSaveRequest request = new SaveUnSaveRequest();
        request.setJobId(JobID);
        request.setStatus(0);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        ((BaseActivity) mContext).processToShowDialog();
        webServices.saveUnSaveJob(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) mContext) {
            @Override
            public void onSuccess(BaseResponse response) {
                try {
                    ((BaseActivity) mContext).showToast(response.getMessage());

                    if (response.getStatus() == 1) {
                        mJobListData.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mJobListData.size());

                    /*
                      Update the search job screens for un-save events.
                     */
                        EventBus.getDefault().post(new SaveUnSaveEvent(JobID, 0));

                    /*
                      Notify the helper class to update the data from the server.
                     */
                        TrackJobsDataHelper.getInstance().updateSavedData();
                    } else {
                        notifyItemChanged(position);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    notifyItemChanged(position);
                }

            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public boolean onLongClick(View v) {
        final int position = (int) v.getTag();
        Alert.createYesNoAlert(mContext,
                mContext.getString(R.string.txt_ok),
                mContext.getString(R.string.txt_cancel),
                mContext.getString(R.string.txt_alert_title),
                mContext.getString(R.string.alert_cancel_job),
                new Alert.OnAlertClickListener() {
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

    public void cancelJob(int JobID) {
        try {
            for (int i = 0; i < mJobListData.size(); i++) {

                if (mJobListData.get(i).getId() == JobID) {
                    mJobListData.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, mJobListData.size());
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
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
