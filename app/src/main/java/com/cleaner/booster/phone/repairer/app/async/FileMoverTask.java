package com.cleaner.booster.phone.repairer.app.async;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.utils.Utils;

import java.util.List;

public class FileMoverTask extends AsyncTask<Void,Integer,String> {

    Utils utils ;
    Context context;
    String disDirName;
    List<String> sourcePathList;
    ProgressBar progressBar;
    AlertDialog dialog;

    public FileMoverTask( Context context,List<String> sourcePathList, String disDirName) {
         this.context = context;
         this.disDirName = disDirName;
         this.sourcePathList = sourcePathList;;
    }

    @Override
    protected void onPreExecute() {
        utils = new Utils(context);
        AlertDialog.Builder builder = new AlertDialog.Builder(context.getApplicationContext());

        View view = LayoutInflater.from(context).inflate(R.layout.loading_progressbar_dialog_layout,null);

        builder.setView(view);
        progressBar = view.findViewById(R.id.loading_pb);
        progressBar.setMax(sourcePathList.size());
        dialog = builder.create();
//        /if device is Oreo or latter
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        } else {
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_PHONE);
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        for (int i = 0; i<sourcePathList.size(); i++)
        {
            utils.moveFile(sourcePathList.get(i),disDirName);
            publishProgress(i);
        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (dialog.isShowing())
        {
            dialog.dismiss();
        }
    }
}
