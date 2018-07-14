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

    private static final String TAG = LogUtils.makeLogTag(Alert.class);
    private static Snackbar s_SnackBar;

    /**
     * Create and return a dialog having requested title and message
     *
     * @param context the context
     * @param title   title
     * @param message message
     * @return a dialog
     */
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

    /**
     * Create and return a dialog having requested title, message, positive and negative button along with the listener callbacks
     *
     * @param context     context
     * @param btnPositive positive button
     * @param btnNegative negative button
     * @param title       dialog title
     * @param message     dialog message
     * @param listener    callback listener to get called for respective function
     * @return a dialog
     */
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

    /**
     * Launch the alert dialog having the provided title, message, buttons and handler
     *
     * @param context            a context
     * @param title              header title
     * @param message            content message
     * @param defaultButton      button default
     * @param defaultHandler     default handler
     * @param alternativeButton  alternate button
     * @param alternativeHandler alternate handler
     * @param showLogo           logo to be shown
     * @param isCancelable       flag to set cancellable dialog
     */
    private static void alert(BaseActivity context, String title,
                              String message, String defaultButton,
                              final Runnable defaultHandler, String alternativeButton,
                              final Runnable alternativeHandler, boolean showLogo, boolean isCancelable) {
        if (!context.isActive())
            return;
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
    }

    /**
     * Call and launch alert dialog by passing not required parameter as optional
     *
     * @param context        a context
     * @param title          header title
     * @param message        content message
     * @param defaultHandler default handler
     */
    public static void alert(BaseActivity context, String title,
                             String message, final Runnable defaultHandler) {
        alert(context, title, message, context.getString(android.R.string.ok),
                defaultHandler);
    }

    /**
     * Call and launch alert dialog by passing not required parameter as optional
     *
     * @param context        a context
     * @param title          header title
     * @param message        content message
     * @param defaultButton  default button
     * @param defaultHandler default handler
     */
    private static void alert(BaseActivity context, String title,
                              String message, String defaultButton, final Runnable defaultHandler) {
        alert(context, title, message, defaultButton,
                defaultHandler, null, null, false, true);
    }

    /**
     * Call and launch alert dialog by passing not required parameter as optional
     *
     * @param context a context
     * @param title   header title
     * @param message content message
     */
    public static void alert(BaseActivity context, String title,
                             String message) {
        alert(context, title, message, context.getString(R.string.txt_ok),
                null, null, null, false, true);
    }

    /**
     * To dismiss bottom alert window message
     */
    public static void dismissSnackBar() {
        if (s_SnackBar != null) {
            s_SnackBar.dismiss();
            s_SnackBar = null;
        }
    }

    /**
     * To show a alert or message window at bottom
     *
     * @param view    a view
     * @param message content message
     * @return
     */
    private static Snackbar getSnackBar(View view, String message) {
        dismissSnackBar();
        return s_SnackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
    }

    /**
     * To show a alert or message window at bottom
     *
     * @param view    a view
     * @param message content message
     */
    static public void showSnackBar(View view, String message) {
        if (StringUtils.isNullOrEmpty(message)) return;
        s_SnackBar = getSnackBar(view, message);
        s_SnackBar.show();
    }

    /**
     * To show a alert or message window at bottom
     *
     * @param view       view
     * @param message    content message
     * @param buttonText button text
     * @param listener   listener for callback
     */
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
