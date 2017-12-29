package com.appster.dentamatch.ui.auth;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.appster.dentamatch.DentaApp;
import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityLoginBinding;
import com.appster.dentamatch.model.UserModel;
import com.appster.dentamatch.network.BaseCallback;
import com.appster.dentamatch.network.BaseResponse;
import com.appster.dentamatch.network.RequestController;
import com.appster.dentamatch.network.request.auth.LoginRequest;
import com.appster.dentamatch.network.request.jobs.SearchJobRequest;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationData;
import com.appster.dentamatch.network.response.PreferredJobLocation.PreferredJobLocationModel;
import com.appster.dentamatch.network.response.auth.LoginResponse;
import com.appster.dentamatch.network.response.auth.SearchFilterModel;
import com.appster.dentamatch.network.retrofit.AuthWebServices;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.ui.common.HomeActivity;
import com.appster.dentamatch.ui.map.PlacesMapActivity;
import com.appster.dentamatch.ui.profile.CreateProfileActivity1;
import com.appster.dentamatch.ui.termsnprivacy.TermsAndConditionActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.PreferenceUtil;
import com.appster.dentamatch.util.Utils;
import com.crashlytics.android.Crashlytics;

import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;

import static com.appster.dentamatch.util.Constants.REQUEST_CODE.REQUEST_CODE_LOCATION;

/**
 * Created by virender on 13/12/16.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ActivityLoginBinding mBinder;
    private boolean mIsAccepted, mIsLogin;
    private String mPostalCode;
    private String mPlaceName;
    private String mLatitude;
    private String mLongitude;
    private String mSelectedCountry;
    private String mSelectedCity;
    private String mSelectedState;
    private boolean mIsLoginShow, mIsRegisterShow;

    private ArrayAdapter<PreferredJobLocationData> mPreferredJobLocationDataArrayAdapter;
    private ArrayList<PreferredJobLocationData> mPreferredJobLocationList;
    private int preferredJobLocationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_login);

        if (getIntent() != null && getIntent().hasExtra(Constants.EXTRA_IS_LOGIN)) {
            mIsLogin = getIntent().getBooleanExtra(Constants.EXTRA_IS_LOGIN, false);
        }

        initViews();

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initViews() {
        mBinder.loginViewLogin.setOnClickListener(this);
        mBinder.loginViewRegister.setOnClickListener(this);
        mBinder.loginTvForgotPassword.setOnClickListener(this);
        mBinder.loginBtnRegister.setOnClickListener(this);
        mBinder.tvPreferredJobLocation.setOnClickListener(this);
        mBinder.loginTvShowPassword.setOnClickListener(this);
        mBinder.registerTvShowPassword.setOnClickListener(this);
        mBinder.ivAcceptPolicy.setOnClickListener(this);

        setPolicySpanString();
        showSelectedView(mIsLogin);
    }

    private void setPolicySpanString() {
        SpannableString spanString = new SpannableString(
                getString(R.string.label_accept_term_n_condition));

        ClickableSpan termsAndCondition = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(LoginActivity.this, TermsAndConditionActivity.class);
                mIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, false);
                startActivity(mIntent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        ClickableSpan privacy = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent mIntent = new Intent(LoginActivity.this, TermsAndConditionActivity.class);
                mIntent.putExtra(Constants.INTENT_KEY.FROM_WHERE, true);
                startActivity(mIntent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };

        int tncStart = getString(R.string.label_accept_term_n_condition).indexOf("Term");
        int tncEnd = getString(R.string.label_accept_term_n_condition).lastIndexOf("and") - 1;
        Utils.setSpannClickEvent(spanString, tncStart, tncEnd, termsAndCondition);
        Utils.setSpannColor(spanString, tncStart, tncEnd, ContextCompat.getColor(this, R.color.button_bg_color));

        int privacyStart = getString(R.string.label_accept_term_n_condition).indexOf("Privacy");
        Utils.setSpannClickEvent(spanString, privacyStart + 1, spanString.length(), privacy);
        Utils.setSpannColor(spanString, privacyStart, spanString.length(), ContextCompat.getColor(this, R.color.button_bg_color));
        Utils.setSpannCommanProperty(mBinder.tvTermNPolicy, spanString);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_view_login:
                mIsLogin = true;
                hideKeyboard();
                showSelectedView(true);
                break;

            case R.id.login_view_register:
                mIsLogin = false;
                hideKeyboard();
                showSelectedView(false);
                break;

            case R.id.login_tv_show_password:
                if (mBinder.loginEtPassword.getText().toString().length() > 0) {

                    if (mIsLoginShow) {
                        Utils.showPassword(LoginActivity.this, mBinder.loginEtPassword, mIsLoginShow, mBinder.loginTvShowPassword);
                        mIsLoginShow = false;

                    } else {
                        Utils.showPassword(LoginActivity.this, mBinder.loginEtPassword, mIsLoginShow, mBinder.loginTvShowPassword);
                        mIsLoginShow = true;
                    }
                }
                break;

            case R.id.register_tv_show_password:
                if (mBinder.registerEtPassword.getText().toString().length() > 0) {

                    if (mIsRegisterShow) {
                        Utils.showPassword(LoginActivity.this, mBinder.registerEtPassword, mIsRegisterShow, mBinder.registerTvShowPassword);
                        mIsRegisterShow = false;

                    } else {
                        Utils.showPassword(LoginActivity.this, mBinder.registerEtPassword, mIsRegisterShow, mBinder.registerTvShowPassword);
                        mIsRegisterShow = true;
                    }
                }
                break;

            case R.id.login_btn_register:
                hideKeyboard();

                if (validateInput()) {
                    if (mIsLogin) {
                        signInApi(prepareLoginRequest());
                    } else {
                        signUpApi(prepareSignUpRequest());
                    }
                }
                break;

            case R.id.login_tv_forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;

            case R.id.iv_accept_policy:
                if (mIsAccepted) {
                    mIsAccepted = false;
                    mBinder.ivAcceptPolicy.setImageResource(R.drawable.ic_check_empty);
                } else {
                    mBinder.ivAcceptPolicy.setImageResource(R.drawable.ic_check_fill);
                    mIsAccepted = true;
                }
                break;

            case R.id.tv_preferred_job_location:
                if (mPreferredJobLocationDataArrayAdapter == null) {
                    processToShowDialog();
                    AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
                    webServices.getPreferredJobLocationList().enqueue(new BaseCallback<PreferredJobLocationModel>(LoginActivity.this) {
                        @Override
                        public void onSuccess(PreferredJobLocationModel response) {
                            hideKeyboard();
                            mPreferredJobLocationList = (ArrayList<PreferredJobLocationData>) response.getResult().getPreferredJobLocations();
                            mPreferredJobLocationDataArrayAdapter = new PreferredJobLocationAdapter(LoginActivity.this, mPreferredJobLocationList);
                            showLocationList();
                        }

                        @Override
                        public void onFail(Call<PreferredJobLocationModel> call, BaseResponse baseResponse) {

                        }
                    });
                } else {
                    showLocationList();
                }
             /*   Intent intent = new Intent(this, PlacesMapActivity.class);

                if (mLatitude != null && mLongitude != null) {
                    intent.putExtra(Constants.EXTRA_LATITUDE, mLatitude);
                    intent.putExtra(Constants.EXTRA_LONGITUDE, mLongitude);
                    intent.putExtra(Constants.EXTRA_POSTAL_CODE, mPostalCode);
                    intent.putExtra(Constants.EXTRA_PLACE_NAME, mPlaceName);
                    intent.putExtra(Constants.EXTRA_COUNTRY_NAME, mSelectedCountry);
                    intent.putExtra(Constants.EXTRA_CITY_NAME, mSelectedCity);
                    intent.putExtra(Constants.EXTRA_STATE_NAME, mSelectedState);
                }

                startActivityForResult(intent, REQUEST_CODE_LOCATION);*/
                break;
        }
    }

    private void showLocationList() {
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(LoginActivity.this);
        builderSingle.setTitle(getResources().getString(R.string.preferred_location_label));
        builderSingle.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(mPreferredJobLocationDataArrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mBinder.tvPreferredJobLocation.setText(mPreferredJobLocationDataArrayAdapter.getItem(which).getPreferredLocationName());
                preferredJobLocationId = mPreferredJobLocationDataArrayAdapter.getItem(which).getId();

                mBinder.tvPreferredJobLocation.setTextColor(ContextCompat.getColor(getApplicationContext(),R.color.black_color));
            }
        });
        builderSingle.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();

                    if (bundle != null) {
                        mLatitude = bundle.getString(Constants.EXTRA_LATITUDE);
                        mLongitude = bundle.getString(Constants.EXTRA_LONGITUDE);
                        mPlaceName = bundle.getString(Constants.EXTRA_PLACE_NAME);
                        mPostalCode = bundle.getString(Constants.EXTRA_POSTAL_CODE);
                        mSelectedCity = bundle.getString(Constants.EXTRA_CITY_NAME);
                        mSelectedCountry = bundle.getString(Constants.EXTRA_COUNTRY_NAME);
                        mSelectedState = bundle.getString(Constants.EXTRA_STATE_NAME);
                        mBinder.tvPreferredJobLocation.setText(mPlaceName);
                    }
                }
        }
    }

    private boolean validateInput() {
        if (mIsLogin) {
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.loginEtEmail))) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_email_alert));
                mBinder.loginEtEmail.requestFocus();
                return false;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(getTextFromEditText(mBinder.loginEtEmail)).matches()) {
                mBinder.loginEtEmail.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.valid_email_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.loginEtPassword))) {
                mBinder.loginEtPassword.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.blank_password_alert));
                return false;
            }
            if (getTextFromEditText(mBinder.loginEtPassword).contains(" ")) {
                mBinder.loginEtPassword.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.password_contains_space));
                return false;
            }
            if (getTextFromEditText(mBinder.loginEtPassword).length() < Constants.PASSWORD_MIN_LENGTH ||
                    getTextFromEditText(mBinder.loginEtPassword).length() > Constants.PASSWORD_MAX_LENGTH) {
                mBinder.loginEtPassword.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.password_min_length_alert));
                return false;
            }
        } else {
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.registerEtFname))) {
                mBinder.registerEtFname.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.blank_fname_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.registerEtLname))) {
                mBinder.registerEtLname.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.blank_lname_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.registerEtEmail))) {
                mBinder.registerEtEmail.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.blank_email_alert));
                return false;
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(getTextFromEditText(mBinder.registerEtEmail)).matches()) {
                mBinder.registerEtEmail.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.valid_email_alert));
                return false;
            }
            if (TextUtils.isEmpty(getTextFromEditText(mBinder.registerEtPassword))) {
                mBinder.registerEtPassword.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.blank_password_alert));
                return false;
            }
            if (getTextFromEditText(mBinder.registerEtPassword).contains(" ")) {
                mBinder.registerEtPassword.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.password_contains_space));
                return false;
            }
            if (getTextFromEditText(mBinder.registerEtPassword).length() < Constants.PASSWORD_MIN_LENGTH ||
                    getTextFromEditText(mBinder.registerEtPassword).length() > Constants.PASSWORD_MAX_LENGTH) {
                mBinder.registerEtPassword.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.password_min_length_alert));
                return false;
            }
            if (TextUtils.isEmpty(mBinder.tvPreferredJobLocation.getText().toString())) {
                mBinder.tvPreferredJobLocation.requestFocus();
                Utils.showToast(getApplicationContext(), getString(R.string.blank_location_alert));
                return false;
            }

          /*  if (mPostalCode.isEmpty()) {
                Utils.showToastLong(getApplicationContext(), getString(R.string.blank_postal_code));
                return false;
            }
*/
            if (!mIsAccepted) {
                Utils.showToast(getApplicationContext(), getString(R.string.blank_tnc_alert));
                return false;
            }


          /*  if (TextUtils.isEmpty(mSelectedCountry)) {
                mSelectedCountry = "";
//                showToast(getString(R.string.msg_empty_country));
//                return false;

            }*/

           /* if (TextUtils.isEmpty(mSelectedCity)) {
                mSelectedCity = "";
//                showToast(getString(R.string.msg_empty_city));
//                return false;

            }*/

           /* if (TextUtils.isEmpty(mSelectedState)) {
                mSelectedState = "";
//                showToast(getString(R.string.msg_empty_state));
//                return false;

            }*/
        }
        return true;
    }

    private LoginRequest prepareSignUpRequest() {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDeviceId(Utils.getDeviceID());
        loginRequest.setDeviceToken(Utils.getDeviceToken());
        loginRequest.setDeviceType(Constants.DEVICE_TYPE);
        loginRequest.setEmail(getTextFromEditText(mBinder.registerEtEmail));
        loginRequest.setPassword(getTextFromEditText(mBinder.registerEtPassword));
        loginRequest.setFirstName(getTextFromEditText(mBinder.registerEtFname));
        loginRequest.setLastName(getTextFromEditText(mBinder.registerEtLname));
        loginRequest.setPreferredJobLocationId(preferredJobLocationId);

        /*loginRequest.setCountry(mSelectedCountry);
        loginRequest.setCity(mSelectedCity);
        loginRequest.setState(mSelectedState);

        loginRequest.setLatitude(mLatitude);
        loginRequest.setLongitude(mLongitude);
        loginRequest.setZipCode(mPostalCode);
        loginRequest.setPreferredLocation(mPlaceName);*/
        return loginRequest;
    }

    private void signUpApi(LoginRequest loginRequest) {
        processToShowDialog();

        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.signUp(loginRequest).enqueue(new BaseCallback<LoginResponse>(LoginActivity.this) {
            @Override
            public void onSuccess(LoginResponse response) {

                if (response.getStatus() == 1) {
                    /*
                      Track event via mixpanel.
                     */
                    DentaApp.getInstance().getMixpanelAPI().track(getString(R.string.mixpanel_event_signup));

                    PreferenceUtil.setFistName(getTextFromEditText(mBinder.registerEtFname));
                    PreferenceUtil.setLastName(getTextFromEditText(mBinder.registerEtLname));
                    //Utils.showToast(getApplicationContext(), response.getMessage());
                    mIsLogin = true;
                    //showSelectedView(true);
                   // clearRegistrationFields();

                    PreferenceUtil.setUserToken(response.getLoginResponseData().getUserDetail().getUserToken());
                    PreferenceUtil.setFistName(response.getLoginResponseData().getUserDetail().getFirstName());
                    PreferenceUtil.setLastName(response.getLoginResponseData().getUserDetail().getLastName());
                    PreferenceUtil.setProfileImagePath(response.getLoginResponseData().getUserDetail().getImageUrl());
                    PreferenceUtil.setUserChatId(response.getLoginResponseData().getUserDetail().getId());
                    PreferenceUtil.setPreferredJobLocationName(response.getLoginResponseData().getUserDetail().getPreferredLocationName());
                    PreferenceUtil.setPreferredJobLocationID(response.getLoginResponseData().getUserDetail().getPreferredJobLocationId());
                    PreferenceUtil.setLicenseNumber(response.getLoginResponseData().getUserDetail().getLicenseNumber());

                    PreferenceUtil.setKeyJobSeekerVerified(response.getLoginResponseData().getUserDetail().getIsJobSeekerVerified());
                    PreferenceUtil.setUserVerified(response.getLoginResponseData().getUserDetail().getIsVerified());


                    showSignUpSuccessDialog(response.getMessage());


                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<LoginResponse> call, BaseResponse baseResponse) {

            }
        });
    }

    private LoginRequest prepareLoginRequest() {
        processToShowDialog();
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setDeviceId(Utils.getDeviceID());
        loginRequest.setDeviceToken(Utils.getDeviceToken());
        loginRequest.setDeviceType(Constants.DEVICE_TYPE);
        loginRequest.setEmail(getTextFromEditText(mBinder.loginEtEmail));
        loginRequest.setPassword(getTextFromEditText(mBinder.loginEtPassword));
        return loginRequest;
    }

    private String getTextFromEditText(EditText et) {
        return et.getText().toString().trim();
    }

    private void logUser(String email, String userName, String userID) {
        Crashlytics.setUserIdentifier(userID);
        Crashlytics.setUserEmail(email);
        Crashlytics.setUserName(userName);
    }


    private void signInApi(final LoginRequest loginRequest) {
        AuthWebServices webServices = RequestController.createService(AuthWebServices.class);
        webServices.signIn(loginRequest).enqueue(new BaseCallback<LoginResponse>(LoginActivity.this) {
            @Override
            public void onSuccess(LoginResponse response) {
                if (response.getStatus() == 1) {
                    /*
                      Track event via mixpanel.
                     */
                    try {
                        JSONObject userDetails = new JSONObject();
                        userDetails.put(getString(R.string.user_name_label), response.getLoginResponseData().getUserDetail().getFirstName()
                                .concat(" ")
                                .concat(response.getLoginResponseData().getUserDetail().getLastName()));
                        userDetails.put(getString(R.string.email_label), loginRequest.getEmail());
                        DentaApp.getInstance().getMixpanelAPI().track(getString(R.string.mixpanel_event_login), userDetails);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (response.getLoginResponseData().getSearchFilters() != null) {
                        SearchFilterModel searchFilters = response.getLoginResponseData().getSearchFilters();
                        SearchJobRequest request = new SearchJobRequest();
                        if(searchFilters.getIsParttime()!=null && !TextUtils.isEmpty(searchFilters.getIsParttime())) {
                            request.setIsParttime(Integer.parseInt(searchFilters.getIsParttime()));
                        }
                        if(searchFilters.getIsFulltime()!=null && !TextUtils.isEmpty(searchFilters.getIsFulltime())) {
                            request.setIsFulltime(Integer.parseInt(searchFilters.getIsFulltime()));
                        }

                        request.setLat(searchFilters.getLat());
                        request.setLng(searchFilters.getLng());
                        request.setJobTitle(searchFilters.getJobTitle());
                        request.setPage(1);

                        if (searchFilters.getParttimeDays() != null && searchFilters.getParttimeDays().size() > 0) {
                            request.setParttimeDays(searchFilters.getParttimeDays());
                        } else {
                            request.setParttimeDays(new ArrayList<String>());
                        }

                        request.setCountry(searchFilters.getCountry());
                        request.setCity(searchFilters.getCity());
                        request.setState(searchFilters.getState());

                        request.setZipCode(searchFilters.getZipCode());
                        request.setSelectedJobTitles(searchFilters.getSelectedJobTitles());
                        request.setAddress(searchFilters.getAddress());

                        /**
                         * This value is set in order to redirect user from login or splash screen.
                         */
                        PreferenceUtil.setJobFilter(true);
                        PreferenceUtil.saveJobFilter(request);
                    }

                    PreferenceUtil.setIsLogin(true);
                    PreferenceUtil.setUserToken(response.getLoginResponseData().getUserDetail().getUserToken());
                    PreferenceUtil.setFistName(response.getLoginResponseData().getUserDetail().getFirstName());
                    PreferenceUtil.setLastName(response.getLoginResponseData().getUserDetail().getLastName());
                    PreferenceUtil.setProfileImagePath(response.getLoginResponseData().getUserDetail().getImageUrl());
                    PreferenceUtil.setUserChatId(response.getLoginResponseData().getUserDetail().getId());
                    PreferenceUtil.setPreferredJobLocationName(response.getLoginResponseData().getUserDetail().getPreferredLocationName());
                    PreferenceUtil.setPreferredJobLocationID(response.getLoginResponseData().getUserDetail().getPreferredJobLocationId());
                    PreferenceUtil.setLicenseNumber(response.getLoginResponseData().getUserDetail().getLicenseNumber());

                    PreferenceUtil.setKeyJobSeekerVerified(response.getLoginResponseData().getUserDetail().getIsJobSeekerVerified());
                    PreferenceUtil.setUserVerified(response.getLoginResponseData().getUserDetail().getIsVerified());
                    if(response.getLoginResponseData().getUserDetail()!=null && response.getLoginResponseData().getUserDetail().getJobTitileId()!=null) {
                        PreferenceUtil.setJobTitleId(response.getLoginResponseData().getUserDetail().getJobTitileId());
                    }




                    UserModel userModel= new UserModel();
                    userModel.setEmail(response.getLoginResponseData().getUserDetail().getEmail());
                    userModel.setFirstName(response.getLoginResponseData().getUserDetail().getFirstName());
                    userModel.setLastName(response.getLoginResponseData().getUserDetail().getFirstName());
                    userModel.setProfileCompleted(response.getLoginResponseData().getUserDetail().getProfileCompleted());
                   // userModel.setUserId(Integer.parseInt(response.getLoginResponseData().getUserDetail().getUserId()));

                    userModel.setIsVerified(response.getLoginResponseData().getUserDetail().getIsVerified());
                    userModel.setIsJobSeekerVerified(response.getLoginResponseData().getUserDetail().getIsJobSeekerVerified());
                    userModel.setIsJobSeekerVerified(response.getLoginResponseData().getUserDetail().getIsJobSeekerVerified());
                    userModel.setJobTitleId(response.getLoginResponseData().getUserDetail().getJobTitileId());
                    userModel.setIsVerified(response.getLoginResponseData().getUserDetail().getJobTitileId());
                    PreferenceUtil.setUserModel(userModel);
                    PreferenceUtil.setSetAvailability(true);




                    logUser(loginRequest.getEmail(),
                            response.getLoginResponseData().getUserDetail().getFirstName()
                                    .concat(" ")
                                    .concat(response.getLoginResponseData().getUserDetail().getLastName()),
                            response.getLoginResponseData().getUserDetail().getId());


                    if (response.getLoginResponseData().getUserDetail().getJobTitileId() == null) {
                        PreferenceUtil.setProfileCompleted(false);
                        Intent intent = new Intent(getApplicationContext(), CreateProfileActivity1.class);
                        startActivity(intent);
                        finish();
                    } else {
                        PreferenceUtil.setProfileCompleted(true);
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }

                 /*   if (response.getLoginResponseData().getUserDetail().getProfileCompleted() == 1) {
                        PreferenceUtil.setProfileCompleted(true);
                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        PreferenceUtil.setProfileCompleted(false);
                        Intent intent = new Intent(getApplicationContext(), CreateProfileActivity1.class);
                        startActivity(intent);
                        finish();

                    }*/
                } else {
                    Utils.showToast(getApplicationContext(), response.getMessage());
                }
            }

            @Override
            public void onFail(Call<LoginResponse> call, BaseResponse baseResponse) {
            }
        });
    }

    private void showSelectedView(boolean isLogin) {
        if (isLogin) {
            mBinder.loginLayoutRegisterView.setVisibility(View.GONE);
            mBinder.registerIvPeg.setVisibility(View.INVISIBLE);
            mBinder.ivPeg.setVisibility(View.VISIBLE);
            mBinder.loginLayoutLoginView.setVisibility(View.VISIBLE);
            mBinder.loginBtnRegister.setText(getString(R.string.login_label));
            Utils.setFontFaceRobotoBold(mBinder.tvLogin);
            Utils.setFontFaceRobotoLight(mBinder.registerTvRegister);

        } else {
            mBinder.loginLayoutRegisterView.setVisibility(View.VISIBLE);
            mBinder.registerIvPeg.setVisibility(View.VISIBLE);
            mBinder.ivPeg.setVisibility(View.INVISIBLE);
            mBinder.loginLayoutLoginView.setVisibility(View.GONE);
            mBinder.loginBtnRegister.setText(getString(R.string.register_label));
            Utils.setFontFaceRobotoBold(mBinder.registerTvRegister);
            Utils.setFontFaceRobotoLight(mBinder.tvLogin);
        }
    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    protected void onDestroy() {
        DentaApp.getInstance().getMixpanelAPI().flush();
        super.onDestroy();
    }

    private void clearRegistrationFields() {
        mBinder.registerEtFname.getText().clear();
        mBinder.registerEtLname.getText().clear();
        mBinder.registerEtEmail.getText().clear();
        mBinder.registerEtPassword.getText().clear();
        mBinder.tvPreferredJobLocation.setText("");
        mBinder.ivAcceptPolicy.setBackgroundResource(R.drawable.ic_check_empty);
    }

    private  void showSignUpSuccessDialog( String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.success))
                .setMessage(msg)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        PreferenceUtil.setIsLogin(true);
                        PreferenceUtil.setProfileCompleted(false);

                        Intent intent = new Intent(getApplicationContext(), CreateProfileActivity1.class);
                        startActivity(intent);
                        finish();
                    }
                }).setCancelable(false)

                //.setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}

