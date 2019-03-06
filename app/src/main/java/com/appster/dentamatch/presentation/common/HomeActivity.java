/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.common;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.base.BaseLoadingActivity;
import com.appster.dentamatch.chat.SocketManager;
import com.appster.dentamatch.eventbus.LocationEvent;
import com.appster.dentamatch.model.UserModel;
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse;
import com.appster.dentamatch.network.response.notification.UnReadNotificationResponseData;
import com.appster.dentamatch.presentation.calendar.CalendarFragment;
import com.appster.dentamatch.presentation.messages.ChatActivity;
import com.appster.dentamatch.presentation.messages.MessagesListFragment;
import com.appster.dentamatch.presentation.profile.ProfileFragment;
import com.appster.dentamatch.presentation.searchjob.JobsFragment;
import com.appster.dentamatch.presentation.tracks.TrackFragment;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.LocationUtils;
import com.appster.dentamatch.util.LogUtils;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.notification.AHNotification;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by virender on 17/01/17.
 * To inject activity reference.
 */
public class HomeActivity extends BaseLoadingActivity<HomeViewModel> {

    private static final String TAG = LogUtils.makeLogTag(HomeActivity.class);
    private final int SEARCH_JOBS_FRAGMENT_POS = 0;
    private final int TRACKS_FRAGMENT_POS = 1;
    private final int CALENDAR_FRAGMENT_POS = 2;
    private final int MESSAGE_FRAGMENT_POS = 3;
    private final int PROFILE_FRAGMENT_POS = 4;

    private AHBottomNavigation bottomBar;
    private ProfileFragment mProfileFragment;
    private JobsFragment mJobsFragment;
    private MessagesListFragment mMessagesFragment;
    private CalendarFragment mCalendarFragment;
    private TrackFragment mTrackFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        /*
          Connect Socket for chatting initialization.
         */
        SocketManager.getInstance().connect(this);
        setContentView(R.layout.activity_home);
        initViews();

        /*
          Launch job search fragment if redirected from search activity.
         */
        if (getIntent().hasExtra(Constants.EXTRA_SEARCH_JOB)) {
            bottomBar.setCurrentItem(SEARCH_JOBS_FRAGMENT_POS);

        } else if (getIntent().hasExtra(Constants.EXTRA_FROM_CHAT)) {
            /*
              Launch job message fragment if redirected from notification click.
             */
            bottomBar.setCurrentItem(MESSAGE_FRAGMENT_POS);
            String RecruiterID = getIntent().getStringExtra(Constants.EXTRA_FROM_CHAT);
            startActivity(new Intent(this, ChatActivity.class).putExtra(Constants.EXTRA_CHAT_MODEL, RecruiterID)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));

        } else if (getIntent().hasExtra(Constants.EXTRA_FROM_JOB_DETAIL) || getIntent().hasExtra(Constants.EXTRA_FROM_SETTINGS)) {
            bottomBar.setCurrentItem(PROFILE_FRAGMENT_POS);

        }/*else if (getIntent().hasExtra(Constants.EXTRA_FROM_JOB_NOTIF)) {

          startActivity(new Intent(this, NotificationActivity.class));
        }*/ else {
            bottomBar.setCurrentItem(SEARCH_JOBS_FRAGMENT_POS);

        }

        /*
          Retrieve user's current location.
         */
        LocationUtils.addFragment(this);
        updateToken(PreferenceUtil.getFcmToken());

        viewModel.getUnReadNotificationCount().observe(this,
                this::onSuccessUnreadNotificationCountRequest);
        viewModel.getUnreadMessages().observe(this, this::setNewMessagesCount);
    }

    private void onSuccessUnreadNotificationCountRequest(@Nullable UnReadNotificationCountResponse response) {
        if (response != null && response.getUnReadNotificationResponse() != null) {
            if (response.getUnReadNotificationResponse().getNotificationCount() == 0) {
                UnReadNotificationCountResponse countResponse = new UnReadNotificationCountResponse();
                UnReadNotificationResponseData responseData = new UnReadNotificationResponseData();
                responseData.setNotificationCount(0);
                countResponse.setUnReadNotificationResponse(responseData);
                onCount(countResponse);
                ShortcutBadger.removeCount(HomeActivity.this);
            } else {
                onCount(response);
                ShortcutBadger.applyCount(HomeActivity.this, response.getUnReadNotificationResponse().getNotificationCount());
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.hasExtra(Constants.EXTRA_FROM_CHAT)) {
            bottomBar.setCurrentItem(MESSAGE_FRAGMENT_POS);
            String RecruiterID = intent.getStringExtra(Constants.EXTRA_FROM_CHAT);
            startActivity(new Intent(this, ChatActivity.class).putExtra(Constants.EXTRA_CHAT_MODEL, RecruiterID)
                    .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));

        } else if (intent.hasExtra(Constants.EXTRA_FROM_SETTINGS)) {
            bottomBar.setCurrentItem(PROFILE_FRAGMENT_POS);

        }

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        SocketManager.getInstance().disconnect();
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
        UserModel userModel = PreferenceUtil.getUserModel();
        if (userModel != null) {
            LogUtils.LOGD(TAG, "Email::" + userModel.getEmail());
        }
        bottomBar = findViewById(R.id.ntb_horizontal);
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

        bottomBar.setOnTabSelectedListener((position, wasSelected) -> {
            switch (position) {
                case SEARCH_JOBS_FRAGMENT_POS:
                    if (mJobsFragment != null) {
                        pushFragment(mJobsFragment, null, ANIMATION_TYPE.FADE);
                    } else {
                        mJobsFragment = JobsFragment.newInstance();
                        pushFragment(mJobsFragment, null, ANIMATION_TYPE.FADE);
                    }
                    break;
                case TRACKS_FRAGMENT_POS:
                    if (mTrackFragment != null) {
                        pushFragment(mTrackFragment, null, ANIMATION_TYPE.FADE);
                    } else {
                        mTrackFragment = TrackFragment.newInstance();
                        pushFragment(mTrackFragment, null, ANIMATION_TYPE.FADE);
                    }
                    break;
                case CALENDAR_FRAGMENT_POS:
                    if (mCalendarFragment != null) {
                        pushFragment(mCalendarFragment, null, ANIMATION_TYPE.FADE);
                    } else {
                        mCalendarFragment = CalendarFragment.newInstance();
                        pushFragment(mCalendarFragment, null, ANIMATION_TYPE.FADE);
                    }
                    break;
                case MESSAGE_FRAGMENT_POS:
                    if (mMessagesFragment != null) {
                        pushFragment(mMessagesFragment, null, ANIMATION_TYPE.FADE);
                    } else {
                        mMessagesFragment = MessagesListFragment.newInstance();
                        pushFragment(mMessagesFragment, null, ANIMATION_TYPE.FADE);
                    }
                    break;
                case PROFILE_FRAGMENT_POS:
                    if (mProfileFragment != null) {
                        pushFragment(mProfileFragment, null, ANIMATION_TYPE.FADE);
                    } else {
                        mProfileFragment = ProfileFragment.newInstance();
                        pushFragment(mProfileFragment, null, ANIMATION_TYPE.FADE);
                    }
                    break;
                default:
                    break;
            }
            return true;
        });

        new Handler().postDelayed(() -> {
            setTitleMargins(SEARCH_JOBS_FRAGMENT_POS);
            setTitleMargins(TRACKS_FRAGMENT_POS);
            setTitleMargins(CALENDAR_FRAGMENT_POS);
            setTitleMargins(MESSAGE_FRAGMENT_POS);
            setTitleMargins(PROFILE_FRAGMENT_POS);
        }, 1000);

    }

    @Subscribe
    public void onCount(UnReadNotificationCountResponse count) {
        AHNotification notification = new AHNotification.Builder()
                .setText(count.getUnReadNotificationResponse().getNotificationCount() == 0 ? "" : count.getUnReadNotificationResponse().getNotificationCount() > 100 ? getString(R.string.ntf_cnt) : String.valueOf(count.getUnReadNotificationResponse().getNotificationCount()))
                .setBackgroundColor(ContextCompat.getColor(this, R.color.clr_cnl))
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                .build();
        bottomBar.setNotification(notification, 4);
        EventBus.getDefault().post(count.getUnReadNotificationResponse());
        ShortcutBadger.applyCount(getApplicationContext(), count.getUnReadNotificationResponse().getNotificationCount());
    }

    private void setTitleMargins(int bottomBarPosition) {
        switch (bottomBarPosition) {

            case 0:
                TextView tvTitle0 = bottomBar.getViewAtPosition(bottomBarPosition).findViewById(R.id.bottom_navigation_item_title);
                FrameLayout.LayoutParams llp0 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                llp0.setMargins(0, 0, 0, 8); // llp.setMargins(left, top, right, bottom);
                llp0.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                tvTitle0.setLayoutParams(llp0);
                break;

            case 1:
                TextView tvTitle1 = bottomBar.getViewAtPosition(bottomBarPosition).findViewById(R.id.bottom_navigation_item_title);
                FrameLayout.LayoutParams llp1 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                llp1.setMargins(0, 0, 0, 8); // llp.setMargins(left, top, right, bottom);
                llp1.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                tvTitle1.setLayoutParams(llp1);
                break;

            case 2:
                TextView tvTitle2 = bottomBar.getViewAtPosition(bottomBarPosition).findViewById(R.id.bottom_navigation_item_title);
                FrameLayout.LayoutParams llp2 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                llp2.setMargins(0, 0, 0, 8); // llp.setMargins(left, top, right, bottom);
                llp2.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                tvTitle2.setLayoutParams(llp2);
                break;

            case 3:
                TextView tvTitle3 = bottomBar.getViewAtPosition(bottomBarPosition).findViewById(R.id.bottom_navigation_item_title);
                FrameLayout.LayoutParams llp3 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                llp3.setMargins(0, 0, 0, 8); // llp.setMargins(left, top, right, bottom);
                llp3.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                tvTitle3.setLayoutParams(llp3);
                break;

            case 4:
                TextView tvTitle4 = bottomBar.getViewAtPosition(bottomBarPosition).findViewById(R.id.bottom_navigation_item_title);
                FrameLayout.LayoutParams llp4 = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
                llp4.setMargins(0, 0, 0, 8); // llp.setMargins(left, top, right, bottom);
                llp4.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
                tvTitle4.setLayoutParams(llp4);
                break;

            default:
                break;
        }
    }

    private void updateToken(@NonNull String fcmToken) {
        viewModel.updateFcmToken(fcmToken);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.requestUnreadNotificationCount();
        viewModel.requestUnreadMessagesCount();
    }

    public void setNewMessagesCount(@Nullable Integer count) {
        if (count != null) {
            AHNotification notification = new AHNotification.Builder()
                    .setText(count == 0 ? "" : count > 100 ? getString(R.string.ntf_cnt) : String.valueOf(count))
                    .setBackgroundColor(ContextCompat.getColor(this, R.color.clr_cnl))
                    .setTextColor(ContextCompat.getColor(this, R.color.white))
                    .build();
            bottomBar.setNotification(notification, 3);
            ShortcutBadger.applyCount(getApplicationContext(), count);
        }
    }
}
