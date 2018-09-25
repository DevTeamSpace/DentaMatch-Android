/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.ui.common;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.chat.SocketManager;
import com.appster.dentamatch.eventbus.LocationEvent;
import com.appster.dentamatch.model.UserModel;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.Notification.UpdateFcmTokenRequest;
import com.appster.dentamatch.network.response.notification.UnReadNotificationCountResponse;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.calendar.CalendarFragment;
import com.appster.dentamatch.ui.messages.ChatActivity;
import com.appster.dentamatch.ui.messages.MessagesListFragment;
import com.appster.dentamatch.ui.profile.ProfileFragment;
import com.appster.dentamatch.ui.searchjob.JobsFragment;
import com.appster.dentamatch.ui.tracks.TrackFragment;
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
import retrofit2.Call;

/**
 * Created by virender on 17/01/17.
 * To inject activity reference.
 */
public class HomeActivity extends BaseActivity {
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
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
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

        bottomBar.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
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
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setTitleMargins(SEARCH_JOBS_FRAGMENT_POS);
                setTitleMargins(TRACKS_FRAGMENT_POS);
                setTitleMargins(CALENDAR_FRAGMENT_POS);
                setTitleMargins(MESSAGE_FRAGMENT_POS);
                setTitleMargins(PROFILE_FRAGMENT_POS);
            }
        }, 1000);

    }

    public void onCount(int count) {
        AHNotification notification = new AHNotification.Builder()
                .setText(count == 0 ? "" : count > 100 ? getString(R.string.ntf_cnt) : String.valueOf(count))
                .setBackgroundColor(ContextCompat.getColor(this, R.color.clr_cnl))
                .setTextColor(ContextCompat.getColor(this, R.color.white))
                .build();
        bottomBar.setNotification(notification, 4);
        ShortcutBadger.applyCount(getApplicationContext(), count);
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


    @Override
    public String getActivityName() {
        return null;
    }

    private void updateToken(String fcmToken) {
        try {
            UpdateFcmTokenRequest request = new UpdateFcmTokenRequest();
            request.setUpdateDeviceToken(fcmToken);
            AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
            webServices.updateFcmToken(request).enqueue(new BaseCallback<BaseResponse>(this) {
                @Override
                public void onSuccess(BaseResponse response) {

                    if (response.getStatus() == 1) {
                        LogUtils.LOGD(TAG, "token updated successfully");
                    } else {
                        LogUtils.LOGD(TAG, "token  not updated fails");

                    }
                }

                @Override
                public void onFail(Call<BaseResponse> call, BaseResponse baseResponse) {
                }
            });

        } catch (Exception e) {
            LogUtils.LOGE(TAG, e.getMessage());
        }
    }

    private void getBatchCount() {
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class, true);
        webServices.getUnreadNotificationCount().enqueue(new BaseCallback<UnReadNotificationCountResponse>(this) {
            @Override
            public void onSuccess(UnReadNotificationCountResponse response) {
                /*
                  Once data has been loaded from the filter changes we can dismiss this filter.
                 */
                if (response.getStatus() == 1) {
                    if (response.getUnReadNotificationResponse().getNotificationCount() == 0) {
                        onCount(0);
                        ShortcutBadger.removeCount(HomeActivity.this);
                    } else {
                        onCount(response.getUnReadNotificationResponse().getNotificationCount());
                        ShortcutBadger.applyCount(HomeActivity.this, response.getUnReadNotificationResponse().getNotificationCount());
                    }
                } else {
                    showToast(response.getMessage());
                }
            }

            @Override
            public void onFail(Call<UnReadNotificationCountResponse> call, BaseResponse baseResponse) {
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getBatchCount();
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
