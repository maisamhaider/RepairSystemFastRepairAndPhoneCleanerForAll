package com.cleaner.booster.phone.repairer.app.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.activities.OnImageVideoAct;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

public class WhatsAppStatusAdapter extends RecyclerView.Adapter<WhatsAppStatusAdapter.WhatsAppStatusHolder> {
    Context context;
    List<CommonModel> fileList;
    List<String> sendList;
    boolean isSaved;

    public WhatsAppStatusAdapter(Context context, List<CommonModel> fileList, boolean isSaved) {
        this.context = context;
        this.fileList = fileList;
        this.sendList = new ArrayList<>();
        this.isSaved = isSaved;

    }

    public List<String> getSendList() {
        return sendList;
    }

    public void setSendList(List<String> sendList) {
        this.sendList = sendList;
    }

    @NonNull
    @Override
    public WhatsAppStatusHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.whats_app_saver_rv_layout, parent, false);

        return new WhatsAppStatusHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WhatsAppStatusHolder holder, int position) {

        final String imageVideoString = fileList.get(position).getPath();
            if (sendList.contains(imageVideoString))
            {
                Glide.with(context)
                        .load(R.drawable.ic_select)
                        .into(holder.select_deselected_iv);
            }else
            {
                Glide.with(context)
                        .load(R.drawable.ic_deselect)
                        .into(holder.select_deselected_iv);
            }

        if (imageVideoString.endsWith("mp4"))
        {
            holder.isVideo_iv.setVisibility(View.VISIBLE);
        }
        else
            {
                holder.isVideo_iv.setVisibility(View.GONE);
            }
        if (isSaved)
        {
            holder.select_deselected_iv.setVisibility(View.GONE);
        }
        else
        {
            holder.select_deselected_iv.setVisibility(View.VISIBLE);

        }

        Glide.with(context)
                .load(imageVideoString)
                .into(holder.imageView);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sendList.size()==0)
                {
                    Intent intent = new Intent(context, OnImageVideoAct.class);
                    if (isSaved)
                    {
                        intent.putExtra("isSaved",true);
                    }
                    intent.putExtra("imageOrVideoPath",imageVideoString);
                    context.startActivity(intent);

                }
            }
        });

        holder.select_deselected_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    if (!sendList.contains(imageVideoString))
                    {
                      sendList.add(imageVideoString);
                        Glide.with(context)
                                .load(R.drawable.ic_select)
                                .into(holder.select_deselected_iv);
                    }else
                    {
                        sendList.remove(imageVideoString);
                        Glide.with(context)
                                .load(R.drawable.ic_deselect)
                                .into(holder.select_deselected_iv);

                    }

            }
        });

    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    class WhatsAppStatusHolder extends RecyclerView.ViewHolder {

        CircularImageView imageView;
        ImageView isVideo_iv,select_deselected_iv;

        public WhatsAppStatusHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.whatsAppRv_iv);
            isVideo_iv = itemView.findViewById(R.id.isVideo_iv);
            select_deselected_iv = itemView.findViewById(R.id.select_deselected_iv);
        }
    }
}
