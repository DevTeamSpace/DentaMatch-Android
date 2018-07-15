/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.searchjob;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.interfaces.JobTitleSelected;
import com.appster.dentamatch.model.JobTitleListModel;
import com.appster.dentamatch.util.LogUtils;

import java.util.ArrayList;

/**
 * Created by virender on 27/01/17.
 * Adapter to handle job title.
 */
class JobTitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final String TAG = LogUtils.makeLogTag(JobTitleAdapter.class);
    private final ArrayList<JobTitleListModel> mAffiliationList = new ArrayList<>();
    private ArrayList<JobTitleListModel> mSelectedJobTitles = new ArrayList<>();
    private final JobTitleSelected mListener;

    JobTitleAdapter(JobTitleSelected listener) {
        mListener = listener;
    }

    void addList(ArrayList<JobTitleListModel> list) {
        mAffiliationList.clear();
        mAffiliationList.addAll(list);
        notifyDataSetChanged();
    }

    void setSelectedListItems(ArrayList<JobTitleListModel> List) {
        if (mAffiliationList.size() > 0) {
            for (JobTitleListModel myList : mAffiliationList) {

                for (JobTitleListModel selectedList : List) {

                    if (myList.getId() == selectedList.getId()) {
                        myList.setSelected(true);
                        break;
                    }
                }
            }
        }
        mSelectedJobTitles = List;
        notifyDataSetChanged();
    }

    public ArrayList<JobTitleListModel> getList() {
        return mAffiliationList;
    }


    @Override
    public int getItemCount() {
        return mAffiliationList.size();
    }


    private JobTitleListModel getItem(int position) {
        return mAffiliationList.size() > 0 ? mAffiliationList.get(position) : null;
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
            final JobTitleListModel currentItem = getItem(position);
            if (currentItem != null) {
                itemHolder.tvType.setText(currentItem.getJobTitle());
                itemHolder.cbCheckBox.setTag(position);
                itemHolder.cbCheckBox.setOnClickListener(this);
                itemHolder.cbCheckBox.setChecked(currentItem.isSelected());
            }
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }

    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        try {
            final JobTitleListModel currentItem = getItem(position);
            if (currentItem != null) {
                if (currentItem.isSelected()) {

                    for (JobTitleListModel i : mSelectedJobTitles) {
                        if (i.getId() == currentItem.getId()) {
                            mSelectedJobTitles.remove(i);
                            break;
                        }
                    }
                    currentItem.setSelected(false);

                } else {
                    mSelectedJobTitles.add(currentItem);
                    currentItem.setSelected(true);
                }
            }

            mListener.onJobTitleSelected(mSelectedJobTitles);
            notifyItemChanged(position);
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvType;
        private final CheckBox cbCheckBox;
        private final EditText etOther;
        private final View viewUnderLine;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvType = itemLayoutView.findViewById(R.id.tv_affiliation_type);
            cbCheckBox = itemLayoutView.findViewById(R.id.cb_check_box);
            etOther = itemLayoutView.findViewById(R.id.et_other);
            viewUnderLine = itemLayoutView.findViewById(R.id.view_line);

        }
    }
}