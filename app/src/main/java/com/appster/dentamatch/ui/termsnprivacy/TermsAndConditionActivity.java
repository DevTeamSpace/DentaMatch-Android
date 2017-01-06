package com.appster.dentamatch.ui.termsnprivacy;

import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebView;

import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityTermsAndConditionBinding;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;

/**
 * Created by virender on 03/01/17.
 */
public class TermsAndConditionActivity extends BaseActivity {
    //    private ActivityT mBinder;
    private ActivityTermsAndConditionBinding mBinder;
    private boolean isPrivacyPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_create_profile1);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_terms_and_condition);
        if (getIntent() != null) {
            isPrivacyPolicy = getIntent().getBooleanExtra(Constants.INTENT_KEY.FROM_WHERE, false);
        }
        initViews();


    }

    private void initViews() {
        if(isPrivacyPolicy){
            mBinder.toolbarPrivacyPolicy.tvToolbarGeneralLeft.setText(getString(R.string.header_privacy));

        }else{
            mBinder.toolbarPrivacyPolicy.tvToolbarGeneralLeft.setText(getString(R.string.header_term));


        }
        mBinder.webviewTermAndCondition.post(new Runnable() {
            @Override
            public void run() {
//                if (NetWorkCheck.isNetworkAvailable(TNCActivity.this)) {
                mBinder.webviewTermAndCondition.setWebViewClient(new WebViewClient());
                mBinder.webviewTermAndCondition.loadUrl("");

//                } else {
//                }
            }
        });

    }

    @Override
    public String getActivityName() {
        return null;
    }

    public class WebViewClient extends android.webkit.WebViewClient {
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

        }

    }


}
