package com.cleaner.booster.phone.repairer.app.async;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.activities.DeepCleanAllDocsAct;
import com.cleaner.booster.phone.repairer.app.activities.DeepCleanAllImagesAct;
import com.cleaner.booster.phone.repairer.app.adapters.DeepCleanImagesAdapter;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.utils.LoadingDialog;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class DeepCleanImagesTask extends AsyncTask<Void, Integer, String> {

    Context context;
    private DeepCleanImagesAdapter deepCleanImagesAdapter;
    private RecyclerView recyclerView;
    private List<CommonModel> list;
    Utils utils;
    LoadingDialog loadingDialog;

    public DeepCleanImagesTask(Context context, DeepCleanImagesAdapter deepCleanImagesAdapter,
                               RecyclerView recyclerView) {
        this.context = context;
        this.deepCleanImagesAdapter = deepCleanImagesAdapter;
        this.recyclerView = recyclerView;
        this.list = new ArrayList<>();
        utils = new Utils(context);
        loadingDialog = new LoadingDialog(context);


    }

    @Override
    protected void onPreExecute() {
        try {
            loadingDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String doInBackground(Void... voids) {
        list = utils.getAllImagePaths();
    return null;
    }

    @Override
    protected void onPostExecute(String s) {
        recyclerView.setLayoutManager(new GridLayoutManager(context,3));
        deepCleanImagesAdapter.setFileList(list);
        recyclerView.setAdapter(deepCleanImagesAdapter);
        deepCleanImagesAdapter.notifyDataSetChanged();
            loadingDialog.dismiss();
        if (list.isEmpty())
        {
            ((DeepCleanAllImagesAct)context).selectAll_cb1.setVisibility(View.GONE);
            ((DeepCleanAllImagesAct)context).select_tv.setVisibility(View.GONE);
            ((DeepCleanAllImagesAct)context).noData_tv.setVisibility(View.VISIBLE);

        }
        else
        {
            ((DeepCleanAllImagesAct)context).selectAll_cb1.setVisibility(View.VISIBLE);
            ((DeepCleanAllImagesAct)context).select_tv.setVisibility(View.VISIBLE);
            ((DeepCleanAllImagesAct)context).noData_tv.setVisibility(View.GONE);
        }
        }
}
