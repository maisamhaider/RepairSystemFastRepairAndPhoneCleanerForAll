package com.cleaner.booster.phone.repairer.app.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.adapters.DeepCleanAdapter;
import com.cleaner.booster.phone.repairer.app.adapters.DeepCleanImagesAdapter;
import com.cleaner.booster.phone.repairer.app.adapters.DeepCleanVideosAdapter;
import com.cleaner.booster.phone.repairer.app.async.DeepCleanImagesTask;
import com.cleaner.booster.phone.repairer.app.async.DeepCleanVideosTask;
import com.cleaner.booster.phone.repairer.app.async.FileMoverTask;
import com.cleaner.booster.phone.repairer.app.interfaces.SelectAll;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.io.File;
import java.util.List;

public class DeepCleanAllImagesAct extends AppCompatActivity implements SelectAll {


    DeepCleanImagesTask deepCleanImagesTask;
    DeepCleanImagesAdapter deepCleanImagesAdapter;
    RecyclerView DeepCleanAllImages_rv;
    File file;
    Utils utils;
    boolean isSend;
    public CheckBox selectAll_cb1;
    public TextView noData_tv,select_tv;
        boolean b = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_clean_all_images);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        utils = new Utils(this);
        DeepCleanAllImages_rv = findViewById(R.id.DeepCleanAllImages_rv);
        LinearLayout deepCleanImagesClean_ll = findViewById(R.id.deepCleanImagesClean_ll);
        TextView deepCleanImagesCleanBtn_tv = findViewById(R.id.deepCleanImagesCleanBtn_tv);
        selectAll_cb1 = findViewById(R.id.selectAll_cb1);
        noData_tv = findViewById(R.id.noData_tv);
        select_tv = findViewById(R.id.select_tv);


        loadData();
        isSend= getIntent().getBooleanExtra("isSend",false);
        if (isSend)
        {
            deepCleanImagesCleanBtn_tv.setText("MOVE");
        }
        deepCleanImagesClean_ll.setOnClickListener(v -> {
            List<String> pathList = deepCleanImagesAdapter.getList();
            if (isSend)
            {
               String hgg = String.valueOf(utils.getExternalMounts());

                FileMoverTask fileMoverTask = new FileMoverTask(getApplicationContext(),pathList,"Images");
                fileMoverTask.execute();


            }else {
                View view = getLayoutInflater().inflate(R.layout.are_you_sure_to_delete_dialog_layout, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(DeepCleanAllImagesAct.this);
                LinearLayout no_ll = view.findViewById(R.id.no_ll);
                LinearLayout yes_ll = view.findViewById(R.id.yes_ll);

                builder.setView(view).setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                if (!pathList.isEmpty())
                {
                    dialog.show();
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
                                file = new File(pathList.get(i));
                                utils.scanaddedFile(pathList.get(i));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        dialog.dismiss();
                        loadData();                    }
                });
            }
        });
        selectAll_cb1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectAll selectAll = deepCleanImagesAdapter.getSelectAll();
                if (!b) {
                    selectAll.selectAll(false);
                    b = true;
                } else {
                    selectAll.selectAll(true);
                    b = false;

                }
            }
        });

    }
    @Override
    public void selectAll(boolean isSelectAll) {

        if (isSelectAll)
        {
            selectAll_cb1.setChecked(true);
            b = false;
        }
        else {
            selectAll_cb1.setChecked(false);
            b = true;
        }
    }
    public void loadData()
    {
        deepCleanImagesAdapter = new DeepCleanImagesAdapter(this,this);
        deepCleanImagesTask = new DeepCleanImagesTask(this, deepCleanImagesAdapter, DeepCleanAllImages_rv);
        deepCleanImagesTask.execute();
    }

}