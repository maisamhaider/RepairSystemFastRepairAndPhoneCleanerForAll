package com.cleaner.booster.phone.repairer.app.async;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.activities.WhatsAppBaseActivity;
import com.cleaner.booster.phone.repairer.app.adapters.CommonAdapter;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.utils.LoadingDialog;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class WhatsAppCommonTask  {

    Context context;
    private CommonAdapter commonAdapter;
    private RecyclerView recyclerView;
    public List<CommonModel> list;
    Utils utils;
    LoadingDialog loadingDialog;
    String whatThingIs;
    File file1;
    private File file12;


    public WhatsAppCommonTask(Context context, CommonAdapter commonAdapter, RecyclerView recyclerView, String whatThingIs) {
        this.context = context;
        this.commonAdapter = commonAdapter;
        this.recyclerView = recyclerView;
        this.list = new ArrayList<>();
        utils = new Utils(context);
        loadingDialog = new LoadingDialog(context);
        this.whatThingIs = whatThingIs;
        try {
            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (whatThingIs.matches("videos")) {
            file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/WhatsApp Video");
        } else if (whatThingIs.matches("audios")) {
            file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/WhatsApp Audio");
        } else if (whatThingIs.matches("images")) {
            file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/WhatsApp Images");
        } else if (whatThingIs.matches("doc")) {
            file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/WhatsApp Documents");

        } else if (whatThingIs.matches("BUCH")) {
            //back up and conversation history
            file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Backups");
            file12 = new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Databases");
//            List<CommonModel> list1 ;
//            List<CommonModel> list2 ;

            if (file1.exists()) {
                list = utils.getListFiles(file1);
            }
            if (file12.exists()) {
                list.addAll(utils.getListFiles(file12));
            }
//            list2 = utils.getListFiles(file12);
        }


        list = utils.getListFiles(file1);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                commonAdapter.setFileList(list);
                recyclerView.setAdapter(commonAdapter);
                commonAdapter.notifyDataSetChanged();
                loadingDialog.dismiss();
                ((WhatsAppBaseActivity) context).toggleVisibility(list.size() > 0);
            }
        }, 2500);


    }

}
