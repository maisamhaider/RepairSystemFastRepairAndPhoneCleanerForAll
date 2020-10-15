package com.cleaner.booster.phone.repairer.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.database.Db;
import com.cleaner.booster.phone.repairer.app.interfaces.SelectAll;
import com.cleaner.booster.phone.repairer.app.utils.AppUtility;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import bot.box.appusage.utils.UsageUtils;

public class FastChargeAllAppsAdapter extends RecyclerView.Adapter<FastChargeAllAppsAdapter.AllAppsHolder> implements SelectAll {

    private List<String> apps;
    private List<String> fullList;
    private List<String> checkList;
    private Context context;
    private AppUtility appUtility;
    private Db db;
    private Utils utils;
    private SelectAll selectAll;
    AllAppsHolder holder;

    @SuppressLint("NewApi")
    public FastChargeAllAppsAdapter(Context context, SelectAll selectAll) {

        this.context = context;
        appUtility = new AppUtility(context);
        apps = new ArrayList<>();
        fullList = new ArrayList<>();
        db = new Db(context);
        this.selectAll = selectAll;


    }

    public void setList(List<String> apps, Db db) {
        this.apps.clear();
        this.fullList.clear();
        this.apps = apps;
        this.fullList.addAll(apps);
//        checkList.addAll(apps);
        this.db = db;
    }


    @NonNull
    @Override
    public AllAppsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fast_charge_allapp_lo, parent, false);
        holder = new AllAppsHolder(view);
        return holder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull AllAppsHolder holder, final int position) {
        utils = new Utils(context);
        String appName = utils.GetAppName(apps.get(position));
        final String appPackage = apps.get(position);


        if (db.isPkgAvail(appPackage)) {
            holder.fastChargeAllApp_iv.setImageResource(R.drawable.ic_select);
        } else {
            holder.fastChargeAllApp_iv.setImageResource(R.drawable.ic_deselect);

        }

        holder.fastChargeAllAppName_Tv.setText(appName);
        Glide.with(context).load(UsageUtils.parsePackageIcon(apps.get(position), R.mipmap.ic_launcher))
                .transition(new DrawableTransitionOptions().crossFade()).into(holder.fastChargeAllAppImage_Iv);

        holder.fastChargeallApp_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = apps.get(position);
                if (!db.isPkgAvail(packageName)) {
                    boolean isInserted = db.insertPkgPath(packageName);
                    if (isInserted) {
                        if (db.getProfilesCount() == apps.size()) {
                            selectAll.selectAll(true);
                        }

                        holder.fastChargeAllApp_iv.setImageResource(R.drawable.ic_select);

                    }

                } else {

                    if (db.isPkgAvail(packageName)) {
                        boolean isDeleted = db.deletePkgPath(packageName);
                        if (isDeleted) {
                            selectAll.selectAll(false);
                            holder.fastChargeAllApp_iv.setImageResource(R.drawable.ic_deselect);

                        }
                    }

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return apps.size();
    }

    public SelectAll getSelectAll() {
        return this;
    }

    private void selectAll() {
        if (db.getProfilesCount() != 0) {
            db.deletePkgAll();
        }
        for (String path : apps) {
            db.insertPkgPath(path);
            holder.fastChargeAllApp_iv.setImageResource(R.drawable.ic_select);
            notifyDataSetChanged();
        }
    }

    private void clearList() {
        if (db.getProfilesCount() != 0) {
            db.deletePkgAll();
            holder.fastChargeAllApp_iv.setImageResource(R.drawable.ic_deselect);
            notifyDataSetChanged();
        }
    }

    @Override
    public void selectAll(boolean isSelectAll) {
        if (isSelectAll) {
            selectAll();
        } else {
            clearList();
        }
    }

    class AllAppsHolder extends RecyclerView.ViewHolder {
        TextView fastChargeAllAppName_Tv;
        ImageView fastChargeAllAppImage_Iv, fastChargeAllApp_iv;
        ConstraintLayout fastChargeallApp_cl;

        public AllAppsHolder(@NonNull View itemView) {
            super(itemView);
            fastChargeallApp_cl = itemView.findViewById(R.id.fastChargeallApp_cl);
            fastChargeAllAppName_Tv = itemView.findViewById(R.id.fastChargeAllAppName_Tv);
            fastChargeAllAppImage_Iv = itemView.findViewById(R.id.fastChargeAllAppImage_Iv);
            fastChargeAllApp_iv = itemView.findViewById(R.id.fastChargeAllApp_iv);

        }
    }
}
