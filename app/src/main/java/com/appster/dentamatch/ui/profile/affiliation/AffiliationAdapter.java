package com.appster.dentamatch.ui.profile.affiliation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.network.response.affiliation.AffiliationData;
import com.appster.dentamatch.network.response.affiliation.AffiliationResponse;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by virender on 13/01/17.
 */
public class AffiliationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_ITEM_PROFILE = 1;
    private final int TYPE_ITEM_DEFAULT = 2;
    //    private final int TYPE_ITEM_OTHER = 3;
    private Context mContext;
    private ArrayList<AffiliationData> mAffiliationList = new ArrayList<>();
    private EditText etOtherTemp;

//    private ItemAffiliationBinding mDefaultBinder;
//    private ItemAffiliationsOtherBinding mBinderOther;
//    private LayoutProfileHeaderBinding mBinderProfileHeader;

    public AffiliationAdapter(Context context) {
        mContext = context;
    }

    public void addList(ArrayList<AffiliationData> list) {
        if (mAffiliationList != null) {
            mAffiliationList.clear();
        }
        mAffiliationList.addAll(list);
        notifyDataSetChanged();
    }

    public ArrayList<AffiliationData> getList() {
        return mAffiliationList;
    }

    public String getOtherData() {
        if (etOtherTemp != null) {
            return etOtherTemp.getText().toString().trim();
        }
        return null;
    }

    @Override
    public int getItemCount() {


//        return mAffiliationList != null ? mAffiliationList.size() + extraViewCount : extraViewCount;
        return mAffiliationList != null ? mAffiliationList.size() + 1 : 0;
//        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionProfileHeader(position)) {
            return TYPE_ITEM_PROFILE;
        }
        return TYPE_ITEM_DEFAULT;
    }


    private AffiliationData getItem(int position) {
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
            itemProfileHolder.progressBar.setProgress(80);
            itemProfileHolder.tvTitle.setText(mContext.getString(R.string.title_affiliation));
            if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
                Picasso.with(mContext).load(PreferenceUtil.getProfileImagePath()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).into(itemProfileHolder.ivProfile);

            }


        } else if (holder instanceof ViewHolder) {
            try {
                final ViewHolder itemHolder = (ViewHolder) holder;
                final AffiliationData currentItem = getItem(position);
                itemHolder.tvType.setText(currentItem.getAffiliationName());
                itemHolder.ivCheckBox.setTag(position);
                if (currentItem.getJobSeekerAffiliationStatus() == 0) {
                    itemHolder.ivCheckBox.setBackgroundResource(R.drawable.ic_check_empty);
                } else {
                    itemHolder.ivCheckBox.setBackgroundResource(R.drawable.ic_check_fill);

                }
                if (currentItem.getAffiliationName().equalsIgnoreCase(Constants.OTHERS)) {
                    itemHolder.viewUnderLine.setVisibility(View.GONE);

                    if (!TextUtils.isEmpty(currentItem.getOtherAffiliation())) {
                        itemHolder.etOther.setVisibility(View.VISIBLE);
                        itemHolder.etOther.setText(currentItem.getOtherAffiliation());
                        etOtherTemp = itemHolder.etOther;

                    }

                } else {
//                    itemHolder.etOther.setVisibility(View.GONE);
                    itemHolder.viewUnderLine.setVisibility(View.VISIBLE);


                }

                itemHolder.ivCheckBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (currentItem.getJobSeekerAffiliationStatus() == 0) {
                            currentItem.setJobSeekerAffiliationStatus(1);
                            itemHolder.ivCheckBox.setBackgroundResource(R.drawable.ic_check_fill);

                        } else {
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


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvType;
        public ImageView ivCheckBox;
        private EditText etOther;
        private View viewUnderLine;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvType = (TextView) itemLayoutView.findViewById(R.id.tv_affiliation_type);
            ivCheckBox = (ImageView) itemLayoutView.findViewById(R.id.iv_check_box);
            etOther = (EditText) itemLayoutView.findViewById(R.id.et_other);
            viewUnderLine = (View) itemLayoutView.findViewById(R.id.view_line);

        }
    }

//    public static class ViewHolderOther extends RecyclerView.ViewHolder {
//
//        public TextView tvType;
//        public ImageView ivCheckBox;
//        public EditText etOther;
//
//        public ViewHolderOther(View itemLayoutView) {
//            super(itemLayoutView);
//            tvType = (TextView) itemLayoutView.findViewById(R.id.tv_affiliation_type);
//            ivCheckBox = (ImageView) itemLayoutView.findViewById(R.id.iv_check_box);
//            etOther = (EditText) itemLayoutView.findViewById(R.id.et_other);
//
//        }
//    }

    public static class ViewHolderProfile extends RecyclerView.ViewHolder {

        public TextView tvTitle;
        public TextView tvDesc;
        public ImageView ivProfile;
        public ProgressBar progressBar;

        public ViewHolderProfile(View itemLayoutView) {
            super(itemLayoutView);
            tvTitle = (TextView) itemLayoutView.findViewById(R.id.tv_title);
            tvDesc = (TextView) itemLayoutView.findViewById(R.id.tv_description);
            ivProfile = (ImageView) itemLayoutView.findViewById(R.id.iv_profile_icon);
            progressBar = (ProgressBar) itemLayoutView.findViewById(R.id.progress_bar);


        }
    }


}


