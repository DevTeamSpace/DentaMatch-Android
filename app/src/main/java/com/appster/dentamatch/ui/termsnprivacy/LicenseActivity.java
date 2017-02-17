package com.appster.dentamatch.ui.termsnprivacy;

import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;

import com.appster.dentamatch.BuildConfig;
import com.appster.dentamatch.R;
import com.appster.dentamatch.databinding.ActivityLicenseBinding;
import com.appster.dentamatch.databinding.ActivityTermsAndConditionBinding;
import com.appster.dentamatch.ui.common.BaseActivity;
import com.appster.dentamatch.util.Constants;

/**
 * Created by ramkumar on 17/02/17.
 */
public class LicenseActivity extends BaseActivity implements View.OnClickListener {
    private ActivityLicenseBinding mBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinder = DataBindingUtil.setContentView(this, R.layout.activity_license);
        initViews();
    }

    private void initViews() {
        mBinder.toolbarLicense.ivToolBarLeft.setOnClickListener(this);
        mBinder.toolbarLicense.tvToolbarGeneralLeft.setText(getString(R.string.header_license));

        mBinder.webviewLicense.post(new Runnable() {
            @Override
            public void run() {
                mBinder.webviewLicense.setWebViewClient(new WebViewClient());
                mBinder.webviewLicense.loadUrl("file:///android_asset/licenses.html");
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

        @SuppressWarnings("deprecation")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);
        }
    }
}
