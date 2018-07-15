/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.common;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityImageViewBinding;
import com.appster.dentamatch.util.Constants;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by bawenderyandra on 18/04/17.
 * To inject activity reference.
 */

public class ImageViewingActivity extends BaseActivity implements View.OnClickListener {
    private ActivityImageViewBinding mBinding;
    private String mPicUrl;

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_image_view);
        mBinding.toolbarImageViewing.ivToolBarLeft.setOnClickListener(this);

        if(getIntent().hasExtra(Constants.EXTRA_PIC)){
            mBinding.imageProgress.setVisibility(View.VISIBLE);
            mPicUrl = getIntent().getStringExtra(Constants.EXTRA_PIC);

            Picasso.with(this)
                    .load(mPicUrl)
                    .placeholder(R.drawable.ic_image_preview_placeholder)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            mBinding.ivPic.setImageBitmap(bitmap);
                            mBinding.imageProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {
                            mBinding.imageProgress.setVisibility(View.GONE);
                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });
        }

    }

    @Override
    public void onClick(View v) {
        onBackPressed();
    }
}
