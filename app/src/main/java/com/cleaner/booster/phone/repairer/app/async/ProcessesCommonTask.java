package com.cleaner.booster.phone.repairer.app.async;

import android.content.Context;
import android.os.AsyncTask;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.adapters.ProcessesAdapter;
import com.cleaner.booster.phone.repairer.app.utils.LoadingDialog;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class ProcessesCommonTask extends AsyncTask<Void, Integer, String> {

    Context context;
    private ProcessesAdapter processesAdapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<String> list;
    Utils utils;
    LoadingDialog loadingDialog;

    public ProcessesCommonTask(Context context, ProcessesAdapter processesAdapter,
                               RecyclerView recyclerView ) {
        this.context = context;
        this.processesAdapter = processesAdapter;
        this.recyclerView = recyclerView;
         this.list = new ArrayList<>();
        utils = new Utils(context);
        loadingDialog = new LoadingDialog(context);

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

        list = utils.getActiveApps();
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        processesAdapter.setApps(list);
        recyclerView.setAdapter(processesAdapter);
        processesAdapter.notifyDataSetChanged();
        loadingDialog.dismiss();


    }


}
