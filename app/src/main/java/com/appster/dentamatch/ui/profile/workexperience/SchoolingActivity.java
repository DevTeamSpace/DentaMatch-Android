package com.appster.dentamatch.ui.profile.workexperience;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.appster.dentamatch.R;
import com.appster.dentamatch.adapters.SchoolsAdapter;
import com.appster.dentamatch.databinding.ActivitySchoolingBinding;
import com.appster.dentamatch.interfaces.EditTextSelected;
import com.appster.dentamatch.model.School;
import com.appster.dentamatch.model.SchoolType;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.schools.AddSchoolRequest;
import com.appster.dentamatch.network.request.schools.PostSchoolData;
import com.appster.dentamatch.network.response.schools.SchoolingResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

/**
 * Created by ram on 15/01/17.
 */
public class SchoolingActivity extends BaseActivity implements View.OnClickListener, EditTextSelected {
    private static final String TAG = "Schooling";
    private ActivitySchoolingBinding mBinder;
    private SchoolsAdapter mSchoolsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_schooling);
        initViews();

        getSchoolListApi();
    }

    private void initViews() {
        mBinder.toolbarSchooling.ivToolBarLeft.setOnClickListener(this);
        mBinder.toolbarSchooling.tvToolbarGeneralLeft.setText(getString(R.string.header_schooling_exp).toUpperCase());
        mBinder.btnNext.setOnClickListener(this);
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                hideKeyboard();
                onBackPressed();

                break;
            case R.id.btn_next:
//                addSchoolListApi();

                if (checkValidation()) {

                    addSchoolListApi(prepareRequest());

                }
                break;
        }
    }

    private AddSchoolRequest prepareRequest() {
        HashMap<Integer, PostSchoolData> hashMap = mSchoolsAdapter.getPostMapData();
        ArrayList<PostSchoolData> requestList = new ArrayList<>();
        for (Map.Entry<Integer, PostSchoolData> entry : hashMap.entrySet()) {
            PostSchoolData school = new PostSchoolData();
            boolean isMatchScoolName = false;
            for (int i = 0; i < mSchoolsAdapter.getList().size(); i++) {

                for (int j = 0; j < mSchoolsAdapter.getList().get(i).getSchoolList().size(); j++) {
                    if (entry.getValue().getSchoolName().equalsIgnoreCase(mSchoolsAdapter.getList().get(i).getSchoolList().get(j).getSchoolName())) {
                        isMatchScoolName = true;
                        school.setSchoolName(entry.getValue().getSchoolName());
                        school.setSchoolId(mSchoolsAdapter.getList().get(i).getSchoolList().get(j).getSchoolId());
                        school.setOtherSchooling("");
                        break;
                    }
//                    else {
////                   school.setSchoolId("");
//                        school.setOtherSchooling(entry.getValue().getSchoolName());
//                        school.setSchoolName("");
//
//
//                    }


                }
//                if (!isMatchScoolName) {
////                    school.setSchoolId("");
//                    school.setOtherSchooling(entry.getValue().getSchoolName());
//                    school.setSchoolName("");
//                }
//
//                school.setYearOfGraduation(entry.getValue().getYearOfGraduation());
//                requestList.add(school);

            }


            if (!isMatchScoolName) {
                school.setSchoolId(Integer.parseInt(entry.getValue().getOtherId()));
                school.setOtherSchooling(entry.getValue().getSchoolName());
                school.setSchoolName("");
            }

            school.setYearOfGraduation(entry.getValue().getYearOfGraduation());
            requestList.add(school);
        }
        AddSchoolRequest request = new AddSchoolRequest();
        request.setSchoolingData(requestList);
        return request;

    }


    private boolean checkValidation() {
        HashMap<Integer, PostSchoolData> hashMap = mSchoolsAdapter.getPostMapData();
        if (!(hashMap != null && hashMap.size() > 0)) {
            showToast("please choose atleast one college");
            return false;
        } else {
            for (Map.Entry<Integer, PostSchoolData> entry : hashMap.entrySet()) {

                if (TextUtils.isEmpty(entry.getValue().getSchoolName())) {
                    showToast("School name can never blank.");
                    return false;
                }

                if (TextUtils.isEmpty(entry.getValue().getYearOfGraduation())) {
                    showToast("Year of graduation can never blank for school " + entry.getValue().getSchoolName());
                    return false;
                }

            }
        }
        return true;
    }


    private void setAdapter(List<SchoolType> schoolTypeList) {
        mSchoolsAdapter = new SchoolsAdapter(schoolTypeList, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mBinder.recyclerSchools.setLayoutManager(layoutManager);
        mBinder.recyclerSchools.setItemAnimator(new DefaultItemAnimator());
        mBinder.recyclerSchools.setAdapter(mSchoolsAdapter);
        mSchoolsAdapter.notifyDataSetChanged();
    }

    private void getSchoolListApi() {
        LogUtils.LOGD(TAG, "getSchoolListApi");
        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getSchoolList().enqueue(new BaseCallback<SchoolingResponse>(this) {
            @Override
            public void onSuccess(SchoolingResponse response) {
                if (response.getStatus() == 1) {
                    setAdapter(response.getSchoolingResponseData().getSchoolTypeList());
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<SchoolingResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    private void addSchoolListApi(AddSchoolRequest addSchoolRequest) {
        LogUtils.LOGD(TAG, "addSchoolListApi");
        processToShowDialog("", getString(R.string.please_wait), null);

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.addSchooling(addSchoolRequest).enqueue(new BaseCallback<BaseResponse>(this) {
            @Override
            public void onSuccess(BaseResponse response) {
                Utils.showToast(getApplicationContext(), response.getMessage());

                if (response.getStatus() == 1) {
//                    setAdapter(response.getSchoolingResponseData().getSchoolTypeList());
                    startActivity(new Intent(SchoolingActivity.this, SkillsActivity.class));

                }
            }

            @Override
            public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    @Override
    public void onEditTextSelected(int position) {
        mBinder.recyclerSchools.smoothScrollToPosition(position);
    }

//    private AddSchoolRequest prepareAddSchoolRequest() {
//        AddSchoolRequest request = new AddSchoolRequest();
//        request.getSchoolingData();
//    }
}
