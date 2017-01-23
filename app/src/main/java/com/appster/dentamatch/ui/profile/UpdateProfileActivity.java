package com.appster.dentamatch.ui.profile;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityUpdateProfileBinding;
import com.appster.dentamatch.network.request.profile.ProfileUpdateRequest;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Utils;

/**
 * Created by virender on 21/01/17.
 */
public class UpdateProfileActivity extends BaseActivity {

    ActivityUpdateProfileBinding mBinding;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding= DataBindingUtil.setContentView(this,R.layout.activity_update_profile);
    }

    @Override
    public String getActivityName() {
        return null;
    }


    private void prepareRquest(){
        ProfileUpdateRequest  request=new ProfileUpdateRequest();
        request.setAboutMe(Utils.getStringFromEditText(mBinding.etDesc));
        request.setFirstName(Utils.getStringFromEditText(mBinding.etFname));
        request.setLastName(Utils.getStringFromEditText(mBinding.etLname));
        request.setPreferredJobLocation(Utils.getStringFromEditText(mBinding.etLocation));


    }
}
