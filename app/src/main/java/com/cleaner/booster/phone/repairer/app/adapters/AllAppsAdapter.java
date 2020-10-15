package com.cleaner.booster.phone.repairer.app.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.activities.UnInstallAppAct;
import com.cleaner.booster.phone.repairer.app.interfaces.TrueFalse;
import com.cleaner.booster.phone.repairer.app.utils.AppUtility;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import bot.box.appusage.utils.UsageUtils;

public class AllAppsAdapter extends RecyclerView.Adapter<AllAppsAdapter.AllAppsHolder> implements Filterable {

    private List<String> apps;
    private List<String> fullList;
    private Context context;
    private AppUtility appUtility;
    private Utils utils;
    TrueFalse trueFalse;

    @SuppressLint("NewApi")
    public AllAppsAdapter(Context context,TrueFalse trueFalse) {

        this.context = context;
        appUtility = new AppUtility(context);
        apps = new ArrayList<>();
        fullList = new ArrayList<>();
        this.trueFalse = trueFalse;

    }

    public List<String> getApps() {
        return apps;
    }

    public void setList(List<String> apps) {
        this.apps.clear();
        this.fullList.clear();
        this.apps = apps;
        this.fullList.addAll(apps);
    }

    @NonNull
    @Override
    public AllAppsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allappsrecyclerviewitem_lo, parent, false);
        return new AllAppsHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull AllAppsHolder holder, final int position) {
        utils = new Utils(context);
        String appName = utils.GetAppName(apps.get(position));
        final String appPackage = apps.get(position);

        holder.appName_Tv.setText(appName);

        Glide.with(context).load(UsageUtils.parsePackageIcon(apps.get(position), R.mipmap.ic_launcher))
                .transition(new DrawableTransitionOptions().crossFade()).into(holder.appImage_Iv);


        holder.allAppsMain_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String packageName = apps.get(position);

                if (appUtility.isSystemApp(packageName)) {
                    Toast.makeText(context, "Can not Uninstall system's application", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!appUtility.isAppPreLoaded(packageName)) {
                    Intent intent = new Intent(Intent.ACTION_DELETE);
                    intent.setData(Uri.parse("package:" + packageName));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        apps.remove(packageName);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    context.startActivity(intent);
                    notifyDataSetChanged();
                    if (context instanceof UnInstallAppAct) {
                        ((UnInstallAppAct) context).loadData();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }


    class AllAppsHolder extends RecyclerView.ViewHolder {
        TextView appName_Tv;
        ImageView appImage_Iv, deleteAppImage_Iv;
        ConstraintLayout allAppsMain_cl;
        View itemView;

        public AllAppsHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            appName_Tv = itemView.findViewById(R.id.allAppName_Tv);
            appImage_Iv = itemView.findViewById(R.id.allAppImage_Iv);
            allAppsMain_cl = itemView.findViewById(R.id.allAppsMain_cl);
            deleteAppImage_Iv = itemView.findViewById(R.id.allAppSelected_deselect_Iv);

        }
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<String> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredList.addAll(fullList);
            } else {
                String char4 = charSequence.toString().toLowerCase();
                for (String app : fullList) {
                    String appName = utils.GetAppName(app);
                    if (appName.toLowerCase().startsWith(char4)) {
                        filteredList.add(app);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            apps.clear();
            apps.addAll((Collection<? extends String>) filterResults.values);
            if (apps.size() == 0)
            {
                trueFalse.isTrue(false);
            }
            else
            {
                trueFalse.isTrue(true);
            }
            notifyDataSetChanged();
        }
    };


}
