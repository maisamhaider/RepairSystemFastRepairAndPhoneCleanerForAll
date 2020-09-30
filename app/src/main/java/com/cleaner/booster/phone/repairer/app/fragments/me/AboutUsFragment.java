package com.cleaner.booster.phone.repairer.app.fragments.me;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.cleaner.booster.phone.repairer.app.BuildConfig;
import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.activities.MainActivity;
import com.cleaner.booster.phone.repairer.app.fragments.BaseFragment;

public class AboutUsFragment extends BaseFragment {


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_setting, container, false);

         ConstraintLayout shareCl = root.findViewById(R.id.share_cl);
        ConstraintLayout rateUsCl = root.findViewById(R.id.rateUs_cl);
        ConstraintLayout aboutUsCl = root.findViewById(R.id.aboutUs_cl);

        adView(root.findViewById(R.id.settingBanner_adView));


        shareCl.setOnClickListener(v -> shareUs());
        rateUsCl.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + getActivity().getPackageName()))));

        aboutUsCl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.about_us_dialog_layout, null, false);
                Button closebtn = view.findViewById(R.id.aboutUsMoreApp_btn);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true).setView(view);

                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
                TextView appVersion_tv = view.findViewById(R.id.appVersion_tv);

                closebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(dialog.isShowing()){
                            dialog.cancel();
                        }
                    }
                });

                try {
                    PackageInfo packageInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                    String versionName = packageInfo.versionName;
                    appVersion_tv.setText(versionName);

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        return root;
    }

    public void shareUs() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
}