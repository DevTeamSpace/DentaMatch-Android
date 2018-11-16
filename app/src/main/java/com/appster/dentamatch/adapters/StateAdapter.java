/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.adapters;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemStateListBinding;
import com.appster.dentamatch.network.response.profile.StateList;

import java.util.ArrayList;

/**
 * Created by atul on 13/11/18.
 * To inject activity reference.
 */
public class StateAdapter extends RecyclerView.Adapter<StateVH> {

    private final ISettingCallback mCallback;
    private ArrayList<StateList> mStateLists;

    public StateAdapter(ArrayList<StateList> stateLists, ISettingCallback callback) {
        mCallback = callback;
        mStateLists = stateLists;
    }

    @Override
    public StateVH onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemStateListBinding stateListBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_state_list, parent, false);
        return new StateVH(stateListBinding);
    }

    @Override
    public void onBindViewHolder(final StateVH holder, final int position) {
        final StateList stateList = mStateLists.get(holder.getAdapterPosition());
        holder.getItemListBinding().tvStateName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.onOptionSelected(stateList.getStateName());
            }
        });

        holder.getItemListBinding().tvStateName.setText(stateList.getStateName());
        if (stateList.isSelected())
            holder.getItemListBinding().tvStateName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.correct, 0);
        else
            holder.getItemListBinding().tvStateName.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    @Override
    public int getItemCount() {
        if (mStateLists == null || mStateLists.isEmpty())
            return 0;
        return mStateLists.size();
    }


    public void updateList(ArrayList<StateList> stateList) {
        mStateLists = stateList;
        notifyDataSetChanged();
    }

    public interface ISettingCallback {
        void onOptionSelected(String stateName);
    }
}
