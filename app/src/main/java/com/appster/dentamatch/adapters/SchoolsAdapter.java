package com.appster.dentamatch.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
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
import com.appster.dentamatch.model.SchoolModel;
import com.appster.dentamatch.model.SchoolTypeModel;
import com.appster.dentamatch.network.request.schools.PostSchoolData;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Calendar;
import java.util.List;

/**
 * Created by ram on 12/01/17.
 */

public class SchoolsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private static final String TAG = "SchoolsAdapter";

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private List<SchoolTypeModel> mSchoolList;
    private ItemSchoolBinding mBinder;
    private LayoutProfileHeaderBinding mBinderHeader;
    private Context mContext;
    private final ArrayList<String> mYearsList;
    private EditTextSelected mNameSelectedListener;
    private HashMap<Integer, PostSchoolData> mHashMap = new HashMap<>();
    private boolean mIsFromEditProfile;
    private String mYear = "";

    private AdapterView.OnItemClickListener autoCompleteClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideKeyBoard();
        }
    };


    public SchoolsAdapter(List<SchoolTypeModel> schoolTypeList, Context context, EditTextSelected nameSelectedListener, boolean isFromEditProfile) {
        this.mSchoolList = schoolTypeList;
        this.mContext = context;
        mIsFromEditProfile = isFromEditProfile;

        mYearsList = new ArrayList<>();
        mYearsList.add(context.getString(R.string.hint_year_of_graduation));

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int minimumYear = currentYear - Constants.EDUCATION_HISTORY_YEARS;

        for (int i = currentYear; i > minimumYear; i--) {
            mYearsList.add(String.valueOf(i));
        }

        mNameSelectedListener = nameSelectedListener;
    }

    public List<SchoolTypeModel> getList() {
        return mSchoolList;
    }

    public HashMap<Integer, PostSchoolData> getPostMapData() {
        return mHashMap;
    }

    public void setSchoolMapData(HashMap<Integer, PostSchoolData> data) {
        mHashMap = data;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        if (viewType == TYPE_ITEM) {
            mBinder = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_school, parent, false));

            holder = new ViewHolderItem(mBinder.getRoot());

        } else {
            mBinderHeader = DataBindingUtil.bind(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_profile_header, parent, false));

            holder = new ViewHolderHeader(mBinderHeader.getRoot());

        }

        return holder;
    }

//    @Override
//    public void onBindViewHolder(final RecyclerView.ViewHolder holder1, final int position) {
//        final List<String> listSchools = new ArrayList<String>();
//
//        if (holder1 instanceof ViewHolderHeader) {
//
//            if (mIsFromEditProfile) {
//                mBinderHeader.tvTitleScreen.setVisibility(View.VISIBLE);
//                mBinderHeader.tvTitleScreen.setText(mContext.getString(R.string.header_schooling_exp));
//                mBinderHeader.progressLayout.setVisibility(View.GONE);
//                mBinderHeader.tvTitle.setVisibility(View.GONE);
//                mBinderHeader.tvDescription.setVisibility(View.GONE);
//
//            } else {
//                if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
//                    Picasso.with(mContext).load(PreferenceUtil.getProfileImagePath()).centerCrop().
//                            resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).
//                            placeholder(R.drawable.profile_pic_placeholder).into(mBinderHeader.ivProfileIcon);
//                }
//
//                mBinderHeader.progressBar.setProgress(Constants.PROFILE_PERCENTAGE.SCHOOLING);
//                mBinderHeader.tvTitle.setText(mContext.getString(R.string.where_did_you_study));
//                mBinderHeader.tvDescription.setText(mContext.getString(R.string.lorem_ipsum));
//            }
//
//        } else {
//            final ViewHolderItem holder = (ViewHolderItem) holder1;
//            final SchoolTypeModel schoolTypeModel = mSchoolList.get(position - 1);
//
//            holder.tvSchoolTypeName.setText(schoolTypeModel.getSchoolTypeName());
//            holder.autoCompleteTextView.setTag(position - 1);
//            holder.etYearOfGraduation.setTag(position - 1);
//            holder.spinnerYears.setTag(position - 1);
//
//            if (schoolTypeModel.getOtherList() != null &&
//                    schoolTypeModel.getOtherList().size() > 0 &&
//                    schoolTypeModel.getOtherList().get(0).getIsSelected() == 1) {
//
//                PostSchoolData data = new PostSchoolData();
//                data.setOtherSchooling(schoolTypeModel.getOtherList().get(0).getOtherSchooling());
//                data.setYearOfGraduation(schoolTypeModel.getOtherList().get(0).getYearOfGraduation());
//                data.setSchoolId(schoolTypeModel.getOtherList().get(0).getSchoolId());
//                data.setSchoolName(schoolTypeModel.getOtherList().get(0).getOtherSchooling());
//                data.setParentSchoolName(schoolTypeModel.getSchoolTypeName());
//                data.setOtherId("" + schoolTypeModel.getOtherList().get(0).getSchoolId());
//
//                holder.autoCompleteTextView.setText(schoolTypeModel.getOtherList().get(0).getOtherSchooling());
////                holder.etYearOfGraduation.setText(schoolTypeModel.getOtherList().get(0).getYearOfGraduation());
//                mYear = schoolTypeModel.getOtherList().get(0).getYearOfGraduation();
//
//                mHashMap.put(position - 1, data);
//            }
//
//            for (SchoolModel schoolModel : schoolTypeModel.getSchoolList()) {
//                listSchools.add(schoolModel.getSchoolName());
//
//                if (schoolModel.getIsSelected() == 1) {
//                    PostSchoolData data = new PostSchoolData();
//                    data.setOtherSchooling(schoolModel.getOtherSchooling());
//                    data.setYearOfGraduation(schoolModel.getYearOfGraduation());
//                    data.setSchoolId(schoolModel.getSchoolId());
//                    data.setSchoolName(schoolModel.getSchoolName());
//                    data.setOtherId("" + schoolModel.getSchoolTypeId());
//                    holder.autoCompleteTextView.setText(schoolModel.getSchoolName());
//                    data.setParentSchoolName(schoolTypeModel.getSchoolTypeName());
//
////                    holder.etYearOfGraduation.setText(schoolModel.getYearOfGraduation());
//                    mYear = schoolModel.getYearOfGraduation();
//
//                    mHashMap.put(position - 1, data);
//
//                } else {
//                    mYear = "";
//                    holder.autoCompleteTextView.setHint(mContext.getString(R.string.hint_type_to_search));
////                    holder.etYearOfGraduation.setHint(mContext.getString(R.string.hint_year_of_graduation));
////                    holder.etYearOfGraduation.setTextColor(ContextCompat.getColor(mContext, R.color.edt_hint_color));
//                }
//            }
//
//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, listSchools);
//            holder.autoCompleteTextView.setAdapter(arrayAdapter);
//
//            holder.autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                    hideKeyBoard();
//                }
//            });
//
//            holder.autoCompleteTextView.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    int key = (Integer) holder.autoCompleteTextView.getTag();
//                    PostSchoolData school = getSchool(key);
//                    school.setSchoolName(editable.toString());
//                    school.setParentSchoolName(mSchoolList.get(key).getSchoolTypeName());
//                    mHashMap.put(key, school);
//                }
//            });
//
//            holder.autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//                @Override
//                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                    /**
//                     * we scroll the autocomplete textview to center in order for it to take focus
//                     */
//                    mNameSelectedListener.onEditTextSelected(position);
//                    return false;
//                }
//            });
//
//            SpinnerGraduationAdapter adapter = new SpinnerGraduationAdapter(mContext, R.layout.item_spinner_text, mYearsList);
//            holder.spinnerYears.setAdapter(adapter);
//            holder.spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                    TextView text = (TextView) view.findViewById(R.id.text_spinner);
//                    text.setText("");
//                    holder.etYearOfGraduation.setText(mYearsList.get(position));
//
//                    if (position != 0) {
//                        PostSchoolData school = getSchool((Integer) holder.spinnerYears.getTag());
//                        school.setYearOfGraduation(mYearsList.get(position));
//                        school.setOtherId(String.valueOf(mSchoolList.get((Integer) holder.etYearOfGraduation.getTag()).getSchoolTypeId()));
//                        mHashMap.put((Integer) holder.spinnerYears.getTag(), school);
//                        mBinder.spinnerGraduation.setSelection(position);
//
//                    } else {
//                        holder.etYearOfGraduation.setTextColor(ContextCompat.getColor(mContext,R.color.edt_hint_color));
//                        PostSchoolData school = getSchool((Integer) holder.spinnerYears.getTag());
//                        school.setYearOfGraduation("");
//                    }
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> parent) {
//                    ((BaseActivity) mContext).hideKeyboard(holder.etSchoolName);
//                }
//            });
//
//            holder.textInputLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (holder.spinnerYears.getSelectedItem() == null) { // user selected nothing...
//                        holder.spinnerYears.performClick();
//                    }
//                }
//            });
//
//            holder.spinnerYears.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    ((BaseActivity) mContext).hideKeyboard(holder.etSchoolName);
//                    return false;
//                }
//            });
//
//            /**
//             * this task is done here to set the year in spinner
//             */
//            if (TextUtils.isEmpty(mYear)) {
//                holder.spinnerYears.setSelection(0);
//            } else {
//                for (int i = 0; i < mYearsList.size(); i++) {
//                    if (mYearsList.get(i).equals(mYear)) {
//                        holder.spinnerYears.setSelection(i);
//                        mYear = "";
//                    }
//                }
//            }
//
//        }
//    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final List<String> listSchools = new ArrayList<String>();

        if (holder instanceof ViewHolderHeader) {

            if (mIsFromEditProfile) {
                mBinderHeader.tvTitleScreen.setVisibility(View.VISIBLE);
                mBinderHeader.tvTitleScreen.setText(mContext.getString(R.string.header_schooling_exp));
                mBinderHeader.progressLayout.setVisibility(View.GONE);
                mBinderHeader.tvTitle.setVisibility(View.GONE);
                mBinderHeader.tvDescription.setVisibility(View.GONE);

            } else {
                if (!TextUtils.isEmpty(PreferenceUtil.getProfileImagePath())) {
                    Picasso.with(mContext).load(PreferenceUtil.getProfileImagePath()).centerCrop().
                            resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).
                            placeholder(R.drawable.profile_pic_placeholder).into(mBinderHeader.ivProfileIcon);
                }

                mBinderHeader.progressBar.setProgress(Constants.PROFILE_PERCENTAGE.SCHOOLING);
                mBinderHeader.tvTitle.setText(mContext.getString(R.string.where_did_you_study));
                mBinderHeader.tvDescription.setText(mContext.getString(R.string.lorem_ipsum));
            }

        } else {
            final ViewHolderItem holder1 = (ViewHolderItem) holder;
            final SchoolTypeModel schoolTypeModel = mSchoolList.get(position - 1);
            holder1.tvSchoolTypeName.setText(schoolTypeModel.getSchoolTypeName());

            /**
             * Settings tags for later reference.
             */
            holder1.autoCompleteTextView.setTag(position - 1);
            holder1.etYearOfGraduation.setTag(position - 1);
            holder1.spinnerYears.setTag(position - 1);

            holder1.autoCompleteTextView.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    int key = (Integer) holder1.autoCompleteTextView.getTag();
                    PostSchoolData school = getSchool(key);
                    school.setSchoolName(s.toString());
                    school.setParentSchoolName(mSchoolList.get(key).getSchoolTypeName());
                    mHashMap.put(key, school);
                }
            });

            /**
             * we scroll the autocomplete textview to center in order for it to take focus
             */
            final int refPosition = position;
            holder1.autoCompleteTextView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    mNameSelectedListener.onEditTextSelected(refPosition);
                    return false;
                }
            });

            /**
             * Set spinner with the years of graduation.
             */
            SpinnerGraduationAdapter adapter = new SpinnerGraduationAdapter(mContext, R.layout.item_spinner_text, mYearsList);
            holder1.spinnerYears.setAdapter(adapter);
            holder1.autoCompleteTextView.setOnItemClickListener(autoCompleteClick);


            if (schoolTypeModel.getOtherList() != null &&
                    schoolTypeModel.getOtherList().size() > 0 &&
                    schoolTypeModel.getOtherList().get(0).getIsSelected() == 1) {
//
                PostSchoolData data = new PostSchoolData();
                data.setOtherSchooling(schoolTypeModel.getOtherList().get(0).getOtherSchooling());
                data.setYearOfGraduation(schoolTypeModel.getOtherList().get(0).getYearOfGraduation());
                data.setSchoolId(schoolTypeModel.getOtherList().get(0).getSchoolId());
                data.setSchoolName(schoolTypeModel.getOtherList().get(0).getOtherSchooling());
                data.setParentSchoolName(schoolTypeModel.getSchoolTypeName());
                data.setOtherId("" + schoolTypeModel.getOtherList().get(0).getSchoolId());

                holder1.autoCompleteTextView.setText(schoolTypeModel.getOtherList().get(0).getOtherSchooling());
                mYear = schoolTypeModel.getOtherList().get(0).getYearOfGraduation();

                mHashMap.put(position - 1, data);
            } else {

                /**
                 * Look for schools which the user might have selected and fill in autocomplete.
                 */
                for (SchoolModel schoolModel : schoolTypeModel.getSchoolList()) {
                    listSchools.add(schoolModel.getSchoolName());

                    if (schoolModel.getIsSelected() == 1) {
                        PostSchoolData data = new PostSchoolData();
                        data.setOtherSchooling(schoolModel.getOtherSchooling());
                        data.setYearOfGraduation(schoolModel.getYearOfGraduation());
                        data.setSchoolId(schoolModel.getSchoolId());
                        data.setSchoolName(schoolModel.getSchoolName());
                        data.setOtherId("" + schoolModel.getSchoolTypeId());
                        data.setParentSchoolName(schoolTypeModel.getSchoolTypeName());

                        holder1.autoCompleteTextView.setText(schoolModel.getSchoolName());
                        mYear = schoolModel.getYearOfGraduation();
                        mHashMap.put(position - 1, data);
                        break;

                    }else{
                        mYear = "";
                    }
                }
            }

            /**
             * Selects the chosen year in the spinner.
             */
            if (TextUtils.isEmpty(mYear)) {
                holder1.spinnerYears.setSelection(0);

            } else {

                for (int i = 0; i < mYearsList.size(); i++) {
                    if (mYearsList.get(i).equals(mYear)) {
                        holder1.spinnerYears.setSelection(i);
                        mYear = "";
                        break;
                    }
                }

            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, listSchools);
            holder1.autoCompleteTextView.setAdapter(arrayAdapter);

            holder1.spinnerYears.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    TextView text = (TextView) view.findViewById(R.id.text_spinner);

                    if(!TextUtils.isEmpty(holder1.autoCompleteTextView.getText().toString())) {
                        if (position != 0) {
                            text.setTextColor(ContextCompat.getColor(mContext, R.color.text_default_color));
                            PostSchoolData school = getSchool((Integer) holder1.spinnerYears.getTag());
                            school.setYearOfGraduation(mYearsList.get(position));
                            school.setOtherId(String.valueOf(mSchoolList.get((Integer) holder1.etYearOfGraduation.getTag()).getSchoolTypeId()));
                            mHashMap.put((Integer) holder1.spinnerYears.getTag(), school);

                        } else {
                            text.setTextColor(ContextCompat.getColor(mContext, R.color.edt_hint_color));
                            PostSchoolData school = getSchool((Integer) holder1.spinnerYears.getTag());
                            school.setYearOfGraduation("");
                        }
                    }else{
                        ((BaseActivity)mContext).showToast(mContext.getString(R.string.error_school_name));
                        text.setText(mContext.getString(R.string.hint_year_of_graduation));
                        text.setTextColor(ContextCompat.getColor(mContext, R.color.edt_hint_color));
                        mBinder.spinnerGraduation.setSelection(0);
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    ((BaseActivity) mContext).hideKeyboard(holder1.etSchoolName);
                }
            });

            holder1.etYearOfGraduation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder1.spinnerYears.getSelectedItem() == null) { // user selected nothing...
                        holder1.spinnerYears.performClick();
                    }
                }
            });

        }

    }

    @Override
    public int getItemCount() {
        return mSchoolList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position)) {
            return TYPE_HEADER;
        }

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

        ViewHolderItem(View view) {
            super(view);
            tvSchoolTypeName = mBinder.tvSchoolTypeName;
            etSchoolName = mBinder.etSchoolName;
            etYearOfGraduation = mBinder.etGraduation;
            autoCompleteTextView = mBinder.etSchoolName;
            spinnerYears = mBinder.spinnerGraduation;
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



