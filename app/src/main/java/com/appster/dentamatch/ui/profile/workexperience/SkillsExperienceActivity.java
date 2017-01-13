package com.appster.dentamatch.ui.profile.workexperience;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySkillExpBinding;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

/**
 * Created by ram on 12/01/17.
 */
public class SkillsExperienceActivity extends BaseActivity implements View.OnClickListener {
    private ActivitySkillExpBinding mBinder;
    private ResideMenu resideMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_skill_exp);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarWorkExpList.ivToolBarLeft.setOnClickListener(this);
        mBinder.textDummy.setOnClickListener(this);

        mBinder.toolbarWorkExpList.tvToolbarGeneralLeft.setText(getString(R.string.header_skills_exp));

        // attach to current activity;
        resideMenu = new ResideMenu(this);
        resideMenu.setBackground(R.drawable.gradient_shape_register);
        resideMenu.attachToActivity(this);

        // create menu items;
        String titles[] = { "Home", "Profile", "Calendar", "Settings" };
        int icon[] = { R.drawable.profile_pic_placeholder, R.drawable.profile_pic_placeholder,
                R.drawable.profile_pic_placeholder, R.drawable.profile_pic_placeholder };

        for (int i = 0; i < titles.length; i++){
            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
            item.setOnClickListener(this);
            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_LEFT); // or  ResideMenu.DIRECTION_RIGHT
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev);
    }

    @Override
    public String getActivityName() {
        return null;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                hideKeyboard();
                onBackPressed();

                break;

            case R.id.textDummy:

                break;
        }
    }
}
