package com.cleaner.booster.phone.repairer.app.async;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.activities.DeepCleanAllAudiosAct;
import com.cleaner.booster.phone.repairer.app.adapters.DeepCleanAdapter;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.utils.LoadingDialog;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class DeepCleanAudiosTask {

    Context context;
    private DeepCleanAdapter deepCleanAdapter;
    private RecyclerView recyclerView;
    private List<CommonModel> list;
    Utils utils;
    LoadingDialog loadingDialog;

    public DeepCleanAudiosTask(Context context, DeepCleanAdapter deepCleanAdapter,
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
        list = utils.getAllAudiosPaths();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
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
                    ((DeepCleanAllAudiosAct)context).selectAll_cb1.setVisibility(View.GONE);
                    ((DeepCleanAllAudiosAct)context).select_tv.setVisibility(View.GONE);
                    ((DeepCleanAllAudiosAct)context).noData_tv.setVisibility(View.VISIBLE);

                }
                else
                {
                    ((DeepCleanAllAudiosAct)context).selectAll_cb1.setVisibility(View.VISIBLE);
                    ((DeepCleanAllAudiosAct)context).select_tv.setVisibility(View.VISIBLE);
                    ((DeepCleanAllAudiosAct)context).noData_tv.setVisibility(View.GONE);
                }
            }
        },2500);

    }



}
