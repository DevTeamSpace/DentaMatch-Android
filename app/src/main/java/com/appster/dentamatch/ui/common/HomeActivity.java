package com.appster.dentamatch.ui.common;

import android.location.Location;
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
import com.appster.dentamatch.model.LocationEvent;
import com.appster.dentamatch.ui.calendar.CalendarFragment;
import com.appster.dentamatch.ui.messages.MessagesListFragment;
import com.appster.dentamatch.ui.profile.ProfileFragment;
import com.appster.dentamatch.ui.searchjob.JobsFragment;
import com.appster.dentamatch.ui.tracks.TrackFragment;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LocationUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by virender on 17/01/17.
 */
public class HomeActivity extends BaseActivity {

    //   categoryId valid for maximum 6 level
    private AHBottomNavigation bottomBar;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;

    private ProfileFragment mProfileFragment;
    private JobsFragment mJobsFragment;
    private MessagesListFragment mMessagesFragment;
    private CalendarFragment mCalendarFragment;
    private TrackFragment mTrackFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
        setContentView(R.layout.activity_home);
        initViews();

        /**
         * Launch job search fragment if redirected from search activity.
         */
        if (getIntent().hasExtra(Constants.EXTRA_SEARCH_JOB)) {
            bottomBar.setCurrentItem(0);
        }

        /**
         * Retrieve user's current location.
         */
        LocationUtils.addFragment(this);

    }


    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();

    }

    @Subscribe
    public void onEvent(LocationEvent locationEvent) {
        Location location = locationEvent.getMessage();
        PreferenceUtil.setUserCurrentLocation(location);
    }
    /**
     * initViews is used to initialize this view at app launch
     */
    private void initViews() {
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
                        if (mJobsFragment != null) {
                            pushFragment(mJobsFragment, null, ANIMATION_TYPE.FADE);
                        } else {
                            mJobsFragment = JobsFragment.newInstance();
                            pushFragment(mJobsFragment, null, ANIMATION_TYPE.FADE);
                        }
                        break;

                    case 1:
                        if (mTrackFragment != null) {
                            pushFragment(mTrackFragment, null, ANIMATION_TYPE.FADE);
                        } else {
                            mTrackFragment = TrackFragment.newInstance();
                            pushFragment(mTrackFragment, null, ANIMATION_TYPE.FADE);
                        }

                        break;

                    case 2:
                        if (mCalendarFragment != null) {
                            pushFragment(mCalendarFragment, null, ANIMATION_TYPE.FADE);
                        } else {
                            mCalendarFragment = CalendarFragment.newInstance();
                            pushFragment(mCalendarFragment, null, ANIMATION_TYPE.FADE);
                        }

                        break;

                    case 3:
                        if (mMessagesFragment != null) {
                            pushFragment(mMessagesFragment, null, ANIMATION_TYPE.FADE);
                        } else {
                            mMessagesFragment = MessagesListFragment.newInstance();
                            pushFragment(mMessagesFragment, null, ANIMATION_TYPE.FADE);
                        }
                        break;

                    case 4:
                        if (mProfileFragment != null) {
                            pushFragment(mProfileFragment, null, ANIMATION_TYPE.FADE);
                        } else {
                            mProfileFragment = ProfileFragment.newInstance();
                            pushFragment(mProfileFragment, null, ANIMATION_TYPE.FADE);
                        }
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

//    @Override
//    protected void onStart() {
//        super.onResume();
//        /**
//         * Connect Socket for chatting initialization.
//         */
//        SocketManager.getInstance(this).connect();
//    }
//
//    @Override
//    protected void onStop() {
//        /**
//         * Socket disconnected.
//         */
//        SocketManager.getInstance(this).disconnect();
//        super.onStop();
//    }

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
