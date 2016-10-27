package com.appster.dentamatch.ui.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.appster.dentamatch.R;

/**
 * Created by Appster on 28/01/16.
 */
@SuppressLint("Registered")
public class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";
    private int mOnStartCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOnStartCount = 1;

        if (savedInstanceState == null) { // 1st time
//            this.overridePendingTransition(R.anim.anim_slide_in_left,
//                    R.anim.anim_slide_out_left);
        } else { // already created so reverse animation
            mOnStartCount = 2;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mOnStartCount > 1) {
//            this.overridePendingTransition(R.anim.anim_slide_in_right,
//                    R.anim.anim_slide_out_right);
        } else if (mOnStartCount == 1) {
            mOnStartCount++;
        }
    }

    public void hideKeyBoard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

}
