package com.appster.dentamatch.adapters;

import android.content.Context;
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
import com.appster.dentamatch.network.response.jobs.SearchJobModel;
import com.appster.dentamatch.ui.searchjob.JobDetailActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;

import java.util.ArrayList;
import java.util.Locale;

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

            if (data.getJobType() == Constants.JOBTYPE.PART_TIME.getValue()) {
                holder.tvJobType.setText("Part Time");
                holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_part_time);

                ArrayList<String> partTimeDaysArray = new ArrayList<>();
                if (data.getIsMonday() == 1) {
                    partTimeDaysArray.add("Monday");
                }

                if (data.getIsTuesday() == 1) {
                    partTimeDaysArray.add("Tuesday");
                }

                if (data.getIsWednesday() == 1) {
                    partTimeDaysArray.add("Wednesday");
                }

                if (data.getIsThursday() == 1) {
                    partTimeDaysArray.add("Thursday");
                }

                if (data.getIsFriday() == 1) {
                    partTimeDaysArray.add("Friday");
                }

                if (data.getIsSaturday() == 1) {
                    partTimeDaysArray.add("Saturday");
                }

                if (data.getIsSunday() == 1) {
                    partTimeDaysArray.add("Sunday");
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
                holder.tvJobType.setText("Full Time");

            } else if (data.getJobType() == Constants.JOBTYPE.TEMPORARY.getValue()) {
                holder.tvJobType.setBackgroundResource(R.drawable.job_type_background_temporary);
                holder.tvDate.setVisibility(View.GONE);
                holder.tvJobType.setText("Temporary");
            }

            holder.tvDocAddress.setText(data.getAddress());
            String endMessage = data.getDays() > 1 ? "DAYS AGO" : " DAY AGO";
            holder.tvDuration.setText(String.valueOf(data.getDays()).concat(" ").concat(endMessage));
            holder.tvDistance.setText(String.format(Locale.getDefault(), "%.1f", data.getDistance()).concat(" miles"));
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
        int jobID = (int) v.getTag();
        mContext.startActivity(new Intent(mContext, JobDetailActivity.class)
                .putExtra(Constants.EXTRA_JOB_DETAIL_ID, jobID));
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvJobType, tvDate, tvDocName, tvDocAddress, tvDuration, tvDistance;
        CheckBox cbSelect;

        public MyHolder(View itemView) {
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
