package com.appster.dentamatch.adapters;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemSkillBinding;
import com.appster.dentamatch.model.ParentSkill;
import com.appster.dentamatch.model.SubSkill;
import com.appster.dentamatch.ui.profile.workexperience.SubSkillsActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.widget.CustomEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 12/01/17.
 */

public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.MyViewHolder> {

    private static final String TAG = "SkillsAdapter";
    private List<ParentSkill> mSkillList;
    private ItemSkillBinding mBinder;
    private Context mContext;
    private int windowWidth;

    public SkillsAdapter(List<ParentSkill> skillList, Context context) {
        this.mSkillList = skillList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mBinder = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_skill, parent, false));

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;

        LogUtils.LOGD(TAG, "WIDTH " + windowWidth);

        return new MyViewHolder(mBinder.getRoot());
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ParentSkill skill = mSkillList.get(position);

//        LogUtils.LOGD("SkillsAdapt", "Skill "+ skill.getSkillName());
        holder.tvSkillName.setText(skill.getSkillName());

//        holder.layoutSkills.measure(0, 0);

//        LogUtils.LOGD(TAG, "Skills layout " + holder.layoutSkills.getMeasuredWidth());

        if (skill.getSkillName().equals("Other")) {
            holder.etOther.setVisibility(View.VISIBLE);
            holder.ivArrow.setVisibility(View.GONE);

        } else {
            holder.layoutSkills.setOnClickListener(new View.OnClickListener() {

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

//        if (position == 0) {

//            setSkillsBricks(holder.layoutBricks, mSkillList.get(position).getSubSkills());
            setSkillsBricks(holder.layoutSkills, holder.layoutSkillsInner, mSkillList.get(position).getSubSkills());

//        }
    }

    private void setSkillsBricks(RelativeLayout layoutSkills, RelativeLayout layoutSkillsInner, ArrayList<SubSkill> listSkills) {

        RelativeLayout layoutBricks = new RelativeLayout(mContext);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(10);
        params.setMarginEnd(20);

        layoutSkills.addView(layoutBricks, params);
        params.addRule(RelativeLayout.BELOW, layoutSkillsInner.getId());

        int prevId = 0, currentId = 0, upperId = 0;
        int width = 0;

//        layout.setVisibility(View.VISIBLE);
//        layout.measure(0, 0);
        layoutBricks.measure(0, 0);
//        int totalWidth = layoutBricks.getMeasuredWidth();


        for (int i = 0; i < listSkills.size(); i++) {

//            if (i > 3) break;

            prevId = currentId;

            currentId = View.generateViewId();

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMarginStart(10);
//            layoutParams.setMarginEnd(10);
            layoutParams.setMargins(10, 0, 10, 20);

            TextView textView = new TextView(mContext);
            textView.setId(currentId);
            textView.setSingleLine();
            textView.setEllipsize(TextUtils.TruncateAt.END);
            textView.setBackgroundResource(R.drawable.edit_text_selector);
            textView.setText(listSkills.get(i).getSkillName());
            textView.measure(0, 0);

            LogUtils.LOGD(TAG, windowWidth + " Width before " + width);

            width += textView.getMeasuredWidth();

            LogUtils.LOGD(TAG, "Width after " + width);

            layoutBricks.addView(textView, layoutParams);

//            if (i > 0) {
            if (width < windowWidth) {
                layoutParams.addRule(RelativeLayout.BELOW, upperId);
                layoutParams.addRule(RelativeLayout.RIGHT_OF, prevId);
            } else {
                layoutParams.addRule(RelativeLayout.BELOW, prevId);
                width = textView.getMeasuredWidth();
                upperId = prevId;
            }
//            }


//            LogUtils.LOGD(TAG, "Width (W, T) " + width + ", " + windowWidth);
        }
    }

    private int calculateRule(int totalWidth, int width) {
        int half = totalWidth / 2;

        if (width > totalWidth) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getItemCount() {
        return mSkillList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layoutSkills;
        RelativeLayout layoutSkillsInner;
        //        RelativeLayout layoutBricks;
        TextView tvSkillName;
        ImageView ivArrow;
        CustomEditText etOther;

        MyViewHolder(View view) {
            super(view);
            layoutSkills = mBinder.layoutSkillsTop;
            layoutSkillsInner = mBinder.layoutSkillsInner;
//            layoutBricks = mBinder.layoutSkillBricks;
            tvSkillName = mBinder.tvSkillName;
            ivArrow = mBinder.ivRightArrow;
            etOther = mBinder.etOther;
        }
    }
}

