/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.onboardtutorial;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;

/**
 * Created by virender on 06/01/17.
 * To inject activity reference.
 */

class OnBoardingAdapter extends PagerAdapter {
    private final Context mContext;
    private final int[] images = {R.drawable.onboard_1, R.drawable.onboard_2, R.drawable.onboard_3, R.drawable.onboard_4};
    private final String[] mTitle;
    private final String[] mDesc;

    private final LayoutInflater mLayoutInflater;

    OnBoardingAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTitle = mContext.getResources().getStringArray(R.array.on_boarding_title);
        mDesc = mContext.getResources().getStringArray(R.array.on_boarding_desc);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_onboarding, container, false);

        ImageView imageView = itemView.findViewById(R.id.ivTutImg);
        TextView tvTitle = itemView.findViewById(R.id.tv_title);
        TextView tvDesc = itemView.findViewById(R.id.tv_desc);
        imageView.setImageResource(images[position]);
        tvTitle.setText(mTitle[position]);
        tvDesc.setText(mDesc[position]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout) object);
    }
}