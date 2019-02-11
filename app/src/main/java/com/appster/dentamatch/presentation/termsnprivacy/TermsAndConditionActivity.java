/*
 *
 *  * Copyright © 2018 DentaMatch. All rights reserved.
 *  * Developed by Appster.
 *  *
 *
 */

package com.appster.dentamatch.presentation.termsnprivacy;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.appster.dentamatch.BuildConfig;
import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityTermsAndConditionBinding;
import com.appster.dentamatch.base.BaseActivity;
import com.appster.dentamatch.util.Constants;

/**
 * Created by virender on 03/01/17.
 * To display application terms and condition.
 */
public class TermsAndConditionActivity extends BaseActivity implements View.OnClickListener {
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
                processToShowDialog();
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

            default:
                break;
        }
    }

    private class WebViewClient extends android.webkit.WebViewClient {

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        //TODO: Uncomment this code when we change this to target Version N.
//        @TargetApi(Build.VERSION_CODES.N)
//        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            view.loadUrl(request.getUrl().toString());
//            return true;
//        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideProgressBar();
        }
    }
}
