package com.cleaner.booster.phone.repairer.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.activities.MainActivity;
import com.cleaner.booster.phone.repairer.app.utils.AppUtility;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import bot.box.appusage.utils.UsageUtils;

public class ProcessesAdapter extends RecyclerView.Adapter<ProcessesAdapter.AllAppsHolder>   {

    private List<String> apps;
    private List<String> sendList;
    private Context context;
    private AppUtility appUtility;
    private MainActivity mainActivity;
    Utils utils;


    public ProcessesAdapter() {
    }

    @SuppressLint("NewApi")
    public ProcessesAdapter(Context context) {

        this.context = context;
        appUtility = new AppUtility( context );
        this.apps = new ArrayList<>();
        sendList = new ArrayList<>(  );
     }

    public List<String> getApps() {
        return apps;
    }

    public void setApps(List<String> apps) {
        this.apps = apps;
        sendList = apps;
    }

    public List<String> getSendList() {
        return sendList;
    }

    public void setSendList(List<String> sendList) {
        this.sendList = sendList;
    }

    @NonNull
    @Override
    public AllAppsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.processes_lo, parent, false );
        return new AllAppsHolder( view );
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull AllAppsHolder holder, final int position) {
        utils = new Utils(context);
         String appName = utils.GetAppName(apps.get( position ));
        final String appPackage = apps.get( position );

        holder.processesName_Tv.setText( appPackage );
        Log.i("pkg",appPackage);

        Glide.with(context).load( UsageUtils.parsePackageIcon(apps.get( position ), R.mipmap.ic_launcher))
                .transition(new DrawableTransitionOptions().crossFade()).into(holder.processes_Iv);
        holder.processes_cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                    sendList.add(appPackage) ;
                }
                else
                {
                    sendList.remove(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return apps.size();
    }



    class AllAppsHolder extends RecyclerView.ViewHolder {
        TextView processesName_Tv;
        ImageView processes_Iv;
        CheckBox processes_cb;
        public AllAppsHolder(@NonNull View itemView) {
            super( itemView );
            processesName_Tv = itemView.findViewById( R.id.processesName_Tv );
            processes_Iv = itemView.findViewById( R.id.processes_Iv );
            processes_cb = itemView.findViewById( R.id.processes_cb );

        }
    }
}
