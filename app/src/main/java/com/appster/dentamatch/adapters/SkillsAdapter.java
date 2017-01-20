package com.appster.dentamatch.adapters;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemSkillBinding;
import com.appster.dentamatch.databinding.LayoutProfileHeaderBinding;
import com.appster.dentamatch.model.ParentSkill;
import com.appster.dentamatch.model.SubSkill;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.widget.CustomEditText;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 12/01/17.
 */

public class SkillsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SkillsAdapter";
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private ItemSkillBinding mBinder;
    private LayoutProfileHeaderBinding mBinderHeader;

    private List<ParentSkill> mSkillList;
    //    private ItemSkillBinding mBinder;
    private Context mContext;
    private int windowWidth;
    private Activity activity;
    private OnSkillClick mListener;
    public EditText etOtherTemp;

    public SkillsAdapter(List<ParentSkill> skillList, Context context, OnSkillClick listener) {
        this.mSkillList = skillList;
        this.mContext = context;
        mListener = listener;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        windowWidth = displaymetrics.widthPixels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            mBinder = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_skill, parent, false));

//            DisplayMetrics displaymetrics = new DisplayMetrics();
//            ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//            int height = displaymetrics.heightPixels;
//            windowWidth = displaymetrics.widthPixels;

            return new ViewHolderItem(mBinder.getRoot());
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            mBinderHeader = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_profile_header, parent, false));

            return new ViewHolderHeader(mBinderHeader.getRoot());
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");


//    mBinder = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
//            .inflate(R.layout.item_skill, parent, false));
//
//    DisplayMetrics displaymetrics = new DisplayMetrics();
//    ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
//    int height = displaymetrics.heightPixels;
//    windowWidth = displaymetrics.widthPixels;
//
////        LogUtils.LOGD(TAG, "WIDTH " + windowWidth);
//
//    return new MyViewHolder(mBinder.getRoot());
    }

    public List<ParentSkill> getmSkillList() {
        return mSkillList;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {

        if (holder1 instanceof ViewHolderHeader) {
            if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
                LogUtils.LOGD("pabd", "path is--=" + PreferenceUtil.getProfileImagePath());
                Picasso.with(mContext).load(PreferenceUtil.getProfileImagePath()).centerCrop().
                        resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).
                        placeholder(R.drawable.profile_pic_placeholder).into(mBinderHeader.ivProfileIcon);
            }

            mBinderHeader.progressBar.setProgress(80);
            mBinderHeader.tvTitle.setText(mContext.getString(R.string.header_skills_exp));
            mBinderHeader.tvDescription.setText(mContext.getString(R.string.lorem_ipsum));
        } else {
            final ViewHolderItem holder = (ViewHolderItem) holder1;
            final ParentSkill skill = mSkillList.get(position - 1);

//        LogUtils.LOGD("SkillsAdapt", "Skill "+ skill.getSkillName());
            holder.tvSkillName.setText(skill.getSkillName());
            holder.etOther.setText(skill.getOtherSkill());
            holder.layoutSkills.setTag(position - 1);

//        holder.layoutSkills.measure(0, 0);

//        LogUtils.LOGD(TAG, "Skills layout " + holder.layoutSkills.getMeasuredWidth());

            if (skill.getSkillName().equalsIgnoreCase(Constants.OTHERS)) {
                holder.etOther.setVisibility(View.VISIBLE);
                holder.ivArrow.setVisibility(View.GONE);
                etOtherTemp = holder.etOther;

                holder.etOther.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        mSkillList.get(position - 1).setOtherSkill(s.toString());
                    }
                });

            } else {
                holder.etOther.setVisibility(View.GONE);

                holder.layoutSkills.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

//                    Bundle bundle = new Bundle();
//                    bundle.putParcelableArrayList(Constants.BundleKey.SUB_SKILLS, skill.getSubSkills());
//
//                    Intent intent = new Intent(mContext, SubSkillsActivity.class);
//                    intent.putExtra(Constants.EXTRA_SUB_SKILLS, bundle);

//                    ((SkillsActivity) mContext).startActivityForResult(intent, 901);
                        mListener.onItemSelected(skill.getSubSkills(), (Integer) holder.layoutSkills.getTag());
                    }
                });

                setSkillsBricks(holder.layoutSkills, holder.layoutSkillsInner, mSkillList.get(position - 1).getSubSkills());
            }
        }
    }

    private void setChips() {
//        ChipsEditText chipsEditText = new ChipsEditText()
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

//            layoutBricks.removeAllViews();

        for (int i = 0; i < listSkills.size() - 1; i++) {


            if (listSkills.get(i).getIsSelected() == 1) {


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
        return mSkillList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        ViewHolderHeader(View view) {
            super(view);
        }
    }

    private class ViewHolderItem extends RecyclerView.ViewHolder {
        RelativeLayout layoutSkills;
        RelativeLayout layoutSkillsInner;
        //        RelativeLayout layoutBricks;
        TextView tvSkillName;
        ImageView ivArrow;
        CustomEditText etOther;

        ViewHolderItem(View view) {
            super(view);
            layoutSkills = mBinder.layoutSkillsTop;
            layoutSkillsInner = mBinder.layoutSkillsInner;
//            layoutBricks = mBinder.layoutSkillBricks;
            tvSkillName = mBinder.tvSkillName;
            ivArrow = mBinder.ivRightArrow;
            etOther = mBinder.etOther;
        }
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

    public interface OnSkillClick {
        public void onItemSelected(ArrayList<SubSkill> subSkillList, int position);
    }
}

