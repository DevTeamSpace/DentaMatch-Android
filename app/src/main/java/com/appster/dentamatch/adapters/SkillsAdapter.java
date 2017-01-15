package com.appster.dentamatch.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemSkillBinding;
import com.appster.dentamatch.model.ParentSkill;
import com.appster.dentamatch.model.Skill;
import com.appster.dentamatch.ui.profile.workexperience.SubSkillsActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.widget.CustomEditText;

import java.util.List;

/**
 * Created by ram on 12/01/17.
 */

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.MyViewHolder> {

    private List<ParentSkill> mSkillList;
    private ItemSkillBinding mBinder;
    private Context mContext;

    public SkillsAdapter(List<ParentSkill> skillList, Context context) {
        this.mSkillList = skillList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mBinder = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_skill, parent, false));

        return new MyViewHolder(mBinder.getRoot());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ParentSkill skill = mSkillList.get(position);

//        LogUtils.LOGD("SkillsAdapt", "Skill "+ skill.getSkillName());
        holder.tvSkillName.setText(skill.getSkillName());

        if(skill.getSkillName().equals("Other")) {
            holder.etOther.setVisibility(View.VISIBLE);
            holder.ivArrow.setVisibility(View.GONE);
        }else {
            holder.layout.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(Constants.BundleKey.SUB_SKILLS, skill.getSubSkills());

                    Intent intent = new Intent(mContext, SubSkillsActivity.class);
                    intent.putExtra(Constants.EXTRA_SUB_SKILLS, bundle);

                    mContext.startActivity(intent);
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
        ImageView ivArrow;
        CustomEditText etOther;

        MyViewHolder(View view) {
            super(view);
            layout=mBinder.layoutSkillsInner;
            tvSkillName = mBinder.tvSkillName;
            ivArrow = mBinder.ivRightArrow;
            etOther = mBinder.etOther;
        }
    }
}
