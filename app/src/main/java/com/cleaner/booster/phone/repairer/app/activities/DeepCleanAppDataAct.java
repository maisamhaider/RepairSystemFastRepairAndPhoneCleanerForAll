package com.cleaner.booster.phone.repairer.app.activities;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.adapters.DeepCleanImagesAdapter;
import com.cleaner.booster.phone.repairer.app.adapters.DeepCleanVideosAdapter;
import com.cleaner.booster.phone.repairer.app.interfaces.SelectAll;
import com.cleaner.booster.phone.repairer.app.models.CommonModel;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DeepCleanAppDataAct extends AppCompatActivity implements SelectAll {

    Utils utils;
    List<CommonModel> videosModelList;
    List<CommonModel> imagesModelList;
    boolean isImages = false;
    boolean isFacebookClicked = false;
    boolean isMessengerClicked = false;
    boolean isInstagramClicked = false;

    ConstraintLayout facebook_cl, facebookSavedImages_cl,
            messenger_cl, messengerSavedImages_cl, messengerSavedVideos_cl,
            instagram_cl, instagramSavedImages_cl, instagramSavedVideos_cl;

    ImageView facebookArrow_iv, facebookSavedImagesArrow_iv,
            messengerArrow_iv, messengerSavedImagesArrow_iv, messengerSavedVideosArrow_iv,
            instagramArrow_iv, instagramSavedImagesArrow_iv, instagramSavedVideosArrow_iv,
            appDataBack_iv;

    RelativeLayout appRvMain_rl;

    private File file;
    private DeepCleanVideosAdapter videosAdapter;
    private DeepCleanImagesAdapter imagesAdapter;
    private RecyclerView appData_rv;
    private ImageView appRvLayoutBack_iv;
    private LinearLayout appDataClean_ll;
    private TextView noData_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_clean_app_data_atc);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        utils = new Utils(this);

        TextView facebookDataSize_tv, facebookSavedImagesSize_tv, messengerDataSize_tv,
                messengerSavedImagesSize_tv, messengerSavedVideosSize_tv, instagramDataSize_tv,
                instagramSavedImagesSize_tv, instagramSavedVideosSize_tv;


        facebook_cl = findViewById(R.id.facebook_cl);
        noData_tv = findViewById(R.id.noData_tv);
        facebookSavedImages_cl = findViewById(R.id.facebookSavedImages_cl);

        messenger_cl = findViewById(R.id.messenger_cl);
        messengerSavedImages_cl = findViewById(R.id.messengerSavedImages_cl);
        messengerSavedVideos_cl = findViewById(R.id.messengerSavedVideos_cl);

        instagram_cl = findViewById(R.id.instagram_cl);
        instagramSavedImages_cl = findViewById(R.id.instagramSavedImages_cl);
        instagramSavedVideos_cl = findViewById(R.id.instagramSavedVideos_cl);

        facebookArrow_iv = findViewById(R.id.facebookArrow_iv);
        messengerArrow_iv = findViewById(R.id.messengerArrow_iv);
        instagramArrow_iv = findViewById(R.id.instagramArrow_iv);

        facebookSavedImagesArrow_iv = findViewById(R.id.facebookSavedImagesArrow_iv);
        messengerSavedImagesArrow_iv = findViewById(R.id.messengerSavedImagesArrow_iv);
        messengerSavedVideosArrow_iv = findViewById(R.id.messengerSavedVideosArrow_iv);
        instagramSavedImagesArrow_iv = findViewById(R.id.instagramSavedImagesArrow_iv);
        instagramSavedVideosArrow_iv = findViewById(R.id.instagramSavedVideosArrow_iv);

        facebookDataSize_tv = findViewById(R.id.facebookDataSize_tv);
        facebookSavedImagesSize_tv = findViewById(R.id.facebookSavedImagesSize_tv);

        messengerDataSize_tv = findViewById(R.id.messengerDataSize_tv);
        messengerSavedImagesSize_tv = findViewById(R.id.messengerSavedImagesSize_tv);
        messengerSavedVideosSize_tv = findViewById(R.id.messengerSavedVideosSize_tv);

        instagramDataSize_tv = findViewById(R.id.instagramDataSize_tv);
        instagramSavedImagesSize_tv = findViewById(R.id.instagramSavedImagesSize_tv);
        instagramSavedVideosSize_tv = findViewById(R.id.instagramSavedVideosSize_tv);

        appDataBack_iv = findViewById(R.id.appDataBack_iv);


        appRvMain_rl = findViewById(R.id.appRvMain_rl);

        //app_data_rv_layout
        appData_rv = findViewById(R.id.appData_rv);
        appRvLayoutBack_iv = findViewById(R.id.appRvLayoutBack_iv);
        appDataClean_ll = findViewById(R.id.appDataClean_ll);
        //
        videosModelList = new ArrayList<>();
        imagesModelList = new ArrayList<>();

        //adapter
        imagesAdapter = new DeepCleanImagesAdapter(DeepCleanAppDataAct.this,this);
        videosAdapter = new DeepCleanVideosAdapter(DeepCleanAppDataAct.this,this);


        //Done child layouts
        facebookSavedImages_cl.setVisibility(View.GONE);

        messengerSavedImages_cl.setVisibility(View.GONE);
        messengerSavedVideos_cl.setVisibility(View.GONE);

        instagramSavedImages_cl.setVisibility(View.GONE);
        instagramSavedVideos_cl.setVisibility(View.GONE);

        appRvMain_rl.setVisibility(View.GONE);

        appDataBack_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //facebook work
        File facebookFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Facebook");
        if (facebookFolder.exists()) {
            facebook_cl.setVisibility(View.VISIBLE);
        } else {
            facebook_cl.setVisibility(View.GONE);
        }
        float facebookSavedImagesSize = utils.getAllSize(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Facebook");
        facebookDataSize_tv.setText(utils.getCalculatedDataSize(facebookSavedImagesSize));
        facebookSavedImagesSize_tv.setText(utils.getCalculatedDataSize(facebookSavedImagesSize));


        facebook_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFacebookClicked) {
                    isFacebookClicked = false;
                    facebookSavedImages_cl.setVisibility(View.GONE);
                } else {
                    isFacebookClicked = true;
                    facebookSavedImages_cl.setVisibility(View.VISIBLE);
                }
            }
        });
        facebookArrow_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFacebookClicked) {
                    isFacebookClicked = false;
                    facebookSavedImages_cl.setVisibility(View.GONE);
                } else {
                    isFacebookClicked = true;
                    facebookSavedImages_cl.setVisibility(View.VISIBLE);
                }
            }
        });
        facebookSavedImages_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookSavedImagesFun();
            }
        });
        facebookSavedImagesArrow_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookSavedImagesFun();
            }
        });

        //messenger work
        File messengerFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/Messenger");
        if (messengerFolder.exists()) {
            messenger_cl.setVisibility(View.VISIBLE);
        } else {
            messenger_cl.setVisibility(View.GONE);
        }
        float messengerSavedImagesSize = utils.getAllSize(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Messenger");
        float messengerSavedVideosSize = utils.getAllSize(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/Messenger");

        messengerDataSize_tv.setText(utils.getCalculatedDataSize(messengerSavedImagesSize + messengerSavedVideosSize));
        messengerSavedVideosSize_tv.setText(utils.getCalculatedDataSize(messengerSavedImagesSize));
        messengerSavedImagesSize_tv.setText(utils.getCalculatedDataSize(messengerSavedVideosSize));

        messenger_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (isMessengerClicked) {
                    isMessengerClicked = false;
                    messengerSavedImages_cl.setVisibility(View.GONE);
                    messengerSavedVideos_cl.setVisibility(View.GONE);
                } else {
                    isMessengerClicked = true;
                    messengerSavedImages_cl.setVisibility(View.VISIBLE);
                    messengerSavedVideos_cl.setVisibility(View.VISIBLE);
                }
            }
        });
        messengerArrow_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMessengerClicked) {
                    isMessengerClicked = false;
                    messengerSavedImages_cl.setVisibility(View.GONE);
                    messengerSavedVideos_cl.setVisibility(View.GONE);
                } else {
                    isMessengerClicked = true;
                    messengerSavedImages_cl.setVisibility(View.VISIBLE);
                    messengerSavedVideos_cl.setVisibility(View.VISIBLE);
                }
            }
        });

        messengerSavedImagesArrow_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messengerSavedImagesFun();
            }
        });

        messengerSavedVideosArrow_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messengerSavedVideosFun();
            }

        });
        messengerSavedImages_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messengerSavedImagesFun();
            }
        });

        messengerSavedVideos_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messengerSavedVideosFun();
            }

        });

        //Instagram Data
        File InstagramFolder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Instagram");
        if (InstagramFolder.exists()) {
            instagram_cl.setVisibility(View.VISIBLE);
        } else {
            instagram_cl.setVisibility(View.GONE);
        }

        if (InstagramFolder.exists() && messengerFolder.exists() && facebookFolder.exists())
        {
            noData_tv.setVisibility(View.GONE);
        }
        else
        {
            noData_tv.setVisibility(View.VISIBLE);

        }
        float instagramSavedImagesSize = utils.getAllSize(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Instagram");
        float instagramSavedVideosSize = utils.getAllSize(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/Instagram");

        instagramDataSize_tv.setText(utils.getCalculatedDataSize(instagramSavedImagesSize + instagramSavedVideosSize));
        instagramSavedImagesSize_tv.setText(utils.getCalculatedDataSize(instagramSavedImagesSize));
        instagramSavedVideosSize_tv.setText(utils.getCalculatedDataSize(instagramSavedVideosSize));

        instagram_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isInstagramClicked) {
                    isInstagramClicked = false;
                    instagramSavedImages_cl.setVisibility(View.GONE);
                    instagramSavedVideos_cl.setVisibility(View.GONE);
                } else {
                    isInstagramClicked = true;
                    instagramSavedImages_cl.setVisibility(View.VISIBLE);
                    instagramSavedVideos_cl.setVisibility(View.VISIBLE);
                }
            }
        });
        instagramArrow_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInstagramClicked) {
                    isInstagramClicked = false;
                    instagramSavedImages_cl.setVisibility(View.GONE);
                    instagramSavedVideos_cl.setVisibility(View.GONE);
                } else {
                    isInstagramClicked = true;
                    instagramSavedImages_cl.setVisibility(View.VISIBLE);
                    instagramSavedVideos_cl.setVisibility(View.VISIBLE);
                }
            }
        });

        instagramSavedImagesArrow_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instagramSavedImagesFun();

            }
        });

        instagramSavedVideosArrow_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                instagramSavedVideosFun();

            }
        });


        instagramSavedImages_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instagramSavedImagesFun();

            }
        });

        instagramSavedVideos_cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                instagramSavedVideosFun();
            }
        });


        appRvLayoutBack_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appRvMain_rl.setVisibility(View.GONE);
            }
        });

        appDataClean_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CommonModel> pathList;
                if (isImages) {
                    pathList = imagesAdapter.getList();
                } else {
                    pathList = videosAdapter.getList();
                }
                View view = getLayoutInflater().inflate(R.layout.are_you_sure_to_delete_dialog_layout, null, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(DeepCleanAppDataAct.this);
                LinearLayout no_ll = view.findViewById(R.id.no_ll);
                LinearLayout yes_ll = view.findViewById(R.id.yes_ll);

                builder.setView(view).setCancelable(true);
                AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
                                file.delete();
                                appRvMain_rl.setVisibility(View.GONE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        dialog.dismiss();
                        finish();
                    }
                });

            }
        });
    }

    private void instagramSavedVideosFun() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/Instagram");
        if (file.exists()) {
            appData_rv.setVisibility(View.VISIBLE);
            appRvMain_rl.setVisibility(View.VISIBLE);
            videosModelList = utils.getVideos(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/Instagram");
            videosAdapter.setFileList(videosModelList);
            appData_rv.setLayoutManager(new GridLayoutManager(DeepCleanAppDataAct.this, 3));
            appData_rv.setAdapter(videosAdapter);
            videosAdapter.notifyDataSetChanged();
        } else {
            appData_rv.setVisibility(View.GONE);

        }
    }

    private void instagramSavedImagesFun() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Instagram");
        if (file.exists()) {
            appData_rv.setVisibility(View.VISIBLE);
            appRvMain_rl.setVisibility(View.VISIBLE);
            imagesModelList = utils.getImages(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Instagram");
            imagesAdapter.setFileList(imagesModelList);
            appData_rv.setLayoutManager(new GridLayoutManager(DeepCleanAppDataAct.this, 3));
            appData_rv.setAdapter(imagesAdapter);
            imagesAdapter.notifyDataSetChanged();
            isImages = true;
        } else {
            appData_rv.setVisibility(View.GONE);
        }
    }

    private void facebookSavedImagesFun() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Facebook");
        if (file.exists()) {
            appData_rv.setVisibility(View.VISIBLE);
            appRvMain_rl.setVisibility(View.VISIBLE);
            imagesModelList = utils.getImages(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Facebook");
            imagesAdapter.setFileList(imagesModelList);
            appData_rv.setLayoutManager(new GridLayoutManager(DeepCleanAppDataAct.this, 3));
            appData_rv.setAdapter(imagesAdapter);
            imagesAdapter.notifyDataSetChanged();
            isImages = true;

        } else {
            appData_rv.setVisibility(View.GONE);
        }

    }

    private void messengerSavedImagesFun() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Messenger");
        if (file.exists()) {
            appData_rv.setVisibility(View.VISIBLE);
            appRvMain_rl.setVisibility(View.VISIBLE);
            imagesModelList = utils.getImages(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Pictures/Messenger");
            imagesAdapter.setFileList(imagesModelList);
            appData_rv.setLayoutManager(new GridLayoutManager(DeepCleanAppDataAct.this, 3));
            appData_rv.setAdapter(imagesAdapter);
            imagesAdapter.notifyDataSetChanged();
            isImages = true;
        } else {
            appData_rv.setVisibility(View.GONE);
        }
    }

    public void messengerSavedVideosFun() {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/Messenger");
        if (file.exists()) {
            appRvMain_rl.setVisibility(View.VISIBLE);
            appData_rv.setVisibility(View.VISIBLE);
            videosModelList = utils.getVideos(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Movies/Messenger");
            videosAdapter.setFileList(videosModelList);
            appData_rv.setLayoutManager(new GridLayoutManager(DeepCleanAppDataAct.this, 3));
            appData_rv.setAdapter(videosAdapter);
            videosAdapter.notifyDataSetChanged();

        } else {
            appData_rv.setVisibility(View.GONE);
        }
    }

    @Override
    public void selectAll(boolean isSelectAll) {

    }
}
