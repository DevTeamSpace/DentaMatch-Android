package com.appster.dentamatch.ui.auth;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationData;

import java.util.List;

/**
 * Created by zishan on 23/11/17.
 */

public class PreferredJobLocationAdapter extends ArrayAdapter<PreferredJobLocationData> {

    private List<PreferredJobLocationData> preferredJobLocationDataList;
    private final Activity context;

    static class ViewHolder {
        TextView categoryName;
    }

    public PreferredJobLocationAdapter(Activity context, List<PreferredJobLocationData> preferredJobLocationDataList) {
        super(context, R.layout.preferred_job_location_list_item, preferredJobLocationDataList);
        this.context = context;
        this.preferredJobLocationDataList = preferredJobLocationDataList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;

        if (convertView == null) {
            LayoutInflater inflator = context.getLayoutInflater();
            view = inflator.inflate(R.layout.preferred_job_location_list_item, null);
            final ViewHolder viewHolder = new ViewHolder();
            viewHolder.categoryName = (TextView) view.findViewById(R.id.tv_job_category);
            view.setTag(viewHolder);
        } else {
            view = convertView;
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.categoryName.setText(preferredJobLocationDataList.get(position).getPreferredLocationName());
        return view;
    }
}

