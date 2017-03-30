package com.appster.dentamatch.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.ContextThemeWrapper;
import android.view.View;

import com.appster.dentamatch.R;
import com.appster.dentamatch.ui.common.BaseActivity;

public class Alert {

    private static Snackbar s_SnackBar;

    private static AlertDialog.Builder createAlert(Context context,
                                                   String title, String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(context,
                        R.style.CustomAlertDialogTheme));
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("Information");
        dialog.setMessage(message);
        dialog.setCancelable(false);

        return dialog;
    }

    public static AlertDialog.Builder createYesNoAlert(Context context, String btnPositive, String btnNegative,
                                                       String title, String message, final OnAlertClickListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(context,
                        R.style.CustomAlertDialogTheme));
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("");
        dialog.setMessage(message);
        dialog.setPositiveButton(btnPositive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null)
                    listener.onPositive(dialogInterface);
            }
        });
        dialog.setNegativeButton(btnNegative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (listener != null)
                    listener.onNegative(dialogInterface);
            }
        });
        dialog.setCancelable(false);
        dialog.show();
        return dialog;
    }


    private static AlertDialog alert(BaseActivity context, String title,
                                     String message, String defaultButton,
                                     final Runnable defaultHandler, String alternativeButton,
                                     final Runnable alternativeHandler, boolean showLogo, boolean isCancelable) {
        if (!context.isActive())
            return null;
        AlertDialog.Builder dialog = createAlert(context, title, message);
        dialog.setCancelable(isCancelable);
        dialog.setNegativeButton(defaultButton,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (defaultHandler != null)
                            defaultHandler.run();
                    }
                });
        if (alternativeButton != null) {
            dialog.setPositiveButton(alternativeButton,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (alternativeHandler != null)
                                alternativeHandler.run();
                        }
                    });
        }
        dialog.setCancelable(false);

        if (showLogo)
            dialog.setIcon(R.mipmap.ic_launcher);
        try {
            if (context.isActive())
                return dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void alert(BaseActivity context, String title,
                             String message, final Runnable defaultHandler) {
        alert(context, title, message, context.getString(android.R.string.ok),
                defaultHandler);
    }

    private static void alert(BaseActivity context, String title,
                             String message, String defaultButton, final Runnable defaultHandler) {
        alert(context, title, message, defaultButton,
                defaultHandler, null, null, false, true);
    }

    public static void alert(BaseActivity context, String title,
                             String message) {
        alert(context, title, message, context.getString(R.string.txt_ok),
                null, null, null, false, true);
    }

    public static void dismissSnackBar() {
        if (s_SnackBar != null) {
            s_SnackBar.dismiss();
            s_SnackBar = null;
        }
    }

    private static Snackbar getSnackBar(View view, String message) {
        dismissSnackBar();
        return s_SnackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
    }

    static public void showSnackBar(View view, String message) {
        if (StringUtils.isNullOrEmpty(message)) return;
        s_SnackBar = getSnackBar(view, message);
        s_SnackBar.show();
    }

    static public void showSnackBar(View view, String message, String buttonText, View.OnClickListener listener) {
        if (StringUtils.isNullOrEmpty(message)) return;
        s_SnackBar = getSnackBar(view, message);
        s_SnackBar.setAction(buttonText, listener);
        s_SnackBar.setDuration(Snackbar.LENGTH_INDEFINITE);
        s_SnackBar.show();
    }

    public interface OnAlertClickListener {
        void onPositive(DialogInterface dialog);
        void onNegative(DialogInterface dialog);
    }
}
