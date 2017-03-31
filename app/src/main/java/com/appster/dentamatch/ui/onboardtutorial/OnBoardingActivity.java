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
import com.appster.dentamatch.util.PreferenceUtil;

/**
 * Created by virender on 06/01/17.
 */
public class OnBoardingActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ActivityOnboardingBinding mBinder;
    private OnBoardingAdapter onBoardingAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_onboarding);
        onBoardingAdapter = new OnBoardingAdapter(this);
        mBinder.pagerOnboarding.setAdapter(onBoardingAdapter);
        mBinder.tvSkipOnboarding.setOnClickListener(this);
        mBinder.pagerOnboarding.addOnPageChangeListener(this);
    }

    private void setDotSelection(int pos) {
        mBinder.ivDot1.setBackgroundResource(R.drawable.shape_dot_normal);
        mBinder.ivDot2.setBackgroundResource(R.drawable.shape_dot_normal);
        mBinder.ivDot3.setBackgroundResource(R.drawable.shape_dot_normal);
        mBinder.ivDot4.setBackgroundResource(R.drawable.shape_dot_normal);

        switch (pos) {
            case 0:
                mBinder.ivDot1.setBackgroundResource(R.drawable.shape_dot_selected);
                mBinder.ivDot2.setBackgroundResource(R.drawable.shape_dot_normal);
                mBinder.ivDot3.setBackgroundResource(R.drawable.shape_dot_normal);
                mBinder.ivDot4.setBackgroundResource(R.drawable.shape_dot_normal);
                break;

            case 1:
                mBinder.ivDot1.setBackgroundResource(R.drawable.shape_dot_normal);
                mBinder.ivDot2.setBackgroundResource(R.drawable.shape_dot_selected);
                mBinder.ivDot3.setBackgroundResource(R.drawable.shape_dot_normal);
                mBinder.ivDot4.setBackgroundResource(R.drawable.shape_dot_normal);
                break;

            case 2:
                mBinder.ivDot1.setBackgroundResource(R.drawable.shape_dot_normal);
                mBinder.ivDot2.setBackgroundResource(R.drawable.shape_dot_normal);
                mBinder.ivDot3.setBackgroundResource(R.drawable.shape_dot_selected);
                mBinder.ivDot4.setBackgroundResource(R.drawable.shape_dot_normal);
                break;

            case 3:
                mBinder.ivDot1.setBackgroundResource(R.drawable.shape_dot_normal);
                mBinder.ivDot2.setBackgroundResource(R.drawable.shape_dot_normal);
                mBinder.ivDot3.setBackgroundResource(R.drawable.shape_dot_normal);
                mBinder.ivDot4.setBackgroundResource(R.drawable.shape_dot_selected);
                break;

            default:
                break;

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
                PreferenceUtil.setIsOnBoarding(true);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 3) {
            mBinder.tvSkipOnboarding.setText(getString(R.string.get_started));
        } else {
            mBinder.tvSkipOnboarding.setText(getString(R.string.skip_button));
        }
        setDotSelection(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
