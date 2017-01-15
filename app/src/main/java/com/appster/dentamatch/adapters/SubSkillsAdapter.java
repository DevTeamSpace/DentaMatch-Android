package com.appster.dentamatch.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemSkillBinding;
import com.appster.dentamatch.databinding.ItemSubSkillBinding;
import com.appster.dentamatch.model.ParentSkill;
import com.appster.dentamatch.model.SubSkill;
import com.appster.dentamatch.widget.CustomEditText;

import java.util.List;

/**
 * Created by ram on 12/01/17.
 */

public class SubSkillsAdapter extends RecyclerView.Adapter<SubSkillsAdapter.MyViewHolder> {

    private List<SubSkill> mSkillList;
    private ItemSubSkillBinding mBinder;
    private Context mContext;

    public SubSkillsAdapter(List<SubSkill> skillList, Context context) {
        this.mSkillList = skillList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mBinder = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_skill, parent, false));

        return new MyViewHolder(mBinder.getRoot());
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final SubSkill skill = mSkillList.get(position);

        holder.tvSkillName.setText(skill.getSkillName());

        if (skill.getSkillName().equals("Other")) {
            mBinder.etOther.setVisibility(View.VISIBLE);
            mBinder.cbSelected.setVisibility(View.GONE);
        } else {
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = mSkillList.get(position).getIsSelected() == 1;
                    holder.cbSelected.setChecked(!checked);
                    mSkillList.get(position).setIsSelected(!checked ? 1 : 0);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mSkillList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        TextView tvSkillName;
        CheckBox cbSelected;
        CustomEditText etOther;

        MyViewHolder(View view) {
            super(view);
            layout = mBinder.layoutTop;
            tvSkillName = mBinder.tvSkillName;
            cbSelected = mBinder.cbSelected;
            etOther = mBinder.etOther;
        }
    }
}

