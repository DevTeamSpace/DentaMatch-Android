package com.appster.dentamatch.ui.common;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.navigationtabbar.NavigationTabBar;
import com.appster.dentamatch.ui.profile.ProfileFragment;
import com.appster.dentamatch.util.Constants;

import java.util.ArrayList;

/**
 * Created by virender on 17/01/17.
 */
public class HomeActivity extends BaseActivity {

    public int count;
    private String[] ITEMS;
    //   categoryId valid for maximum 6 level
    private NavigationTabBar navigationTabBar;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ITEMS = new String[]{getString(R.string.nav_job), getString(R.string.nav_tracks), getString(R.string.nav_calendar), getString(R.string.nav_message), getString(R.string.nav_profile)};
        initUI();

    }


    /**
     * initUI is used to initialize this view at app launch
     */
    private void initUI() {
        count = 0;
        fragmentManager = getSupportFragmentManager();
        final String[] colors = getResources().getStringArray(R.array.default_preview);
        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        navigationTabBar.setInactiveColor(getResources().getColor(R.color.white_color));
        navigationTabBar.setActiveColor(getResources().getColor(R.color.black_color));
        int spSize = (int) (11 * getApplicationContext().getResources().getDisplayMetrics().density);
        navigationTabBar.setTitleSize(spSize);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.nav_job_selected),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.nav_job_selected))
                        .title(getResources().getString(R.string.nav_job))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.nav_track_selected),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.nav_track_selected))
                        .title(getResources().getString(R.string.nav_tracks))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.nav_calendar_selected),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.nav_calendar_selected))

                        .title(getResources().getString(R.string.nav_calendar))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.nav_message_selected),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.nav_message_selected))

                        .title(getResources().getString(R.string.nav_message))
                        .build());
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.nav_profile),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.nav_profile_slected))

                        .title(getResources().getString(R.string.nav_profile))
                        .build()
        );

        navigationTabBar.setModels(models);
//        navigationTabBar.setViewPager(viewPager, 0);


        //IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
        navigationTabBar.setBehaviorEnabled(false);

        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
//                textHeader.setText(ITEMS[index]);
                if (index == 0) {
//                    viewShadow.setVisibility(View.GONE);
//                    adapterViewPager.notifyDataSetChanged();
                } else {
//                    viewShadow.setVisibility(View.VISIBLE);
                }
//                mTxvToolbarCenter.setText(ITEMS[index]);
                navigationTabBar.getModels().get(index).hideBadge();
//                mTxvToolbarCenter.setText(ITEMS[position]);
//
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {

            }
        });
        launchProfileFragment();
    }


    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (viewPager != null)
//            textHeader.setText(ITEMS[viewPager.getCurrentItem()]);

    }

    private void launchProfileFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        String fragTagName = Constants.FRAGMENT_NAME.PROFILE_FRAGMENT;
        Fragment fragment = ProfileFragment.newInstance();
        fragmentTransaction.replace(R.id.fragment_container, fragment, fragTagName)
                .addToBackStack(fragTagName)
                .commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
