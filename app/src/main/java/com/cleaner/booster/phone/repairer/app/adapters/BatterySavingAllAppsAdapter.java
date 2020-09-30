package com.cleaner.booster.phone.repairer.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.database.Db;
import com.cleaner.booster.phone.repairer.app.interfaces.SelectAll;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.utils.AppUtility;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import bot.box.appusage.utils.UsageUtils;

public class BatterySavingAllAppsAdapter extends RecyclerView.Adapter<BatterySavingAllAppsAdapter.AllAppsHolder> implements SelectAll{

    private List<String> apps;
    private List<String> checkList;
    private Context context;
    private AppUtility appUtility;
    private Utils utils;
    AllAppsHolder holder;

    @SuppressLint("NewApi")
    public BatterySavingAllAppsAdapter(Context context, List<String> apps) {

        this.context = context;
        appUtility = new AppUtility(context);
        this.apps = apps;
        checkList = new ArrayList<>();
        checkList.addAll(apps);
     }

    public List<String> getCheckList() {
        return checkList;
    }



    @NonNull
    @Override
    public AllAppsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.battery_saving_apps_lo, parent, false);
       holder = new AllAppsHolder(view);
        return holder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull AllAppsHolder holder, final int position) {
        utils = new Utils(context);
        String appName = utils.GetAppName(apps.get(position));
        final String appPackage = apps.get(position);

        if (checkList.contains(appPackage)) {
            holder.batterySavingApp_iv.setImageResource(R.drawable.ic_select);
        } else {
            holder.batterySavingApp_iv.setImageResource(R.drawable.ic_deselect);
        }


        holder.batterySavingAppName_Tv.setText(appName);

        Glide.with(context).load(UsageUtils.parsePackageIcon(apps.get(position), R.mipmap.ic_launcher))
                .transition(new DrawableTransitionOptions().crossFade()).into(holder.batterySavingAppImage_Iv);


        holder.batterySavingApp_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = apps.get(position);

                if (checkList.contains(packageName)) {
                    checkList.remove(packageName);
                    holder.batterySavingApp_iv.setImageResource(R.drawable.ic_deselect);
                } else {
                        checkList.add(packageName);
                        holder.batterySavingApp_iv.setImageResource(R.drawable.ic_select);
                }
//                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public SelectAll getSelectAll()
    {
        return this;
    }
    private void selectAll() {
        if(!checkList.isEmpty())
        {
            checkList.clear();
        }
        for (String path : apps) {
            checkList.add(path);
            holder.batterySavingApp_iv.setImageResource(R.drawable.ic_select);
            notifyDataSetChanged();
        }
    }

    private void clearList() {
        if(!checkList.isEmpty())
        {
            checkList.clear();
            holder.batterySavingApp_iv.setImageResource(R.drawable.ic_deselect);
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
        TextView batterySavingAppName_Tv;
        ImageView batterySavingAppImage_Iv, batterySavingApp_iv;

        public AllAppsHolder(@NonNull View itemView) {
            super(itemView);
            batterySavingAppName_Tv = itemView.findViewById(R.id.batterySavingAppName_Tv);
            batterySavingAppImage_Iv = itemView.findViewById(R.id.batterySavingAppImage_Iv);
            batterySavingApp_iv = itemView.findViewById(R.id.batterySavingApp_iv);

        }
    }
}
