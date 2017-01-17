package com.appster.dentamatch.widget.bottomsheet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.NumberPicker;

import com.appster.dentamatch.R;
import com.appster.dentamatch.interfaces.JobTitleSelectionListener;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.CustomTextView;

/**
 * Created by virender on 15/01/17.
 */
public class BottomSheetJobTitle {

    private BottomSheetDialog mBottomSheetDialog;
    private String jobTitle[];
    private JobTitleSelectionListener mJobTitleSelectionListener;

    public BottomSheetJobTitle(final Context context, JobTitleSelectionListener jobTitleSelectionListener, int postion) {
        mJobTitleSelectionListener = jobTitleSelectionListener;
        mBottomSheetDialog = new BottomSheetDialog(context);
        jobTitle = new String[PreferenceUtil.getJobTitleList().size()];
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.bottom_sheet_job_title, null);
        CustomTextView tvCancel = (CustomTextView) view.findViewById(R.id.bottom_sheet_picker_tv_cancel);
        CustomTextView tvDone = (CustomTextView) view.findViewById(R.id.bottom_sheet_picker_tv_done);
        final NumberPicker pickerTitle = (NumberPicker) view.findViewById(R.id.bottom_sheet_picker_job_title);
        pickerTitle.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        pickerTitle.setMinValue(0);
        if (PreferenceUtil.getJobTitleList() != null&&PreferenceUtil.getJobTitleList().size()>0) {
            pickerTitle.setMaxValue(PreferenceUtil.getJobTitleList().size() - 1);
            if (postion != -1) {
                pickerTitle.setValue(postion);
            }

            for (int i = 0; i < PreferenceUtil.getJobTitleList().size(); i++) {
                jobTitle[i] = PreferenceUtil.getJobTitleList().get(i).getJobTitle();
            }
            pickerTitle.setDisplayedValues(jobTitle);
        }
        pickerTitle.setWrapSelectorWheel(true);
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

                mJobTitleSelectionListener.onJobTitleSelection(PreferenceUtil.getJobTitleList().get(pickerTitle.getValue()).getJobTitle(),PreferenceUtil.getJobTitleList().get(pickerTitle.getValue()).getId(),pickerTitle.getValue());

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
