/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.profile;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingFragment;
import com.appster.dentamatch.databinding.FragmentProfileBinding;
import com.appster.dentamatch.databinding.ItemProfileCellCertificateBinding;
import com.appster.dentamatch.databinding.ItemProfileSchoolingBinding;
import com.appster.dentamatch.databinding.ItemProfileSkillBinding;
import com.appster.dentamatch.databinding.ItemProfileWorkExpBinding;
import com.appster.dentamatch.eventbus.ProfileUpdatedEvent;
import com.appster.dentamatch.model.ProfileSchoolModel;
import com.appster.dentamatch.model.ProfileSkillModel;
import com.appster.dentamatch.model.UserModel;
import com.appster.dentamatch.network.request.workexp.WorkExpRequest;
import com.appster.dentamatch.network.response.certificates.CertificatesList;
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse;
import com.appster.dentamatch.network.response.notification.UnReadNotificationResponseData;
import com.appster.dentamatch.network.response.profile.ProfileResponse;
import com.appster.dentamatch.network.response.profile.ProfileResponseData;
import com.appster.dentamatch.presentation.notification.NotificationActivity;
import com.appster.dentamatch.presentation.profile.affiliation.AffiliationActivity;
import com.appster.dentamatch.presentation.profile.workexperience.MyWorkExpListActivity;
import com.appster.dentamatch.presentation.profile.workexperience.SchoolingActivity;
import com.appster.dentamatch.presentation.profile.workexperience.SkillsActivity;
import com.appster.dentamatch.presentation.profile.workexperience.UpdateCertificateActivity;
import com.appster.dentamatch.presentation.profile.workexperience.UpdateLicenseActivity;
import com.appster.dentamatch.presentation.settings.SettingsActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.StringUtils;
import com.appster.dentamatch.util.Utils;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;


/**
 * Created by virender on 17/01/17.
 * To inject activity reference.
 */
public class ProfileFragment extends BaseLoadingFragment<ProfileViewModel>
        implements View.OnClickListener {

    private static int instanceCount = 1;
    private FragmentProfileBinding profileBinding;
    private ProfileResponseData profileResponseData;

    public static ProfileFragment newInstance() {
        ProfileFragment frag = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("instanceCount", instanceCount);
        instanceCount++;
        return frag;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        profileBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        initViews();
        viewModel.getProfile().observe(this, this::onSuccessProfileRequest);
        viewModel.getUnreadNotificationCount().observe(this, this::onSuccessNotificationCountRequest);
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
        getBatchCount();
    }

    private void initViews() {
        profileBinding.cellAffiliation.tvAddCertificates.setText(getString(R.string.add_affiliation));
        profileBinding.cellExp.tvAddCertificates.setText(getString(R.string.ad_more_exp));
        profileBinding.cellKeySkill.tvAddCertificates.setText(getString(R.string.add_key_skill));
        profileBinding.cellSchooling.tvAddCertificates.setText(getString(R.string.add_school));
        profileBinding.cellLicence.tvAddCertificates.setText(getString(R.string.add_licence));
        profileBinding.cellLicence.tvCertificatesName.setText(getString(R.string.label_licence_number));
        profileBinding.cellExp.tvCertificatesName.setText(getString(R.string.hdr_work_exp));
        profileBinding.cellSchooling.tvCertificatesName.setText(getString(R.string.title_schooling));
        profileBinding.cellKeySkill.tvCertificatesName.setText(getString(R.string.title_skill));
        profileBinding.cellAffiliation.tvCertificatesName.setText(getString(R.string.title_affiliation));
        profileBinding.ivSetting.setOnClickListener(this);
        profileBinding.tvEdit.setOnClickListener(this);
        profileBinding.ivToolBarLeft.setOnClickListener(this);
        profileBinding.cellLicence.tvEditCell.setOnClickListener(view ->
                profileBinding.cellLicence.tvAddCertificates.performClick());
        profileBinding.cellLicence.tvAddCertificates.setOnClickListener(view -> {
            Intent licenseIntent = new Intent(getActivity(), UpdateLicenseActivity.class);
            licenseIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
            licenseIntent.putExtra(Constants.INTENT_KEY.DATA, profileResponseData != null ? profileResponseData.getLicence() : null);
            startActivity(licenseIntent);
        });

        profileBinding.cellAffiliation.tvEditCell.setOnClickListener(view -> {
            Intent affiliationIntent = new Intent(getActivity(), AffiliationActivity.class);
            affiliationIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
            startActivity(affiliationIntent);
        });

        profileBinding.cellAffiliation.tvAddCertificates.setOnClickListener(view ->
                profileBinding.cellAffiliation.tvEditCell.performClick());

        profileBinding.cellSchooling.tvEditCell.setOnClickListener(view ->
                profileBinding.cellSchooling.tvAddCertificates.performClick());

        profileBinding.cellSchooling.tvAddCertificates.setOnClickListener(view -> {
            Intent skillIntent = new Intent(getActivity(), SchoolingActivity.class);
            skillIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
            startActivity(skillIntent);
        });

        profileBinding.cellKeySkill.tvEditCell.setOnClickListener(view ->
                profileBinding.cellKeySkill.tvAddCertificates.performClick());

        profileBinding.cellKeySkill.tvAddCertificates.setOnClickListener(view -> {
            Intent skillIntent = new Intent(getActivity(), SkillsActivity.class);
            skillIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
            startActivity(skillIntent);
        });

        profileBinding.cellExp.tvAddCertificates.setOnClickListener(view -> {
            Intent workIntent = new Intent(getActivity(), MyWorkExpListActivity.class);
            workIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
            startActivity(workIntent);
        });

        profileBinding.cellExp.tvEditCell.setOnClickListener(view ->
                profileBinding.cellExp.tvAddCertificates.performClick());


        profileBinding.tvUserStatus.setOnClickListener(view -> {
            if (profileResponseData.getUser().getIsJobSeekerVerified() != Constants.JOBSEEKAR_VERIFY_STATUS) {
                profileBinding.tvName.setVisibility(View.GONE);
                profileBinding.tvJobTitle.setVisibility(View.GONE);
                profileBinding.tvLocation.setVisibility(View.GONE);
                profileBinding.relInfo.setVisibility(View.VISIBLE);
                profileBinding.tvInfo.setText(getString(R.string.your_profile_pending));
            } else if (profileResponseData.getUser().getIsCompleted() != Constants.PROFILE_COMPLETED_STATUS) {
                profileBinding.tvName.setVisibility(View.GONE);
                profileBinding.tvJobTitle.setVisibility(View.GONE);
                profileBinding.tvLocation.setVisibility(View.GONE);
                profileBinding.relInfo.setVisibility(View.VISIBLE);
                profileBinding.tvInfo.setText(getString(R.string.your_profile_incomplete));
            }
        });

        profileBinding.tvInfoGotIt.setOnClickListener(view -> {
            profileBinding.tvName.setVisibility(View.VISIBLE);
            profileBinding.tvJobTitle.setVisibility(View.VISIBLE);
            profileBinding.tvLocation.setVisibility(View.VISIBLE);
            profileBinding.relInfo.setVisibility(View.GONE);
        });

        profileBinding.layoutLicenceData.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_edit:
                startActivity(new Intent(getActivity(), UpdateProfileActivity.class)
                        .putExtra(Constants.EXTRA_PROFILE_DATA, profileResponseData));
                break;
            case R.id.iv_setting:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            case R.id.iv_tool_bar_left:
                startActivity(new Intent(getActivity(), NotificationActivity.class));
                break;
            default:
                break;
        }
    }

    private void getProfileData() {
        viewModel.requestProfile();
    }

    private void setViewData(final ProfileResponseData response) {
        if (response != null) {
            if (response.getUser() != null) {
                if (!TextUtils.isEmpty(response.getUser().getProfilePic())) {
                    Picasso.get()
                            .load(response.getUser().getProfilePic())
                            .placeholder(R.drawable.profile_pic_placeholder)
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .into(profileBinding.ivProfileIcon);
                    profileBinding.ivProfileIcon.setOnClickListener(v ->
                            launchImageViewer(v, response.getUser().getProfilePic()));
                }
                if (response.getUser().getIsJobSeekerVerified() != Constants.JOBSEEKAR_VERIFY_STATUS) {
                    profileBinding.tvUserStatus.setText(getString(R.string.pending));
                    profileBinding.tvUserStatus.setBackground(getResources().getDrawable(R.drawable.rounded_yellow_drawable));
                    profileBinding.tvUserStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_pending_yellow, 0, R.drawable.ic_down_arrow_white, 0);
                    profileBinding.layoutPlaceHolder.setBackground(getResources().getDrawable(R.drawable.drawable_circle_yellow));
                    profileBinding.tvUserStatus.setPadding(0, 0, Utils.dpToPx(getActivity(), 6), 0);
                } else if (response.getUser().getIsCompleted() != Constants.PROFILE_COMPLETED_STATUS) {
                    profileBinding.tvUserStatus.setText(getString(R.string.need_attention));
                    profileBinding.tvUserStatus.setBackground(getResources().getDrawable(R.drawable.rounded_red_drawable));
                    profileBinding.tvUserStatus.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_needs_attention_red, 0, R.drawable.ic_down_arrow_white, 0);
                    profileBinding.layoutPlaceHolder.setBackground(getResources().getDrawable(R.drawable.drawable_circle_red));
                    profileBinding.tvUserStatus.setPadding(0, 0, Utils.dpToPx(getActivity(), 6), 0);
                } else {
                    profileBinding.tvUserStatus.setVisibility(View.VISIBLE);
                    profileBinding.tvUserStatus.setText(getString(R.string.active));
                    profileBinding.tvUserStatus.setBackground(getResources().getDrawable(R.drawable.ic_active_bg));
                    profileBinding.tvUserStatus.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    profileBinding.layoutPlaceHolder.setBackground(getResources().getDrawable(R.drawable.drawable_circle_green));
                    profileBinding.tvUserStatus.setPadding(123, 0, 0, 0);
                }
                profileBinding.tvName.setText(response.getUser().getFirstName() + " " + response.getUser().getLastName());
                profileBinding.tvAboutMe.setText(response.getUser().getAboutMe());
                profileBinding.tvJobTitle.setText(response.getUser().getJobtitleName());
                profileBinding.tvLocation.setText(response.getUser().getPreferredLocationName());
                saveUserProfile(response.getUser());
            }
            if (response.getWorkExperience() != null && response.getWorkExperience().getSaveList().size() > 0) {
                goneViews(profileBinding.cellExp.tvAddCertificates, profileBinding.cellExp.tvEditCell);
                inflateExperience(response.getWorkExperience().getSaveList());
            } else {
                profileBinding.expInflater.setVisibility(View.GONE);
                profileBinding.expInflater.removeAllViews();
                visibleView(profileBinding.cellExp.tvAddCertificates, profileBinding.cellExp.tvEditCell);
            }
            if (response.getAffiliationList() != null && response.getAffiliationList().size() > 0) {
                profileBinding.flowLayout.setVisibility(View.VISIBLE);
                goneViews(profileBinding.cellAffiliation.tvAddCertificates, profileBinding.cellAffiliation.tvEditCell);
                profileBinding.flowLayout.removeAllViews();

                for (int i = 0; i < response.getAffiliationList().size(); i++) {
                    if (response.getAffiliationList().get(i).getAffiliationId() == Constants.OTHER_AFFILIATION_ID) {
                        String otherAffiliation = response.getAffiliationList().get(i).getOtherAffiliation();
                        if (otherAffiliation != null && !TextUtils.isEmpty(otherAffiliation)) {
                            if (otherAffiliation.contains(",")) {
                                String[] otherArr = otherAffiliation.split(",");
                                for (String text : otherArr) {
                                    com.appster.dentamatch.databinding.ItemFlowChildBinding flowBinding = DataBindingUtil.bind(LayoutInflater.from(getActivity())
                                            .inflate(R.layout.item_flow_child, profileBinding.flowLayout, false));
                                    flowBinding.flowChild.setText(text.trim());
                                    profileBinding.flowLayout.addView(flowBinding.getRoot());
                                }
                            } else {

                                com.appster.dentamatch.databinding.ItemFlowChildBinding flowBinding = DataBindingUtil.bind(LayoutInflater.from(getActivity())
                                        .inflate(R.layout.item_flow_child, profileBinding.flowLayout, false));
                                flowBinding.flowChild.setText(otherAffiliation);

                                profileBinding.flowLayout.addView(flowBinding.getRoot());
                            }
                        }
                    } else {
                        com.appster.dentamatch.databinding.ItemFlowChildBinding flowBinding = DataBindingUtil.bind(LayoutInflater.from(getActivity())
                                .inflate(R.layout.item_flow_child, profileBinding.flowLayout, false));
                        flowBinding.flowChild.setText(response.getAffiliationList().get(i).getAffiliationName());
                        profileBinding.flowLayout.addView(flowBinding.getRoot());
                    }
                }
            } else {
                profileBinding.flowLayout.setVisibility(View.GONE);
                visibleView(profileBinding.cellAffiliation.tvAddCertificates, profileBinding.cellAffiliation.tvEditCell);
            }
            if (response.getLicence() != null &&
                    !TextUtils.isEmpty(response.getLicence().getLicenseNumber()) &&
                    !TextUtils.isEmpty(response.getLicence().getState())) {
                profileBinding.cellLicence.tvAddCertificates.setVisibility(View.GONE);
                profileBinding.cellLicence.tvEditCell.setVisibility(View.VISIBLE);
                profileBinding.layoutLicenceData.setVisibility(View.GONE);
                profileBinding.tvLicenceNumber.setText(response.getLicence().getLicenseNumber());
                profileBinding.tvLicenceState.setText(response.getLicence().getState());
            } else {
                profileBinding.cellLicence.tvAddCertificates.setVisibility(View.VISIBLE);
                profileBinding.cellLicence.tvEditCell.setVisibility(View.GONE);
                profileBinding.layoutLicenceData.setVisibility(View.GONE);
            }
            if (response.getCertificatesLists() != null && response.getCertificatesLists().size() > 0) {
                inflateCertification(response.getCertificatesLists());
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

    private void inflateSkill(ArrayList<ProfileSkillModel> skillArrayList) {
        profileBinding.keySkillInflater.setVisibility(View.VISIBLE);
        profileBinding.keySkillInflater.removeAllViews();
        ItemProfileSkillBinding skillBinding;

        for (int i = 0; i < skillArrayList.size(); i++) {
            skillBinding = DataBindingUtil.bind(LayoutInflater.from(profileBinding.keySkillInflater.getContext())
                    .inflate(R.layout.item_profile_skill, profileBinding.keySkillInflater, false));
            skillBinding.tvSkillName.setText(skillArrayList.get(i).getSkillsName());

            for (int j = 0; j < skillArrayList.get(i).getChildSkillList().size(); j++) {
                String skName = (skillArrayList.get(i).getChildSkillList().get(j).getSkillsChildName());
                LogUtils.LOGD("OtherSkill>>", skName);

                if (skName.equalsIgnoreCase(Constants.OTHERS)) {
                    if (!StringUtils.isNullOrEmpty(skillArrayList.get(i).getChildSkillList().get(j).getOtherSkills())) {
                        com.appster.dentamatch.databinding.ItemFlowChildBinding flowBinding = DataBindingUtil.bind(LayoutInflater.from(getActivity())
                                .inflate(R.layout.item_flow_child, skillBinding.skillFlowLayout, false));
                        flowBinding.flowChild.setText(skillArrayList.get(i).getChildSkillList().get(j).getOtherSkills());
                        skillBinding.skillFlowLayout.addView(flowBinding.getRoot());
                    }
                } else {
                    com.appster.dentamatch.databinding.ItemFlowChildBinding flowBinding = DataBindingUtil.bind(LayoutInflater.from(getActivity())
                            .inflate(R.layout.item_flow_child, skillBinding.skillFlowLayout, false));
                    flowBinding.flowChild.setText(skillArrayList.get(i).getChildSkillList().get(j).getSkillsChildName());
                    skillBinding.skillFlowLayout.addView(flowBinding.getRoot());
                }
            }
            profileBinding.keySkillInflater.addView(skillBinding.getRoot());
        }
    }

    private void visibleView(TextView tvAdd, TextView tvEdit) {
        if (tvAdd != null) {
            tvAdd.setVisibility(View.VISIBLE);
        }
        if (tvEdit != null) {
            tvEdit.setVisibility(View.GONE);
        }
    }

    private void goneViews(TextView tvAdd, TextView tvEdit) {
        if (tvAdd != null) {
            tvAdd.setVisibility(View.GONE);
        }
        if (tvEdit != null) {
            tvEdit.setVisibility(View.VISIBLE);
        }
    }

    private void inflateExperience(ArrayList<WorkExpRequest> expList) {
        profileBinding.expInflater.setVisibility(View.VISIBLE);
        profileBinding.expInflater.removeAllViews();

        for (int i = 0; i < expList.size(); i++) {
            boolean isNoContactInfo = false;
            ItemProfileWorkExpBinding expBinding
                    = DataBindingUtil.bind(LayoutInflater.from(profileBinding.expInflater.getContext())
                    .inflate(R.layout.item_profile_work_exp, profileBinding.expInflater, false));

            expBinding.tvJobTitle.setText(expList.get(i).getJobTitleName());
            expBinding.tvName.setText(expList.get(i).getOfficeName());
            expBinding.tvAddress.setText(expList.get(i).getOfficeAddress() + "\n" + expList.get(i).getCity());
            if (TextUtils.isEmpty(expList.get(i).getReference1Email()) &&
                    TextUtils.isEmpty(expList.get(i).getReference1Mobile()) &&
                    TextUtils.isEmpty(expList.get(i).getReference1Name())) {
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

            if (TextUtils.isEmpty(expList.get(i).getReference2Email()) &&
                    TextUtils.isEmpty(expList.get(i).getReference2Mobile()) &&
                    TextUtils.isEmpty(expList.get(i).getReference2Name())) {

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


            int months = expList.get(i).getMonthsOfExpereince() % 12;
            int years = expList.get(i).getMonthsOfExpereince() / 12;

            String yearLabel, monthLabel;

            if (years == 1) {
                yearLabel = getString(R.string.txt_single_year);
            } else {
                yearLabel = getString(R.string.txt_multiple_years);
            }

            if (months == 1) {
                monthLabel = getString(R.string.txt_single_month);
            } else {
                monthLabel = getString(R.string.txt_multiple_months);
            }

            if (months == 0) {
                expBinding.tvExpDuration.setText(String.valueOf(years)
                        .concat(yearLabel));
            } else if (years == 0) {
                expBinding.tvExpDuration.setText(String.valueOf(months)
                        .concat(monthLabel));
            } else {
                expBinding.tvExpDuration.setText(String.valueOf(years)
                        .concat(yearLabel)
                        .concat(" ")
                        .concat(String.valueOf(months))
                        .concat(monthLabel));
            }


            profileBinding.expInflater.addView(expBinding.getRoot());
        }

    }

    private void inflateCertification(final ArrayList<CertificatesList> certificateList) {
        profileBinding.certificationInflater.removeAllViews();

        for (int i = 0; i < certificateList.size(); i++) {
            ItemProfileCellCertificateBinding cellCertificateBinding = DataBindingUtil.bind(LayoutInflater.from(profileBinding.certificationInflater.getContext())
                    .inflate(R.layout.item_profile_cell_certificate, profileBinding.certificationInflater, false));
            final View tempView = cellCertificateBinding.getRoot();
            tempView.setTag(i);
            cellCertificateBinding.tvEdit.setId(i);
            cellCertificateBinding.tvCertificateImageName.setVisibility(View.VISIBLE);
            cellCertificateBinding.tvCertificateValidityDate.setVisibility(View.VISIBLE);
            cellCertificateBinding.tvAddCertificates.setTag(i);
            final CertificatesList certificate = certificateList.get(i);
            cellCertificateBinding.tvCertificatesName.setText(certificate.getCertificateName());
            cellCertificateBinding.tvCertificateImageName.setText(certificate.getCertificateName());

            if (StringUtils.isDateNullOrEmpty(certificate.getValidityDate())) {
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

                Picasso.get()
                        .load(certificate.getImage())
                        .placeholder(R.drawable.ic_upload)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(cellCertificateBinding.ivCertificateImage);

                cellCertificateBinding.ivCertificateImage.setOnClickListener(v ->
                        launchImageViewer(v, certificate.getImage()));
            } else {
                cellCertificateBinding.tvAddCertificates.setVisibility(View.VISIBLE);
                cellCertificateBinding.tvEdit.setVisibility(View.GONE);
                cellCertificateBinding.ivCertificateImage.setVisibility(View.GONE);
                cellCertificateBinding.layoutValidationDate.setVisibility(View.GONE);
            }
            cellCertificateBinding.tvEdit.setOnClickListener(view -> {
                Intent intent = new Intent(getActivity(), UpdateCertificateActivity.class);
                intent.putExtra(Constants.INTENT_KEY.DATA, certificateList.get((Integer) tempView.getTag()));
                startActivity(intent);
            });
            cellCertificateBinding.tvAddCertificates.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), UpdateCertificateActivity.class);
                    intent.putExtra(Constants.INTENT_KEY.DATA, certificateList.get((Integer) tempView.getTag()));
                    startActivity(intent);
                }
            });

            profileBinding.certificationInflater.addView(cellCertificateBinding.getRoot());
        }

    }

    private void inflateSchools(ArrayList<ProfileSchoolModel> schoolList) {
        profileBinding.schoolInflater.setVisibility(View.VISIBLE);

        profileBinding.schoolInflater.removeAllViews();
        ItemProfileSchoolingBinding schoolBinding;

        for (int i = 0; i < schoolList.size(); i++) {
            schoolBinding = DataBindingUtil.bind(LayoutInflater.from(profileBinding.schoolInflater.getContext())
                    .inflate(R.layout.item_profile_schooling, profileBinding.schoolInflater, false));
            schoolBinding.tvSchoolName.setText(schoolList.get(i).getSchoolTitle()
                    .concat("(")
                    .concat(String.valueOf(schoolList.get(i).getYearOfGraduation()))
                    .concat(")"));

            schoolBinding.tvCourseName.setText(schoolList.get(i).getSchoolName());
            profileBinding.schoolInflater.addView(schoolBinding.getRoot());
        }
    }

    @Subscribe()
    public void onProfileUpdatedEvent(ProfileUpdatedEvent profileUpdatedEvent) {
        if (profileUpdatedEvent.ismIsProfileUpdated()) {
            getProfileData();
        }
    }

    @Subscribe
    public void onMessage(UnReadNotificationResponseData responseData) {
        if (responseData.getNotificationCount() == 0) {
            profileBinding.tvBatchCount.setVisibility(View.GONE);
        } else {
            profileBinding.tvBatchCount.setVisibility(View.VISIBLE);
            profileBinding.tvBatchCount.setText(responseData.getNotificationCount() > 100 ? getString(R.string.ntf_cnt) : String.valueOf(responseData.getNotificationCount()));
        }
    }

    private void saveUserProfile(UserModel userModel) {
        userModel.setIsVerified(Constants.USER_VERIFIED_STATUS);
        PreferenceUtil.setUserModel(userModel);
    }

    private void getBatchCount() {
        viewModel.requestUnreadNotificationCount();
    }

    private void onSuccessNotificationCountRequest(@Nullable UnReadNotificationCountResponse response) {
        if (response != null) {
            onMessage(response.getUnReadNotificationResponse());
            EventBus.getDefault().post(response);
        }
    }

    private void onSuccessProfileRequest(@Nullable ProfileResponse response) {
        if (response != null) {
            profileResponseData = response.getProfileResponseData();
            if (isAlive()) {
                setViewData(response.getProfileResponseData());
            }
            profileBinding.layoutLicenceData.setVisibility(View.GONE);
        }
    }
}
