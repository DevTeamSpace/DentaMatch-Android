/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.adapters;

import android.support.v7.widget.RecyclerView;

import com.appster.dentamatch.databinding.ItemStateListBinding;

/**
 * Created by atul on 13/11/18.
 * To inject activity reference.
 */
public class StateVH extends RecyclerView.ViewHolder {

    private ItemStateListBinding mStateListBinding;

    StateVH(ItemStateListBinding stateListBinding) {
        super(stateListBinding.getRoot());
        mStateListBinding = stateListBinding;
    }

    public ItemStateListBinding getItemListBinding() {
        return mStateListBinding;
    }
}
