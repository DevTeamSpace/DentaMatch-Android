package com.appster.dentamatch.adapters; /**
 * Created by ram on 16/01/17.
 * Graduation listing adapter.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.appster.dentamatch.R;

import java.util.ArrayList;

class SpinnerGraduationAdapter extends ArrayAdapter<String> {
    private static final int SPINNER_FIRST_POS = 0;
    private final Context mContext;
    private final ArrayList<String> mDataList;
    private final LayoutInflater mInflater;

    SpinnerGraduationAdapter(Context mContext, int textViewResourceId, ArrayList<String> dataList) {
        super(mContext, textViewResourceId, dataList);
        this.mContext = mContext;
        mDataList = dataList;
        mInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    private View getCustomView(int position, ViewGroup parent) {
        View row = mInflater.inflate(R.layout.item_spinner_text, parent, false);
        TextView textSpinner = row.findViewById(R.id.text_spinner);

        if (position == SPINNER_FIRST_POS) {
            textSpinner.setText(mContext.getString(R.string.hint_year_of_graduation));
        } else {
            textSpinner.setText(mDataList.get(position));
        }

        return row;
    }
}

