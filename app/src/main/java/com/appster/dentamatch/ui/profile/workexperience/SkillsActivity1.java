package com.appster.dentamatch.ui.profile.workexperience;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivitySkillsBinding;
import com.appster.dentamatch.ui.common.BaseActivity;
//import com.special.ResideMenu.ResideMenu;
//import com.special.ResideMenu.ResideMenuItem;

/**
 * Created by ram on 12/01/17.
 */
public class SkillsActivity1 extends BaseActivity implements View.OnClickListener {
    private ActivitySkillsBinding mBinder;
//    private ResideMenu resideMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_skills);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarSkills.ivToolBarLeft.setOnClickListener(this);

        mBinder.toolbarSkills.tvToolbarGeneralLeft.setText(getString(R.string.header_skills_exp));

        // attach to current activity;
//        resideMenu = new ResideMenu(this);
//        resideMenu.setBackground(R.drawable.gradient_shape_register);
//        resideMenu.attachToActivity(this);
//        resideMenu.openMenu(ResideMenu.DIRECTION_RIGHT);
//
//        // create menu items;
//        String titles[] = { "Home", "Profile", "Calendar", "Settings" };
//        int icon[] = { R.drawable.profile_pic_placeholder, R.drawable.profile_pic_placeholder,
//                R.drawable.profile_pic_placeholder, R.drawable.profile_pic_placeholder };
//
//        for (int i = 0; i < titles.length; i++){
//            ResideMenuItem item = new ResideMenuItem(this, icon[i], titles[i]);
//            item.setOnClickListener(this);
//            resideMenu.addMenuItem(item,  ResideMenu.DIRECTION_RIGHT); // or  ResideMenu.DIRECTION_RIGHT
//        }
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
////        return resideMenu.dispatchTouchEvent(ev);
//        return null;
//    }

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

        }
    }
}
