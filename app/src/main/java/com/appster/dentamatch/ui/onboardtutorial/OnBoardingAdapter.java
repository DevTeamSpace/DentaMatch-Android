package com.appster.dentamatch.ui.onboardtutorial;

import android.content.Context;
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
 */

class OnBoardingAdapter extends PagerAdapter {
    private Context mContext;
    private int images[] = {R.drawable.onboard_1, R.drawable.onboard_2, R.drawable.onboard_3, R.drawable.onboard_4};
    private String mTitle[], mDesc[];

    private LayoutInflater mLayoutInflater;

    OnBoardingAdapter(Context context) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mTitle = mContext.getResources().getStringArray(R.array.onbording_title);
        mDesc = mContext.getResources().getStringArray(R.array.onbording_desc);
    }

    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_onboarding, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.iv_bg_onboarding);
        TextView tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
        TextView tvDesc = (TextView) itemView.findViewById(R.id.tv_desc);
        imageView.setBackgroundResource(images[position]);
        tvTitle.setText(mTitle[position]);
        tvDesc.setText(mDesc[0]);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}