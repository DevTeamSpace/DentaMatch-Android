package com.appster.dentamatch.ui.onboardtutorial;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityOnboardingBinding;
import com.appster.dentamatch.ui.auth.LoginActivity;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.LogUtils;

/**
 * Created by virender on 06/01/17.
 */
public class OnBoardingActivity extends BaseActivity implements View.OnClickListener {
    private ActivityOnboardingBinding mBinder;
    private OnBoardingAdapter onBoardingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_onboarding);
        onBoardingAdapter = new OnBoardingAdapter(this);
        mBinder.pagerOnboarding.setAdapter(onBoardingAdapter);
        mBinder.tvSkipOnboarding.setOnClickListener(this);
        mBinder.pagerOnboarding.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                LogUtils.LOGD("tag", "posiition--onPageScrolled--" + position);
                if (position == 3) {
                    mBinder.tvSkipOnboarding.setText(getString(R.string.get_started));
                } else {
                    mBinder.tvSkipOnboarding.setText(getString(R.string.skip_button));

                }
                setDotSlection(position);
            }

            @Override
            public void onPageSelected(int position) {
                LogUtils.LOGD("tag", "posiition-select---" + position);

            }

            @Override
            public void onPageScrollStateChanged(int position) {
                LogUtils.LOGD("tag", "posiition--scroll state--" + position);
//                if(position==4){
//                    mBinder.tvSkipOnboarding.setText(getString(R.string.get_started));
//                }else{
//                    mBinder.tvSkipOnboarding.setText(getString(R.string.skip_button));
//
//                }

            }
        });
    }

    private void setDotSlection(int pos) {
        mBinder.ivDot1.setBackgroundResource(R.drawable.shape_dot_normal);
        mBinder.ivDot2.setBackgroundResource(R.drawable.shape_dot_normal);
        mBinder.ivDot3.setBackgroundResource(R.drawable.shape_dot_normal);
        mBinder.ivDot4.setBackgroundResource(R.drawable.shape_dot_normal);
        if (pos == 0) {
            mBinder.ivDot1.setBackgroundResource(R.drawable.shape_dot_selected);
            mBinder.ivDot2.setBackgroundResource(R.drawable.shape_dot_normal);
            mBinder.ivDot3.setBackgroundResource(R.drawable.shape_dot_normal);
            mBinder.ivDot4.setBackgroundResource(R.drawable.shape_dot_normal);
        } else if (pos == 1) {
            mBinder.ivDot1.setBackgroundResource(R.drawable.shape_dot_normal);
            mBinder.ivDot2.setBackgroundResource(R.drawable.shape_dot_selected);
            mBinder.ivDot3.setBackgroundResource(R.drawable.shape_dot_normal);
            mBinder.ivDot4.setBackgroundResource(R.drawable.shape_dot_normal);
        } else if (pos == 2) {
            mBinder.ivDot1.setBackgroundResource(R.drawable.shape_dot_normal);
            mBinder.ivDot2.setBackgroundResource(R.drawable.shape_dot_normal);
            mBinder.ivDot3.setBackgroundResource(R.drawable.shape_dot_selected);
            mBinder.ivDot4.setBackgroundResource(R.drawable.shape_dot_normal);
        } else if (pos == 3) {
            mBinder.ivDot1.setBackgroundResource(R.drawable.shape_dot_normal);
            mBinder.ivDot2.setBackgroundResource(R.drawable.shape_dot_normal);
            mBinder.ivDot3.setBackgroundResource(R.drawable.shape_dot_normal);
            mBinder.ivDot4.setBackgroundResource(R.drawable.shape_dot_selected);

        }

    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_skip_onboarding:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

        }
    }
}
