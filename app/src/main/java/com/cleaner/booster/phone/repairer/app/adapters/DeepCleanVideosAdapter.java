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

public class DeepCleanVideosAdapter extends
        RecyclerView.Adapter<DeepCleanVideosAdapter.WhatsAppStatusHolder> implements SelectAll {
    Context context;
    List<CommonModel> fileList;
    List<CommonModel> list;
    WhatsAppStatusHolder holder;
    SelectAll selectAll;

    public DeepCleanVideosAdapter(Context context, SelectAll selectAll) {
        this.context = context;
        list = new ArrayList<>();
        this.selectAll = selectAll;

    }


    public void setFileList(List<CommonModel> fileList) {
         this.fileList = fileList;
     }

    public List<CommonModel> getList() {
        return list;
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

    public void setList(List<CommonModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public WhatsAppStatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deep_clean_videos_rv_layout,
                parent, false);
        holder = new WhatsAppStatusHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull WhatsAppStatusHolder holder, int position) {

        final CommonModel videoString = fileList.get(position);

        if (list.contains(videoString)) {
            holder.selectVideo_iv.setImageDrawable(ResourcesCompat.getDrawable(context.getResources()
                    , R.drawable.ic_selected, null));
        } else {
            holder.selectVideo_iv.setImageBitmap(null);
        }
        Glide.with(context)
                .load(videoString.getPath())
                .into(holder.deepCleanVideosRv_iv);


        holder.videoAdapter_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!list.contains(videoString)) {
                    list.add(videoString);
                    if (list.size() == fileList.size()) {
                        selectAll.selectAll(true);
                    }
                    holder.selectVideo_iv.setImageDrawable(ResourcesCompat.getDrawable(context.getResources()
                            , R.drawable.ic_selected, null));
                } else {
                    list.remove(videoString);
                    selectAll.selectAll(list.size() == fileList.size());
                    holder.selectVideo_iv.setImageBitmap(null);

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
            holder.selectVideo_iv.setImageDrawable(ResourcesCompat.getDrawable(context.getResources()
                    ,R.drawable.ic_selected,null));
            notifyDataSetChanged();
        }
    }

    private void clearList() {
        if (!list.isEmpty()) {
            list.clear();
            holder.selectVideo_iv.setImageBitmap(null);
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

        ImageView deepCleanVideosRv_iv, selectVideo_iv;
        ConstraintLayout videoAdapter_cl;

        public WhatsAppStatusHolder(@NonNull View itemView) {
            super(itemView);

            videoAdapter_cl = itemView.findViewById(R.id.videoAdapter_cl);
            deepCleanVideosRv_iv = itemView.findViewById(R.id.deepCleanVideosRv_iv);
            selectVideo_iv = itemView.findViewById(R.id.selectVideo_iv);
        }
    }
}
