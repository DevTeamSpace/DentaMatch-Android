package com.appster.dentamatch.ui.searchjob;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.network.response.affiliation.AffiliationData;
import com.appster.dentamatch.network.response.profile.JobTitleList;
import com.appster.dentamatch.network.response.profile.JobTitleResponseData;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by virender on 27/01/17.
 */
public class JobTitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<JobTitleList> mAffiliationList = new ArrayList<>();

    public JobTitleAdapter(Context context) {
        mContext = context;
    }

    public void addList(ArrayList<JobTitleList> list) {
        if (mAffiliationList != null) {
            mAffiliationList.clear();
        }
        mAffiliationList.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<JobTitleList> getList() {
        return mAffiliationList;
    }


    @Override
    public int getItemCount() {


        return mAffiliationList != null ? mAffiliationList.size() : 0;
    }


    private JobTitleList getItem(int position) {
        return mAffiliationList != null && mAffiliationList.size() > 0 ? mAffiliationList.get(position) : null;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_affiliation, parent, false);

        return new ViewHolder(rowView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            final ViewHolder itemHolder = (ViewHolder) holder;
            final JobTitleList currentItem = getItem(position);
            itemHolder.tvType.setText(currentItem.getJobTitle());
            itemHolder.ivCheckBox.setTag(position);
            if (currentItem.isSelected()) {
                itemHolder.ivCheckBox.setBackgroundResource(R.drawable.ic_check_fill);

            } else {
                itemHolder.ivCheckBox.setBackgroundResource(R.drawable.ic_check_empty);


            }


            itemHolder.ivCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentItem.isSelected()) {
                        currentItem.setSelected(false);
                        itemHolder.ivCheckBox.setBackgroundResource(R.drawable.ic_check_empty);
                    } else {
                        currentItem.setSelected(true);
                        itemHolder.ivCheckBox.setBackgroundResource(R.drawable.ic_check_fill);


                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvType;
        public ImageView ivCheckBox;
        private EditText etOther;
        private View viewUnderLine;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvType = (TextView) itemLayoutView.findViewById(R.id.tv_affiliation_type);
            ivCheckBox = (ImageView) itemLayoutView.findViewById(R.id.iv_check_box);
            etOther = (EditText) itemLayoutView.findViewById(R.id.et_other);
            viewUnderLine = (View) itemLayoutView.findViewById(R.id.view_line);

        }
    }


}