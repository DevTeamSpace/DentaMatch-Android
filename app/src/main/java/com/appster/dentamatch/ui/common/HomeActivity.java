package com.appster.dentamatch.ui.common;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.profile.ProfileFragment;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

/**
 * Created by virender on 17/01/17.
 */
public class HomeActivity extends BaseActivity {

    public int count;
    private String[] ITEMS;
    //   categoryId valid for maximum 6 level
    private AHBottomNavigation bottomBar;
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
        bottomBar = (AHBottomNavigation) findViewById(R.id.ntb_horizontal);
        bottomBar.setTitleTextSize(Utils.convertSpToPixels(10.0f, this), Utils.convertSpToPixels(10.0f, this));
        bottomBar.addItem(new AHBottomNavigationItem(getString(R.string.nav_job), R.drawable.img_nav_jobs));
        bottomBar.addItem(new AHBottomNavigationItem(getString(R.string.nav_tracks), R.drawable.img_nav_track));
        bottomBar.addItem(new AHBottomNavigationItem(getString(R.string.nav_calendar), R.drawable.img_nav_calendar));
        bottomBar.addItem(new AHBottomNavigationItem(getString(R.string.nav_message), R.drawable.img_nav_messages));
        bottomBar.addItem(new AHBottomNavigationItem(getString(R.string.nav_profile), R.drawable.img_nav_profile));

        bottomBar.setDefaultBackgroundColor(ContextCompat.getColor(this, R.color.nav_text_default_color));
        bottomBar.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        bottomBar.setAccentColor(ContextCompat.getColor(this, R.color.cerulean_color));
        bottomBar.setInactiveColor(ContextCompat.getColor(this, R.color.gray_nav_bottom_unselected));

        bottomBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        showToast("Under Development");
                        break;

                    case 1:
                        showToast("Under Development");

                        break;

                    case 2:
                        showToast("Under Development");

                        break;

                    case 3:
                        showToast("Under Development");
                        break;

                    case 4:
                        launchProfileFragment();
                        break;
                }
                return true;
            }
        });

        bottomBar.setCurrentItem(4);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setTitleMargins(0);
                setTitleMargins(1);
                setTitleMargins(2);
                setTitleMargins(3);
            }
        }, 1000);

//        final String[] colors = getResources().getStringArray(R.array.default_preview);
//        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
//        navigationTabBar.setInactiveColor(ContextCompat.getColor(this,R.color.gray_nav_bottom_unselected));
//        navigationTabBar.setActiveColor(ContextCompat.getColor(this,R.color.cerulean_color));
//        int spSize = (int) (11 * getApplicationContext().getResources().getDisplayMetrics().density);
//        navigationTabBar.setTitleSize(spSize);
//        navigationTabBar.setBackgroundColor(ContextCompat.getColor(this,R.color.black_color));
//        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
//        models.add(
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.img_nav_jobs),
//                        Color.parseColor(colors[0]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.nav_job_selected))
//                        .title(getResources().getString(R.string.nav_job))
//                        .build()
//        );
//        models.add(
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.img_nav_track),
//                        Color.parseColor(colors[0]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.nav_track_selected))
//                        .title(getResources().getString(R.string.nav_tracks))
//                        .build()
//        );
//        models.add(
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.img_nav_calendar),
//                        Color.parseColor(colors[0]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.nav_calendar_selected))
//
//                        .title(getResources().getString(R.string.nav_calendar))
//                        .build()
//        );
//        models.add(
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.img_nav_messages),
//                        Color.parseColor(colors[0]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.nav_message_selected))
//
//                        .title(getResources().getString(R.string.nav_message))
//                        .build());
//        models.add(
//                new NavigationTabBar.Model.Builder(
//                        getResources().getDrawable(R.drawable.img_nav_profile),
//                        Color.parseColor(colors[0]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.nav_profile_slected))
//
//                        .title(getResources().getString(R.string.nav_profile))
//                        .build()
//        );
//
//        navigationTabBar.setModels(models);


//        //IMPORTANT: ENABLE SCROLL BEHAVIOUR IN COORDINATOR LAYOUT
//        navigationTabBar.setBehaviorEnabled(false);
//
//        navigationTabBar.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
//            @Override
//            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
////                textHeader.setText(ITEMS[index]);
//                if (index == 0) {
////                    viewShadow.setVisibility(View.GONE);
////                    adapterViewPager.notifyDataSetChanged();
//                } else {
////                    viewShadow.setVisibility(View.VISIBLE);
//                }
////                mTxvToolbarCenter.setText(ITEMS[index]);
//                navigationTabBar.getModels().get(index).hideBadge();
////                mTxvToolbarCenter.setText(ITEMS[position]);
////
//            }
//
//            @Override
//            public void onEndTabSelected(NavigationTabBar.Model model, int index) {
//
//            }
//        });


    }

    private void setTitleMargins(int bottomBarPosition) {
        switch (bottomBarPosition) {

            case 0:
                TextView tvTitle0 = (TextView) bottomBar.getViewAtPosition(0).findViewById(R.id.bottom_navigation_item_title);
                FrameLayout.LayoutParams llp0 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                llp0.setMargins(0, 0, 0, 8); // llp.setMargins(left, top, right, bottom);
                llp0.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                tvTitle0.setLayoutParams(llp0);
                break;

            case 1:
                TextView tvTitle1 = (TextView) bottomBar.getViewAtPosition(1).findViewById(R.id.bottom_navigation_item_title);
                FrameLayout.LayoutParams llp1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                llp1.setMargins(0, 0, 0, 8); // llp.setMargins(left, top, right, bottom);
                llp1.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                tvTitle1.setLayoutParams(llp1);
                break;

            case 2:
                TextView tvTitle2 = (TextView) bottomBar.getViewAtPosition(2).findViewById(R.id.bottom_navigation_item_title);
                FrameLayout.LayoutParams llp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                llp2.setMargins(0, 0, 0, 8); // llp.setMargins(left, top, right, bottom);
                llp2.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                tvTitle2.setLayoutParams(llp2);
                break;

            case 3:
                TextView tvTitle3 = (TextView) bottomBar.getViewAtPosition(3).findViewById(R.id.bottom_navigation_item_title);
                FrameLayout.LayoutParams llp3 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                llp3.setMargins(0, 0, 0, 8); // llp.setMargins(left, top, right, bottom);
                llp3.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                tvTitle3.setLayoutParams(llp3);
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
