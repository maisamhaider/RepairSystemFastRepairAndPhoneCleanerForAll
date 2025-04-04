package com.cleaner.booster.phone.repairer.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.interfaces.SelectAll;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;

import java.util.ArrayList;
import java.util.List;

public class DeepCleanImagesAdapter extends RecyclerView.Adapter<DeepCleanImagesAdapter.WhatsAppStatusHolder> implements SelectAll {
    Context context;
    List<CommonModel> fileList;
    List<CommonModel> list;
    WhatsAppStatusHolder holder;
    SelectAll selectAll;

    public DeepCleanImagesAdapter(Context context, SelectAll selectAll) {
        this.context = context;
        list = new ArrayList<>();
        this.selectAll = selectAll;
    }


    public void setFileList(List<CommonModel> fileList) {
        this.fileList = fileList;

    }
    public void removeDeleted(){
        for (int i = 0; i < list.size(); i++) {
            if (fileList.contains(list.get(i))){
                fileList.remove(list.get(i));
            }
        }
        notifyDataSetChanged();
        list.clear();
    }
    public List<CommonModel> getList() {
        return list;
    }

    public void setList(List<CommonModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public WhatsAppStatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.deep_clean_images_rv_layout, parent, false);
        holder = new WhatsAppStatusHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WhatsAppStatusHolder holder, int position) {

        final CommonModel imageString = fileList.get(position);

        if (list.contains(imageString)) {
            holder.selectImage_iv.setImageDrawable(ResourcesCompat.getDrawable(context.getResources()
                    ,R.drawable.ic_selected,null));
        } else {
            holder.selectImage_iv.setImageBitmap(null);
        }


        Glide.with(context)
                .load(imageString.getPath())
                .into(holder.deepCleanRv_iv);

        holder.imageAdapter_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.contains(imageString)) {
                    list.remove(imageString);
                    selectAll.selectAll(false);
                    holder.selectImage_iv.setImageBitmap(null);
                } else {
                    list.add(imageString);
                    selectAll.selectAll(list.size() == fileList.size());

                    holder.selectImage_iv.setImageDrawable(ResourcesCompat.getDrawable(context.getResources()
                            ,R.drawable.ic_selected,null));
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
            list.add(path);
            holder.selectImage_iv.setImageDrawable(ResourcesCompat.getDrawable(context.getResources()
                    ,R.drawable.ic_selected,null));
            notifyDataSetChanged();
        }
    }

    private void clearList() {
        if (!list.isEmpty()) {
            list.clear();
            holder.selectImage_iv.setImageBitmap(null);
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

    class WhatsAppStatusHolder extends RecyclerView.ViewHolder {
        ImageView deepCleanRv_iv, selectImage_iv;
        ConstraintLayout imageAdapter_cl;

        public WhatsAppStatusHolder(@NonNull View itemView) {
            super(itemView);

            imageAdapter_cl = itemView.findViewById(R.id.imageAdapter_cl);
            deepCleanRv_iv = itemView.findViewById(R.id.deepCleanRv_iv);
            selectImage_iv = itemView.findViewById(R.id.selectImage_iv);
        }
    }
}
