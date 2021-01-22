package com.cleaner.booster.phone.repairer.app.async;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.activities.DeepCleanAllDocsAct;
import com.cleaner.booster.phone.repairer.app.adapters.DeepCleanAdapter;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.utils.LoadingDialog;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class DeepCleanDocsTask {

    Context context;
    private DeepCleanAdapter deepCleanAdapter;
    private RecyclerView recyclerView;
    private List<CommonModel> list;
    Utils utils;
    LoadingDialog loadingDialog;

    public DeepCleanDocsTask(Context context, DeepCleanAdapter deepCleanAdapter,
                             RecyclerView recyclerView) {
        this.context = context;
        this.deepCleanAdapter = deepCleanAdapter;
        this.recyclerView = recyclerView;
        this.list = new ArrayList<>();
        utils = new Utils(context);
        loadingDialog = new LoadingDialog(context);
        try {
            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        list = utils.getAllDocs(String.valueOf(Environment.getExternalStorageDirectory()));

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                deepCleanAdapter.setFileList(list);
                recyclerView.setAdapter(deepCleanAdapter);
                deepCleanAdapter.notifyDataSetChanged();
                loadingDialog.dismiss();
                if (list.isEmpty())
                {
                    ((DeepCleanAllDocsAct)context).selectAll_cb1.setVisibility(View.GONE);
                    ((DeepCleanAllDocsAct)context).select_tv.setVisibility(View.GONE);
                    ((DeepCleanAllDocsAct)context).noData_tv.setVisibility(View.VISIBLE);

                }
                else
                {
                    ((DeepCleanAllDocsAct)context).selectAll_cb1.setVisibility(View.VISIBLE);
                    ((DeepCleanAllDocsAct)context).select_tv.setVisibility(View.VISIBLE);
                    ((DeepCleanAllDocsAct)context).noData_tv.setVisibility(View.GONE);
                }
            }
        });
    }
}
