package com.appster.dentamatch.ui.profile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.FragmentProfileBinding;
import com.appster.dentamatch.ui.common.BaseFragment;
import com.appster.dentamatch.ui.settings.SettingActivity;

/**
 * Created by virender on 17/01/17.
 */
public class ProfileFragment extends Fragment implements View.OnClickListener {
    private static int instanceCount = 1;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private FragmentProfileBinding profileBinding;
    private String TAG = "ProfileFragment-";

    public static ProfileFragment newInstance() {
        ProfileFragment frag = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt("instanceCount", instanceCount);
        instanceCount++;
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        View convertView = inflater.inflate(R.layout.fragment_profile, null);
//        profileBinding = FragmentProfileBinding.inflate(inflater, container, false);
        profileBinding= DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        //set variables in Binding

        fragmentManager = getActivity().getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        initViews();
        System.out.println("Profile fragment called");
//        FlurryAgent.logEvent(getString(R.string.settings));

        return profileBinding.getRoot();

    }

    private void initViews() {
        profileBinding.profileHeader.layoutSetting.setVisibility(View.VISIBLE);
        profileBinding.profileHeader.profileMainLayout.setBackgroundResource(R.drawable.bg_register);
        profileBinding.profileHeader.ivSetting.setOnClickListener(this);
        profileBinding.profileHeader.tvEdit.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tv_edit:
                break;
            case R.id.iv_setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;

        }

    }

    //    @Override
    public String getFragmentName() {
        return null;
    }


//    @Override
//    public String getFragmentName() {
//        return null;
//    }
}
