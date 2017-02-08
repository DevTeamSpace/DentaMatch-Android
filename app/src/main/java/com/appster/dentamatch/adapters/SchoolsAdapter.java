package com.appster.dentamatch.adapters;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ItemSchoolBinding;
import com.appster.dentamatch.databinding.LayoutProfileHeaderBinding;
import com.appster.dentamatch.interfaces.EditTextSelected;
import com.appster.dentamatch.model.School;
import com.appster.dentamatch.model.SchoolType;
import com.appster.dentamatch.network.request.schools.PostSchoolData;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
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
    private EditTextSelected mNameSelectedListener;
    private HashMap<Integer, PostSchoolData> mHashMap = new HashMap<>();
    private boolean mIsFromEditProfile;
    private String year = "";


    public SchoolsAdapter(List<SchoolType> schoolTypeList, Context context, EditTextSelected nameSelectedListener, boolean isFromEditProfile) {
        this.mSchoolList = schoolTypeList;
        this.mContext = context;
        mIsFromEditProfile = isFromEditProfile;

        mYearsList = new ArrayList<String>();
        mYearsList.add(context.getString(R.string.hint_year_of_graduation));

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int minimumYear = currentYear - Constants.EDUCATION_HISTORY_YEARS;

        for (int i = currentYear; i > minimumYear; i--) {
            mYearsList.add(String.valueOf(i));
        }

        mNameSelectedListener = nameSelectedListener;
    }

    public List<SchoolType> getList() {
        return mSchoolList;
    }

    public HashMap<Integer, PostSchoolData> getPostMapData() {
        return mHashMap;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {

        final List<String> listSchools = new ArrayList<String>();

        if (holder1 instanceof ViewHolderHeader) {

            if (mIsFromEditProfile) {
                mBinderHeader.tvTitleScreen.setVisibility(View.VISIBLE);
                mBinderHeader.tvTitleScreen.setText(mContext.getString(R.string.header_schooling_exp));
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
                mBinderHeader.progressBar.setProgress(Constants.PROFILE_PERCENTAGE.SCHOOLING);

                mBinderHeader.tvTitle.setText(mContext.getString(R.string.where_did_you_study));
                mBinderHeader.tvDescription.setText(mContext.getString(R.string.lorem_ipsum));
            }

        } else {
            final ViewHolderItem holder = (ViewHolderItem) holder1;
            final SchoolType schoolType = mSchoolList.get(position - 1);
            LogUtils.LOGD(TAG, "SchoolType " + schoolType.getSchoolTypeName());

            holder.tvSchoolTypeName.setText(schoolType.getSchoolTypeName());
            holder.autoCompleteTextView.setTag(position - 1);
            holder.etYearOfGraduation.setTag(position - 1);
            holder.spinnerYears.setTag(position - 1);

            if (schoolType.getOtherList() != null && schoolType.getOtherList().size() > 0 &&
                    schoolType.getOtherList().get(0).getIsSelected() == 1) {
                PostSchoolData data = new PostSchoolData();
                data.setOtherSchooling(schoolType.getOtherList().get(0).getOtherSchooling());
                data.setYearOfGraduation(schoolType.getOtherList().get(0).getYearOfGraduation());
                data.setSchoolId(schoolType.getOtherList().get(0).getSchoolId());
                data.setSchoolName(schoolType.getOtherList().get(0).getOtherSchooling());
                data.setParentSchoolName(schoolType.getSchoolTypeName());
                data.setOtherId("" + schoolType.getOtherList().get(0).getSchoolId());

                holder.autoCompleteTextView.setText(schoolType.getOtherList().get(0).getOtherSchooling());
                holder.etYearOfGraduation.setText(schoolType.getOtherList().get(0).getYearOfGraduation());
                year = schoolType.getOtherList().get(0).getYearOfGraduation();

                mHashMap.put(position - 1, data);
            }

            for (School school : schoolType.getSchoolList()) {
                listSchools.add(school.getSchoolName());

                if (school.getIsSelected() == 1) {
                    PostSchoolData data = new PostSchoolData();
                    data.setOtherSchooling(school.getOtherSchooling());
                    data.setYearOfGraduation(school.getYearOfGraduation());
                    data.setSchoolId(school.getSchoolId());
                    data.setSchoolName(school.getSchoolName());
                    data.setOtherId("" + school.getSchoolTypeId());
                    holder.autoCompleteTextView.setText(school.getSchoolName());
                    data.setParentSchoolName(schoolType.getSchoolTypeName());

                    holder.etYearOfGraduation.setText(school.getYearOfGraduation());
                    year = school.getYearOfGraduation();

                    mHashMap.put(position - 1, data);

                } else {
                    holder.autoCompleteTextView.setHint(mContext.getString(R.string.hint_type_to_search));
                    holder.etYearOfGraduation.setHint(mContext.getString(R.string.hint_year_of_graduation));
                }
//                LogUtils.LOGD(TAG, school.getSchoolName());
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, listSchools);

            holder.autoCompleteTextView.setAdapter(arrayAdapter);

            holder.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    hideKeyBoard();
//                    int key = (Integer) holder.autoCompleteTextView.getTag();
//                    PostSchoolData school = getSchool(key);
//                    school.setSchoolId(mSchoolList.get(key).getSchoolList().get(i).getSchoolId());
//                    school.setSchoolName(mSchoolList.get(key).getSchoolList().get(i).getSchoolName());
//                    mHashMap.put(key, school);
                }
            });

            holder.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int key = (Integer) holder.autoCompleteTextView.getTag();
                    PostSchoolData school = getSchool(key);
//                    school.setSchoolId(mSchoolList.get(key).getSchoolList().get(i).getSchoolId());
//                    if (TextUtils.isEmpty(editable.toString())) {
//                        if (school != null) {
//                            mHashMap.remove(key);
//                            year = school.getYearOfGraduation();
//                            holder.spinnerYears.setSelection(0);
//                            if (TextUtils.isEmpty(year)) {
//                                holder.spinnerYears.setSelection(0);
//
//                            } else {
//                                for (int i = 0; i < mYearsList.size(); i++) {
//                                    if (mYearsList.get(i).equals(year)) {
//                                        holder.spinnerYears.setSelection(0);
//                                    }
//                                }
//                            }
//                        }
//                    } else {
                    school.setSchoolName(editable.toString());
                    school.setParentSchoolName(mSchoolList.get(key).getSchoolTypeName());
                    mHashMap.put(key, school);
//                    }

                }
            });

            holder.autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    /**
                     * we scroll the autocomplete textview to center in order for it to take focus
                     */
                    mNameSelectedListener.onEditTextSelected(position);
                    return false;
                }
            });

            SpinnerGraduationAdapter adapter = new SpinnerGraduationAdapter(mContext, R.layout.item_spinner_text, mYearsList);

            holder.spinnerYears.setAdapter(adapter);

            holder.spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView text = (TextView) view.findViewById(R.id.text_spinner);
                    text.setText("");
                    holder.etYearOfGraduation.setText(mYearsList.get(position));
                    if (position != 0) {
                        holder.etYearOfGraduation.setTextColor(mContext.getResources().getColor(R.color.text_default_color));

                        PostSchoolData school = getSchool((Integer) holder.spinnerYears.getTag());
                        school.setYearOfGraduation(mYearsList.get(position));
                        school.setOtherId("" + mSchoolList.get((Integer) holder.etYearOfGraduation.getTag()).getSchoolTypeId());
                    } else {
                        holder.etYearOfGraduation.setTextColor(mContext.getResources().getColor(R.color.edt_hint_color));

                        PostSchoolData school = getSchool((Integer) holder.spinnerYears.getTag());
                        school.setYearOfGraduation("");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    holder.etYearOfGraduation.setText("");
                    holder.etYearOfGraduation.setHint(mContext.getString(R.string.hint_year_of_graduation));
                    holder.etYearOfGraduation.setTextColor(mContext.getResources().getColor(R.color.edt_hint_color));
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
/**
 * this task is done here to set the year in spinner
 */
            if (TextUtils.isEmpty(year)) {
                holder.spinnerYears.setSelection(0);
            } else {
                for (int i = 0; i < mYearsList.size(); i++) {
                    if (mYearsList.get(i).equals(year)) {
                        holder.spinnerYears.setSelection(i);
                        year = "";
                    }
                }
            }

        }
    }

    @Override
    public int getItemCount() {
        return mSchoolList.size() + 1;
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

    private PostSchoolData getSchool(Integer key) {
        if (mHashMap != null) {
            if (mHashMap.containsKey(key)) {
                return mHashMap.get(key);

            }
        }
        return new PostSchoolData();

    }
}



