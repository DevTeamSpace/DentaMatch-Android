package com.appster.dentamatch.widget.bottomsheet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.DatePicker;
import android.widget.NumberPicker;

import com.appster.dentamatch.R;
import com.appster.dentamatch.interfaces.DateSelectedListener;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.CustomTextView;

/**
 * Created by virender on 10/01/17.
 * To inject activity reference.
 */
public class BottomSheetDatePicker implements DatePicker.OnDateChangedListener {
    private BottomSheetDialog mBottomSheetDialog;
    private final DateSelectedListener mDateSelectedListener;
    private int currentYear, currentMonth, currentDay;
    private final Context mContext;

    public BottomSheetDatePicker(final Context context, final DateSelectedListener dateSelectedListener, final int position) {
        mDateSelectedListener = dateSelectedListener;
        mBottomSheetDialog = new BottomSheetDialog(context);
        mContext = context;
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.layout_bottom_sheet_date_picker, null);
        CustomTextView tvCancel = view.findViewById(R.id.bottom_sheet_picker_tv_cancel);
        CustomTextView tvDone = view.findViewById(R.id.bottom_sheet_picker_tv_done);
        final DatePicker datePicker = view.findViewById(R.id.bottom_sheet_date_picker);
        datePicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        setPickerInfo(datePicker);
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

                int dayPickerDay = datePicker.getDayOfMonth();

                if (currentYear < datePicker.getYear()) {

                    if (mBottomSheetDialog != null) {
                        mBottomSheetDialog.dismiss();

                    }
                    selectedDate(datePicker, position);

                } else if (currentYear == datePicker.getYear() && (currentMonth < datePicker.getMonth())) {
                    selectedDate(datePicker, position);


                    if (mBottomSheetDialog != null) {
                        mBottomSheetDialog.dismiss();

                    }

                } else if (currentYear == datePicker.getYear() && currentMonth == datePicker.getMonth() && currentDay <=dayPickerDay) {
                    selectedDate(datePicker, position);

                    if (mBottomSheetDialog != null) {
                        mBottomSheetDialog.dismiss();

                    }
                } else {

                    Utils.showToast(mContext, mContext.getString(R.string.alert_pervious_date));

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

    private void setPickerInfo(DatePicker datePicker) {
        currentYear = datePicker.getYear();
        currentMonth = datePicker.getMonth();
        currentDay = datePicker.getDayOfMonth();
        datePicker.init(currentYear, currentMonth, currentDay, this);
    }

    private void selectedDate(DatePicker datePicker, int pos) {
        mDateSelectedListener.onDateSelection(datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth(), pos);
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {

    }
}
