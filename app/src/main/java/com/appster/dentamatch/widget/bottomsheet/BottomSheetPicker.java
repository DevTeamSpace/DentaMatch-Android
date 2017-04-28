package com.appster.dentamatch.widget.bottomsheet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.NumberPicker;

import com.appster.dentamatch.R;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.CustomTextView;

/**
 * Created by virender on 04/01/17.
 */
public class BottomSheetPicker {
    private BottomSheetDialog mBottomSheetDialog;
    private YearSelectionListener mYearsYearSelectionListener;

    public BottomSheetPicker(final Context context, YearSelectionListener yearSelectionListener, int year, int month) {
        mYearsYearSelectionListener = yearSelectionListener;
        mBottomSheetDialog = new BottomSheetDialog(context);
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.bottom_sheet_picker, null);
        CustomTextView tvCancel = (CustomTextView) view.findViewById(R.id.bottom_sheet_picker_tv_cancel);
        CustomTextView tvDone = (CustomTextView) view.findViewById(R.id.bottom_sheet_picker_tv_done);
        final NumberPicker pickerYear = (NumberPicker) view.findViewById(R.id.bottom_sheet_picker_year);
        final NumberPicker pickerMonth = (NumberPicker) view.findViewById(R.id.bottom_sheet_picker_month);
        pickerMonth.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        pickerYear.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        pickerYear.setMinValue(0);
        pickerYear.setMaxValue(30);
        pickerMonth.setMinValue(0);
        pickerMonth.setMaxValue(11);
        pickerMonth.setValue(month);
        pickerYear.setValue(year);
        pickerMonth.setWrapSelectorWheel(true);
        pickerYear.setWrapSelectorWheel(true);
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
                if (pickerYear.getValue() == 0 && pickerMonth.getValue() == 0) {
                    Utils.showToast(context, context.getString(R.string.invalid_exp_selection));
                } else {
                    mYearsYearSelectionListener.onExperienceSection(pickerYear.getValue(), pickerMonth.getValue());
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
