package com.appster.dentamatch.adapters;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemSkillBinding;
import com.appster.dentamatch.databinding.LayoutProfileHeaderBinding;
import com.appster.dentamatch.interfaces.EditTextSelected;
import com.appster.dentamatch.model.ParentSkill;
import com.appster.dentamatch.model.SubSkill;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.widget.CustomEditText;
import com.squareup.picasso.Picasso;
import com.wefika.flowlayout.FlowLayout;

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
    private boolean mIsFromEditProfile;
    private int windowWidth;
    private Activity activity;
    private OnSkillClick mListener;
    public EditText etOtherTemp;
    private EditTextSelected mOthersSelectedListener;

    public SkillsAdapter(List<ParentSkill> skillList, Context context, OnSkillClick listener, EditTextSelected othersSelectedListener, boolean isFromEditProfile) {
        this.mSkillList = skillList;
        this.mContext = context;
        mListener = listener;
        mOthersSelectedListener = othersSelectedListener;
        mIsFromEditProfile = isFromEditProfile;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            mBinder = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_skill, parent, false));

            return new ViewHolderItem(mBinder.getRoot());
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            mBinderHeader = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_profile_header, parent, false));

            return new ViewHolderHeader(mBinderHeader.getRoot());
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {


        if (holder1 instanceof ViewHolderHeader) {
            if (mIsFromEditProfile) {
                mBinderHeader.tvTitleScreen.setVisibility(View.VISIBLE);
                mBinderHeader.tvTitleScreen.setText(mContext.getString(R.string.header_skills_exp));
                mBinderHeader.progressLayout.setVisibility(View.GONE);
                mBinderHeader.tvTitle.setVisibility(View.GONE);
                mBinderHeader.tvDescription.setVisibility(View.GONE);


            } else {
                if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
                    LogUtils.LOGD("pabd", "path is--=" + PreferenceUtil.getProfileImagePath());
                    Picasso.with(mContext).load(PreferenceUtil.getProfileImagePath()).centerCrop().
                            resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).
                            placeholder(R.drawable.profile_pic_placeholder).into(mBinderHeader.ivProfileIcon);
                }

                mBinderHeader.progressBar.setProgress(80);
                mBinderHeader.tvTitle.setText(mContext.getString(R.string.header_skills_exp));
                mBinderHeader.tvDescription.setText(mContext.getString(R.string.lorem_ipsum));
            }
        } else {
            final ViewHolderItem holder = (ViewHolderItem) holder1;
            final ParentSkill skill = mSkillList.get(position - 1);

//        LogUtils.LOGD("SkillsAdapt", "Skill "+ skill.getSkillName());
            holder.tvSkillName.setText(skill.getSkillName());

            holder.layoutSkills.setTag(position - 1);

            if (skill.getSkillName().equalsIgnoreCase(Constants.OTHERS)) {
                holder.layoutSkills.setOnClickListener(null);
                holder.etOther.setVisibility(View.VISIBLE);
                holder.ivArrow.setVisibility(View.GONE);
                holder.etOther.setText(skill.getOtherSkill());

                holder.flowLayout.setVisibility(View.GONE);
                holder.etOther.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        v.requestFocus();
                        if (hasFocus) {
                            mOthersSelectedListener.onEditTextSelected(position);
                        }

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
                        mSkillList.get(position - 1).setOtherSkill(s.toString());
                    }
                });


            } else {
                holder.etOther.setVisibility(View.GONE);
                holder.ivArrow.setVisibility(View.VISIBLE);

                holder.layoutSkills.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        mListener.onItemSelected(skill.getSubSkills(), (Integer) holder.layoutSkills.getTag());
                    }
                });

                holder.flowLayout.setVisibility(View.VISIBLE);
                holder.flowLayout.removeAllViews();

                setSkillsBricks(holder.flowLayout, mSkillList.get(position - 1).getSubSkills());
            }
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

    private void setSkillsBricks(FlowLayout flowLayout, ArrayList<SubSkill> listSkills) {

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMarginStart(10);
        params.setMarginEnd(20);

        for (int i = 0; i < listSkills.size(); i++) {
            if (listSkills.get(i).getIsSelected() == 1) {

                FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,
                        FlowLayout.LayoutParams.WRAP_CONTENT);

                layoutParams.setMargins(10, 0, 10, 20);

                String text = listSkills.get(i).getSkillName();

                if (text.equalsIgnoreCase(Constants.OTHERS)) {
                    text = Constants.OTHERS;
                }

                TextView textView = new TextView(mContext);
                textView.setSingleLine();
                textView.setEllipsize(TextUtils.TruncateAt.END);
                textView.setBackgroundResource(R.drawable.edit_text_selector);
                textView.setText(text);

                flowLayout.addView(textView, layoutParams);
            }
        }
    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        ViewHolderHeader(View view) {
            super(view);
        }
    }

    private class ViewHolderItem extends RecyclerView.ViewHolder {
        //        RelativeLayout layoutSkills;
        LinearLayout layoutSkills;
        RelativeLayout layoutSkillsInner;
        FlowLayout flowLayout;
        //        RelativeLayout layoutBricks;
        TextView tvSkillName;
        ImageView ivArrow;
        CustomEditText etOther;

        ViewHolderItem(View view) {
            super(view);
            layoutSkills = mBinder.layoutSkillsTop;
            layoutSkillsInner = mBinder.layoutSkillsInner;
            flowLayout = mBinder.flowLayoutChips;
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

