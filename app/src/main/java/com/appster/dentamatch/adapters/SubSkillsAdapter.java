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
import com.appster.dentamatch.model.SubSkillModel;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;

import java.util.List;

/**
 * Created by ram on 12/01/17.
 */

public class SubSkillsAdapter extends RecyclerView.Adapter<SubSkillsAdapter.MyViewHolder> {
    private static String TAG = "SubSkillAdapter";
    private int SKILL_SELECTED = 1;
    private int SKILL_CHECKED = 1;
    private int SKILL_UNCHECKED = 0;

    private List<SubSkillModel> mSkillList;
    private ItemSubSkillBinding mBinder;
    private Context mContext;

    public SubSkillsAdapter(List<SubSkillModel> skillList, Context context) {
        this.mSkillList = skillList;
        this.mContext = context;
        sortListForOther();
    }

    private void sortListForOther() {
        SubSkillModel otherModel = null;

        if(mSkillList != null){

            for (int i = 0; i < mSkillList.size(); i++){

                if(mSkillList.get(i).getSkillName().equalsIgnoreCase(Constants.OTHERS) && i != mSkillList.size() - 1){
                    otherModel = mSkillList.get(i);
                    mSkillList.remove(i);
                    break;
                }
            }

            if(otherModel != null) {
                mSkillList.add(mSkillList.size(), otherModel);
            }
        }
    }

    public List<SubSkillModel> getList() {
        return mSkillList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mBinder = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_sub_skill, parent, false));

        return new MyViewHolder(mBinder.getRoot());
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,  int position) {
        final SubSkillModel skill = mSkillList.get(position);
        final int refPosition = position;

        if(skill != null) {
            holder.tvSkillName.setText(skill.getSkillName());
            holder.etOther.setVisibility(View.GONE);

            if (mSkillList.get(position).getIsSelected() == SKILL_SELECTED) {
                holder.ivSelected.setBackgroundResource(R.drawable.ic_check_selected);
            } else {
                holder.ivSelected.setBackgroundResource(R.drawable.ic_check_unselected);
            }

            if (mSkillList.get(position).getSkillName().equalsIgnoreCase(Constants.OTHERS)) {
                if (mSkillList.get(position).getIsSelected() == SKILL_SELECTED) {
                    holder.etOther.setVisibility(View.VISIBLE);
                    holder.etOther.setText(mSkillList.get(position).getOtherText());

                } else {
                    holder.etOther.setVisibility(View.GONE);

                }
            }

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = mSkillList.get(refPosition).getIsSelected() == SKILL_SELECTED;

                    if (!checked) {
                        holder.ivSelected.setBackgroundResource(R.drawable.ic_check_selected);
                    } else {
                        holder.ivSelected.setBackgroundResource(R.drawable.ic_check_unselected);
                    }

                    if (mSkillList.get(refPosition).getSkillName().equalsIgnoreCase(Constants.OTHERS)) {
                        if (!checked) {
                            holder.etOther.setVisibility(View.VISIBLE);

                        } else {
                            holder.etOther.setVisibility(View.GONE);
                            holder.etOther.setText("");
                            ((BaseActivity) mContext).hideKeyboard();
                        }
                    } else {
                        ((BaseActivity) mContext).hideKeyboard();

                    }

                    mSkillList.get(refPosition).setIsSelected(!checked ? SKILL_CHECKED : SKILL_UNCHECKED);
                    notifyDataSetChanged();
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
                    mSkillList.get(refPosition).setOtherText(s.toString());
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

