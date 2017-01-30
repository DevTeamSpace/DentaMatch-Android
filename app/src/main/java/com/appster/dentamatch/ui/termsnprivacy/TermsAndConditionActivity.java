package com.appster.dentamatch.ui.termsnprivacy;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.appster.dentamatch.BuildConfig;
import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityTermsAndConditionBinding;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;

/**
 * Created by virender on 03/01/17.
 */
public class TermsAndConditionActivity extends BaseActivity implements View.OnClickListener {
    //    private ActivityT mBinder;
    private ActivityTermsAndConditionBinding mBinder;
    private boolean isPrivacyPolicy;
    private String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_condition);
        if (getIntent() != null) {
            isPrivacyPolicy = getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false);
        }
        initViews();
    }

    private void initViews() {
        mBinder.toolbarPrivacyPolicy.ivToolBarLeft.setOnClickListener(this);
        if (isPrivacyPolicy) {
            url = BuildConfig.BASE_URL + Constants.APIS.PRIVACY_POLICY;

            mBinder.toolbarPrivacyPolicy.tvToolbarGeneralLeft.setText(getString(R.string.header_privacy));

        } else {
            url = BuildConfig.BASE_URL + Constants.APIS.TERM_CONDITION;

            mBinder.toolbarPrivacyPolicy.tvToolbarGeneralLeft.setText(getString(R.string.header_term));


        }
        mBinder.webviewTermAndCondition.post(new Runnable() {
            @Override
            public void run() {
                processToShowDialog("", getString(R.string.please_wait), null);
                mBinder.webviewTermAndCondition.setWebViewClient(new WebViewClient());
                mBinder.webviewTermAndCondition.loadUrl(url);
            }
        });

    }

    @Override
    public String getActivityName() {
        return null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_tool_bar_left:
                onBackPressed();
                break;
        }
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);
            hideProgressBar();
        }
    }
}
