package com.appster.dentamatch.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemSchoolBinding;
import com.appster.dentamatch.model.School;
import com.appster.dentamatch.model.SchoolType;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 12/01/17.
 */

public class SchoolsAdapter extends RecyclerView.Adapter<SchoolsAdapter.MyViewHolder> {

    private static final String TAG = "SchoolsAdapter";
    private List<SchoolType> mSchoolList;
    private ItemSchoolBinding mBinder;
    private Context mContext;
    private final ArrayList<String> mYearsList;

    public SchoolsAdapter(List<SchoolType> schoolTypeList, Context context) {
        this.mSchoolList = schoolTypeList;
        this.mContext = context;

        mYearsList = new ArrayList<String>();
        mYearsList.add(context.getString(R.string.hint_year_of_graduation));

        for (int i = 1970; i < 2010; i++) {
            mYearsList.add(String.valueOf(i));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        mBinder = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_school, parent, false));

        return new MyViewHolder(mBinder.getRoot());
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final SchoolType schoolType = mSchoolList.get(position);

        List<String> list = new ArrayList<String>();

        LogUtils.LOGD(TAG, "SchoolType " + schoolType.getSchoolTypeName());

        holder.tvSchoolTypeName.setText(schoolType.getSchoolTypeName());

        for (School school : mSchoolList.get(position).getSchoolList()) {
            list.add(school.getSchoolName());
            LogUtils.LOGD(TAG, school.getSchoolName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list);

        holder.autoCompleteTextView.setAdapter(arrayAdapter);

        SpinnerGraduationAdapter adapter = new SpinnerGraduationAdapter(mContext, R.layout.item_spinner_text, mYearsList);

        holder.spinnerYears.setAdapter(adapter);

        holder.spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView text = (TextView) ((LinearLayout) view).findViewById(R.id.text_spinner);
                text.setText("");
                holder.etYearOfGraduation.setText(mYearsList.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                holder.etYearOfGraduation.setText("");
                holder.etYearOfGraduation.setHint(mContext.getString(R.string.hint_year_of_graduation));
                ((BaseActivity) mContext).hideKeyboard(holder.etSchoolName);
            }
        });

        holder.textInputLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.spinnerYears.getSelectedItem() == null) { // user selected nothing...
                    holder.spinnerYears.performClick();
                }
            }
        });

        holder.spinnerYears.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ((BaseActivity) mContext).hideKeyboard(holder.etSchoolName);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSchoolList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvSchoolTypeName;
        EditText etSchoolName;
        EditText etYearOfGraduation;
        Spinner spinnerYears;
        AutoCompleteTextView autoCompleteTextView;
        TextInputLayout textInputLayout;

        MyViewHolder(View view) {
            super(view);
            tvSchoolTypeName = mBinder.tvSchoolTypeName;
            etSchoolName = mBinder.etSchoolName;
            etYearOfGraduation = mBinder.etGraduation;
            autoCompleteTextView = mBinder.etSchoolName;
            spinnerYears = mBinder.spinnerGraduation;
            textInputLayout = mBinder.tilGraduation;
        }
    }
}


/*public class SchoolsAdapter extends RecyclerView.Adapter<SchoolsAdapter.MyViewHolder> {

    private static final String TAG = "SchoolsAdapter";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<SchoolType> mSchoolList;
    private ItemSchoolBinding mBinder;
    private Context mContext;

    public SchoolsAdapter(List<SchoolType> schoolTypeList, Context context) {
        this.mSchoolList = schoolTypeList;
        this.mContext = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            return new VHItem(null);
        } else if (viewType == TYPE_HEADER) {
            //inflate your layout and pass it to view holder
            return new VHHeader(null);
        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");


        mBinder = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_school, parent, false));

        return new MyViewHolder(mBinder.getRoot());
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

    private SchoolType getItem(int position) {
        return mSchoolList.get(position - 1);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final SchoolType schoolType = mSchoolList.get(position);

        List<String> list = new ArrayList<String>();

        LogUtils.LOGD(TAG, "SchoolType " + schoolType.getSchoolTypeName());

        for (School school : mSchoolList.get(position).getSchoolList()) {
            list.add(school.getSchoolName());
            LogUtils.LOGD(TAG, school.getSchoolName());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, list);

        holder.listView.setAdapter(arrayAdapter);

        holder.tvSchoolTypeName.setText(schoolType.getSchoolTypeName());

        holder.etSchoolName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return mSchoolList.size()+1;

    }

    class VHHEADER extends RecyclerView.ViewHolder {
        VHHEADER(View view) {
            super(view);

        }
    }

    class VHITEM extends RecyclerView.ViewHolder {
        TextView tvSchoolTypeName;
        EditText etSchoolName;
        EditText etYearOfGraduation;
        ListView listView;

        VHITEM(View view) {
            super(view);
            tvSchoolTypeName = mBinder.tvSchoolTypeName;
            etSchoolName = mBinder.etSchoolName;
            etYearOfGraduation = mBinder.etGraduation;
            listView = mBinder.listItem;
        }
    }
}*/



