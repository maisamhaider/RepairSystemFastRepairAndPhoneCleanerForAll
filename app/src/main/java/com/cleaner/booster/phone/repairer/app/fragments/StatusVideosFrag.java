package com.cleaner.booster.phone.repairer.app.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.adapters.WhatsAppStatusAdapter;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.permission.Permissions;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.io.File;
import java.util.List;


public class StatusVideosFrag extends Fragment {


    public StatusVideosFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status_videos, container, false);

        Utils utils = new Utils(getContext());

         RecyclerView statusVideos_rv = view.findViewById(R.id.statusVideos_rv);
        LinearLayout saveStatusVideos_ll = view.findViewById(R.id.saveStatusVideos_ll);
        TextView statusNoVideo_tv = view.findViewById(R.id.statusNoVideo_tv);
        statusNoVideo_tv.setVisibility(View.GONE);

        List<CommonModel> list;
        File file1 = new File(Environment.getExternalStorageDirectory().getPath() + "/WhatsApp/Media/.Statuses");
        list = utils.getListFiles(file1, "videos");
        if (list.size() == 0) {
            statusNoVideo_tv.setVisibility(View.VISIBLE);
            saveStatusVideos_ll.setVisibility(View.GONE);

        } else {
            statusNoVideo_tv.setVisibility(View.GONE);
            saveStatusVideos_ll.setVisibility(View.VISIBLE);

            statusVideos_rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
            WhatsAppStatusAdapter statusAdapter = new WhatsAppStatusAdapter(getContext(), list, false);
            statusVideos_rv.setAdapter(statusAdapter);
            statusAdapter.notifyDataSetChanged();

            saveStatusVideos_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<String> data = statusAdapter.getSendList();
                    Permissions p = new Permissions(getContext());
                    if (p.permission()) {
                        if (data.size() != 0) {
                            for (int i = 0; i < data.size(); i++) {
                                utils.copyFileOrDirectory(data.get(i));
                            }
                        }
                    }
                }
            });
        }
        return view;
    }
}