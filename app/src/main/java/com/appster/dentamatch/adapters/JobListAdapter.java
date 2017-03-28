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
 */

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.MyHolder> implements View.OnClickListener {
    private ItemJobListBinding mBinding;
    private Context mContext;
    private ArrayList<SearchJobModel> mJobListData;


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
            holder.cbSelect.setChecked(data.getIsSaved() == 1);

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
                            params.addRule(RelativeLayout.ALIGN_TOP, holder.tvJobType.getId());
                            holder.tvDate.setLayoutParams(params);
                            holder.tvDate.setPadding(0, 0, 0, 0);
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
            if (data.getDays() == 0) {
                holder.tvDuration.setText(mContext.getString(R.string.text_todays));

            } else {
                String endMessage = data.getDays() > 1 ? mContext.getString(R.string.txt_days_ago) : mContext.getString(R.string.txt_day_ago);
                holder.tvDuration.setText(String.valueOf(data.getDays()).concat(" ").concat(endMessage));
            }
//            String endMessage = data.getDays() > 1 ? mContext.getString(R.string.txt_days_ago) : mContext.getString(R.string.txt_day_ago);
//            holder.tvDuration.setText(String.valueOf(data.getDays()).concat(" ").concat(endMessage));
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

    private void saveUnSaveJob(int JobID, final int status, final int position) {
        SaveUnSaveRequest request = new SaveUnSaveRequest();
        request.setJobId(JobID);
        request.setStatus(status);
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        ((BaseActivity) mContext).processToShowDialog("","",null);
        webServices.saveUnSaveJob(request).enqueue(new BaseCallback<BaseResponse>((BaseActivity) mContext) {
            @Override
            public void onSuccess(BaseResponse response) {
                ((BaseActivity) mContext).showToast(response.getMessage());

                if (response.getStatus() == 1) {
                    mJobListData.get(position).setIsSaved(status);
                    notifyItemChanged(position);
                    SearchJobDataHelper.getInstance().notifyItemsChanged(mJobListData.get(position));
                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cb_job_selection:
                final int position = (int) v.getTag();
                final int status = mJobListData.get(position).getIsSaved() == 1 ? 0 : 1;
                if (status == 0) {
                    Alert.createYesNoAlert(mContext, mContext.getString(R.string.txt_ok), mContext.getString(R.string.txt_cancel), mContext.getString(R.string.txt_alert_title), mContext.getString(R.string.msg_unsave_warning), new Alert.OnAlertClickListener() {
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
                mContext.startActivity(new Intent(mContext, JobDetailActivity.class)
                        .putExtra(Constants.EXTRA_JOB_DETAIL_ID, jobID));
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
        TextView tvName, tvJobType, tvDate, tvDocName, tvDocAddress, tvDuration, tvDistance;
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
        }
    }
}
