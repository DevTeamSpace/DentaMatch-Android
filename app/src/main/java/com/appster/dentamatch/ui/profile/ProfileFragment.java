package com.appster.dentamatch.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentProfileBinding;
import com.appster.dentamatch.databinding.ItemProfileCellCertificateBinding;
import com.appster.dentamatch.databinding.ItemProfileSchoolingBinding;
import com.appster.dentamatch.databinding.ItemProfileSkillBinding;
import com.appster.dentamatch.databinding.ItemProfileWorkExpBinding;
import com.appster.dentamatch.model.ProfileSchool;
import com.appster.dentamatch.model.ProfileSkill;
import com.appster.dentamatch.model.ProfileUpdatedEvent;
import com.appster.dentamatch.model.User;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.appster.dentamatch.network.response.certificates.CertificatesList;
import com.appster.dentamatch.network.response.profile.ProfileResponse;
import com.appster.dentamatch.network.response.profile.ProfileResponseData;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseFragment;
import com.appster.dentamatch.ui.profile.affiliation.AffiliationActivity;
import com.appster.dentamatch.ui.profile.workexperience.SchoolingActivity;
import com.appster.dentamatch.ui.profile.workexperience.SkillsActivity;
import com.appster.dentamatch.ui.profile.workexperience.UpdateCertificateActivity;
import com.appster.dentamatch.ui.profile.workexperience.UpdateLicenseActivity;
import com.appster.dentamatch.ui.profile.workexperience.WorkExpListActivity;
import com.appster.dentamatch.ui.settings.SettingActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.CustomTextView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.apmem.tools.layouts.FlowLayout;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private ProfileResponseData profileResponseData;
    private ItemProfileCellCertificateBinding cellCertificateBinding;

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
        return profileBinding.getRoot();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        EventBus.getDefault().unregister(this);
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
        profileBinding.cellKeySkill.tvCertificatesName.setText(getString(R.string.title_skill));
        profileBinding.cellAffiliation.tvCertificatesName.setText(getString(R.string.title_affiliation));
        profileBinding.ivSetting.setOnClickListener(this);
        profileBinding.tvEdit.setOnClickListener(this);
        profileBinding.cellLicence.tvEditCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileBinding.cellLicence.tvAddCertificates.performClick();
            }
        });
        profileBinding.cellLicence.tvAddCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent licenseIntent = new Intent(getActivity(), UpdateLicenseActivity.class);
                licenseIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
                licenseIntent.putExtra(Constants.INTENT_KEY.DATA, profileResponseData != null ? profileResponseData.getLicence() : null);
                startActivity(licenseIntent);
            }
        });
        profileBinding.cellDentalStateBoard.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), UpdateCertificateActivity.class);
                CertificatesList data = new CertificatesList();
                if (TextUtils.isEmpty(profileResponseData.getDentalStateBoard().getImageUrl())) {
                    data.setImage("");

                } else {
                    data.setImage(profileResponseData.getDentalStateBoard().getImageUrl());
                }
                data.setCertificateName(getString(R.string.dental_state_board));

                intent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
                intent.putExtra(Constants.INTENT_KEY.DATA, data);
                startActivity(intent);
            }
        });
        profileBinding.cellDentalStateBoard.tvAddCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileBinding.cellDentalStateBoard.tvEdit.performClick();

            }
        });


        profileBinding.cellAffiliation.tvEditCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent affiliationIntent = new Intent(getActivity(), AffiliationActivity.class);
                affiliationIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
                startActivity(affiliationIntent);
            }
        });
        profileBinding.cellAffiliation.tvAddCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileBinding.cellAffiliation.tvEditCell.performClick();
            }
        });
        profileBinding.cellSchooling.tvEditCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileBinding.cellSchooling.tvAddCertificates.performClick();
            }
        });
        profileBinding.cellSchooling.tvAddCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent skillIntent = new Intent(getActivity(), SchoolingActivity.class);
                skillIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
                startActivity(skillIntent);
            }
        });
        profileBinding.cellKeySkill.tvEditCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileBinding.cellKeySkill.tvAddCertificates.performClick();
            }
        });
        profileBinding.cellKeySkill.tvAddCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent skillIntent = new Intent(getActivity(), SkillsActivity.class);
                skillIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
                startActivity(skillIntent);
            }
        });
        profileBinding.cellExp.tvAddCertificates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent workIntent = new Intent(getActivity(), WorkExpListActivity.class);
                workIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
                startActivity(workIntent);
            }
        });
        profileBinding.cellExp.tvEditCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profileBinding.cellExp.tvAddCertificates.performClick();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_edit:
                startActivity(new Intent(getActivity(), UpdateProfileActivity.class)
                        .putExtra(Constants.EXTRA_PROFILE_DATA, profileResponseData));
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
                    profileResponseData = response.getProfileResponseData();
                    if (isAlive()) {
                        PreferenceUtil.setJobTitleList(response.getProfileResponseData().getJobTitleLists());

                        setViewData(response.getProfileResponseData());
                    }
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
                    Picasso.with(getActivity())
                            .load(response.getUser().getProfilePic())
//                            .centerCrop()
                            .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                            .onlyScaleDown()
                            .placeholder(R.drawable.profile_pic_placeholder)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(profileBinding.ivProfileIcon);

                }

                profileBinding.tvName.setText(response.getUser().getFirstName() + " " + response.getUser().getLastName());
                profileBinding.tvAboutMe.setText(response.getUser().getAboutMe());
                profileBinding.tvJobTitle.setText(response.getUser().getJobTitle());
                profileBinding.tvLocation.setText(response.getUser().getPreferredJobLocation());

                saveUserProfile(response.getUser());
            }

            if (response.getWorkExperience() != null && response.getWorkExperience().getSaveList().size() > 0) {
                goneViews(profileBinding.cellExp.tvAddCertificates, profileBinding.cellExp.tvEditCell);

                inflateExperience(response.getWorkExperience().getSaveList());
            } else {
                visibleView(profileBinding.cellExp.tvAddCertificates, profileBinding.cellExp.tvEditCell);

            }
            if (response.getAffiliationList() != null && response.getAffiliationList().size() > 0) {
                profileBinding.flowLayout.setVisibility(View.VISIBLE);
                goneViews(profileBinding.cellAffiliation.tvAddCertificates, profileBinding.cellAffiliation.tvEditCell);
                profileBinding.flowLayout.removeAllViews();
                for (int i = 0; i < response.getAffiliationList().size(); i++) {
                    CustomTextView textView = new CustomTextView(getActivity());
                    FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                    lp.setMargins(10, 10, 10, 10);
                    textView.setLayoutParams(lp);
                    textView.setSingleLine(true);

                    textView.setBackgroundResource(R.drawable.bg_edit_text);
                    textView.setPadding(30, 10, 30, 10);
                    textView.setText(response.getAffiliationList().get(i).getAffiliationName());
                    profileBinding.flowLayout.addView(textView, lp);
                }

            } else {
//                profileBinding.expInflater.setVisibility(View.GONE);

                profileBinding.flowLayout.setVisibility(View.GONE);
                visibleView(profileBinding.cellAffiliation.tvAddCertificates, profileBinding.cellAffiliation.tvEditCell);
            }

            if (response.getLicence() != null && !TextUtils.isEmpty(response.getLicence().getLicenseNumber()) && !TextUtils.isEmpty(response.getLicence().getState())) {
//                goneViews(profileBinding.cellLicence.tvAddCertificates, profileBinding.cellLicence.tvEditCell);
                profileBinding.cellLicence.tvAddCertificates.setVisibility(View.GONE);
                profileBinding.cellLicence.tvEditCell.setVisibility(View.VISIBLE);
                profileBinding.layoutLicenceData.setVisibility(View.VISIBLE);

                profileBinding.tvLicenceNumber.setText(response.getLicence().getLicenseNumber());
                profileBinding.tvLicenceState.setText(response.getLicence().getState());


            } else {
                profileBinding.cellLicence.tvAddCertificates.setVisibility(View.VISIBLE);
                profileBinding.cellLicence.tvEditCell.setVisibility(View.GONE);
                profileBinding.layoutLicenceData.setVisibility(View.GONE);
//                visibleView(profileBinding.cellLicence.tvAddCertificates, profileBinding.cellLicence.tvEditCell);

            }
            if (response.getCertificatesLists() != null && response.getCertificatesLists().size() > 0) {
                inflateCertification(response.getCertificatesLists());
            }
            if (response.getDentalStateBoard() != null && !TextUtils.isEmpty(response.getDentalStateBoard().getImageUrl())) {
                profileBinding.cellDentalStateBoard.tvAddCertificates.setVisibility(View.GONE);
                profileBinding.cellDentalStateBoard.tvEdit.setVisibility(View.VISIBLE);
                profileBinding.cellDentalStateBoard.ivCertificateImage.setVisibility(View.VISIBLE);
                profileBinding.cellDentalStateBoard.tvCertificateImageName.setVisibility(View.VISIBLE);
                profileBinding.cellDentalStateBoard.tvCertificateValidityDate.setVisibility(View.GONE);
                profileBinding.cellDentalStateBoard.layoutValidationDate.setVisibility(View.GONE);

//                profileBinding.cellDentalStateBoard.tvCertificateImageName.setText(getString(R.string.certificate_dental_state_board));
                profileBinding.cellDentalStateBoard.tvCertificateImageName.setVisibility(View.GONE);

                if (!TextUtils.isEmpty(response.getDentalStateBoard().getImageUrl())) {
                    Picasso.with(getActivity()).load(response.getDentalStateBoard().getImageUrl())
//                            .centerCrop()
                            .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                            .onlyScaleDown()
                            .placeholder(R.drawable.ic_upload)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(profileBinding.cellDentalStateBoard.ivCertificateImage);

                }
            } else {
                profileBinding.cellDentalStateBoard.tvAddCertificates.setVisibility(View.VISIBLE);
                profileBinding.cellDentalStateBoard.tvEdit.setVisibility(View.GONE);
                profileBinding.cellDentalStateBoard.ivCertificateImage.setVisibility(View.GONE);
                profileBinding.cellDentalStateBoard.tvCertificateImageName.setVisibility(View.GONE);
                profileBinding.cellDentalStateBoard.layoutValidationDate.setVisibility(View.GONE);


            }
            if (response.getSchoolArrayList() != null && response.getSchoolArrayList().size() > 0) {
                profileBinding.cellSchooling.tvAddCertificates.setVisibility(View.GONE);
                profileBinding.cellSchooling.tvEditCell.setVisibility(View.VISIBLE);
                inflateSchools(response.getSchoolArrayList());

            } else {
                profileBinding.schoolInflater.setVisibility(View.GONE);

                profileBinding.cellSchooling.tvAddCertificates.setVisibility(View.VISIBLE);
                profileBinding.cellSchooling.tvEditCell.setVisibility(View.GONE);
            }
            if (response.getSkillArrayList() != null && response.getSkillArrayList().size() > 0) {
                profileBinding.cellKeySkill.tvAddCertificates.setVisibility(View.GONE);
                profileBinding.cellKeySkill.tvEditCell.setVisibility(View.VISIBLE);
                inflateSkill(response.getSkillArrayList());

            } else {
                profileBinding.keySkillInflater.setVisibility(View.GONE);
                profileBinding.cellKeySkill.tvAddCertificates.setVisibility(View.VISIBLE);
                profileBinding.cellKeySkill.tvEditCell.setVisibility(View.GONE);
            }
        }
    }

    private void inflateSkill(ArrayList<ProfileSkill> skillArrayList) {
        profileBinding.keySkillInflater.setVisibility(View.VISIBLE);

        profileBinding.keySkillInflater.removeAllViews();
        ItemProfileSkillBinding skillBinding;
        for (int i = 0; i < skillArrayList.size(); i++) {
            skillBinding = DataBindingUtil.bind(LayoutInflater.from(profileBinding.keySkillInflater.getContext())
                    .inflate(R.layout.item_profile_skill, profileBinding.keySkillInflater, false));
            skillBinding.tvSkillName.setText(skillArrayList.get(i).getSkillsName().toUpperCase());

            for (int j = 0; j < skillArrayList.get(i).getChildSkillList().size(); j++) {
                CustomTextView textView = new CustomTextView(getActivity());
                FlowLayout.LayoutParams lp = new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
                lp.setMargins(10, 10, 10, 10);
                textView.setLayoutParams(lp);
                textView.setBackgroundResource(R.drawable.bg_edit_text);
                textView.setPadding(30, 10, 30, 10);
//                textView.setTextSize(getResources().getDimension(R.dimen.text_size_12));
                textView.setText(skillArrayList.get(i).getChildSkillList().get(j).getSkillsChildName());
                skillBinding.skillFlowLayout.addView(textView, lp);

            }
            profileBinding.keySkillInflater.addView(skillBinding.getRoot());
        }
    }

    private void visibleView(TextView tvAdd, TextView tvEdit) {
        if (tvAdd != null)
            tvAdd.setVisibility(View.VISIBLE);
        if (tvEdit != null)
            tvEdit.setVisibility(View.GONE);
    }

    private void goneViews(TextView tvAdd, TextView tvEdit) {
        if (tvAdd != null)
            tvAdd.setVisibility(View.GONE);
        if (tvEdit != null)
            tvEdit.setVisibility(View.VISIBLE);
    }

    private void inflateExperience(ArrayList<WorkExpRequest> expList) {
        profileBinding.expInflater.setVisibility(View.VISIBLE);
        profileBinding.expInflater.removeAllViews();
//        ItemProfileWorkExpBinding expBinding;
        for (int i = 0; i < expList.size(); i++) {
            boolean isNoContactInfo = false;
            ItemProfileWorkExpBinding expBinding
                    = DataBindingUtil.bind(LayoutInflater.from(profileBinding.expInflater.getContext())
                    .inflate(R.layout.item_profile_work_exp, profileBinding.expInflater, false));
            expBinding.tvJobTitle.setText(expList.get(i).getJobTitleName());
            expBinding.tvName.setText(expList.get(i).getOfficeName());
            expBinding.tvAddress.setText(expList.get(i).getOfficeAddress());
            if (TextUtils.isEmpty(expList.get(i).getReference1Email()) && TextUtils.isEmpty(expList.get(i).getReference1Mobile()) && TextUtils.isEmpty(expList.get(i).getReference1Name())) {
                isNoContactInfo = true;
                expBinding.tvReference1Email.setVisibility(View.GONE);
                expBinding.tvReference1Name.setVisibility(View.GONE);
                expBinding.tvReference1PhoneNumber.setVisibility(View.GONE);
            } else {
                if (TextUtils.isEmpty(expList.get(i).getReference1Email())) {
                    expBinding.tvReference1Email.setText(getString(R.string.not_applicable));

                } else {
                    expBinding.tvReference1Email.setText(expList.get(i).getReference1Email());

                }
                if (TextUtils.isEmpty(expList.get(i).getReference1Name())) {
                    expBinding.tvReference1Name.setText(getString(R.string.not_applicable));

                } else {
                    expBinding.tvReference1Name.setText(expList.get(i).getReference1Name());

                }
                if (TextUtils.isEmpty(expList.get(i).getReference1Mobile())) {
                    expBinding.tvReference1PhoneNumber.setText(getString(R.string.not_applicable));

                } else {
                    expBinding.tvReference1PhoneNumber.setText(expList.get(i).getReference1Mobile());

                }
            }
            if (TextUtils.isEmpty(expList.get(i).getReference2Email()) && TextUtils.isEmpty(expList.get(i).getReference2Mobile()) && TextUtils.isEmpty(expList.get(i).getReference2Name())) {
                if (isNoContactInfo) {
                    expBinding.tvContactInfo.setVisibility(View.GONE);
                }
                expBinding.tvReference2Email.setVisibility(View.GONE);
                expBinding.tvReference2Name.setVisibility(View.GONE);
                expBinding.tvReference2PhoneNumber.setVisibility(View.GONE);
            } else {
                if (TextUtils.isEmpty(expList.get(i).getReference2Email())) {
                    expBinding.tvReference2Email.setText(getString(R.string.not_applicable));

                } else {
                    expBinding.tvReference2Email.setText(expList.get(i).getReference2Email());

                }
                if (TextUtils.isEmpty(expList.get(i).getReference2Name())) {
                    expBinding.tvReference2Name.setText(getString(R.string.not_applicable));

                } else {
                    expBinding.tvReference2Name.setText(expList.get(i).getReference2Name());

                }
                if (TextUtils.isEmpty(expList.get(i).getReference2Mobile())) {
                    expBinding.tvReference2PhoneNumber.setText(getString(R.string.not_applicable));

                } else {
                    expBinding.tvReference2PhoneNumber.setText(expList.get(i).getReference2Mobile());

                }
            }


            expBinding.tvExpDuration.setText("" + expList.get(i).getMonthsOfExpereince() / 12 + "." + (expList.get(i).getMonthsOfExpereince() % 12) + " " + getString(R.string.yrs));
            profileBinding.expInflater.addView(expBinding.getRoot());
        }

    }

    private void inflateCertification(final ArrayList<CertificatesList> certificateList) {
        profileBinding.certificationInflater.removeAllViews();

        for (int i = 0; i < certificateList.size(); i++) {

            cellCertificateBinding = DataBindingUtil.bind(LayoutInflater.from(profileBinding.certificationInflater.getContext())
                    .inflate(R.layout.item_profile_cell_certificate, profileBinding.certificationInflater, false));
            final View tempView = cellCertificateBinding.getRoot();
            tempView.setTag(i);
            cellCertificateBinding.tvEdit.setId(i);
            cellCertificateBinding.tvCertificateImageName.setVisibility(View.VISIBLE);
            cellCertificateBinding.tvCertificateValidityDate.setVisibility(View.VISIBLE);
            cellCertificateBinding.tvAddCertificates.setTag(i);
            CertificatesList certificate = certificateList.get(i);
            cellCertificateBinding.tvCertificatesName.setText(certificate.getCertificateName());
            cellCertificateBinding.tvCertificateImageName.setText(certificate.getCertificateName());
            if (TextUtils.isEmpty(certificate.getValidityDate())) {
                cellCertificateBinding.tvCertificateValidityDate.setVisibility(View.GONE);
                cellCertificateBinding.tvCertificateValidityText.setVisibility(View.GONE);
                cellCertificateBinding.tvCertificateImageName.setVisibility(View.GONE);

            } else {
                cellCertificateBinding.tvCertificateValidityText.setVisibility(View.VISIBLE);
                cellCertificateBinding.tvCertificateValidityDate.setText(Utils.dateFormatYYYYMMMMDD(certificate.getValidityDate()));

            }

            if (!TextUtils.isEmpty(certificate.getImage())) {
                cellCertificateBinding.tvAddCertificates.setVisibility(View.GONE);
                cellCertificateBinding.tvEdit.setVisibility(View.VISIBLE);
                cellCertificateBinding.ivCertificateImage.setVisibility(View.VISIBLE);
                Picasso.with(getActivity())
                        .load(certificate.getImage())
//                        .centerCrop()
                        .resize(Constants.IMAGE_DIMEN, Constants.IMAGE_DIMEN)
                        .onlyScaleDown()
                        .placeholder(R.drawable.ic_upload)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(cellCertificateBinding.ivCertificateImage);

            } else {
                cellCertificateBinding.tvAddCertificates.setVisibility(View.VISIBLE);
                cellCertificateBinding.tvEdit.setVisibility(View.GONE);
                cellCertificateBinding.ivCertificateImage.setVisibility(View.GONE);
                cellCertificateBinding.layoutValidationDate.setVisibility(View.GONE);


            }

            cellCertificateBinding.tvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UpdateCertificateActivity.class);
//                    intent.putExtra(Constants.INTENT_KEY.DATA, certificateList.get((Integer) cellCertificateBinding.tvEdit.getId()));
                    intent.putExtra(Constants.INTENT_KEY.DATA, certificateList.get((Integer) tempView.getTag()));
                    startActivity(intent);
                }
            });
            cellCertificateBinding.tvAddCertificates.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UpdateCertificateActivity.class);
//                    intent.putExtra(Constants.INTENT_KEY.DATA, certificateList.get((Integer) cellCertificateBinding.tvAddCertificates.getId()));
                    intent.putExtra(Constants.INTENT_KEY.DATA, certificateList.get((Integer) tempView.getTag()));
                    startActivity(intent);
                }
            });
            profileBinding.certificationInflater.addView(cellCertificateBinding.getRoot());
        }

    }

    private void inflateSchools(ArrayList<ProfileSchool> schoolList) {
        profileBinding.schoolInflater.setVisibility(View.VISIBLE);

        profileBinding.schoolInflater.removeAllViews();
        ItemProfileSchoolingBinding schoolBinding;

        for (int i = 0; i < schoolList.size(); i++) {
            schoolBinding = DataBindingUtil.bind(LayoutInflater.from(profileBinding.schoolInflater.getContext())
                    .inflate(R.layout.item_profile_schooling, profileBinding.schoolInflater, false));
            schoolBinding.tvSchoolName.setText(schoolList.get(i).getSchoolName() + "(" + schoolList.get(i).getYearOfGraduation() + ")");
//            schoolBinding.tvCourseName.setText(schoolList.get(i).getSchoolChildName());
            schoolBinding.tvCourseName.setText(schoolList.get(i).getSchoolTitle());
            profileBinding.schoolInflater.addView(schoolBinding.getRoot());
        }

    }

    public String getFragmentName() {
        return null;
    }

    @Subscribe()
    public void onProfileUpdatedEvent(ProfileUpdatedEvent profileUpdatedEvent) {
        if (profileUpdatedEvent.ismIsProfileUpdated()) {
            getProfileData();
        }
    }

    private void saveUserProfile(User user) {
        PreferenceUtil.setUserModel(user);
    }


}
