package com.appster.dentamatch.widget.bottomsheet;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.interfaces.ImageSelectedListener;
import com.appster.dentamatch.widget.CustomTextView;

/**
 * Created by virender on 02/01/17.
 * To inject activity reference.
 */
public class BottomSheetView {
    private BottomSheetDialog mBottomSheetDialog;
    private final ImageSelectedListener mImageSelectedListener;

    public BottomSheetView(Context context, ImageSelectedListener imageSelectedListener) {
        mBottomSheetDialog = new BottomSheetDialog(context);
        mImageSelectedListener = imageSelectedListener;
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.bottom_sheet_select_image, null);
        CustomTextView tvTakePhoto = view.findViewById(R.id.bottom_sheet_tv_take_photo);
        CustomTextView tvChooseGallery = view.findViewById(R.id.bottom_sheet_tv_gallery);

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBottomSheetDialog != null) {
                    mBottomSheetDialog.dismiss();
                    mImageSelectedListener.cameraClicked();
                }
            }
        });
        tvChooseGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mBottomSheetDialog != null) {
                    mBottomSheetDialog.dismiss();
                    mImageSelectedListener.galleryClicked();

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

