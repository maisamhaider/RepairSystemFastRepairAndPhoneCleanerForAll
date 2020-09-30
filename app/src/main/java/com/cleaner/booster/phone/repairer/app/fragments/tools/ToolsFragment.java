package com.cleaner.booster.phone.repairer.app.fragments.tools;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cleaner.booster.phone.repairer.app.R;
import com.cleaner.booster.phone.repairer.app.activities.BluetoothInfoAct;
import com.cleaner.booster.phone.repairer.app.activities.FeaturesAct;
import com.cleaner.booster.phone.repairer.app.activities.HarassmentFilterAct;
import com.cleaner.booster.phone.repairer.app.activities.DeviceInfoAct;
import com.cleaner.booster.phone.repairer.app.activities.HardwareTest;
import com.cleaner.booster.phone.repairer.app.activities.ProcessorDetailAct;
import com.cleaner.booster.phone.repairer.app.activities.RootCheckerAct;
import com.cleaner.booster.phone.repairer.app.activities.SensorListAct;
import com.cleaner.booster.phone.repairer.app.activities.UnInstallAppAct;
import com.cleaner.booster.phone.repairer.app.fragments.BaseFragment;

public class ToolsFragment extends BaseFragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tool, container, false);
         ConstraintLayout appInstall_cl = root.findViewById(R.id.appInstall_cl);
//        ConstraintLayout fileMover_cl = root.findViewById(R.id.fileMover_cl);
        ConstraintLayout hardwareInfo_cl = root.findViewById(R.id.hardwareInfo_cl);
        ConstraintLayout harassmentFiler_cl = root.findViewById(R.id.harassmentFiler_cl);
        ConstraintLayout rootchecker_cl = root.findViewById(R.id.rootchecker_cl);
        ConstraintLayout sensorlist_cl = root.findViewById(R.id.sensorlist_cl);
        ConstraintLayout processesor_cl = root.findViewById(R.id.processesor_cl);
        ConstraintLayout hrdTest_cl = root.findViewById(R.id.hrdTest_cl);
        ConstraintLayout blueInfo_cl = root.findViewById(R.id.blueInfo_cl);
        ConstraintLayout deviceFeatures_cl = root.findViewById(R.id.deviceFeatures_cl);
        CardView harassmentFiler_cv = root.findViewById(R.id.harassmentFiler_cv);
         appInstall_cl.setOnClickListener(v -> startActivity(new Intent(getActivity(), UnInstallAppAct.class)));
        processesor_cl.setOnClickListener(v -> sNewActivityAds(new ProcessorDetailAct()));
        rootchecker_cl.setOnClickListener(v -> sNewActivityAds(new RootCheckerAct()));
        sensorlist_cl.setOnClickListener(v -> sNewActivityAds(new SensorListAct()));
        hardwareInfo_cl.setOnClickListener(v -> sNewActivityAds(new DeviceInfoAct()));
        blueInfo_cl.setOnClickListener(v -> sNewActivityAds(new BluetoothInfoAct()));
        hrdTest_cl.setOnClickListener(v -> sNewActivityAds(new HardwareTest()));
        deviceFeatures_cl.setOnClickListener(v -> sNewActivityAds(new FeaturesAct()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            harassmentFiler_cl.setOnClickListener(v -> sNewActivityAds(new HarassmentFilterAct()));
        } else {
            harassmentFiler_cv.setVisibility(View.GONE);
        }
        return root;
    }
}