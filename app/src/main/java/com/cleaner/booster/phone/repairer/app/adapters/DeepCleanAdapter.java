package com.cleaner.booster.phone.repairer.app.adapters;

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
import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.interfaces.SelectAll;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class DeepCleanAdapter extends
        RecyclerView.Adapter<DeepCleanAdapter.WhatsAppStatusHolder> implements SelectAll {
    Context context;
    List<CommonModel> fileList;
    List<String> list;
    WhatsAppStatusHolder holder;
    SelectAll selectAll;

    private int fileType = 0;
    public static final int AUDIO = 1;
    public static final int DOCUMENT = 2;
    public static final int PACKAGES = 3;

    public DeepCleanAdapter(Context context, SelectAll selectAll,int fileType) {
        this.context = context;
        list = new ArrayList<>();
        this.selectAll = selectAll;
        this.fileType =fileType;
    }


    public void setFileList(List<CommonModel> fileList) {
        this.fileList = fileList;

    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public WhatsAppStatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deep_clean_pkgs_rv_layout, parent, false);
        holder = new WhatsAppStatusHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WhatsAppStatusHolder holder, int position) {

        final String pkgString = fileList.get(position).getPath();
        final String pkgName = fileList.get(position).getName();

        if (list.contains(pkgString)) {
            holder.selectPkg_iv.setImageResource(R.drawable.ic_select);
        } else {
            holder.selectPkg_iv.setImageResource(R.drawable.ic_deselect);
        }
        holder.deepCleanPkgName_tv.setText(pkgName);
        if (fileType == AUDIO) {
            holder.deepCleanPgksRv_iv.
                    setImageDrawable(context.getResources().getDrawable(R.drawable.ic_audio));
        } else if (fileType == DOCUMENT) {
            holder.deepCleanPgksRv_iv
                    .setImageDrawable(context.getResources().getDrawable(R.drawable.ic_received_file));
        } else if (fileType == PACKAGES) {
            holder.deepCleanPgksRv_iv
                    .setImageDrawable(context.getResources()
                            .getDrawable(R.drawable.ic_backup_conversation_history));
        }
        holder.pkgAdapter_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.contains(pkgString)) {
                    list.add(pkgString);
                    if (list.size() == fileList.size()) {
                        selectAll.selectAll(true);
                    }
                    holder.selectPkg_iv.setImageResource(R.drawable.ic_select);
                } else {
                    list.remove(position);
                        selectAll.selectAll(false);
                        holder.selectPkg_iv.setImageResource(R.drawable.ic_deselect);

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public SelectAll getSelectAll() {
        return this;
    }

    private void selectAll() {
        if (!list.isEmpty()) {
            list.clear();
        }
        for (CommonModel path : fileList) {
            list.add(path.getPath());
            holder.selectPkg_iv.setImageResource(R.drawable.ic_select);
            notifyDataSetChanged();
        }
    }

    private void clearList() {
        if (!list.isEmpty()) {
            list.clear();
            holder.selectPkg_iv.setImageResource(R.drawable.ic_deselect);
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

    static class WhatsAppStatusHolder extends RecyclerView.ViewHolder {

        ImageView deepCleanPgksRv_iv, selectPkg_iv;
        TextView deepCleanPkgName_tv;
        ConstraintLayout pkgAdapter_cl;

        public WhatsAppStatusHolder(@NonNull View itemView) {
            super(itemView);

            pkgAdapter_cl = itemView.findViewById(R.id.pkgAdapter_cl);
            deepCleanPgksRv_iv = itemView.findViewById(R.id.deepCleanPgksRv_iv);
            selectPkg_iv = itemView.findViewById(R.id.selectPkg_iv);
            deepCleanPkgName_tv = itemView.findViewById(R.id.deepCleanPkgName_tv);

        }
    }
}
