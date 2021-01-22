package com.cleaner.booster.phone.repairer.app.async;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.activities.DeepCleanAllVideosAct;
import com.cleaner.booster.phone.repairer.app.adapters.DeepCleanVideosAdapter;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.utils.LoadingDialog;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class DeepCleanVideosTask {

    Context context;
    private DeepCleanVideosAdapter deepCleanVideosAdapter;
    private RecyclerView recyclerView;

    LoadingDialog loadingDialog;

    public DeepCleanVideosTask(Context context, DeepCleanVideosAdapter deepCleanVideosAdapter,
                               RecyclerView recyclerView) {
        this.context = context;
        this.deepCleanVideosAdapter = deepCleanVideosAdapter;
        this.recyclerView = recyclerView;
        List<CommonModel> list = new ArrayList<>();
        Utils utils = new Utils(context);
        loadingDialog = new LoadingDialog(context);

         list = (utils.getAllVideosPaths());


        try {
            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<CommonModel> finalList = list;
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                deepCleanVideosAdapter.setFileList(finalList);
                recyclerView.setAdapter(deepCleanVideosAdapter);
                deepCleanVideosAdapter.notifyDataSetChanged();
                loadingDialog.dismiss();
                if (finalList.isEmpty()) {
                    ((DeepCleanAllVideosAct) context).selectAll_cb1.setVisibility(View.GONE);
                    ((DeepCleanAllVideosAct) context).select_tv.setVisibility(View.GONE);
                    ((DeepCleanAllVideosAct) context).noData_tv.setVisibility(View.VISIBLE);

                } else {
                    ((DeepCleanAllVideosAct) context).selectAll_cb1.setVisibility(View.VISIBLE);
                    ((DeepCleanAllVideosAct) context).select_tv.setVisibility(View.VISIBLE);
                    ((DeepCleanAllVideosAct) context).noData_tv.setVisibility(View.GONE);
                }

            }
        }, 2500);

    }


}
