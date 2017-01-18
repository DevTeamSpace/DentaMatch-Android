package com.appster.dentamatch.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.appster.dentamatch.databinding.LayoutProfileHeaderBinding;
import com.appster.dentamatch.model.School;
import com.appster.dentamatch.model.SchoolType;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ram on 12/01/17.
 */

public class SchoolsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = "SchoolsAdapter";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<SchoolType> mSchoolList;
    private ItemSchoolBinding mBinder;
    private LayoutProfileHeaderBinding mBinderHeader;
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            mBinder = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_school, parent, false));

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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, int position) {

        List<String> listSchools = new ArrayList<String>();

        if(holder1 instanceof ViewHolderHeader){
            if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
                LogUtils.LOGD("pabd", "path is--=" + PreferenceUtil.getProfileImagePath());
                Picasso.with(mContext).load(PreferenceUtil.getProfileImagePath()).centerCrop().
                        resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).
                        placeholder(R.drawable.profile_pic_placeholder).into(mBinderHeader.ivProfileIcon);
            }

            mBinderHeader.progressBar.setProgress(50);
            mBinderHeader.tvTitle.setText(mContext.getString(R.string.where_did_you_study));
            mBinderHeader.tvDescription.setText(mContext.getString(R.string.lorem_ipsum));
        }else {
            final ViewHolderItem holder = (ViewHolderItem) holder1;
            final SchoolType schoolType = mSchoolList.get(position-1);
            LogUtils.LOGD(TAG, "SchoolType " + schoolType.getSchoolTypeName());

            holder.tvSchoolTypeName.setText(schoolType.getSchoolTypeName());

            for (School school : schoolType.getSchoolList()) {
                listSchools.add(school.getSchoolName());
//                LogUtils.LOGD(TAG, school.getSchoolName());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, listSchools);

            holder.autoCompleteTextView.setAdapter(arrayAdapter);
            holder.autoCompleteTextView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    hideKeyBoard();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

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
    }

    @Override
    public int getItemCount() {
        return mSchoolList.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader (int position) {
        return position == 0;
    }

    private void hideKeyBoard() {
        ((BaseActivity) mContext).hideKeyboard();
    }

    private class ViewHolderHeader extends RecyclerView.ViewHolder {
        ViewHolderHeader(View view) {
            super(view);
        }
    }

    private class ViewHolderItem extends RecyclerView.ViewHolder {
        TextView tvSchoolTypeName;
        EditText etSchoolName;
        EditText etYearOfGraduation;
        Spinner spinnerYears;
        AutoCompleteTextView autoCompleteTextView;
        TextInputLayout textInputLayout;

        ViewHolderItem(View view) {
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



