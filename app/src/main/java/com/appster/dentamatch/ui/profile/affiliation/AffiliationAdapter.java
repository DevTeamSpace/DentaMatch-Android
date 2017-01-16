package com.appster.dentamatch.ui.profile.affiliation;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.network.request.auth.AffiliationRequest;
import com.appster.dentamatch.network.request.auth.WorkExpRequest;

import java.util.ArrayList;

/**
 * Created by virender on 13/01/17.
 */
public class AffiliationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int TYPE_ITEM_DEFAULT = 1;
    private final int TYPE_ITEM_OTHER = 2;
    private final int TYPE_ITEM_PROFILE = 3;
    private Context mContext;
    private ArrayList<AffiliationRequest> mAffiliationList = new ArrayList<>();

    public AffiliationAdapter(Context context) {
        mContext = context;
    }

    public void addList(ArrayList<AffiliationRequest> list) {
        if (mAffiliationList != null) {
            mAffiliationList.clear();
        }
        mAffiliationList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {


//        return mAffiliationList != null ? mAffiliationList.size() + extraViewCount : extraViewCount;
        return mAffiliationList != null ? mAffiliationList.size() : 0;
//        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionOther(position)) {
            return TYPE_ITEM_OTHER;
        }
//        else if()
        return TYPE_ITEM_DEFAULT;
    }


    private AffiliationRequest getItem(int position) {
        return mAffiliationList != null && mAffiliationList.size() > 0 ? mAffiliationList.get(position) : null;
    }


    private boolean isPositionOther(int position) {

        return position == mAffiliationList.size() - 1;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM_OTHER) {
            View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_affiliations_other, parent, false);

            return new ViewHolderOther(rowView);
        } else if (viewType == TYPE_ITEM_DEFAULT) {
            View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_affiliation, parent, false);

            return new ViewHolder(rowView);
        }
        else if (viewType == TYPE_ITEM_DEFAULT) {
            View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_affiliation, parent, false);

            return new ViewHolder(rowView);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderOther) {
            ViewHolderOther itemOtherHolder = (ViewHolderOther) holder;
        } else if (holder instanceof ViewHolder) {
            try {
                ViewHolder itemHolder = (ViewHolder) holder;
//                    itemHolder.tvProfileTypeTitle.setText("Posted by " + currentItem.fullName + " from " + currentItem.businessName);


//                    itemHolder.btnDocumentTypeReadMore.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                        }
//                    });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvType;
        public ImageView ivCheckBox;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
//            tvType = (TextView) itemLayoutView.findViewById(R.id.tv_profile_type_title_id);
//            ivCheckBox = (ImageView) itemLayoutView.findViewById(R.id.iv_news_profile_cover_id);


        }
    }

    public static class ViewHolderOther extends RecyclerView.ViewHolder {

        public TextView tvType;
        public ImageView ivCheckBox;
        public EditText etOther;

        public ViewHolderOther(View itemLayoutView) {
            super(itemLayoutView);
//            tvType = (TextView) itemLayoutView.findViewById(R.id.tv_profile_type_title_id);
//            ivCheckBox = (ImageView) itemLayoutView.findViewById(R.id.iv_news_profile_cover_id);


        }
    }


}


