package com.appster.dentamatch.ui.auth;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.profile.CreateProfileActivity1;
import com.appster.dentamatch.ui.map.PlacesMapActivity;
import com.appster.dentamatch.ui.termsnprivacy.TermsAndConditionActivity;
import com.appster.dentamatch.util.Constants;
import com.appster.dentamatch.util.Utils;
import com.appster.dentamatch.widget.CustomTextView;

/**
 * Created by virender on 13/12/16.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private ImageView ivRegisterPeg, ivLoginPeg,ivPolicy;
    private LinearLayout layoutRegisterSelector, layoutLoginSelector, layoutOnlyRegister, layoutOnlyLogin;
    private TextView tvLogin, tvRegister, tvForgotPassword,tvTermNcondition;
    private Button btnRegister;
    private CustomTextView tvPreferredJobLocation;
    private boolean isAccepted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initViews() {

        tvRegister = (TextView) findViewById(R.id.register_tv_register);
        tvLogin = (TextView) findViewById(R.id.login_tv_login);
        tvTermNcondition = (TextView) findViewById(R.id.tv_term_n_policy);
        tvForgotPassword = (TextView) findViewById(R.id.login_tv_forgot_password);
        btnRegister = (Button) findViewById(R.id.login_btn_register);
        ivRegisterPeg = (ImageView) findViewById(R.id.register_iv_peg);
        ivLoginPeg = (ImageView) findViewById(R.id.login_iv_peg);
        ivPolicy = (ImageView) findViewById(R.id.iv_accept_policy);
        layoutRegisterSelector = (LinearLayout) findViewById(R.id.login_view_register);
        layoutLoginSelector = (LinearLayout) findViewById(R.id.login_view_login);
        layoutOnlyLogin = (LinearLayout) findViewById(R.id.login_layout_login_view);
        layoutOnlyRegister = (LinearLayout) findViewById(R.id.login_layout_register_view);
        tvPreferredJobLocation = (CustomTextView) findViewById(R.id.tv_preferred_job_location);

        layoutLoginSelector.setOnClickListener(this);
        layoutRegisterSelector.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvPreferredJobLocation.setOnClickListener(this);
        ivPolicy.setOnClickListener(this);
        setPolicySpanString();

    }
    private void setPolicySpanString() {
        SpannableString spanString = new SpannableString(
                getString(R.string.label_accept_term_ncondition));

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

        int tncStart = getString(R.string.label_accept_term_ncondition).indexOf("Term");
        int tncEnd = getString(R.string.label_accept_term_ncondition).lastIndexOf("and") - 1;
        Utils.setSpannClickEvent(spanString, tncStart, tncEnd, termsAndCondition);
        Utils.setSpannColor(spanString, tncStart, tncEnd, getResources().getColor(R.color.button_bg_color));
        Utils.setSpannTypeface(spanString, tncStart, tncEnd, Typeface.BOLD);
        Utils.setSpannUnderline(spanString, tncStart, tncEnd);

        int privacyStart = getString(R.string.label_accept_term_ncondition).indexOf("Privacy");
        Utils.setSpannClickEvent(spanString, privacyStart + 1, spanString.length(), privacy);
        Utils.setSpannColor(spanString, privacyStart, spanString.length(), getResources().getColor(R.color.button_bg_color));
        Utils.setSpannUnderline(spanString, privacyStart + 1, spanString.length());
        Utils.setSpannTypeface(spanString, privacyStart, spanString.length(), Typeface.BOLD);


        Utils.setSpannCommanProperty(tvTermNcondition, spanString);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_view_login:
                showSelectedView(true);
                break;
            case R.id.login_view_register:
                showSelectedView(false);
                break;

            case R.id.login_btn_register:
                startActivity(new Intent(this, CreateProfileActivity1.class));
                break;
            case R.id.login_tv_forgot_password:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;

            case R.id.iv_accept_policy:
                if(isAccepted){
                    isAccepted=false;
                    ivPolicy.setBackgroundResource(R.drawable.ic_check_empty);
                }else{
                    ivPolicy.setBackgroundResource(R.drawable.ic_check_fill);
                    isAccepted=true;


                }
                break;

            case R.id.tv_preferred_job_location:
                startActivity(new Intent(this, PlacesMapActivity.class));
                break;
        }
    }

    private void showSelectedView(boolean isLogin) {
        if (isLogin) {
            layoutOnlyRegister.setVisibility(View.GONE);
            ivRegisterPeg.setVisibility(View.INVISIBLE);
            ivLoginPeg.setVisibility(View.VISIBLE);
            layoutOnlyLogin.setVisibility(View.VISIBLE);
            btnRegister.setText(getString(R.string.login_label));
        } else {
            layoutOnlyRegister.setVisibility(View.VISIBLE);
            ivRegisterPeg.setVisibility(View.VISIBLE);
            ivLoginPeg.setVisibility(View.INVISIBLE);
            layoutOnlyLogin.setVisibility(View.GONE);
            btnRegister.setText(getString(R.string.register_label));

        }
    }
}
