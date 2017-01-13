package com.appster.dentamatch.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemSkillBinding;
import com.appster.dentamatch.model.Skill;

import java.util.List;

/**
 * Created by ram on 12/01/17.
 */


public class SkillsAdapter extends RecyclerView.Adapter<SkillsAdapter.MyViewHolder> {

    private List<Skill> mSkillList;
    private ItemSkillBinding mBinder;
    private Context mContext;

    public SkillsAdapter(List<Skill> skillList, Context context) {
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
        final Skill skill = mSkillList.get(position);


    }

    @Override
    public int getItemCount() {
        return mSkillList.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSkillName;
        ImageView imArrow;

        MyViewHolder(View view) {
            super(view);

        }
    }
}

