package com.cleaner.booster.phone.repairer.app.async;

import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.adapters.FastChargeAllAppsAdapter;
import com.cleaner.booster.phone.repairer.app.database.Db;
import com.cleaner.booster.phone.repairer.app.utils.LoadingDialog;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class FastAllAppsTask extends AsyncTask<Void, Integer, String> {

    Context context;
    private FastChargeAllAppsAdapter appsAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<String> list;
    Utils utils;
    LoadingDialog loadingDialog;
    Db db ;

    public FastAllAppsTask(Context context, FastChargeAllAppsAdapter appsAdapter,
                           RecyclerView recyclerView,Db db) {
        this.context = context;
        this.appsAdapter = appsAdapter;
        this.recyclerView = recyclerView;
        this.linearLayoutManager = linearLayoutManager;
        this.list = new ArrayList<>();
        utils = new Utils(context);
        loadingDialog = new LoadingDialog(context);
        this.db = db;


    }

    @Override
    protected void onPreExecute() {
        linearLayoutManager = new LinearLayoutManager(context);
        try {
            loadingDialog.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected String doInBackground(Void... voids) {

            list = utils.GetAllInstalledApkInfo();

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        recyclerView.setLayoutManager(linearLayoutManager);
        appsAdapter.setList(list,db);
        recyclerView.setAdapter(appsAdapter);
        appsAdapter.notifyDataSetChanged();
        loadingDialog.dismiss();


    }


}
