package com.cleaner.booster.phone.repairer.app.fragments;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.List;

public class StatusSavedFrag extends Fragment {


    public StatusSavedFrag() {
        // Required empty public constructor
    }

    public static StatusSavedFrag newInstance() {
        return new StatusSavedFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status_saved, container, false);

        Utils utils = new Utils(getContext());

        RecyclerView statusSaved_rv = view.findViewById(R.id.statusSaved_rv);
        TextView noStatusData_tv = view.findViewById(R.id.noStatusData_tv);
        noStatusData_tv.setVisibility(View.GONE);

        File file1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+
                "/DCIM/phone_repair");
        List<CommonModel> list = utils.getSavedStatusFiles(file1);
        if (list.size()==0){
            noStatusData_tv.setVisibility(View.VISIBLE);
        }
        else {
            noStatusData_tv.setVisibility(View.GONE);
            statusSaved_rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
            WhatsAppStatusAdapter statusAdapter = new WhatsAppStatusAdapter(getContext(), list, true);
            statusSaved_rv.setAdapter(statusAdapter);
            statusAdapter.notifyDataSetChanged();
        }


        return view;
    }
}