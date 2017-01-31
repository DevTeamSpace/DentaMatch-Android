package com.appster.dentamatch.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemSubSkillBinding;
import com.appster.dentamatch.model.SubSkill;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;

import java.util.List;

/**
 * Created by ram on 12/01/17.
 */

public class SubSkillsAdapter extends RecyclerView.Adapter<SubSkillsAdapter.MyViewHolder> {
    private static String TAG = "SubSkillAdapter";
    private List<SubSkill> mSkillList;
    private ItemSubSkillBinding mBinder;
    private Context mContext;

    public SubSkillsAdapter(List<SubSkill> skillList, Context context) {
        this.mSkillList = skillList;
        this.mContext = context;
    }
    public  List<SubSkill> getList(){
        return mSkillList;
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

        boolean checked = mSkillList.get(position).getIsSelected() == 1;

        if (checked) {
            holder.ivSelected.setBackgroundResource(R.drawable.ic_check_selected);
            if (mSkillList.get(position).getSkillName().equalsIgnoreCase(Constants.OTHERS)) {
                mBinder.etOther.setVisibility(View.VISIBLE);
                mBinder.etOther.setText(mSkillList.get(position).getOtherText());
            }
        } else {
            holder.ivSelected.setBackgroundResource(R.drawable.ic_check_unselected);
            if (mSkillList.get(position).getSkillName().equalsIgnoreCase(Constants.OTHERS)) {
                mBinder.etOther.setVisibility(View.GONE);
            }
        }

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = mSkillList.get(position).getIsSelected() == 1;
                LogUtils.LOGD(TAG, "checked " + checked);

                if (!checked) {
                    holder.ivSelected.setBackgroundResource(R.drawable.ic_check_selected);
                } else {
                    holder.ivSelected.setBackgroundResource(R.drawable.ic_check_unselected);
                }

                if (mSkillList.get(position).getSkillName().equalsIgnoreCase(Constants.OTHERS)) {
                    if (!checked) {
                        mBinder.etOther.setVisibility(View.VISIBLE);
                    } else {
                        mBinder.etOther.setVisibility(View.GONE);
                        ((BaseActivity)mContext).hideKeyboard();
                        mBinder.etOther.setText("");
                    }
                }

                mSkillList.get(position).setIsSelected(!checked ? 1 : 0);
            }
        });

        holder.etOther.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mSkillList.get(position).setOtherText(s.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSkillList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout layout;
        TextView tvSkillName;
        ImageView ivSelected;
        EditText etOther;

        MyViewHolder(View view) {
            super(view);
            layout = mBinder.layoutTop;
            tvSkillName = mBinder.tvSkillName;
            ivSelected = mBinder.ivSelected;
            etOther = mBinder.etOther;
        }
    }
}

