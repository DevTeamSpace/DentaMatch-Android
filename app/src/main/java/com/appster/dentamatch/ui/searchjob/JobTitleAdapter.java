package com.appster.dentamatch.ui.searchjob;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.interfaces.JobTitleSelected;
import com.appster.dentamatch.model.JobTitleList;

import java.util.ArrayList;

/**
 * Created by virender on 27/01/17.
 */
public class JobTitleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements  View.OnClickListener {
    private Context mContext;
    private ArrayList<JobTitleList> mAffiliationList = new ArrayList<>();
    private ArrayList<JobTitleList> mSelectedJobTitles = new ArrayList<>();
    private JobTitleSelected mListener;

    public JobTitleAdapter(Context context, JobTitleSelected listener) {
        mContext = context;
        mListener = listener;
    }

    public void addList(ArrayList<JobTitleList> list) {
        if (mAffiliationList != null) {
            mAffiliationList.clear();
            mAffiliationList.addAll(list);
        }

        notifyDataSetChanged();
    }

    public void setSelectedListItems(ArrayList<JobTitleList> List){
        if (mAffiliationList != null  && mAffiliationList.size() > 0) {
            for(JobTitleList mylist : mAffiliationList){

                for(JobTitleList selectedList : List){

                    if(mylist.getId() == selectedList.getId()){
                        mylist.setSelected(true);
                        break;
                    }
                }
            }
        }
        mSelectedJobTitles = List;
        notifyDataSetChanged();
    }

    public ArrayList<JobTitleList> getList() {
        return mAffiliationList;
    }


    @Override
    public int getItemCount() {
        return mAffiliationList != null ? mAffiliationList.size() : 0;
    }


    private JobTitleList getItem(int position) {
        return mAffiliationList != null && mAffiliationList.size() > 0 ? mAffiliationList.get(position) : null;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_affiliation, parent, false);

        return new ViewHolder(rowView);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            final ViewHolder itemHolder = (ViewHolder) holder;
            final JobTitleList currentItem = getItem(position);
            itemHolder.tvType.setText(currentItem.getJobTitle());
            itemHolder.cbCheckBox.setTag(position);
            itemHolder.cbCheckBox.setOnClickListener(this);
            itemHolder.cbCheckBox.setChecked(currentItem.isSelected());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        try {
            final JobTitleList currentItem = getItem(position);
            if (currentItem.isSelected()) {

                for (JobTitleList i : mSelectedJobTitles) {
                    if (i.getId() == currentItem.getId()) {
                        mSelectedJobTitles.remove(i);
                        break;
                    }
                }
                currentItem.setSelected(false);

            } else {
                mSelectedJobTitles.add(currentItem);
                currentItem.setSelected(true);
            }

            mListener.onJobTitleSelected(mSelectedJobTitles);
            notifyItemChanged(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        try {
//        int position = (int) buttonView.getTag();
//        final JobTitleList currentItem = getItem(position);
//        if (!isChecked) {
//
//            for (JobTitleList i : mSelectedJobTitles) {
//
//                if (i.getId() == currentItem.getId()) {
//                    mSelectedJobTitles.remove(i);
//                    break;
//                }
//
//            }
//
//            currentItem.setSelected(false);
//
//        } else {
//            mSelectedJobTitles.add(currentItem);
//            currentItem.setSelected(true);
//        }
//
//        mListener.onJobTitleSelected(mSelectedJobTitles);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tvType;
        public CheckBox cbCheckBox;
        private EditText etOther;
        private View viewUnderLine;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvType = (TextView) itemLayoutView.findViewById(R.id.tv_affiliation_type);
            cbCheckBox = (CheckBox) itemLayoutView.findViewById(R.id.cb_check_box);
            etOther = (EditText) itemLayoutView.findViewById(R.id.et_other);
            viewUnderLine = (View) itemLayoutView.findViewById(R.id.view_line);

        }
    }
}