package com.appster.dentamatch.ui.profile;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityWorkExperinceBinding;
import com.appster.dentamatch.interfaces.YearSelectionListener;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.BottomSheetPicker;

/**
 * Created by virender on 04/01/17.
 */
public class WorkExperienceActivity extends BaseActivity implements View.OnClickListener, YearSelectionListener {
    //    private ActivityT mBinder;
    private ActivityWorkExperinceBinding mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_work_experince);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarWorkExp.tvToolbarGeneralLeft.setText(getString(R.string.header_work_exp));

        mBinder.btnNextWorkExp.setOnClickListener(this);
        mBinder.tvExperinceWorkExp.setOnClickListener(this);
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_next_work_exp:
                startActivity(new Intent(this, WorkExperienceDetailActivity.class));
                break;
            case R.id.tv_experince_work_exp:
                new BottomSheetPicker(this, this, 0, 0);
                break;
        }
    }

    @Override
    public void onExperienceSection(int year, int month) {
        mBinder.tvExperinceWorkExp.setText(year + " " + getString(R.string.year) + " " + month + " " + getString(R.string.month));
    }
}
