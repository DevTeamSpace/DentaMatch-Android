package com.appster.dentamatch.ui.profile;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentProfileBinding;
import com.appster.dentamatch.databinding.ItemProfileCellCertificateBinding;
import com.appster.dentamatch.databinding.ItemProfileSchoolingBinding;
import com.appster.dentamatch.databinding.ItemProfileWorkExpBinding;
import com.appster.dentamatch.databinding.ItemSchoolBinding;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.appster.dentamatch.network.response.affiliation.AffiliationResponse;
import com.appster.dentamatch.network.response.certificates.CertificatesList;
import com.appster.dentamatch.network.response.profile.ProfileResponse;
import com.appster.dentamatch.network.response.profile.ProfileResponseData;
import com.appster.dentamatch.network.response.workexp.WorkExpSave;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseFragment;
import com.appster.dentamatch.ui.profile.workexperience.ViewAndEditWorkExperienceActivity;
import com.appster.dentamatch.ui.settings.SettingActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.CustomTextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.apmem.tools.layouts.FlowLayout;
import org.w3c.dom.Text;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * Created by virender on 17/01/17.
 */
public class ProfileFragment extends BaseFragment implements View.OnClickListener {
    private static int instanceCount = 1;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private FragmentProfileBinding profileBinding;
    private String TAG = "ProfileFragment-";


    public static ProfileFragment newInstance() {
        ProfileFragment frag = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("instanceCount", instanceCount);
        instanceCount++;
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        profileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        fragmentManager = getActivity().getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        initViews();
        System.out.println("Profile fragment called");
//        FlurryAgent.logEvent(getString(R.string.settings));

        return profileBinding.getRoot();

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getProfileData();
    }

    private void initViews() {
        profileBinding.cellDentalStateBoard.tvAddCertificates.setText(getString(R.string.add_dental_state_board));
        profileBinding.cellAffiliation.tvAddCertificates.setText(getString(R.string.add_affilitaion));
        profileBinding.cellExp.tvAddCertificates.setText(getString(R.string.add_more_exp));
        profileBinding.cellKeySkill.tvAddCertificates.setText(getString(R.string.add_key_skill));
        profileBinding.cellSchooling.tvAddCertificates.setText(getString(R.string.add_school));
        profileBinding.cellLicence.tvAddCertificates.setText(getString(R.string.add_licence));
        profileBinding.cellLicence.tvCertificatesName.setText(getString(R.string.lable_licence_number));
        profileBinding.cellExp.tvCertificatesName.setText(getString(R.string.title_experience));
        profileBinding.cellSchooling.tvCertificatesName.setText(getString(R.string.title_schooling));
        profileBinding.cellDentalStateBoard.tvCertificatesName.setText(getString(R.string.dental_state_board));
        profileBinding.cellAffiliation.tvCertificatesName.setText(getString(R.string.title_affiliation));
        profileBinding.ivSetting.setOnClickListener(this);
        profileBinding.tvEdit.setOnClickListener(this);
//        profileBinding.chipsView.getEditText().setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, 0, 0);
//        profileBinding.chipsView.getEditText().setFocusable(false);
//        profileBinding.chipsView.getEditText().setCursorVisible(false);
//        profileBinding.chipsView.getEditText().setFocusableInTouchMode(false);
//        profileBinding.chipsView.getEditText().setLongClickable(false);
//        profileBinding.chipsView.getEditText().setClickable(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_edit:
                break;
            case R.id.iv_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;

        }

    }

    private void getProfileData() {
        showProgressBar(getString(R.string.please_wait));
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getProfile().enqueue(new BaseCallback<ProfileResponse>(getBaseActivity()) {
            @Override
            public void onSuccess(ProfileResponse response) {
                LogUtils.LOGD(TAG, "onSuccess");
                if (response.getStatus() == 1) {

                    setViewData(response.getProfileResponseData());
                } else {
                    Utils.showToast(getActivity(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<ProfileResponse> call, BaseResponse baseResponse) {
                LogUtils.LOGD(TAG, "onFail");
            }
        });

    }

    private void setViewData(ProfileResponseData response) {
        if (response != null) {
            if (response.getUser() != null) {
                if (!TextUtils.isEmpty(response.getUser().getProfilePic())) {
                    Picasso.with(getActivity()).load(response.getUser().getProfilePic()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).into(profileBinding.ivProfileIcon);

                }
                profileBinding.tvName.setText(response.getUser().getFirstName() + " " + response.getUser().getLastName());
//                profileBinding.tvJobTitle.setText(response.getUser().get);
                profileBinding.tvAboutMe.setText(response.getUser().getAboutMe());
            }
            if (response.getWorkExperience() != null && response.getWorkExperience().getSaveList().size() > 0) {
                goneViews(profileBinding.cellExp.tvAddCertificates, profileBinding.cellExp.tvEdit);

                inflateExperience(response.getWorkExperience().getSaveList());
            } else {
                visibleView(profileBinding.cellExp.tvAddCertificates, profileBinding.cellExp.tvEdit);

            }
            if (response.getAffiliationList() != null && response.getAffiliationList().size() > 0) {
                goneViews(profileBinding.cellAffiliation.tvAddCertificates, profileBinding.cellAffiliation.tvEdit);
                profileBinding.flowLayout.removeAllViews();
                for (int i = 0; i < response.getAffiliationList().size(); i++) {
                    CustomTextView textView = new CustomTextView(getActivity());
                    FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 10, 10, 10);
                    textView.setLayoutParams(lp);
                    textView.setBackgroundResource(R.drawable.bg_edit_text);
                    textView.setPadding(30, 10, 30, 10);
                    textView.setText(response.getAffiliationList().get(i).getAffiliationName());
                    profileBinding.flowLayout.addView(textView, lp);
                }

            } else {
                visibleView(profileBinding.cellAffiliation.tvAddCertificates, profileBinding.cellAffiliation.tvEdit);
            }

            if (response.getLicence() != null) {
                goneViews(profileBinding.cellLicence.tvAddCertificates, profileBinding.cellLicence.tvEdit);
                profileBinding.layoutLicenceData.setVisibility(View.VISIBLE);

                profileBinding.tvLicenceNumber.setText(response.getLicence().getLicenseNumber());
                profileBinding.tvLicenceState.setText(response.getLicence().getState());


            } else {
                profileBinding.layoutLicenceData.setVisibility(View.GONE);
                visibleView(profileBinding.cellLicence.tvAddCertificates, profileBinding.cellLicence.tvEdit);

            }
            if (response.getCertificatesLists() != null && response.getCertificatesLists().size() > 0) {
                inflateCertification(response.getCertificatesLists());
            }
            if (response.getDentalStateBoard() != null) {
                profileBinding.cellDentalStateBoard.tvAddCertificates.setVisibility(View.GONE);
                profileBinding.cellDentalStateBoard.tvEdit.setVisibility(View.VISIBLE);
                profileBinding.cellDentalStateBoard.ivCertificateImage.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(response.getDentalStateBoard().getImageUrl())) {
                    Picasso.with(getActivity()).load(response.getDentalStateBoard().getImageUrl()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).into(profileBinding.cellDentalStateBoard.ivCertificateImage);

                }
            } else {
                profileBinding.cellDentalStateBoard.tvAddCertificates.setVisibility(View.VISIBLE);
                profileBinding.cellDentalStateBoard.tvEdit.setVisibility(View.GONE);
                profileBinding.cellDentalStateBoard.ivCertificateImage.setVisibility(View.GONE);

            }
        }
    }

    private void visibleView(TextView tvAdd, TextView tvEdit) {
        if (tvAdd != null)
            tvAdd.setVisibility(View.VISIBLE);
        if (tvEdit != null)

            tvEdit.setVisibility(View.VISIBLE);

    }

    private void goneViews(TextView tvAdd, TextView tvEdit) {
        if (tvAdd != null)

            tvAdd.setVisibility(View.GONE);
        if (tvEdit != null)

            tvEdit.setVisibility(View.GONE);
    }

    private void inflateExperience(ArrayList<WorkExpRequest> expList) {
        profileBinding.expInflater.removeAllViews();
        ItemProfileWorkExpBinding expBinding;
        for (int i = 0; i < expList.size(); i++) {
            expBinding = DataBindingUtil.bind(LayoutInflater.from(profileBinding.expInflater.getContext())
                    .inflate(R.layout.item_profile_work_exp, profileBinding.expInflater, false));
            expBinding.tvJobTitle.setText(expList.get(i).getJobtitleName());
            expBinding.tvName.setText(expList.get(i).getOfficeName());
            expBinding.tvAddress.setText(expList.get(i).getOfficeAddress());
            expBinding.tvReferenceEmail.setText(expList.get(i).getReference1Email());
            expBinding.tvReferenceName.setText(expList.get(i).getReference1Name());
            expBinding.tvReferencePhoneNumber.setText(expList.get(i).getReference1Mobile());
            expBinding.tvExpDuration.setText("" + expList.get(i).getMonthsOfExpereince() / 12 + "." + (expList.get(i).getMonthsOfExpereince() % 12) + " " + getString(R.string.yrs));
            profileBinding.expInflater.addView(expBinding.getRoot());
        }

    }

    private void inflateCertification(ArrayList<CertificatesList> certificateList) {
        profileBinding.certificationInflater.removeAllViews();
        ItemProfileCellCertificateBinding cellCertificateBinding;
        for (int i = 0; i < certificateList.size(); i++) {
            cellCertificateBinding = DataBindingUtil.bind(LayoutInflater.from(profileBinding.certificationInflater.getContext())
                    .inflate(R.layout.item_profile_cell_certificate, profileBinding.expInflater, false));
            CertificatesList certificate = certificateList.get(i);
            cellCertificateBinding.tvCertificatesName.setText(certificate.getCertificateName());
            if (!TextUtils.isEmpty(certificate.getImage())) {
                cellCertificateBinding.tvAddCertificates.setVisibility(View.GONE);
                cellCertificateBinding.tvEdit.setVisibility(View.VISIBLE);
                cellCertificateBinding.ivCertificateImage.setVisibility(View.VISIBLE);
                Picasso.with(getActivity()).load(certificate.getImage()).centerCrop().resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN).placeholder(R.drawable.profile_pic_placeholder).memoryPolicy(MemoryPolicy.NO_CACHE).into(cellCertificateBinding.ivCertificateImage);

            } else {
                cellCertificateBinding.tvAddCertificates.setVisibility(View.VISIBLE);
                cellCertificateBinding.tvEdit.setVisibility(View.GONE);
                cellCertificateBinding.ivCertificateImage.setVisibility(View.GONE);

            }
            profileBinding.certificationInflater.addView(cellCertificateBinding.getRoot());
        }

    }

    private void inflateSchools(ArrayList<String> schoolList) {
        profileBinding.schoolInflater.removeAllViews();
        ItemProfileSchoolingBinding schoolBinding;
        for (int i = 0; i < schoolList.size(); i++) {
            schoolBinding = DataBindingUtil.bind(LayoutInflater.from(profileBinding.schoolInflater.getContext())
                    .inflate(R.layout.item_profile_schooling, profileBinding.schoolInflater, false));
            schoolBinding.tvCourseName.setText("");
            schoolBinding.tvCourseName.setText("");
            profileBinding.schoolInflater.addView(schoolBinding.getRoot());
        }

    }

    //    @Override
    public String getFragmentName() {
        return null;
    }


//    @Override
//    public String getFragmentName() {
//        return null;
//    }
}
