package com.appster.dentamatch.util;

import android.content.Context;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Appster on 26/01/17.
 */
public class SweetAlertHelper {
    private SweetAlertDialog mSweetDialog;

    private static SweetAlertHelper ourInstance ;

    public static synchronized SweetAlertHelper getInstance() {

        if(ourInstance == null){
            ourInstance = new SweetAlertHelper();
        }

        return ourInstance;
    }

   public void showSuccessAlert(Context ct, String content, SweetAlertDialog.OnSweetClickListener listener){
       mSweetDialog = new SweetAlertDialog(ct, SweetAlertDialog.SUCCESS_TYPE);
       mSweetDialog.setContentText(content);
       mSweetDialog.setConfirmText("OK");
       mSweetDialog.setConfirmClickListener(listener);
       mSweetDialog.setTitleText("Success");
       mSweetDialog.show();
   }

    public void showErrorAlert(Context ct, String content, SweetAlertDialog.OnSweetClickListener listener){
        mSweetDialog = new SweetAlertDialog(ct, SweetAlertDialog.ERROR_TYPE);
        mSweetDialog.setContentText(content);
        mSweetDialog.setConfirmText("OK");
        mSweetDialog.setConfirmClickListener(listener);
        mSweetDialog.setTitleText("Error");
        mSweetDialog.show();
    }

    public void showsWarningAlert(Context ct, String content, SweetAlertDialog.OnSweetClickListener cancelListener, SweetAlertDialog.OnSweetClickListener confirmListener){
        mSweetDialog = new SweetAlertDialog(ct, SweetAlertDialog.WARNING_TYPE);
        mSweetDialog.setContentText(content);
        mSweetDialog.setConfirmText("Continue");
        mSweetDialog.setCancelText("Cancel");
        mSweetDialog.setTitleText("Warning");
        mSweetDialog.setConfirmClickListener(confirmListener);
        mSweetDialog.setCancelClickListener(cancelListener);
        mSweetDialog.show();
    }

    public void showProgressAlert(Context ct){
        mSweetDialog = new SweetAlertDialog(ct, SweetAlertDialog.PROGRESS_TYPE);
        mSweetDialog.setTitleText("Loading");
        mSweetDialog.show();
    }

    public void progressToSuccessAlert(String content,SweetAlertDialog.OnSweetClickListener listener){
        mSweetDialog.setContentText(content);
        mSweetDialog.setConfirmText("OK");
        mSweetDialog.setConfirmClickListener(listener);
        mSweetDialog.setTitleText("Success");
        mSweetDialog.changeAlertType(SweetAlertDialog.SUCCESS_TYPE);
    }

    public void progressToFailureAlert(String content,SweetAlertDialog.OnSweetClickListener listener){
        mSweetDialog.setContentText(content);
        mSweetDialog.setConfirmText("OK");
        mSweetDialog.setConfirmClickListener(listener);
        mSweetDialog.setTitleText("Error");
        mSweetDialog.changeAlertType(SweetAlertDialog.ERROR_TYPE);
    }

    public void dismissAlert(){
        if(mSweetDialog != null && mSweetDialog.isShowing()){
            mSweetDialog.dismissWithAnimation();
        }
    }




}
