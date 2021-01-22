package com.cleaner.booster.phone.repairer.app.utils;

import android.content.Context;
import android.view.Gravity;

import com.cleaner.booster.phone.repairer.app.R;
import com.gmail.samehadar.iosdialog.IOSDialog;

public class LoadingDialog {

    private IOSDialog iosDialog;

    public boolean show() {
        iosDialog.show();
        return true;
    }

    public boolean dismiss() {
        iosDialog.dismiss();
        return false;
    }

    public LoadingDialog(Context context) {
        iosDialog = new IOSDialog.Builder(context)
                .setCancelable(false)
                .setSpinnerClockwise(false)
                .setMessageContentGravity(Gravity.END)
                .setMessageColor(context.getResources().getColor(R.color.standard_white))
                .setMessageContent("Loading Data")
                .build();
    }

    public LoadingDialog(Context context, String msg) {
        iosDialog = new IOSDialog.Builder(context)
                .setCancelable(false)
                .setSpinnerClockwise(false)
                .setMessageContentGravity(Gravity.END)
                .setMessageContent(msg)
                .build();
    }


}