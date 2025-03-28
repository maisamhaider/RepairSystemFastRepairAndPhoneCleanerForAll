package com.cleaner.booster.phone.repairer.app.activities;

import android.animation.ValueAnimator;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.adapters.AllAppsAdapter;
import com.cleaner.booster.phone.repairer.app.async.AllAppsTask;
import com.cleaner.booster.phone.repairer.app.interfaces.TrueFalse;


public class UnInstallAppAct extends AppCompatActivity implements TrueFalse {

    private ImageView uninstall_iv;
    private TextView totalNumber_tv, msg_tv, no_data_tv;
    private AllAppsAdapter allAppsAdapter;
    private RecyclerView allAppsUnInstallApp_rv;
    EditText appsSearch_et;
    ImageView searchApps_iv, cancel_iv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_un_install_app);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        allAppsUnInstallApp_rv = findViewById(R.id.allAppsUnInstallApp_rv);
        uninstall_iv = findViewById(R.id.uninstall_iv);
        totalNumber_tv = findViewById(R.id.tv_total_apps);
        msg_tv = findViewById(R.id.msg_tv);
        appsSearch_et = findViewById(R.id.appsSearch_et);
        searchApps_iv = findViewById(R.id.searchApps_iv);
        cancel_iv = findViewById(R.id.cancel_iv);
        no_data_tv = findViewById(R.id.no_data_tv);
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
        appsSearch_et.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        appsSearch_et.setSingleLine();

        allAppsAdapter = new AllAppsAdapter(this, this::isTrue);

        loadData();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startAnimation();
            }
        }, 1_000);

        searchApps_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchApps_iv.setVisibility(View.GONE);
                appsSearch_et.setVisibility(View.VISIBLE);
                cancel_iv.setVisibility(View.VISIBLE);

            }
        });
        appsSearch_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                allAppsAdapter.getFilter().filter(editable);

            }
        });
        cancel_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchApps_iv.setVisibility(View.VISIBLE);
                appsSearch_et.setText("");
                appsSearch_et.setVisibility(View.GONE);
                cancel_iv.setVisibility(View.GONE);
                loadData();

            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        allAppsAdapter.notifyDataSetChanged();
    }

    public void loadData() {
        new AllAppsTask(this, allAppsAdapter, allAppsUnInstallApp_rv);
        totalNumber_tv.setText(String.valueOf(allAppsAdapter.getApps().size()));
//        startAnimation();
    }


    public void startAnimation() {

        ValueAnimator animator = ValueAnimator.ofInt(0, allAppsAdapter.getApps().size());
        animator.setInterpolator(new LinearInterpolator());
        animator.setStartDelay(0);
        animator.setDuration(800);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int value = (int) valueAnimator.getAnimatedValue();
                totalNumber_tv.setText(String.valueOf(value));
            }
        });

        animator.start();
    }


    @Override
    public void isTrue(boolean isTrue) {
        if (isTrue) {
            no_data_tv.setVisibility(View.GONE);
        } else {
            no_data_tv.setVisibility(View.VISIBLE);
        }
    }
}