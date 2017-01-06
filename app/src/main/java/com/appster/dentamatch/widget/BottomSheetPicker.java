package com.appster.dentamatch.widget;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.appster.dentamatch.R;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.util.Utils;

/**
 * Created by virender on 04/01/17.
 */
public class BottomSheetPicker {
    private BottomSheetDialog mBottomSheetDialog;
    private YearSelectionListener mYearslYearSelectionListener;

    public BottomSheetPicker(final Context context, YearSelectionListener yearSelectionListener, int year, int month) {
        mYearslYearSelectionListener = yearSelectionListener;
        mBottomSheetDialog = new BottomSheetDialog(context);
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.bottom_sheet_picker, null);
        CustomTextView tvCancel = (CustomTextView) view.findViewById(R.id.bottom_sheet_picker_tv_cancel);
        CustomTextView tvDone = (CustomTextView) view.findViewById(R.id.bottom_sheet_picker_tv_done);
        final NumberPicker pickerYear = (NumberPicker) view.findViewById(R.id.bottom_sheet_picker_year);
        final NumberPicker pickerMonth = (NumberPicker) view.findViewById(R.id.bottom_sheet_picker_month);
        pickerYear.setMinValue(0);
        pickerYear.setMaxValue(20);
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
                Utils.showToast(context, "year is--" + pickerMonth.getValue());
                mYearslYearSelectionListener.onExperienceSection(pickerYear.getValue(), pickerMonth.getValue());

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
