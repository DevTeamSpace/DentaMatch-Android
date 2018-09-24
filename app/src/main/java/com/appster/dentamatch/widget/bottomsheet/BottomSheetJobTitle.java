/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.widget.bottomsheet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.NumberPicker;

import com.appster.dentamatch.R;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.model.JobTitleListModel;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.StringUtils;
import com.appster.dentamatch.widget.CustomTextView;

/**
 * Created by virender on 15/01/17.
 * To inject activity reference.
 */
public class BottomSheetJobTitle {

    private BottomSheetDialog mBottomSheetDialog;
    private final JobTitleSelectionListener mJobTitleSelectionListener;

    public BottomSheetJobTitle(final Context context, JobTitleSelectionListener jobTitleSelectionListener, final int position) {
        mJobTitleSelectionListener = jobTitleSelectionListener;
        mBottomSheetDialog = new BottomSheetDialog(context);
        String[] jobTitle = new String[PreferenceUtil.getJobTitleList() != null ? PreferenceUtil.getJobTitleList().size() : 0];
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.bottom_sheet_job_title, null);
        CustomTextView tvCancel = view.findViewById(R.id.bottom_sheet_picker_tv_cancel);
        CustomTextView tvDone = view.findViewById(R.id.bottom_sheet_picker_tv_done);
        final NumberPicker pickerTitle = view.findViewById(R.id.bottom_sheet_picker_job_title);
        pickerTitle.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        pickerTitle.setMinValue(0);
        if (PreferenceUtil.getJobTitleList() != null && PreferenceUtil.getJobTitleList().size() > 0) {
            pickerTitle.setMaxValue(PreferenceUtil.getJobTitleList().size() - 1);
            if (position != -1) {
                pickerTitle.setValue(position);
            }

            for (int i = 0; i < PreferenceUtil.getJobTitleList().size(); i++) {
                JobTitleListModel jlm = PreferenceUtil.getJobTitleList().get(i);
                jobTitle[i] = StringUtils.isNullOrEmpty(jlm.getShortName()) ? jlm.getJobTitle() : jlm.getShortName();
            }
            pickerTitle.setDisplayedValues(jobTitle);
        }
        pickerTitle.setWrapSelectorWheel(false);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetDialog != null) {
                    mBottomSheetDialog.dismiss();
                }
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mBottomSheetDialog != null) {
                    mBottomSheetDialog.dismiss();

                }
                if (PreferenceUtil.getJobTitleList() != null) {
                    JobTitleListModel jlm = PreferenceUtil.getJobTitleList().get(pickerTitle.getValue());
                    mJobTitleSelectionListener.onJobTitleSelection(StringUtils.isNullOrEmpty(jlm.getShortName()) ? jlm.getJobTitle() : jlm.getShortName()
                            , jlm.getId(), pickerTitle.getValue(), jlm.getIsLicenseRequired());
                }
            }
        });

        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.show();
        mBottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBottomSheetDialog = null;
            }
        });
    }
}
