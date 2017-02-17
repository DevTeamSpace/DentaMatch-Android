package com.appster.dentamatch.ui.profile.affiliation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.model.LocationEvent;
import com.appster.dentamatch.network.request.affiliation.Affiliation;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by virender on 13/01/17.
 */

class AffiliationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_ITEM_PROFILE = 1;
    private final int TYPE_ITEM_DEFAULT = 2;
    private Context mContext;
    private ArrayList<Affiliation> mAffiliationList = new ArrayList<>();
    private EditText etOtherTemp;
    private boolean mIsFromEditProfile;


    AffiliationAdapter(Context context, boolean isFromEditProfile) {
        mContext = context;
        mIsFromEditProfile = isFromEditProfile;
    }

    void addList(ArrayList<Affiliation> list) {
        if (mAffiliationList != null) {
            mAffiliationList.clear();
        }
        mAffiliationList.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<Affiliation> getList() {
        return mAffiliationList;
    }

    public String getOtherData() {
        if (etOtherTemp != null) {
            return etOtherTemp.getText().toString().trim();
        }
        return null;
    }

    private Affiliation getItem(int position) {
        return mAffiliationList != null && mAffiliationList.size() > 0 ? mAffiliationList.get(position - 1) : null;
    }

    private boolean isPositionOther(int position) {
        return position == mAffiliationList.size();
    }

    private boolean isPositionProfileHeader(int position) {
        return position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM_PROFILE) {
            View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_profile_header, parent, false);
            return new ViewHolderProfile(rowView);
        } else if (viewType == TYPE_ITEM_DEFAULT) {
            View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_affiliations_other, parent, false);

            return new ViewHolder(rowView);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderProfile) {
            ViewHolderProfile itemProfileHolder = (ViewHolderProfile) holder;
            if (mIsFromEditProfile) {
                itemProfileHolder.tvTitleScreen.setVisibility(View.VISIBLE);
                itemProfileHolder.tvTitleScreen.setText(mContext.getString(R.string.header_affiliation));
                itemProfileHolder.progressLayout.setVisibility(View.GONE);
                itemProfileHolder.tvTitle.setVisibility(View.GONE);
                itemProfileHolder.tvDesc.setVisibility(View.GONE);


            } else {
                itemProfileHolder.progressBar.setProgress(80);
                itemProfileHolder.tvTitle.setText(mContext.getString(R.string.title_affiliation));
                if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
                    Picasso.with(mContext).load(PreferenceUtil.getProfileImagePath()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).into(itemProfileHolder.ivProfile);

                }
            }

        } else if (holder instanceof ViewHolder) {
            try {
                final ViewHolder itemHolder = (ViewHolder) holder;
                final Affiliation currentItem = getItem(position);
                itemHolder.tvType.setText(currentItem.getAffiliationName());
                itemHolder.ivCheckBox.setTag(position);

                if (currentItem.getJobSeekerAffiliationStatus() == 0) {
                    itemHolder.ivCheckBox.setBackgroundResource(R.drawable.ic_check_empty);
                } else {
                    itemHolder.ivCheckBox.setBackgroundResource(R.drawable.ic_check_fill);
                }

                itemHolder.etOther.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (currentItem.getJobSeekerAffiliationStatus() == 1) {
                            mAffiliationList.get((Integer) itemHolder.ivCheckBox.getTag() - 1).setOtherAffiliation(charSequence.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                    }
                });
                if (currentItem.getAffiliationName().equalsIgnoreCase(Constants.OTHERS)) {
                    itemHolder.viewUnderLine.setVisibility(View.GONE);

                    if (!TextUtils.isEmpty(currentItem.getOtherAffiliation())) {
                        itemHolder.etOther.setVisibility(View.VISIBLE);
                        itemHolder.etOther.setText(currentItem.getOtherAffiliation());
                        etOtherTemp = itemHolder.etOther;
                    }
                } else {
                    itemHolder.viewUnderLine.setVisibility(View.VISIBLE);
                }

                itemHolder.ivCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currentItem.getJobSeekerAffiliationStatus() == 0) {
                            currentItem.setJobSeekerAffiliationStatus(1);
                            itemHolder.ivCheckBox.setBackgroundResource(R.drawable.ic_check_fill);
                        } else {
                            ((BaseActivity) mContext).hideKeyboard();
                            mAffiliationList.get((Integer) itemHolder.ivCheckBox.getTag() - 1).setOtherAffiliation("");
                            currentItem.setJobSeekerAffiliationStatus(0);
                            itemHolder.ivCheckBox.setBackgroundResource(R.drawable.ic_check_empty);
                        }

                        if (currentItem.getAffiliationName().equalsIgnoreCase(Constants.OTHERS)) {
                            itemHolder.etOther.setVisibility(View.VISIBLE);
                            etOtherTemp = itemHolder.etOther;
                            if (currentItem.getJobSeekerAffiliationStatus() == 0) {
                                itemHolder.etOther.setVisibility(View.GONE);
                                etOtherTemp = itemHolder.etOther;
                                itemHolder.etOther.setText("");
                                etOtherTemp.setText("");
                            }
                        } else {
                            itemHolder.etOther.setText("");
                            itemHolder.etOther.setVisibility(View.GONE);
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getItemCount() {
        return mAffiliationList != null ? mAffiliationList.size() + 1 : 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionProfileHeader(position)) {
            return TYPE_ITEM_PROFILE;
        }
        return TYPE_ITEM_DEFAULT;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvType;
        private ImageView ivCheckBox;
        private EditText etOther;
        private View viewUnderLine;

        ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvType = (TextView) itemLayoutView.findViewById(R.id.tv_affiliation_type);
            ivCheckBox = (ImageView) itemLayoutView.findViewById(R.id.iv_check_box);
            etOther = (EditText) itemLayoutView.findViewById(R.id.et_other);
            viewUnderLine = itemLayoutView.findViewById(R.id.view_line);
        }
    }

    private static class ViewHolderProfile extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvDesc, tvTitleScreen;
        ImageView ivProfile;
        ProgressBar progressBar;
        RelativeLayout progressLayout;

        private ViewHolderProfile(View itemLayoutView) {
            super(itemLayoutView);
            tvTitle = (TextView) itemLayoutView.findViewById(R.id.tv_title);
            tvDesc = (TextView) itemLayoutView.findViewById(R.id.tv_description);
            tvTitleScreen = (TextView) itemLayoutView.findViewById(R.id.tv_title_screen);
            ivProfile = (ImageView) itemLayoutView.findViewById(R.id.iv_profile_icon);
            progressBar = (ProgressBar) itemLayoutView.findViewById(R.id.progress_bar);
            progressLayout = (RelativeLayout) itemLayoutView.findViewById(R.id.progress_layout);
        }
    }
}


