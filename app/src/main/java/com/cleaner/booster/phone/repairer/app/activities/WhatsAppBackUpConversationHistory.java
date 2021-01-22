package com.cleaner.booster.phone.repairer.app.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.adapters.CommonAdapter;
import com.cleaner.booster.phone.repairer.app.async.WhatsAppCommonTask;
import com.cleaner.booster.phone.repairer.app.interfaces.SelectAll;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.io.File;

public class WhatsAppBackUpConversationHistory extends WhatsAppBaseActivity implements SelectAll{

    Utils utils;
    File file;
    Button whatsAppBUCHList_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_whats_app_data_list);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        utils = new Utils(this);

        rvCleanWhatsApp = findViewById(R.id.whatsApp_rv);
        whatsAppBUCHList_btn = findViewById(R.id.clean_btn);
        group = findViewById(R.id.group);
        noDatatv = findViewById(R.id.no_data_tv);
        selectAll_cb1 = findViewById(R.id.selectAll_cb1);
        select_tv = findViewById(R.id.select_tv);

        type = "BUCH";

        commonAdapter = new CommonAdapter(this, CommonAdapter.BACKUP,this);
         new WhatsAppCommonTask(this, commonAdapter, rvCleanWhatsApp, type );

        whatsAppBUCHList_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });

        selectAll_cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectAll selectAll = commonAdapter.getSelectAll();
                if (!b) {
                    selectAll.selectAll(true);
                    b = true;
                } else {
                    selectAll.selectAll(false);
                    b = false;

                }
            }
        });
    }
    @Override
    public void selectAll(boolean isSelectAll) {

        if (isSelectAll)
        {
            selectAll_cb1.setChecked(true);
            b = true;

        }
        else {
            selectAll_cb1.setChecked(false);
            b = false;

        }
    }

}