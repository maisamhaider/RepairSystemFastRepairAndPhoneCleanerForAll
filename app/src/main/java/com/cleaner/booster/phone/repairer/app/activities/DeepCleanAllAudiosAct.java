package com.cleaner.booster.phone.repairer.app.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.adapters.DeepCleanAdapter;
import com.cleaner.booster.phone.repairer.app.async.DeepCleanAudiosTask;
import com.cleaner.booster.phone.repairer.app.interfaces.SelectAll;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.io.File;
import java.util.List;

public class DeepCleanAllAudiosAct extends AppCompatActivity implements SelectAll {

    DeepCleanAdapter deepCleanAdapter;
    RecyclerView deepCleanAllAudio_rv;

    Utils utils;
    File file;
    boolean isSend;
    public CheckBox selectAll_cb1;
    public TextView noData_tv, select_tv;
    boolean b = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_clean_all_audios);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        utils = new Utils(this);
        deepCleanAllAudio_rv = findViewById(R.id.deepCleanAllAudio_rv);
        LinearLayout deepCleanAudiosClean_ll = findViewById(R.id.deepCleanAudiosClean_ll);
        TextView deepCleanAudiosCleanBtn_tv = findViewById(R.id.deepCleanAudiosCleanBtn_tv);
        selectAll_cb1 = findViewById(R.id.selectAll_cb1);
        noData_tv = findViewById(R.id.noData_tv);
        select_tv = findViewById(R.id.select_tv);

        isSend = getIntent().getBooleanExtra("isSend", false);
        loadData();
        if (isSend) {
            deepCleanAudiosCleanBtn_tv.setText("MOVE");
        }
        deepCleanAudiosClean_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CommonModel> pathList = deepCleanAdapter.getList();
                if (isSend) {
//                    FileMoverTask fileMoverTask = new FileMoverTask(getApplicationContext(), pathList, "Audios");
//                    fileMoverTask.execute();

                } else {
                    View view = getLayoutInflater().inflate(R.layout.are_you_sure_to_delete_dialog_layout, null, false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(DeepCleanAllAudiosAct.this);
                    LinearLayout no_ll = view.findViewById(R.id.no_ll);
                    LinearLayout yes_ll = view.findViewById(R.id.yes_ll);

                    builder.setView(view).setCancelable(true);
                    AlertDialog dialog = builder.create();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    if (!pathList.isEmpty()) {
                        dialog.show();
                    } else {
                        Toast.makeText(DeepCleanAllAudiosAct.this, "No Audio selected", Toast.LENGTH_SHORT).show();
                    }
                    no_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    yes_ll.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int i = 0; i < pathList.size(); i++) {
                                try {
                                    file = new File(pathList.get(i).getPath());
                                    utils.scanaddedFile(pathList.get(i).getPath());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            dialog.dismiss();
                            deepCleanAdapter.removeDeleted();
                        }
                    });
                }
            }
        });
        selectAll_cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectAll selectAll = deepCleanAdapter.getSelectAll();
                if (!b) {
                    selectAll.selectAll(true);
                    b = true;
                } else {
                    selectAll.selectAll(false);
                    b = false;

                }
            }
        });
    }

    @Override
    public void selectAll(boolean isSelectAll) {

        if (isSelectAll) {
            selectAll_cb1.setChecked(true);
            b = true;
        } else {
            selectAll_cb1.setChecked(false);
            b = false;
        }
    }

    public void loadData() {
        deepCleanAdapter = new DeepCleanAdapter(this, this, 1);
        new DeepCleanAudiosTask(this, deepCleanAdapter, deepCleanAllAudio_rv);
        deepCleanAdapter.notifyDataSetChanged();
    }
}