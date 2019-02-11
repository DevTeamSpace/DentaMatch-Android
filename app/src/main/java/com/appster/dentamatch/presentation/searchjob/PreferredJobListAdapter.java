/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.searchjob;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.interfaces.PreferredJobListSelected;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationData;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;

import java.util.ArrayList;

/**
 * Created by virender on 27/01/17.
 * Preferred Job list data holder.
 */
class PreferredJobListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {
    private static final String TAG = LogUtils.makeLogTag(PreferenceUtil.class);
    private final ArrayList<PreferredJobLocationData> mAffiliationList = new ArrayList<>();
    private ArrayList<PreferredJobLocationData> mSelectedJobTitles = new ArrayList<>();
    private final PreferredJobListSelected mListener;

    PreferredJobListAdapter(PreferredJobListSelected listener) {
        mListener = listener;
    }

    void addList(ArrayList<PreferredJobLocationData> list) {
        mAffiliationList.clear();
        mAffiliationList.addAll(list);
        notifyDataSetChanged();
    }

    void setSelectedListItems(ArrayList<PreferredJobLocationData> list) {
        if (mAffiliationList.size() > 0) {
            for (PreferredJobLocationData myList : mAffiliationList) {

                // try {
                for (PreferredJobLocationData selectedList : list) {
                    if (myList.getId() == selectedList.getId()) {
                        myList.setSelected(true);
                        break;
                    }
                }
                /*} catch (Exception e) {

                }*/
            }

        }
        mSelectedJobTitles = list;
        notifyDataSetChanged();
    }

    public ArrayList<PreferredJobLocationData> getList() {
        return mAffiliationList;
    }


    @Override
    public int getItemCount() {
        return mAffiliationList.size();
    }


    private PreferredJobLocationData getItem(int position) {
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
            final PreferredJobLocationData currentItem = getItem(position);
            if (currentItem != null) {
                itemHolder.tvType.setText(currentItem.getPreferredLocationName());
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
            final PreferredJobLocationData currentItem = getItem(position);
            if (currentItem != null) {
                if (currentItem.isSelected()) {

                    for (PreferredJobLocationData i : mSelectedJobTitles) {
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
            mListener.onPrefJobSelected(mSelectedJobTitles);
            notifyItemChanged(position);
        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        final TextView tvType;
        final CheckBox cbCheckBox;
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