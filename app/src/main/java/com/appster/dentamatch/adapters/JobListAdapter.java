package com.appster.dentamatch.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemJobListBinding;
import com.appster.dentamatch.ui.jobs.JobDetailActivity;

/**
 * Created by Appster on 24/01/17.
 */

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.MyHolder> implements View.OnClickListener {
    private ItemJobListBinding mBinding;
    private Context mContext;

    public JobListAdapter(Context context){
        mContext = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_job_list, parent, false);
        return new MyHolder(mBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.itemView.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public void onClick(View v) {
        mContext.startActivity(new Intent(mContext, JobDetailActivity.class));
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
